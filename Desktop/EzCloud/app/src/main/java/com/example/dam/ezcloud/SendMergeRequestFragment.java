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
import android.widget.EditText;
import android.widget.Spinner;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by dam on 25/7/16.
 */
//TODO SEND MERGE REQUEST
public class SendMergeRequestFragment extends MyBasicFragment
{
	public static final String TAG = SendMergeRequestFragment.class.getSimpleName();
	Spinner spinnerUserRepo , spinnerToRepo;
	EditText receiverName ,details;
	Button sendMessage;
	private class JSONQuery
	{
		int repoId;
		String masterName;
		String repoName;
		public JSONQuery(int repoId ,String masterName, String repoName)
		{
			this.repoId = repoId;
			this.masterName = masterName;
			this.repoName = repoName;
		}
	}

	public SendMergeRequestFragment(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState, Context context)
	{
		super(inflater, container, savedInstanceState, context);
	}
	@Override
	public View onCreate()
	{
		View rootView = getRootView(R.layout.merge);
		spinnerToRepo = (Spinner) rootView.findViewById(R.id.spinner_receiver_repo);
		spinnerUserRepo = (Spinner) rootView.findViewById(R.id.spinner_sender_repo);
		receiverName = (EditText) rootView.findViewById(R.id.edit_text_to);
		details = (EditText) rootView.findViewById(R.id.edit_text_details);
		sendMessage = (Button) rootView.findViewById(R.id.btn_send_message);
		/*
			spinnerUserRepo.setPrompt("Select User Repo");
			spinnerToRepo.setPrompt("Select To Repo");
		*/
		ArrayAdapter<String> spinnerUserRepoAdapter = new ArrayAdapter<>(context,android.R.layout.simple_spinner_item,onSpinnerClick(Home.userName));
		ArrayAdapter<String> spinnerTORepoAdapter = new ArrayAdapter<>(context,android.R.layout.simple_spinner_item,onSpinnerClick(receiverName.getText().toString()));
		spinnerUserRepo.setAdapter(spinnerUserRepoAdapter);
		spinnerToRepo.setAdapter(spinnerTORepoAdapter);
		spinnerTORepoAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		spinnerUserRepoAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

		return rootView;
	}

	public ArrayList<String> onSpinnerClick(String username)
	{
		HashMap <String,String> hashMap =  new HashMap<>();
		hashMap.put("username",username);
		final ArrayList<String> arr =  new ArrayList<>();
		PostRequestSend postRequestSend = new PostRequestSend("http://ezcloud.esy.es/ezCloudWebsite/get_users_repo.php?",hashMap);
		postRequestSend.setTaskDoneListener(new PostRequestSend.TaskDoneListener()
		{
			@Override
			public String onTaskDone(String str)
			{
				Log.e(TAG, "onTaskDone: " + str );
				try
				{
					JSONArray jsonArray = new JSONArray(str);
					for (int i = 0 ;i < jsonArray.length() ; i++)
					{
						arr.add(jsonArray.getJSONObject(i).getString("repoName"));
					}
				}
				catch (JSONException e)
				{
					Log.e(TAG, "onTaskDone: "  + e);
					e.printStackTrace();
				}
				return null;
			}
		});
		return arr;
	}
}
