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
import android.widget.Toast;

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
	Spinner spinnerUserRepo, spinnerToRepo;
	EditText receiverName, details;
	Button sendMessage;
	String finalReceiver;
	String finalUserRepo;
	String finalReceiverRepo;
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
		spinnerUserRepo.setFocusable(true);
		spinnerUserRepo.setFocusableInTouchMode(true);
		spinnerUserRepo.requestFocus();
		spinnerToRepo.setFocusableInTouchMode(true);
		spinnerToRepo.setFocusable(true);
		spinnerToRepo.requestFocus();
		receiverName = (EditText) rootView.findViewById(R.id.edit_text_to);
		details = (EditText) rootView.findViewById(R.id.edit_text_details);
		sendMessage = (Button) rootView.findViewById(R.id.btn_send_message);

		receiverName.setOnFocusChangeListener(new View.OnFocusChangeListener()
		{
			@Override
			public void onFocusChange(View v, boolean hasFocus)
			{
				Log.e(TAG, "onFocusChange: I am Here");
				setSpinnerAdapter(receiverName.getText().toString(), spinnerToRepo);
				Log.e(TAG, "onFocusChange: but am I here");
			}
		});
		setSpinnerAdapter(Home.userName, spinnerUserRepo);
		spinnerToRepo.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener()
		{

			@Override
			public void onItemSelected(AdapterView<?> parent, View view, int position, long id)
			{
				finalUserRepo = (String) parent.getAdapter().getItem(position);
				finalReceiver = receiverName.getText().toString();
			}
			@Override
			public void onNothingSelected(AdapterView<?> parent)
			{

			}
		});
		spinnerUserRepo.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener()
		{
			@Override
			public void onItemSelected(AdapterView<?> parent, View view, int position, long id)
			{
				finalReceiverRepo = (String) parent.getAdapter().getItem(position);
			}
			@Override
			public void onNothingSelected(AdapterView<?> parent)
			{
				Toast.makeText(context, "Please select ", Toast.LENGTH_SHORT).show();
			}
		});
		sendMessage.setOnClickListener(new ButtonOnClickListener());
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
				return null;
			}
		});
		postRequestSend.execute();
	}
	private class ButtonOnClickListener implements View.OnClickListener
	{
		@Override
		public void onClick(View v)
		{
			HashMap <String,String> hash = new HashMap<>();
			hash.put("sender",Home.userName);
			hash.put("receiver",finalReceiver);
			hash.put("repo_sender_name",finalUserRepo);
			hash.put("repo_receiver_name",finalReceiverRepo);
			hash.put("details",details.getText().toString());
			Log.e(TAG, "onClick: really" );
			PostRequestSend postRequest = new PostRequestSend("http://ezcloud.esy.es/ezCloudWebsite/sendNewMessage.php?",hash);
			postRequest.setTaskDoneListener(new PostRequestSend.TaskDoneListener()
			{

				@Override
				public String onTaskDone(String str) throws JSONException
				{
					Log.e(TAG, "onTaskDone: am I here ...?" );
					if(str.charAt(0) == 'n')
						Toast.makeText(context, "Message not send", Toast.LENGTH_SHORT).show();
					else
						Toast.makeText(context, "Message successfully send", Toast.LENGTH_SHORT).show();
					return null;
				}
			});
			postRequest.execute();
		}
	}
}
