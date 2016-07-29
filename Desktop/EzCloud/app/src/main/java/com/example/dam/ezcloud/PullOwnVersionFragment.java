package com.example.dam.ezcloud;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by dam on 25/7/16.
 */
public class PullOwnVersionFragment extends MyBasicFragment
{
	private static final String TAG = PullOwnVersionFragment.class.getSimpleName();
	String editText1, editText2;
	Spinner s1, s2;
	boolean flag = false;
	public PullOwnVersionFragment(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState, Context context)
	{
		super(inflater, container, savedInstanceState, context);
	}
	@Override
	public View onCreate()
	{
		View rootView = getRootView(R.layout.pull_version);
		s1 = (Spinner) rootView.findViewById(R.id.select_repository);
		s2 = (Spinner) rootView.findViewById(R.id.select_version);
		setSpinnerAdapter(Home.userName, s1);
		s1.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener()
		{
			@Override
			public void onItemSelected(AdapterView<?> parent, View view, int position, long id)
			{
				Log.e(TAG, "onItemSelected: called ... " );
				if (flag)
				{
					Log.e(TAG, "onItemSelected: "+ parent.getAdapter().getItem(position).toString());
					setSpinnerAdapter(parent.getAdapter().getItem(position).toString(),Home.userName,s2);
					editText1 = parent.getAdapter().getItem(position).toString();
				}
				else
				{
					flag = true ;

				}
			}
			@Override
			public void onNothingSelected(AdapterView<?> parent)
			{
				Toast.makeText(context, "Nothing Selected", Toast.LENGTH_SHORT).show();
			}
		});
		s2.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener()
		{
			@Override
			public void onItemSelected(AdapterView<?> parent, View view, int position, long id)
			{
				editText2 = parent.getAdapter().getItem(position).toString();
			}
			@Override
			public void onNothingSelected(AdapterView<?> parent)
			{
				Toast.makeText(context, "Nothing Selected", Toast.LENGTH_SHORT).show();
			}
		});
		Button btn1 = (Button) rootView.findViewById(R.id.btn_pull_version);
		btn1.setOnClickListener(new Btn1OnClickListener());
		return rootView;
	}
	public void setSpinnerAdapter(String username, final Spinner spinner)
	{
		HashMap<String, String> hashMap = new HashMap<>();
		hashMap.put("username", username);
		final ArrayList<String> arr = new ArrayList<>();
		PostRequestSend postRequestSend = new PostRequestSend("http://ezcloud.esy.es/ezCloudWebsite/get_users_repo.php?", hashMap);
		postRequestSend.setTaskDoneListener(new PostRequestSend.TaskDoneListener()
		{
			@Override
			public String onTaskDone(String str)
			{
				Log.e(TAG, "onTaskDone: " + str);
				try
				{
					JSONArray jsonArray = new JSONArray(str);
					for (int i = 0; i < jsonArray.length(); i++)
					{
						arr.add(jsonArray.getJSONObject(i).getString("repoName"));
					}
				}
				catch (JSONException e)
				{
					Log.e(TAG, "onTaskDone: " + e);
					e.printStackTrace();
				}
				spinner.setAdapter(new ArrayAdapter<>(context, android.R.layout.simple_spinner_item, arr));
				Log.e(TAG, "onTaskDone: completed." );
				return null;
			}
		});
		postRequestSend.execute();
	}
	public void setSpinnerAdapter(String reponame , String username , final Spinner spinner)
	{
		HashMap <String,String> hashMap  = new HashMap<>();
		hashMap.put("reponame",reponame);
		hashMap.put("username",username);
		PostRequestSend prs = new PostRequestSend("http://ezcloud.esy.es/ezCloudWebsite/get_repo_version.php?",hashMap);
		prs.setTaskDoneListener(new PostRequestSend.TaskDoneListener()
		{
			@Override
			public String onTaskDone(String str) throws JSONException
			{
				Log.e(TAG, "onTaskDone: "+str );
				int length = Integer.parseInt(str);
				ArrayList<String> arr = new ArrayList<>();
				for (int i = 0 ; i <  length;i++)
					arr.add(i,""+(i+1));
				spinner.setAdapter(new ArrayAdapter<>(context,android.R.layout.simple_spinner_item,arr));
				return null;
			}
		});
		prs.execute();

	}
	public class Btn1OnClickListener implements View.OnClickListener
	{
		@Override
		public void onClick(View v)
		{
			String path = editText1 + "/" + editText2 + "/" + editText1;
			DownloadFileFTP downloadFileFTP = new DownloadFileFTP(context, path, new DownloadFileFTP.OnFileDownloadListener()
			{
				@Override
				public void onFileDownload(String path)
				{
					Toast.makeText(context, "Folder successfully saved with path= " + path, Toast.LENGTH_SHORT).show();
				}
			});
			downloadFileFTP.execute();
		}
	}
}
