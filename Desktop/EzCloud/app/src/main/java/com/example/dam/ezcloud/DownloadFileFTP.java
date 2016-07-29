package com.example.dam.ezcloud;

import android.app.DownloadManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Environment;
import android.util.Log;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

/**
 * Created by dam on 9/7/16.
 */
public class DownloadFileFTP extends AsyncTask<Void, Void, String>
{
	Context context;
	String fileName;
	String username;
	String path;
	OnFileDownloadListener onFileDownloadListener;
	public DownloadFileFTP(Context context, String fileName,OnFileDownloadListener onFileDownloadListener)
	{
		this.username = Home.userName;
		this.context = context;
		this.fileName = fileName;
		this.onFileDownloadListener = onFileDownloadListener;
	}
	public DownloadFileFTP(Context context, String fileName, String username, OnFileDownloadListener onFileDownloadListener)
	{
		this.context = context;
		this.fileName = fileName;
		this.username = username;
		this.onFileDownloadListener = onFileDownloadListener;
	}


	@Override
	protected String doInBackground(Void... params)
	{
		Log.e("TAG", "getData: " + fileName);
		String url = "http://ezcloud.esy.es/ezCloudWebsite/" + username + "/" + fileName + ".zip";
		Log.e("TAG", "getData: " + url);
		path = Environment.getExternalStorageDirectory() + "/" + fileName + ".zip";
		Log.e("TAG", "doInBackground: " + path );
		try
		{
			URL myFileOnServer = new URL(url);
			HttpURLConnection httpURLConnection = (HttpURLConnection) myFileOnServer.openConnection();
			httpURLConnection.setRequestMethod("GET");
			httpURLConnection.setDoOutput(true);
			File file = new File(path);
			if (!file.exists())
				file.mkdirs();
			Log.e("TAG", "doInBackground: reached Here" );
			FileOutputStream fileOutputStream = new FileOutputStream(file);
			InputStream inputStream = httpURLConnection.getInputStream();

			byte[] buffer = new byte[1024];
			int bufferLength = 0;
			while ( (bufferLength = inputStream.read(buffer)) > 0 )
			{
				fileOutputStream.write(buffer, 0, bufferLength);

			}
			fileOutputStream.close();
			Log.e("TAG", "doInBackground: reached Here2" );
		}
		catch (MalformedURLException e)
		{
			e.printStackTrace();
		}
		catch (IOException e)
		{
			e.printStackTrace();
		}
		return Environment.getExternalStorageDirectory() + "/" + fileName + ".zip";
	}
	@Override
	protected void onPostExecute(String s)
	{
		Log.e("TAG", "onPostExecute: Am i here" );

		onFileDownloadListener.onFileDownload(s);
		super.onPostExecute(s);
	}
	public interface OnFileDownloadListener
	{
		void onFileDownload(String path);
	}
}