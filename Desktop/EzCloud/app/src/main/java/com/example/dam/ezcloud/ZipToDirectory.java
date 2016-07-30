package com.example.dam.ezcloud;

import android.os.AsyncTask;
import android.util.Log;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

/**
 * Created by dam on 28/7/16.
 */
public class ZipToDirectory extends AsyncTask<String, Void, Void>
{
	public OnTaskDoneListener onTaskDoneListener;
	boolean flag = true;
	public void setOnTaskDoneListener(OnTaskDoneListener onTaskDoneListener)
	{
		this.onTaskDoneListener = onTaskDoneListener;
	}
	private static final String TAG = ZipToDirectory.class.getSimpleName();
	@Override
	protected void onPreExecute()
	{
		super.onPreExecute();
		Log.e(TAG, "onPreExecute: onPreExecute Called");
	}
	@Override
	protected Void doInBackground(String... strings)
	{
		Log.e(TAG, "doInBackground: called" );
		final String path = strings[0];
		Log.e(TAG, "doInBackground: " + strings[0] + " " + strings[1] );
		final String destination = strings[1];
		Log.e(TAG, "doInBackground: " + path);
		if (path.equals("") || path.isEmpty() || destination.equals("") || destination.isEmpty()  )
		{
			flag = false;
		}
		final File file = new File(path);
		final File destinationDir = new File(destination);
		Log.e(TAG, "doInBackground: " + path + " " + destination);
		if (file.exists() && file.isFile())
		{
			Log.e(TAG, "doInBackground: Am I HERE");
			try
			{
				InputStream inputStream = new FileInputStream(file);
				ZipInputStream zipInputStream = new ZipInputStream(inputStream);
				byte buffer[] = new byte[4096];
				int bytesRead;
				ZipEntry zipEntry = null;
				while ((zipEntry = zipInputStream.getNextEntry()) != null)
				{
					if (!destinationDir.exists())
					{
						destinationDir.mkdirs();
					}
					if (zipEntry.isDirectory())
					{

						File directory = new File(destinationDir, zipEntry.getName());
						if (!directory.exists())
						{
							directory.mkdirs();
						}
						Log.e(TAG, "[DIR] " + zipEntry.getName());
					}
					else
					{

						File destinationFile = new File(destinationDir, zipEntry.getName());
						if(!destinationFile.exists())
						{
							destinationFile.getParentFile().mkdirs();
							destinationFile.createNewFile();
						}
						FileOutputStream fileOutputStream = new FileOutputStream(destinationFile);
						while ((bytesRead = zipInputStream.read(buffer)) != -1)
						{
							fileOutputStream.write(buffer, 0, bytesRead);
						}
						fileOutputStream.close();

						Log.e(TAG, "[FILE] " + zipEntry.getName());
					}
				}
			}
			catch (FileNotFoundException e)
			{
				Log.e(TAG, "doInBackground: " + e );
				flag = false;
				e.printStackTrace();
			}catch (IOException e)
			{
				flag = false;
				Log.e(TAG, "doInBackground: " + e );
				e.printStackTrace();
			}
		}
		return null;
	}
	public interface OnTaskDoneListener
	{
		void onTaskDone(boolean isCompleted);
	}
	@Override
	protected void onPostExecute(Void aVoid)
	{
		onTaskDoneListener.onTaskDone(flag);
		super.onPostExecute(aVoid);
		Log.e(TAG, "onPostExecute: " + "task completed");
	}
}