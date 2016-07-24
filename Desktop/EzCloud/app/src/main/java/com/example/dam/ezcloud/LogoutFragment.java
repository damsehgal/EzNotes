package com.example.dam.ezcloud;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.HashMap;

/**
 * Created by dam on 25/7/16.
 */
public class LogoutFragment extends MyBasicFragment
{
	Activity activity;
	public LogoutFragment(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState, Context context,Activity activity)
	{
		super(inflater, container, savedInstanceState, context);
		this.activity = activity;
	}
	@Override
	public View onCreate()
	{
		HashMap<String, String> hashMap = new HashMap<>();
		hashMap.put("sess_ID", PreferenceManager.getDefaultSharedPreferences(context).getString("sess_ID", ""));
		PostRequestSend prs = new PostRequestSend("http://ezcloud.esy.es/ezCloudWebsite/logout_app.php?", hashMap);
		prs.setTaskDoneListener(new PostRequestSend.TaskDoneListener()
		{
			@Override
			public String onTaskDone(String str)
			{
				SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
				SharedPreferences.Editor ed = preferences.edit();
				ed.putString("sess_ID", "");
				ed.commit();
				Log.e("PostResponse", "onTaskDone: " + str);
				Log.e("LOGOUT", "onCreateView: " + preferences.getString("sess_ID", ""));
				Intent intent = new Intent(context, MainActivity.class);
				activity.startActivity(intent);
				return null;

			}
		});
		prs.execute();

		return getRootView(R.layout.openfile);
	}
}
