package com.example.btheartrate;

import java.lang.reflect.Method;
import java.util.Set;

import zephyr.android.HxMBT.BTClient;
import zephyr.android.HxMBT.ZephyrProtocol;
import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.media.AudioManager;
import android.media.SoundPool;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.GestureDetector;
import android.view.GestureDetector.OnGestureListener;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

public class HeartMonitor extends Activity implements OnGestureListener {

	private final SoundPool soundpool = new SoundPool(5, AudioManager.STREAM_MUSIC, 0);
	private final int[] sounds = new int[2];
	private GestureDetector gesture = null;


	private BluetoothAdapter adapter = null;
	private BTClient _bt;
	private ZephyrProtocol _protocol;
	private ConnectedListener _NConnListener;
	private final int HEART_RATE = 0x100;
	private final int INSTANT_SPEED = 0x101;
	private final int BATTERY_MESSAGE = 0x102;

	private static int heartRate = 0;
	private final int timer_interval = 30000; // 30 seconds
	private final int hr_check_interval = 5000; // 5 second default
	private final Handler alert_handler = new Handler();


	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_heart_monitor);

		gesture = new GestureDetector(this);

		sounds[0] = soundpool.load(this, R.raw.beep5, 1);
		sounds[1] = soundpool.load(this, R.raw.beep2, 1);


		// Sending a message to android that we are going to initiate a pairing request
		IntentFilter filter = new IntentFilter("android.bluetooth.device.action.PAIRING_REQUEST");
		// Registering a new BTBroadcast receiver from the Main Activity context with pairing request event
		this.getApplicationContext().registerReceiver(new BTBroadcastReceiver(), filter);
		// Registering the BTBondReceiver in the application that the status of the receiver has changed to Paired
		IntentFilter filter2 = new IntentFilter("android.bluetooth.device.action.BOND_STATE_CHANGED");
		this.getApplicationContext().registerReceiver(new BTBondReceiver(), filter2);

		// Obtaining the handle to act on the CONNECT button
		TextView tv = (TextView) findViewById(R.id.labelStatusMsg);
		String ErrorText  = "Not Connected";
		tv.setText(ErrorText);

		Button btnConnect = (Button) findViewById(R.id.ButtonConnect);
		if (btnConnect != null)	{
			btnConnect.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					String BhMacID = "00:07:80:9D:8A:E8";
					adapter = BluetoothAdapter.getDefaultAdapter();

					Set<BluetoothDevice> pairedDevices = adapter.getBondedDevices();

					if (!pairedDevices.isEmpty()) {
						for (BluetoothDevice device : pairedDevices) {
							if (device.getName().startsWith("HXM"))	{
								BluetoothDevice btDevice = device;
								BhMacID = btDevice.getAddress();
								break;
							}
						}
					}

					BluetoothDevice Device = adapter.getRemoteDevice(BhMacID);
					String DeviceName = Device.getName();
					_bt = new BTClient(adapter, BhMacID);
					_NConnListener = new ConnectedListener(messageHandler, messageHandler);
					_bt.addConnectedEventListener(_NConnListener);

					TextView tv1 = (TextView)findViewById(R.id.labelHeartRate);
					tv1.setText("000");

					tv1 = (TextView)findViewById(R.id.labelInstantSpeed);
					tv1.setText("0.0");

					if (_bt.IsConnected()) {
						_bt.start();
						TextView tv = (TextView) findViewById(R.id.labelStatusMsg);
						String ErrorText  = "Connected to HxM "+DeviceName;
						tv.setText(ErrorText);

						// TODO: Start alerts
						alert_handler.post(playSound);
						alert_handler.post(intervalSound);
					} else {
						TextView tv = (TextView) findViewById(R.id.labelStatusMsg);
						String ErrorText  = "Unable to Connect!";
						tv.setText(ErrorText);
					}
				}
			});
		}

		// Obtaining the handle to act on the DISCONNECT button
		Button btnDisconnect = (Button) findViewById(R.id.ButtonDisconnect);
		if (btnDisconnect != null) {
			btnDisconnect.setOnClickListener(new OnClickListener() {
				/**
				 * Functionality to act if the button DISCONNECT is touched
				 */
				@Override
				public void onClick(View v) {
					// Reset the global variables
					TextView tv = (TextView) findViewById(R.id.labelStatusMsg);
					String ErrorText  = "Disconnected from HxM!";
					tv.setText(ErrorText);

					if (_bt != null && _bt.IsConnected()) {
						// This disconnects listener from acting on received messages
						_bt.removeConnectedEventListener(_NConnListener);
						// Close the communication with the device & throw an exception if failure
						_bt.Close();
					}
				}

			});
		}
	}
	private class BTBondReceiver extends BroadcastReceiver {
		@Override
		public void onReceive(Context context, Intent intent) {
			Bundle b = intent.getExtras();
			BluetoothDevice device = adapter.getRemoteDevice(b.get("android.bluetooth.device.extra.DEVICE").toString());
			Log.d("Bond state", "BOND_STATED = " + device.getBondState());
		}
	}

	private class BTBroadcastReceiver extends BroadcastReceiver {
		@Override
		public void onReceive(Context context, Intent intent) {
			Log.d("BTIntent", intent.getAction());
			Bundle b = intent.getExtras();
			Log.d("BTIntent", b.get("android.bluetooth.device.extra.DEVICE").toString());
			Log.d("BTIntent", b.get("android.bluetooth.device.extra.PAIRING_VARIANT").toString());
			try {
				BluetoothDevice device = adapter.getRemoteDevice(b.get("android.bluetooth.device.extra.DEVICE").toString());
				Method m = BluetoothDevice.class.getMethod("convertPinToBytes", new Class[] { String.class } );
				byte[] pin = (byte[])m.invoke(device, "1234");
				m = device.getClass().getMethod("setPin", new Class [] {pin.getClass()});
				Object result = m.invoke(device, pin);
				Log.d("BTTest", result.toString());
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	final Handler messageHandler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			TextView tv;
			switch (msg.what) {
			case HEART_RATE:
				String heartRateText = msg.getData().getString("HeartRate");
				tv = (TextView) findViewById(R.id.labelHeartRate);
				if (tv != null) {
					tv.setText(heartRateText);
				}

				heartRate = Math.abs(Integer.parseInt(heartRateText));

				break;

			case INSTANT_SPEED:
				String InstaneousSpeedText = msg.getData().getString("InstantSpeed");
				tv = (TextView) findViewById(R.id.labelInstantSpeed);
				if (tv != null) {
					tv.setText(InstaneousSpeedText);
				}

				break;

			case BATTERY_MESSAGE:
				String battery = msg.getData().getString("BatteryMessage");
				tv = (TextView) findViewById(R.id.textView1);
				if (tv != null) {
					tv.setText(battery);
				}

				break;
			}
		}
	};


	// TODO Add timers to check HR and play sounds
	private final Runnable playSound = new Runnable() {
		@Override
		public void run() {
			SharedPreferences prefs = HeartMonitor.this.getPreferences(0);
			int lower = prefs.getInt(HeartMonitor.this.getString(R.string.low), 120);
			int upper = prefs.getInt(HeartMonitor.this.getString(R.string.high), 150);

			if (heartRate != 0 && heartRate > upper || heartRate < lower) {
				soundpool.play(sounds[0], 1, 1, 1, 0, 1);
			}

			alert_handler.postDelayed(this, hr_check_interval);
		}
	};

	private final Runnable intervalSound = new Runnable() {
		@Override
		public void run() {
			soundpool.play(sounds[1], 1, 1, 1, 0, 1);

			alert_handler.postDelayed(this, timer_interval);
		}
	};

	@Override
	public boolean onTouchEvent(MotionEvent me) {
		if (gesture != null)
			return gesture.onTouchEvent(me);
		else
			return false;
	}

	@Override
	public boolean onDown(MotionEvent e) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX,
			float velocityY) {
		// insert code here

		float startX = e1.getX();
		float stopX = e2.getX();

		if (Math.abs(startX - stopX) > 200) {
			startActivity(new Intent(this, SettingsActivity.class));
		}

		return true;
	}

	@Override
	public void onLongPress(MotionEvent e) {
		// TODO Auto-generated method stub

	}

	@Override
	public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX,
			float distanceY) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void onShowPress(MotionEvent e) {
		// TODO Auto-generated method stub

	}

	@Override
	public boolean onSingleTapUp(MotionEvent e) {
		// TODO Auto-generated method stub
		return false;
	}

}
