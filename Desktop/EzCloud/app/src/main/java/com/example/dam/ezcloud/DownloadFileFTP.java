package com.example.dam.ezcloud;

import android.app.DownloadManager;
import android.content.Context;
import android.net.Uri;
import android.os.Environment;
import android.util.Log;

import java.io.File;

/**
 * Created by dam on 9/7/16.
 */
public class DownloadFileFTP
{
	Context context;
	String fileName;
	public DownloadFileFTP(Context context, String fileName)
	{
		this.context = context;
		this.fileName = fileName;
	}

	public void getData()
	{

		Log.e("TAG", "getData: " + fileName );
		String url = "http://ezcloud.esy.es/ezCloudWebsite/" + Home.userName + "/" + fileName+ ".zip";
		String path = Environment.getExternalStorageDirectory()+"/"+fileName+".zip";
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
}