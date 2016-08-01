package com.example.dam.ezcloud;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

/**
 * Created by dam on 8/7/16.
 */
public class DirectoryToZip extends AsyncTask<String, Void, String>
{
	Boolean isSuccessfulyZipped;
	String temp;
	ProgressDialog progressDialog;
	Context context;
	public void setContext(Context context)
	{
		this.context = context;
	}
	@Override
	protected void onPreExecute()
	{
		super.onPreExecute();
		progressDialog = new ProgressDialog(context);
		progressDialog.setMessage("Loading...");
		progressDialog.show();
	}
	@Override
	protected String doInBackground(String... params)
	{
		isSuccessfulyZipped = zipFileAtPath(params[0], params[1] + ".zip");
		temp = params[1] + ".zip";
		Log.d("TAG", "doInBackground: " + params[1]);
		return params[0];
	}
	OnTaskDoneListener onTaskDoneListener;
	public DirectoryToZip(OnTaskDoneListener onTaskDoneListener)
	{
		this.onTaskDoneListener = onTaskDoneListener;
	}
	public boolean zipFileAtPath(String sourcePath, String toLocation)
	{
		final int BUFFER = 2048;
		File sourceFile = new File(sourcePath);
		try
		{
			BufferedInputStream origin = null;
			FileOutputStream dest = new FileOutputStream(toLocation);
			ZipOutputStream out = new ZipOutputStream(new BufferedOutputStream(
				dest));
			if (sourceFile.isDirectory())
			{
				zipSubFolder(out, sourceFile, sourceFile.getParent().length());
			}
			else
			{
				byte data[] = new byte[BUFFER];
				FileInputStream fi = new FileInputStream(sourcePath);
				origin = new BufferedInputStream(fi, BUFFER);
				ZipEntry entry = new ZipEntry(getLastPathComponent(sourcePath));
				out.putNextEntry(entry);
				int count;
				while ((count = origin.read(data, 0, BUFFER)) != -1)
				{
					out.write(data, 0, count);
				}
			}
			out.close();
		}
		catch (Exception e)
		{
			e.printStackTrace();
			return false;
		}
		return true;
	}
	private void zipSubFolder(ZipOutputStream out, File folder, int basePathLength) throws IOException
	{
		final int BUFFER = 2048;
		File[] fileList = folder.listFiles();
		BufferedInputStream origin = null;
		for (File file : fileList)
		{
			if (file.isDirectory())
			{
				zipSubFolder(out, file, basePathLength);
			}
			else
			{
				byte data[] = new byte[BUFFER];
				String unmodifiedFilePath = file.getPath();
				String relativePath = unmodifiedFilePath
					.substring(basePathLength);
				FileInputStream fi = new FileInputStream(unmodifiedFilePath);
				origin = new BufferedInputStream(fi, BUFFER);
				ZipEntry entry = new ZipEntry(relativePath);
				out.putNextEntry(entry);
				int count;
				while ((count = origin.read(data, 0, BUFFER)) != -1)
				{
					out.write(data, 0, count);
				}
				origin.close();
			}
		}
	}
	public String getLastPathComponent(String filePath)
	{
		String[] segments = filePath.split("/");
		if (segments.length == 0)
			return "";
		String lastPathComponent = segments[segments.length - 1];
		return lastPathComponent;
	}
	public interface OnTaskDoneListener
	{
		void onTaskDone(boolean flag, String path);
	}
	@Override
	protected void onPostExecute(String s)
	{
		super.onPostExecute(s);
		onTaskDoneListener.onTaskDone(isSuccessfulyZipped, temp);
		progressDialog.dismiss();
	}
}