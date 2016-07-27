package com.example.dam.ezcloud;

import android.app.DownloadManager;
import android.content.Context;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Environment;
import android.util.Log;

import java.io.File;

/**
 * Created by dam on 9/7/16.
 */
public class DownloadFileFTP extends AsyncTask<Void, Void, String>
{
	Context context;
	String fileName;
	String username;
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
	public void getData()
	{
		Log.e("TAG", "getData: " + fileName);
		String url = "http://ezcloud.esy.es/ezCloudWebsite/" + username + "/" + fileName + ".zip";
		Log.e("TAG", "getData: " + url);
		String path = Environment.getExternalStorageDirectory() + "/" + fileName + ".zip";
		DownloadManager downloadManager = (DownloadManager) context.getSystemService(Context.DOWNLOAD_SERVICE);
		Uri uri = Uri.parse(url);
		DownloadManager.Request request = new DownloadManager.Request(uri);
		request.setAllowedOverMetered(true);
		request.setAllowedOverRoaming(true);
		request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE);
		Uri pathUri = Uri.fromFile(new File(path));
		request.setDestinationUri(pathUri);
		downloadManager.enqueue(request);
	}
	@Override
	protected String doInBackground(Void... params)
	{
		getData();
		return Environment.getExternalStorageDirectory() + "/" + fileName + ".zip";
	}
	@Override
	protected void onPostExecute(String s)
	{
		onFileDownloadListener.onFileDownload(s);
		super.onPostExecute(s);
	}
	public interface OnFileDownloadListener
	{
		void onFileDownload(String path);
	}
}