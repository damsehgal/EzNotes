package com.example.dam.ezcloud;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Rect;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import net.rdrei.android.dirchooser.DirectoryChooserActivity;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;

public class Home extends AppCompatActivity
{
	static String userName, passWord;
	FragmentManager fragmentManager;
	FragmentTransaction fragmentTransaction;
	Uri uri;
	ListView mDrawerList;
	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_home);
		ArrayAdapter<String> mAdapter;
		mDrawerList = (ListView) findViewById(R.id.navList);
		String[] osArray = {"CHANGE PASSWORD", "OPEN FILE", "PUSH REQUEST", "CLONE", "PULL VERSION", "MERGE REQUEST", "Create New", "LOGOUT"};
		mAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, osArray);
		mDrawerList.setAdapter(mAdapter);
		mDrawerList.setOnItemClickListener(new MyOnItemClickListener());
		Intent intent = getIntent();
		userName = intent.getStringExtra(MainActivity.USERNAME_KEY);
		passWord = intent.getStringExtra(MainActivity.PASSWORD_KEY);
	}
	public void onActivityResult(int requestCode, int resultCode, Intent data)
	{
		// TODO Auto-generated method stub
		Log.e("isItCalled", "onActivityResult: ");
		switch (requestCode)
		{
			case OpenFileFragment.PICK_FILE_RESULT_CODE:
				if (resultCode == Activity.RESULT_OK)
				{
					uri = data.getData();
					Log.e("URI", "onActivityResult: " + uri.toString());
					String f2 = "";
					try
					{
						BufferedReader buff = new BufferedReader(new InputStreamReader(this.getContentResolver().openInputStream(uri), "UTF-8"));
						String line;
						while ((line = buff.readLine()) != null)
						{
							f2 += line + "\n";
						}
						OpenFileFragment.et1.setText(f2);
					}
					catch (FileNotFoundException e)
					{
						Toast.makeText(this, "File Not Found", Toast.LENGTH_SHORT).show();
					}
					catch (IOException e)
					{
						Toast.makeText(this, "IO Exception", Toast.LENGTH_SHORT).show();
					}
					Log.e("activity", "onActivityResult: " + f2);
				}
				else
				{
					Log.e("dsaasd", "onActivityResult: ");
				}
				break;
			case CreateNewRepoWithPush.REQUEST_DIRECTORY_PUSH:
				if (resultCode == DirectoryChooserActivity.RESULT_CODE_DIR_SELECTED)
					CreateNewRepoWithPush.path.setText(data.getStringExtra(DirectoryChooserActivity.RESULT_SELECTED_DIR));
				else
					CreateNewRepoWithPush.path.setText("nothing selected");
				break;
		}
	}
	public boolean dispatchTouchEvent(MotionEvent event)
	{
		if (event.getAction() == MotionEvent.ACTION_DOWN)
		{
			View v = getCurrentFocus();
			if (v instanceof EditText)
			{
				Rect outRect = new Rect();
				v.getGlobalVisibleRect(outRect);
				if (!outRect.contains((int) event.getRawX(), (int) event.getRawY()))
				{
					v.clearFocus();
					InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
					imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
				}
			}
		}
		return super.dispatchTouchEvent(event);
	}
	public class MyOnItemClickListener implements AdapterView.OnItemClickListener
	{
		@Override
		public void onItemClick(AdapterView<?> parent, View view, int position, long id)
		{
			Log.e("HOME", "onItemClick: called");
			BlankFragment blankFragment = new BlankFragment();
			BlankFragment.OnCreateViewCalledListener onCreateViewCalledListener = new BlankFragment.OnCreateViewCalledListener()
			{
				@Override
				public void onCreateViewCalled(int position)
				{
				}
			};
			blankFragment.setPosition(position);
			if (position == 1)
			{
				uri = blankFragment.getUri();
			}
			blankFragment.setOnCreateViewCalledListener(onCreateViewCalledListener);
			fragmentManager = getSupportFragmentManager();
			fragmentTransaction = fragmentManager.beginTransaction();
			fragmentTransaction.replace(R.id.frame_layout, blankFragment, null);
			Log.e("HOME", "onClick: " + userName + " " + passWord);
			fragmentTransaction.commit();
			((DrawerLayout) findViewById(R.id.drawer_layout)).closeDrawer(mDrawerList);
		}
	}
}