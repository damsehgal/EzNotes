package com.example.dam.ezcloud;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;

/**
 * Created by dam on 24/7/16.
 */
public class OpenFileFragment extends MyBasicFragment
{
	public static final int PICK_FILE_RESULT_CODE = 1;
	private static final String TAG = OpenFileFragment.class.getSimpleName();
	static Uri uri;
	Activity activity;
	Button choseFile, saveFile, upLoadFile;
	static EditText et1;

	public OpenFileFragment(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState, Context context, Activity activity)
	{
		super(inflater, container, savedInstanceState, context);
		this.activity = activity;
	}

	@Override
	public View onCreate()
	{
		Log.e(TAG, "onCreate: called");
		View rootView = getRootView(R.layout.openfile);
		choseFile = (Button) rootView.findViewById(R.id.btn_chose_file);
		saveFile = (Button) rootView.findViewById(R.id.save_file);
		upLoadFile = (Button) rootView.findViewById(R.id.upload_file);
		et1 = (EditText) rootView.findViewById(R.id.opened_file);

		choseFile.setOnClickListener(new ChoseFileOnClick());
		saveFile.setOnClickListener(new SaveFileOnClick());
		upLoadFile.setOnClickListener(new UpLoadFileOnClick());
		return rootView;
	}
	public class UpLoadFileOnClick implements View.OnClickListener
	{
		@Override
		public void onClick(View v)
		{
			String str = Environment.getExternalStorageDirectory() + "/" + (new File(uri.getPath())).getAbsolutePath().split(":")[1];
			Log.e("Upload", "onClick: " + str);
			UploadFile uploadFile = new UploadFile(str, context);
			uploadFile.execute();
		}
	}

	public class SaveFileOnClick implements View.OnClickListener
	{
		@Override
		public void onClick(View v)
		{
			//TODO Toast.makeText(context(), "Not Supported as of now", Toast.LENGTH_SHORT).show();
			try
			{
				context.getContentResolver().openOutputStream(uri).write(et1.getText().toString().getBytes());
				Toast.makeText(context, "File Saved Successfully", Toast.LENGTH_SHORT).show();
			}
			catch (SecurityException e)
			{
				Log.d("Permission Not Granted", "onClick: " + e);
				Toast.makeText(context, "Permission Not Granted", Toast.LENGTH_SHORT).show();
			}
			catch (IOException e)
			{
				Toast.makeText(context, "IO Exception", Toast.LENGTH_SHORT).show();
			}
			catch (Exception e)
			{
				Toast.makeText(context, "Please chose a file first and don't keep it empty", Toast.LENGTH_SHORT).show();
			}
		}
	}

	public class ChoseFileOnClick implements View.OnClickListener
	{
		@Override
		public void onClick(View v)
		{
			Log.e(TAG, "onClick: am I reaching here");
			Intent intent;
			if (Build.VERSION.SDK_INT < 19)
				intent = new Intent(Intent.ACTION_GET_CONTENT);
			else
				intent = new Intent(Intent.ACTION_OPEN_DOCUMENT);
			intent.setType("*/*");
			activity.startActivityForResult(intent, PICK_FILE_RESULT_CODE);
		}
	}
}
