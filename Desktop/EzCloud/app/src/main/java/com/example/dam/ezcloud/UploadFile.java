package com.example.dam.ezcloud;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by dam on 6/7/16.
 */
class UploadFile extends AsyncTask<String, Integer, String>
{
	String path;
	Context context;
	OnTaskDoneListener onTaskDoneListener;
	public void setOnTaskDoneListener(OnTaskDoneListener onTaskDoneListener)
	{
		this.onTaskDoneListener = onTaskDoneListener;
	}
	public UploadFile(String path, Context context)
	{
		this.path = path;
		this.context = context;
	}
	@Override
	protected String doInBackground(String... params)
	{
		try
		{
			int serverResponseCode = 0;
			String sourceFileUri = path;
			Log.e("sda", "doInBackground: " + path);
			HttpURLConnection conn = null;
			DataOutputStream dos = null;
			String lineEnd = "\r\n";
			String twoHyphens = "--";
			String boundary = "*****";
			int bytesRead, bytesAvailable, bufferSize;
			byte[] buffer;
			int maxBufferSize = 1 * 1024 * 1024;
			File sourceFile = new File(sourceFileUri);
			if (sourceFile.isFile())
			{
				try
				{
					Log.e("Step2", "doInBackground: called");
					String upLoadServerUri = "http://ezcloud.esy.es/ezCloudWebsite/uploadFile.php?username=" + Home2.userName + "&versionNum=" + params[0] + "&repoName=" + params[1];
					FileInputStream fileInputStream = new FileInputStream(sourceFile);
					URL url = new URL(upLoadServerUri);
					conn = (HttpURLConnection) url.openConnection();
					conn.setDoInput(true);
					conn.setDoOutput(true);
					conn.setUseCaches(false);
					conn.setRequestMethod("POST");
					conn.setRequestProperty("Connection", "Keep-Alive");
					conn.setRequestProperty("ENCTYPE", "multipart/form-data");
					conn.setRequestProperty("Content-Type", "multipart/form-data;boundary=" + boundary);
					conn.setRequestProperty("bill", sourceFileUri);
					//	conn.setRequestProperty("username",Home2.userName);
					dos = new DataOutputStream(conn.getOutputStream());
					dos.writeBytes(twoHyphens + boundary + lineEnd);
					dos.writeBytes("Content-Disposition: form-data; name=\"bill\";filename=\"" + sourceFileUri + "\"" + lineEnd);
					dos.writeBytes(lineEnd);
					bytesAvailable = fileInputStream.available();
					bufferSize = Math.min(bytesAvailable, maxBufferSize);
					buffer = new byte[bufferSize];
					bytesRead = fileInputStream.read(buffer, 0, bufferSize);
					while (bytesRead > 0)
					{
						dos.write(buffer, 0, bufferSize);
						bytesAvailable = fileInputStream.available();
						bufferSize = Math.min(bytesAvailable, maxBufferSize);
						bytesRead = fileInputStream.read(buffer, 0, bufferSize);
					}
					dos.writeBytes(lineEnd);
					dos.writeBytes(twoHyphens + boundary + twoHyphens + lineEnd);
					serverResponseCode = conn.getResponseCode();
					String serverResponseMessage = conn.getResponseMessage();
					if (serverResponseCode == 200)
					{
						Log.e("Step3", "doInBackground: " + serverResponseMessage);
						//  Toast.makeText(context, "File Upload Finished", Toast.LENGTH_SHORT).show();
					}
					else
					{
						Log.e("file not uploaded", "doInBackground: " + serverResponseCode);
					}
					Log.e("Asd", "doInBackground: " + serverResponseMessage);
					fileInputStream.close();
					dos.flush();
					dos.close();
				}
				catch (Exception e)
				{
					Log.e("FAILED", "doInBackground: ", e);
					e.printStackTrace();
				}
			}
		}
		catch (Exception ex)
		{
			Log.e("this exception", "doInBackground: ", ex);
			ex.printStackTrace();
		}
		return "Executed";
	}
	public interface OnTaskDoneListener
	{
		void onTaskComplete(boolean isSuccessful);
	}
	@Override
	protected void onPostExecute(String result)
	{
		onTaskDoneListener.onTaskComplete(result.equals("Executed"));
		super.onPostExecute(result);
	}
	@Override
	protected void onPreExecute()
	{
	}
	@Override
	protected void onProgressUpdate(Integer... values)
	{
		super.onProgressUpdate(values);
	}
}