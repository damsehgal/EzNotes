package com.example.dam.ezcloud;


import android.app.DownloadManager;
import android.content.Intent;
import android.net.Uri;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;

public class Home extends AppCompatActivity
{

	FragmentManager fragmentManager;
	FragmentTransaction fragmentTransaction;
	static String userName, passWord;
	Uri uri;
	ListView mDrawerList;

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_home);

		ArrayAdapter<String> mAdapter;

		mDrawerList = (ListView) findViewById(R.id.navList);

		String[] osArray = {"CHANGE PASSWORD", "OPEN FILE", "PUSH", "PUSH REQUEST", "FORK", "PULL VERSION", "MERGE REQUEST", "Create New", "LOGOUT"};
		mAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, osArray);
		mDrawerList.setAdapter(mAdapter);
		mDrawerList.setOnItemClickListener(new MyOnItemClickListener());
		Intent intent = getIntent();
		userName = intent.getStringExtra(MainActivity.USERNAME_KEY);
		passWord = intent.getStringExtra(MainActivity.PASSWORD_KEY);


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