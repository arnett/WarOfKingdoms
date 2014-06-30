package br.edu.ufcg.ccc.projeto2.warofkingdoms.service;

import android.app.Service;
import android.content.Intent;
import android.os.CountDownTimer;
import android.os.IBinder;
import android.util.Log;

public class TimerService extends Service{

	private final static String TAG = "TimerService";
	public static final long START_TIME = 60 *1000;
	private final long INTERVAL = 1 * 1000;

	public static final String COUNTDOWN = "br.edu.ufcg.ccc.projeto2.warofkingdoms.service.countdown";
	private Intent bi = new Intent(COUNTDOWN);

	private CountDownTimer countDownTimer = null;

	@Override
	public void onCreate() {       
		super.onCreate();

		Log.i(TAG, "Starting timer...");

		countDownTimer = new CountDownTimer(START_TIME, INTERVAL) {
			@Override
			public void onTick(long millisUntilFinished) {

				bi.putExtra("countdown", millisUntilFinished);
				sendBroadcast(bi);
			}

			@Override
			public void onFinish() {
				bi.putExtra("countdown", 0L);
				sendBroadcast(bi);
				Log.i(TAG, "Timer finished");
			}
		};

		countDownTimer.start();
	}

	@Override
	public void onDestroy() {

		countDownTimer.cancel();
		Log.i(TAG, "Timer cancelled");
		super.onDestroy();
	}

	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {       
		return super.onStartCommand(intent, flags, startId);
	}

	@Override
	public IBinder onBind(Intent arg0) {       
		return null;
	}
}
