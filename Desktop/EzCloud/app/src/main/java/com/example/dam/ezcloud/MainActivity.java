package com.example.dam.ezcloud;
/**
 * Created by dam on 23/7/16.
 */
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.provider.Settings;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.dd.CircularProgressButton;

import java.util.HashMap;

public class MainActivity extends AppCompatActivity
{
	public static final String USERNAME_KEY = "dsaklksdzbcsdzxbcsmz";
	public static final String PASSWORD_KEY = "zdksanO;aslkaaddav";
	CircularProgressButton login, signUp;
	EditText userId, passWord;
	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		Log.e("MainActivity", "onCreate: " + "called");
		final boolean isLoggedIn = checkSessionIDInSharedPrefs();
		login = (CircularProgressButton) findViewById(R.id.login_btn);
		login.setIndeterminateProgressMode(true);
		signUp = (CircularProgressButton) findViewById(R.id.sign_up_btn);
		signUp.setIndeterminateProgressMode(true);
		userId = (EditText) (findViewById(R.id.user_name));
		passWord = (EditText) (findViewById(R.id.password));
		ConnectivityManager cMgr = (ConnectivityManager) getSystemService(CONNECTIVITY_SERVICE);
		NetworkInfo netInfo = cMgr.getActiveNetworkInfo();
		if (netInfo != null && netInfo.isConnected())
		{
			Log.e("isLoggedIn", "onCreate: " + isLoggedIn);
			if (isLoggedIn)
			{
				String sessID = PreferenceManager.getDefaultSharedPreferences(MainActivity.this).getString("sess_ID", "");
				Log.e("isLoggedIn", "onCreate: " + sessID);
				HashMap<String, String> hashMap = new HashMap<>();
				hashMap.put("sess_id", sessID);
				hashMap.put("device", Settings.Secure.getString(this.getContentResolver(), Settings.Secure.ANDROID_ID));
				PostRequestSend postRequestSend = new PostRequestSend("http://ezcloud.esy.es/ezCloudWebsite/index_app.php?", hashMap);
				postRequestSend.setTaskDoneListener(new PostRequestSend.TaskDoneListener()
				{
					@Override
					public String onTaskDone(String str)
					{
						Log.e("Main Activity", "onTaskDone: " + str);
						try
						{
							Log.e("MainActivity is Logge", "onTaskDone: " + str);
							String[] userInfo = str.split("<br>");
							for (String temp : userInfo)
								Log.e("MainActivity", "onTaskDone: " + temp);
							Intent intent = new Intent(MainActivity.this, Home.class);
							intent.putExtra(USERNAME_KEY, userInfo[0]);
							intent.putExtra(PASSWORD_KEY, userInfo[1]);
							PreferenceManager.getDefaultSharedPreferences(MainActivity.this).edit().putString("sess_ID", userInfo[2]).commit();
							startActivity(intent);
						}
						catch (IndexOutOfBoundsException e)
						{
							// Session Id is expired
							PreferenceManager.getDefaultSharedPreferences(MainActivity.this).edit().putString("sess_ID", "").commit();
							recreate();
						}
						return null;
					}
				});
				postRequestSend.execute();
			}
			else
			{
				try
				{
					login.setOnClickListener(new LoginOnClick());
					signUp.setOnClickListener(new SignUpOnClick());
					Log.e("internet connected", "onCreate: ");
				}
				catch (Exception ex)
				{
					Toast.makeText(MainActivity.this, "Plz check your internet connection", Toast.LENGTH_SHORT).show();
				}
			}
		}
		else
		{
			Log.e("not ", "onCreate: ");
			Toast.makeText(MainActivity.this, "Plz check your internet connection", Toast.LENGTH_SHORT).show();
		}
	}
	public class LoginOnClick implements View.OnClickListener
	{
		@Override
		public void onClick(View v)
		{
			login.setProgress(50);
			if (userId.getText().toString().isEmpty() || passWord.getText().toString().isEmpty())
			{
				Toast.makeText(MainActivity.this, "Plz fill all the fields", Toast.LENGTH_SHORT).show();
				login.setProgress(-1);
				Handler handler = new Handler();
				handler.postDelayed(new Runnable()
				{
					@Override
					public void run()
					{
						login.setProgress(0);
					}
				}, 2000);
			}
			else
			{
				HashMap<String, String> hashMap = new HashMap<>();
				hashMap.put("username", userId.getText().toString());
				hashMap.put("password", passWord.getText().toString());
				hashMap.put("device", Settings.Secure.getString(MainActivity.this.getContentResolver(), Settings.Secure.ANDROID_ID));
				PostRequestSend postRequestSend = new PostRequestSend("http://ezcloud.esy.es/ezCloudWebsite/index_app.php?", hashMap);
				postRequestSend.execute();
				postRequestSend.setTaskDoneListener(new PostRequestSend.TaskDoneListener()
				{
					@Override
					public String onTaskDone(String str)
					{
						Log.e("Main Activity", "onTaskDone: " + str + " ");
						if (str.charAt(0) == 't')
						{
							SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(MainActivity.this);
							SharedPreferences.Editor ed = sharedPreferences.edit();
							ed.putString("sess_ID", str.substring(8));
							ed.commit();
							Intent intent = new Intent(MainActivity.this, Home.class);
							login.setProgress(100);
							intent.putExtra(USERNAME_KEY, userId.getText().toString());
							intent.putExtra(PASSWORD_KEY, passWord.getText().toString());
							startActivity(intent);
						}
						else if (str.charAt(0) == 'N')
						{
							Toast.makeText(MainActivity.this, "Username does not exist", Toast.LENGTH_SHORT).show();
							login.setProgress(-1);
							Handler handler = new Handler();
							handler.postDelayed(new Runnable()
							{
								@Override
								public void run()
								{
									login.setProgress(0);
								}
							}, 2000);
						}
						else
						{
							Toast.makeText(MainActivity.this, "Password is Incorrect", Toast.LENGTH_SHORT).show();
							login.setProgress(-1);
							Handler handler = new Handler();
							handler.postDelayed(new Runnable()
							{
								@Override
								public void run()
								{
									login.setProgress(0);
								}
							}, 2000);
						}
						return str;
					}
				});
			}
		}
	}
	public boolean checkSessionIDInSharedPrefs()
	{
		SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(MainActivity.this);
		String sess_ID = sharedPreferences.getString("sess_ID", "");
		Log.e("MainActivity", "checkSessionIDInSharedPrefs: " + sess_ID);
		return !sess_ID.equals("");
	}
	public class SignUpOnClick implements View.OnClickListener
	{
		@Override
		public void onClick(View v)
		{
			signUp.setProgress(50);
			if (userId.getText().toString().isEmpty() || passWord.getText().toString().isEmpty())
			{
				Toast.makeText(MainActivity.this, "Plz fill all the fields", Toast.LENGTH_SHORT).show();
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
				hashMap.put("username", userId.getText().toString());
				hashMap.put("password", passWord.getText().toString());
				hashMap.put("device", Settings.Secure.getString(MainActivity.this.getContentResolver(), Settings.Secure.ANDROID_ID));
				PostRequestSend postRequestSend = new PostRequestSend("http://ezcloud.esy.es/ezCloudWebsite/signup_app.php?", hashMap);
				postRequestSend.execute();
				postRequestSend.setTaskDoneListener(new PostRequestSend.TaskDoneListener()
				{
					@Override
					public String onTaskDone(String str)
					{
						Log.e("Main Activity", "onTaskDone: " + str);
						if (str.charAt(0) == 'U')
						{
							Toast.makeText(MainActivity.this, "Username Already exist", Toast.LENGTH_SHORT).show();
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
							Toast.makeText(MainActivity.this, "ID created plz Login", Toast.LENGTH_SHORT).show();
							signUp.setProgress(100);
							login.performClick();
						}
						return str;
					}
				});
			}
		}
	}
}
