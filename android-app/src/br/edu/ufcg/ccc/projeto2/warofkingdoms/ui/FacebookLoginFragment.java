package br.edu.ufcg.ccc.projeto2.warofkingdoms.ui;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.util.Log;

import com.facebook.Request;
import com.facebook.Request.GraphUserCallback;
import com.facebook.RequestBatch;
import com.facebook.Response;
import com.facebook.Session;
import com.facebook.SessionState;
import com.facebook.UiLifecycleHelper;
import com.facebook.model.GraphObject;
import com.facebook.model.GraphUser;

public class FacebookLoginFragment extends Fragment {

	private static final String TAG = "LoginFragment";
	private UiLifecycleHelper uiHelper;
	private Session.StatusCallback callback = new Session.StatusCallback() {
		@Override
		public void call(Session session, SessionState state, Exception exception) {
			Log.v(TAG, "onSessionStateChange");
			onSessionStateChange(session, state, exception);
		}
	};

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		uiHelper = new UiLifecycleHelper(getActivity(), callback);
		uiHelper.onCreate(savedInstanceState);
	}

	private void onSessionStateChange(Session session, SessionState state, Exception exception) {
		if (state.isOpened()) {
			Session.setActiveSession(session);
			doBatchRequest();
			//Execute the request
		} else if (state.isClosed()) {
			Log.i(TAG, "Logged out...");
		}
	}
	
	private void doBatchRequest() {
	    String[] requestIds = {"me", "4"};
	    RequestBatch requestBatch = new RequestBatch();
	    for (final String requestId : requestIds) {
	        requestBatch.add(new Request(Session.getActiveSession(), 
	                requestId, null, null, new Request.Callback() {
	            public void onCompleted(Response response) {
	            	String s = "";
	                GraphObject graphObject = response.getGraphObject();
	                if (graphObject != null) {
	                    if (graphObject.getProperty("id") != null) {
	                        s = s + String.format("%s: %s\n", 
	                                graphObject.getProperty("id"), 
	                                graphObject.getProperty("name"));
	                    }
	                }
	                Log.v(TAG,s);
	            }
	        }));
	    }
	    requestBatch.executeAsync();
	}

	@Override
	public void onResume() {
		super.onResume();
		Session session = Session.getActiveSession();
		if (session != null &&
				(session.isOpened() || session.isClosed()) ) {
			onSessionStateChange(session, session.getState(), null);
		}

		uiHelper.onResume();
	}

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		uiHelper.onActivityResult(requestCode, resultCode, data);
	}

	@Override
	public void onPause() {
		super.onPause();
		uiHelper.onPause();
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
		uiHelper.onDestroy();
	}

	@Override
	public void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);
		uiHelper.onSaveInstanceState(outState);
	}
}
