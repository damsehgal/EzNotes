package com.example.dam.ezcloud;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.provider.Settings;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.dd.CircularProgressButton;

import java.util.HashMap;

public class SignupActivity extends AppCompatActivity
{
	private static final String TAG = SignupActivity.class.getSimpleName();
	CircularProgressButton signUp, logIn;
	EditText password, username, rePassword;
	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_signup);
		CheckNetConnection checkNetConnection = new CheckNetConnection(SignupActivity.this);
		signUp = (CircularProgressButton) findViewById(R.id.signup_signup_btn);
		logIn = (CircularProgressButton) findViewById(R.id.sign_up_login_btn);
		password = (EditText) findViewById(R.id.signup_password);
		rePassword = (EditText) findViewById(R.id.signup_re_password);
		username = (EditText) findViewById(R.id.signup_user_name);
		signUp.setIndeterminateProgressMode(true);
		signUp.setProgress(0);
		signUp.setOnClickListener(new View.OnClickListener()
		{
			@Override
			public void onClick(View v)
			{
				signUp.setProgress(50);
				if (username.getText().toString().isEmpty() || password.getText().toString().isEmpty() || rePassword.getText().toString().isEmpty() || !password.getText().toString().equals(rePassword.getText().toString()))
				{
					Toast.makeText(SignupActivity.this, "Plz fill all the fields", Toast.LENGTH_SHORT).show();
					signUp.setProgress(-1);
					Handler handler = new Handler();
					handler.postDelayed(new Runnable()
					{
						@Override
						public void run()
						{
							signUp.setProgress(0);
						}
					}, 2000);
				}
				else
				{
					HashMap<String, String> hashMap = new HashMap<>();
					hashMap.put("username", username.getText().toString());
					hashMap.put("password", password.getText().toString());
					hashMap.put("device", Settings.Secure.getString(SignupActivity.this.getContentResolver(), Settings.Secure.ANDROID_ID));
					PostRequestSend postRequestSend = new PostRequestSend("http://ezcloud.esy.es/ezCloudWebsite/signup_app.php?", hashMap);
					postRequestSend.setTaskDoneListener(new PostRequestSend.TaskDoneListener()
					{
						@Override
						public String onTaskDone(String str)
						{
							Log.e("Main Activity", "onTaskDone: " + str);
							if (str.equals("") || str.charAt(0) == 'U')
							{
								if (str.equals(""))
								{
									Toast.makeText(SignupActivity.this, "Check Your Connection", Toast.LENGTH_SHORT).show();
								}
								else
								{
									Toast.makeText(SignupActivity.this, "Username Already exist", Toast.LENGTH_SHORT).show();
								}
								signUp.setProgress(-1);
								Handler handler = new Handler();
								handler.postDelayed(new Runnable()
								{
									@Override
									public void run()
									{
										signUp.setProgress(0);
									}
								}, 2000);
							}
							else if (str.charAt(0) == 'I')
							{
								Toast.makeText(SignupActivity.this, "ID created", Toast.LENGTH_SHORT).show();
								signUp.setProgress(100);
								Intent intent = new Intent(SignupActivity.this, Home2.class);
								intent.putExtra(MainActivity.USERNAME_KEY, username.getText().toString());
								intent.putExtra(MainActivity.PASSWORD_KEY, password.getText().toString());
								startActivity(intent);
								finish();
							}
							return str;
						}
					});
					try
					{
						postRequestSend.execute();
					}
					catch (Exception e)
					{
						Toast.makeText(SignupActivity.this, "Check Your Connection", Toast.LENGTH_SHORT).show();
					}
				}
			}
		});
		logIn.setOnClickListener(new View.OnClickListener()
		{
			@Override
			public void onClick(View v)
			{
				Intent intent = new Intent(SignupActivity.this, MainActivity.class);
				startActivity(intent);
				finish();
			}
		});
		Log.e(TAG, "onCreate: " + checkNetConnection.isConnectingToInternet());
	}
}
