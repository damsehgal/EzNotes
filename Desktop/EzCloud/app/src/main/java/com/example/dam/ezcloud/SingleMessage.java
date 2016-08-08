package com.example.dam.ezcloud;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by dam on 8/8/16.
 */
public class SingleMessage
{
	String sender;
	String details;
	String repo_sender;
	String repo_receiver;
	String time;
	String messageId;
	int isRead;
	public SingleMessage(JSONObject jsonObject) throws JSONException
	{
		sender = jsonObject.getString("sender");
		repo_sender = jsonObject.getString("repo_sender_name");
		repo_receiver = jsonObject.getString("repo_receiver_name");
		details = jsonObject.getString("details");
		time = jsonObject.getString("message_time");
		isRead = jsonObject.getInt("isRead");
		messageId = jsonObject.getString("message_id");
	}
}
