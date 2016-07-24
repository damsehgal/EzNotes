package com.example.dam.ezcloud;

import android.content.Context;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.util.HashMap;

/**
 * Created by dam on 24/7/16.
 * This class changes the Password of User
 */
public class ChangePasswordFragment extends MyBasicFragment
{
	public ChangePasswordFragment(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState, Context context)
	{
		super(inflater,container,savedInstanceState,context);
	}
	public View onCreate()
	{
		View rootView = getRootView(R.layout.changepass);
		final EditText currentPassword = (android.widget.EditText) (rootView).findViewById(R.id.curr_pass);
		final EditText newPassword = (EditText) (rootView).findViewById(R.id.new_pass);
		final EditText confirmPassword = (EditText) (rootView).findViewById(R.id.confirm_password);
		Button btn1 = (Button) (rootView).findViewById(R.id.change_password);
		btn1.setOnClickListener(new View.OnClickListener()
		{
			@Override
			public void onClick(View v)
			{

				Log.e("called", "onClick: ");
				if (currentPassword.getText().toString().equals(Home.passWord) && newPassword.getText().toString().equals(confirmPassword.getText().toString()))
				{
					Home.passWord = changePassword(Home.userName, newPassword.getText().toString());
					Toast.makeText(context, "Password changed successfully", Toast.LENGTH_SHORT).show();
					Log.e("changed", "onClick: ");
				}
				else
				{
					Log.e("unchanged", "onClick: ");
					Toast.makeText(context, "Please Refill the form", Toast.LENGTH_SHORT).show();
				}
			}
		});
		return rootView;
	}
	private String changePassword(String userName, String password)
	{
		Log.e("dsasd", "changePassword: ");
		HashMap<String, String> hash = new HashMap<>(5);
		hash.put("sess_ID", PreferenceManager.getDefaultSharedPreferences(context).getString("sess_ID", ""));
		hash.put("username", userName);
		hash.put("password", password);
		PostRequestSend changePass = new PostRequestSend("http://ezcloud.esy.es/ezCloudWebsite/change_pass_app.php?", hash);
		changePass.setTaskDoneListener(new PostRequestSend.TaskDoneListener()
		{
			@Override
			public String onTaskDone(String str)
			{
				Log.e("Change Pass", "onTaskDone: " + str);
				return str;
			}
		});
		changePass.execute();
		return password;
	}

}
