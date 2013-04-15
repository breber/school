package com.brianreber.lab09;

import java.io.IOException;
import java.util.Arrays;

import android.app.Activity;
import android.app.PendingIntent;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.IntentFilter.MalformedMimeTypeException;
import android.net.Uri;
import android.nfc.FormatException;
import android.nfc.NdefMessage;
import android.nfc.NdefRecord;
import android.nfc.NfcAdapter;
import android.nfc.Tag;
import android.nfc.tech.Ndef;
import android.nfc.tech.NfcA;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;

public class MainActivity extends Activity {

	private IntentFilter[] intentFiltersArray;
	private String[][] techListsArray;
	private PendingIntent pendingIntent;
	private NfcAdapter mAdapter;
	private boolean isWriting = false;
	private EditText editText;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		editText = (EditText) findViewById(R.id.editText1);

		Button open = (Button) findViewById(R.id.button1);
		open.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				String contents = editText.getText().toString();
				startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(contents)));
			}
		});

		Button write = (Button) findViewById(R.id.button2);
		write.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				String contents = editText.getText().toString();
				if (contents.matches("http(s)?://.*")) {
					isWriting = true;
					editText.setEnabled(false);
				}
			}
		});

		mAdapter = NfcAdapter.getDefaultAdapter(this);

		pendingIntent = PendingIntent.getActivity(this, 0, new Intent(this,
				getClass()).addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP), 0);

		IntentFilter ndef = new IntentFilter(NfcAdapter.ACTION_NDEF_DISCOVERED);
		try {
			ndef.addDataType("*/*");
		} catch (MalformedMimeTypeException e) {
			throw new RuntimeException("fail", e);
		}
		intentFiltersArray = new IntentFilter[] { ndef, };

		techListsArray = new String[][] { new String[] { NfcA.class.getName() } };
	}

	@Override
	public void onPause() {
		super.onPause();
		mAdapter.disableForegroundDispatch(this);
	}

	@Override
	public void onResume() {
		super.onResume();
		mAdapter.enableForegroundDispatch(this, pendingIntent, intentFiltersArray, techListsArray);
	}

	@Override
	public void onNewIntent(Intent intent) {
		final Tag tagFromIntent = intent.getParcelableExtra(NfcAdapter.EXTRA_TAG);
		Log.d("LST", Arrays.toString(tagFromIntent.getTechList()));
		Log.d("TAG", tagFromIntent.toString());
		final Ndef ndef = Ndef.get(tagFromIntent);
		Log.d("NDF", ndef + "");
		if (isWriting) {
			final String contents = editText.getText().toString();

			new Thread(new Runnable() {
				@Override
				public void run() {
					try {
						ndef.connect();
						
						ndef.writeNdefMessage(new NdefMessage(NdefRecord.createUri(contents)));

						ndef.close();
					} catch (IOException e) {
						e.printStackTrace();
					} catch (FormatException e) {
						e.printStackTrace();
					}
				}
			}).start();

			editText.setEnabled(true);
			isWriting = false;
		} else {
			NdefMessage msg = ndef.getCachedNdefMessage();
			NdefRecord record = msg.getRecords()[0];
			editText.setText("http://" + new String(record.getPayload()).trim());
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

}
