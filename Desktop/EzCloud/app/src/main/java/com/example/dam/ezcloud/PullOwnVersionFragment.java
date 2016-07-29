package com.example.dam.ezcloud;

import android.content.Context;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

/**
 * Created by dam on 25/7/16.
 */
public class PullOwnVersionFragment extends MyBasicFragment
{
	EditText editText1, editText2;
	public PullOwnVersionFragment(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState, Context context)
	{
		super(inflater, container, savedInstanceState, context);
	}
	@Override
	public View onCreate()
	{
		View rootView = getRootView(R.layout.pull_version);
		editText1 = (EditText) rootView.findViewById(R.id.select_repository);
		editText2 = (EditText) rootView.findViewById(R.id.select_version);
		Button btn1 = (Button) rootView.findViewById(R.id.btn_pull_version);
		btn1.setOnClickListener(new Btn1OnClickListener());
		return rootView;
	}
	public class Btn1OnClickListener implements View.OnClickListener
	{
		@Override
		public void onClick(View v)
		{
			String path = editText1.getText().toString() + "/" + editText2.getText().toString() + "/" + editText1.getText().toString();
			DownloadFileFTP downloadFileFTP = new DownloadFileFTP(context, path, new DownloadFileFTP.OnFileDownloadListener()
			{
				@Override
				public void onFileDownload(String path)
				{

				}
			});
			downloadFileFTP.execute();
			Toast.makeText(context, "Folder successfully saved with path= " + path, Toast.LENGTH_SHORT).show();
		}
	}
}
