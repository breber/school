package com.example.btheartrate;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Set;

import zephyr.android.HxMBT.BTClient;
import zephyr.android.HxMBT.ZephyrProtocol;

import com.example.btheartrate.ConnectedListener;
import com.example.btheartrate.R;

import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

public class HeartMonitor extends Activity {

	/** Called when the activity is first created. */
	BluetoothAdapter adapter = null;
	BTClient _bt;
	ZephyrProtocol _protocol;
	ConnectedListener _NConnListener;
	private final int HEART_RATE = 0x100;
	private final int INSTANT_SPEED = 0x101;;

	//TODO
	private static int heartRate = 0;
	private int timer_interval = 30000; // 30 seconds
	private int hr_check_interval = 5000; // 5 second default
	private Handler alert_handler;


	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_heart_monitor);
		/*Sending a message to android that we are going to initiate a pairing request*/
		IntentFilter filter = new IntentFilter("android.bluetooth.device.action.PAIRING_REQUEST");
		/*Registering a new BTBroadcast receiver from the Main Activity context with pairing request event*/
		this.getApplicationContext().registerReceiver(new BTBroadcastReceiver(), filter);
		// Registering the BTBondReceiver in the application that the status of the receiver has changed to Paired
		IntentFilter filter2 = new IntentFilter("android.bluetooth.device.action.BOND_STATE_CHANGED");
		this.getApplicationContext().registerReceiver(new BTBondReceiver(), filter2);

		//Obtaining the handle to act on the CONNECT button
		TextView tv = (TextView) findViewById(R.id.labelStatusMsg);
		String ErrorText  = "Not Connected";
		tv.setText(ErrorText);
		alert_handler = new Handler();

		Button btnConnect = (Button) findViewById(R.id.ButtonConnect);
		if (btnConnect != null)
		{
			btnConnect.setOnClickListener(new OnClickListener() {
				public void onClick(View v) {
					String BhMacID = "00:07:80:9D:8A:E8";
					adapter = BluetoothAdapter.getDefaultAdapter();

					Set<BluetoothDevice> pairedDevices = adapter.getBondedDevices();

					if (pairedDevices.size() > 0) 
					{
						for (BluetoothDevice device : pairedDevices) 
						{
							if (device.getName().startsWith("HXM")) 
							{
								BluetoothDevice btDevice = device;
								BhMacID = btDevice.getAddress();
								break;

							}
						}


					}

					BluetoothDevice Device = adapter.getRemoteDevice(BhMacID);
					String DeviceName = Device.getName();
					_bt = new BTClient(adapter, BhMacID);
					_NConnListener = new ConnectedListener(Newhandler,Newhandler);
					_bt.addConnectedEventListener(_NConnListener);

					TextView tv1 = (TextView)findViewById(R.id.labelHeartRate);
					tv1.setText("000");

					tv1 = (TextView)findViewById(R.id.labelInstantSpeed);
					tv1.setText("0.0");

					if(_bt.IsConnected())
					{
						_bt.start();
						TextView tv = (TextView) findViewById(R.id.labelStatusMsg);
						String ErrorText  = "Connected to HxM "+DeviceName;
						tv.setText(ErrorText);

						//Start alerts
						

					}
					else
					{
						TextView tv = (TextView) findViewById(R.id.labelStatusMsg);
						String ErrorText  = "Unable to Connect!";
						tv.setText(ErrorText);
					}
				}
			});
		}
		/*Obtaining the handle to act on the DISCONNECT button*/
		Button btnDisconnect = (Button) findViewById(R.id.ButtonDisconnect);
		if (btnDisconnect != null)
		{
			btnDisconnect.setOnClickListener(new OnClickListener() {
				@Override
				/*Functionality to act if the button DISCONNECT is touched*/
				public void onClick(View v) {
					/*Reset the global variables*/
					TextView tv = (TextView) findViewById(R.id.labelStatusMsg);
					String ErrorText  = "Disconnected from HxM!";
					tv.setText(ErrorText);
					//TODO

					
					/*This disconnects listener from acting on received messages*/	
					_bt.removeConnectedEventListener(_NConnListener);
					/*Close the communication with the device & throw an exception if failure*/
					_bt.Close();
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
				Method m = BluetoothDevice.class.getMethod("convertPinToBytes", new Class[] {String.class} );
				byte[] pin = (byte[])m.invoke(device, "1234");
				m = device.getClass().getMethod("setPin", new Class [] {pin.getClass()});
				Object result = m.invoke(device, pin);
				Log.d("BTTest", result.toString());
			} catch (SecurityException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			} catch (NoSuchMethodException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			} catch (IllegalArgumentException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IllegalAccessException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (InvocationTargetException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}


	final  Handler Newhandler = new Handler(){
		public void handleMessage(Message msg)
		{
			TextView tv;
			switch (msg.what)
			{
			//TODO
			case HEART_RATE:
				String InstaneousSpeedText = msg.getData().getString("Instant Speed");
				tv = (TextView)findViewById(R.id.labelInstantSpeed);
				System.out.println("Instaneous speed is "+ InstaneousSpeedText);
				if (tv != null)tv.setText(InstaneousSpeedText);
				String InstantSpeedtext = msg.getData().getString("InstantSpeed");

				break;
			}
			
		}

	};


	//TODO Add timers to check HR and play sounds
	

}
