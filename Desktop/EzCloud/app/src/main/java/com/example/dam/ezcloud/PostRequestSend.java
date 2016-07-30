package com.example.dam.ezcloud;

import android.os.AsyncTask;
import android.util.Log;

import org.json.JSONException;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;

import javax.net.ssl.HttpsURLConnection;

/**
 * Created by dam on 6/7/16.
 */
public class PostRequestSend extends AsyncTask<String, Void, String>
{
	TaskDoneListener taskDoneListener;
	String url;
	HashMap<String, String> hashFromOther;
	public void setTaskDoneListener(TaskDoneListener taskDoneListener)
	{
		this.taskDoneListener = taskDoneListener;
	}
	public PostRequestSend(String url, HashMap<String, String> hashFromOther)
	{
		this.url = url;
		this.hashFromOther = hashFromOther;
	}
	public String performPostCall(String requestURL, HashMap<String, String> postDataParams)
	{
		URL url;
		String response = "";
		try
		{
			url = new URL(requestURL);
			HttpURLConnection conn = (HttpURLConnection) url.openConnection();
			conn.setReadTimeout(15000);
			conn.setConnectTimeout(15000);
			conn.setRequestMethod("POST");
			conn.setDoInput(true);
			conn.setDoOutput(true);
			OutputStream os = conn.getOutputStream();
			BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(os, "UTF-8"));
			writer.write(getPostDataString(postDataParams));
			writer.flush();
			writer.close();
			os.close();
			int responseCode = conn.getResponseCode();
			if (responseCode == HttpsURLConnection.HTTP_OK)
			{
				String line;
				BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream()));
				while ((line = br.readLine()) != null)
				{
					response += line;
				}
			}
			else
			{
				response = "";
			}
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
		return response;
	}
	private String getPostDataString(HashMap<String, String> params) throws UnsupportedEncodingException
	{
		StringBuilder result = new StringBuilder();
		boolean first = true;
		for (Map.Entry<String, String> entry : params.entrySet())
		{
			if (first)
				first = false;
			else
				result.append("&");
			result.append(URLEncoder.encode(entry.getKey(), "UTF-8"));
			result.append("=");
			result.append(URLEncoder.encode(entry.getValue(), "UTF-8"));
		}
		Log.e("dsasd", "getPostDataString: " + result);
		return result.toString();
	}
	@Override
	protected String doInBackground(String... params)
	{
		return performPostCall(url, hashFromOther);
	}
	@Override
	protected void onPostExecute(String s)
	{
		super.onPostExecute(s);
		try
		{
			taskDoneListener.onTaskDone(s);
		}
		catch (JSONException e)
		{
			e.printStackTrace();
		}
	}
	public interface TaskDoneListener
	{
		String onTaskDone(String str) throws JSONException;
	}
}