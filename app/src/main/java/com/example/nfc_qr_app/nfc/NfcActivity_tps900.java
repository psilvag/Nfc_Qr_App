package com.example.nfc_qr_app.nfc;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.widget.Toast;

import com.example.nfc_qr_app.MainActivity;
import com.telpo.tps550.api.TelpoException;
import com.telpo.tps550.api.nfc.Nfc;
import com.telpo.tps550.api.util.StringUtil;

import java.lang.ref.WeakReference;

public class NfcActivity_tps900 extends Activity {
	private final String TAG = "NfcActivity_tps900";
	private Nfc nfc = new Nfc(this);
	private static MyHandler handler;
	private static final int CHECK_NFC_TIMEOUT = 1;
	private static final int SHOW_NFC_DATA = 2;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		//setContentView(R.layout.activity_nfc_tps900);

		// Instanciar el Handler estático
		handler = new MyHandler(this);

		// Open NFC and start checking for cards
		try {
			nfc.open();
			checkForNfcCard();
		} catch (TelpoException e) {
			e.printStackTrace();
		}
	}

	private void checkForNfcCard() {
		// Start a thread to read NFC data
		new Thread(new Runnable() {
			@Override
			public void run() {
				byte[] nfcData = null;
				try {
					nfcData = nfc.activate(10 * 1000); // Wait for 10 seconds for NFC card
					if (nfcData != null) {
						handler.sendMessage(handler.obtainMessage(SHOW_NFC_DATA, nfcData));
					} else {
						handler.sendMessage(handler.obtainMessage(CHECK_NFC_TIMEOUT));
					}
				} catch (TelpoException e) {
					e.printStackTrace();
				}
			}
		}).start();
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		try {
			nfc.close();
		} catch (TelpoException e) {
			e.printStackTrace();
		}
	}

	// Clase estática MyHandler
	private static class MyHandler extends Handler {
		private final WeakReference<NfcActivity_tps900> mActivityReference;

		MyHandler(NfcActivity_tps900 activity) {
			mActivityReference = new WeakReference<>(activity);

		}

		@Override
		public void handleMessage(Message msg) {
			NfcActivity_tps900 activity = mActivityReference.get();
			if (activity != null) {
				switch (msg.what) {
					case CHECK_NFC_TIMEOUT:
						Toast.makeText(activity, "NFC card detection timed out", Toast.LENGTH_LONG).show();
						break;
					case SHOW_NFC_DATA:
						byte[] uid_data = (byte[]) msg.obj;
						// Procesar los datos UID
						String nfcData = StringUtil.toHexString(uid_data);
						Log.d(activity.TAG, "NFC Data: " + nfcData);

						// Pasar los datos NFC a MainActivity
						Intent intent = new Intent(activity, MainActivity.class);
						intent.putExtra("nfcData", nfcData);
						activity.startActivity(intent);
						break;
					default:
						break;
				}
			}
		}
	}
}
