package com.example.dam.ezcloud;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;
import java.util.HashMap;

import co.dift.ui.SwipeToAction;

/**
 * Created by dam on 25/7/16.
 */
public class PushRequestFragment extends MyBasicFragment
{
	private static final String TAG = PushRequestFragment.class.getSimpleName();
	ArrayList<SingleMessage> arrayList;
	RecyclerView recyclerView;
	MyAdapter adapter;
	SwipeToAction swipeToAction;
	public PushRequestFragment(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState, Context context)
	{
		super(inflater, container, savedInstanceState, context);
	}
	@Override
	public View onCreate()
	{
		final View rootView = getRootView(R.layout.push_request);
		recyclerView = (RecyclerView) rootView.findViewById(R.id.list_view_container);
		final HashMap<String, String> hashMap = new HashMap<>(1);
		hashMap.put("receiver", Home2.userName);
		arrayList = new ArrayList<>();
		LinearLayoutManager layoutManager = new LinearLayoutManager(context);
		recyclerView.setLayoutManager(layoutManager);
		recyclerView.setHasFixedSize(true);
		PostRequestSend postRequestSend = new PostRequestSend("http://ezcloud.esy.es/ezCloudWebsite/receiveMessages.php?", hashMap);
		postRequestSend.setContext(context);
		postRequestSend.setTaskDoneListener(new PostRequestSend.TaskDoneListener()
		{
			@Override
			public String onTaskDone(String str) throws JSONException
			{
				if (str.equals(""))
				{
					Toast.makeText(context, "Check your connection", Toast.LENGTH_SHORT).show();
				}
				else
				{
					Log.e(TAG, "onTaskDone: " + str);
					JSONArray jsonArray = new JSONArray(str);
					for (int i = 0; i < jsonArray.length(); i++)
					{
						arrayList.add(new SingleMessage(jsonArray.getJSONObject(i)));
						Log.e(TAG, "onTaskDone: here ?");
					}
					adapter = new MyAdapter(arrayList, context);
					recyclerView.setAdapter(adapter);
					try
					{
						swipeToAction = new SwipeToAction(recyclerView, new SwipeToAction.SwipeListener<SingleMessage>()
						{
							@Override
							public boolean swipeLeft(SingleMessage itemData)
							{
								removeItem(itemData);
								return true;
							}
							@Override
							public boolean swipeRight(SingleMessage itemData)
							{
								removeItem(itemData);
								return true;
							}
							@Override
							public void onClick(SingleMessage itemData)
							{
							}
							@Override
							public void onLongClick(SingleMessage itemData)
							{
							}
						});
					}
					catch (Exception e)
					{
						Log.e(TAG, "onTaskDone: " , e);
					}
				}
				return null;
			}
		});
		try
		{
			postRequestSend.execute();
		}
		catch (Exception e)
		{
			Toast.makeText(context, "Check your connection", Toast.LENGTH_SHORT).show();
		}
		return rootView;
	}
	private int removeItem(SingleMessage singleMessage)
	{
		int position = arrayList.indexOf(singleMessage);
		arrayList.remove(singleMessage);
		HashMap<String, String> hashMap = new HashMap<>(1);
		hashMap.put("message_id", singleMessage.messageId);
		PostRequestSend postRequestSend = new PostRequestSend("http://ezcloud.esy.es/ezCloudWebsite/delete_message.php?", hashMap);
		postRequestSend.setTaskDoneListener(new PostRequestSend.TaskDoneListener()
		{
			@Override
			public String onTaskDone(String str) throws JSONException
			{
				Log.e(TAG, "onTaskDone: " + str);
				return null;
			}
		});
		postRequestSend.execute();
		adapter.notifyItemRemoved(position);
		return position;
	}
}
