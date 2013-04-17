package com.example.btheartrate;

import zephyr.android.HxMBT.BTClient;
import zephyr.android.HxMBT.ConnectListenerImpl;
import zephyr.android.HxMBT.ConnectedEvent;
import zephyr.android.HxMBT.ZephyrPacketArgs;
import zephyr.android.HxMBT.ZephyrPacketEvent;
import zephyr.android.HxMBT.ZephyrPacketListener;
import zephyr.android.HxMBT.ZephyrProtocol;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;

public class ConnectedListener extends ConnectListenerImpl{
	private final Handler _OldHandler;
	private final Handler _aNewHandler;
	private final int GP_MSG_ID = 0x20;
	private final int GP_HANDLER_ID = 0x20;
	private final int HR_SPD_DIST_PACKET =0x26;

	private final int HEART_RATE = 0x100;
	private final int INSTANT_SPEED = 0x101;
	private final int BATTERY_MESSAGE = 0x102;
	private final HRSpeedDistPacketInfo HRSpeedDistPacket = new HRSpeedDistPacketInfo();

	public ConnectedListener(Handler handler, Handler _NewHandler) {
		super(handler, null);
		_OldHandler= handler;
		_aNewHandler = _NewHandler;
	}

	@Override
	public void Connected(ConnectedEvent<BTClient> eventArgs) {
		System.out.println(String.format("Connected to BioHarness %s.", eventArgs.getSource().getDevice().getName()));

		// Creates a new ZephyrProtocol object and passes it the BTComms object
		ZephyrProtocol _protocol = new ZephyrProtocol(eventArgs.getSource().getComms());
		_protocol.addZephyrPacketEventListener(new ZephyrPacketListener() {
			@Override
			public void ReceivedPacket(ZephyrPacketEvent eventArgs) {
				ZephyrPacketArgs msg = eventArgs.getPacket();
				byte CRCFailStatus;
				byte RcvdBytes;

				CRCFailStatus = msg.getCRCStatus();
				RcvdBytes = msg.getNumRvcdBytes() ;
				if (HR_SPD_DIST_PACKET == msg.getMsgID()) {
					byte [] DataArray = msg.getBytes();

					// Displaying the Heart Rate
					int HRate =  HRSpeedDistPacket.GetHeartRate(DataArray);
					Message text1;
					Bundle b1 = new Bundle();
					text1 = _aNewHandler.obtainMessage(HEART_RATE);
					b1.putString("HeartRate", String.valueOf(HRate));
					text1.setData(b1);
					_aNewHandler.sendMessage(text1);
					System.out.println("Heart Rate is "+ HRate);
					//TODO


					// Displaying the Instant Speed
					double InstantSpeed = HRSpeedDistPacket.GetInstantSpeed(DataArray);

					text1 = _aNewHandler.obtainMessage(INSTANT_SPEED);
					b1.putString("InstantSpeed", String.valueOf(InstantSpeed));
					text1.setData(b1);
					_aNewHandler.sendMessage(text1);
					System.out.println("Instant Speed is "+ InstantSpeed);

					// Displaying the battery level
					byte batteryLevel = HRSpeedDistPacket.GetBatteryChargeInd(DataArray);

					text1 = _aNewHandler.obtainMessage(BATTERY_MESSAGE);
					b1.putString("BatteryMessage", String.valueOf(batteryLevel));
					text1.setData(b1);
					_aNewHandler.sendMessage(text1);
				}
			}
		});
	}

}
