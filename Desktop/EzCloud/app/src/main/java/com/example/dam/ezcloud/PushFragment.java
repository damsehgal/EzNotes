package com.example.dam.ezcloud;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import net.rdrei.android.dirchooser.DirectoryChooserActivity;
import net.rdrei.android.dirchooser.DirectoryChooserConfig;

/**
 * Created by dam on 25/7/16.
 */
//TODO PUSH
public class PushFragment extends MyBasicFragment
{
	public static final int REQUEST_DIRECTORY = 4670;
	static TextView directoryName;
	Button openDirectory;
	Activity activity;
	public PushFragment(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState, Context context, Activity activity)
	{
		super(inflater, container, savedInstanceState, context);
		this.activity = activity;
	}
	@Override
	public View onCreate()
	{
		View rootView = getRootView(R.layout.push);
		directoryName = (TextView) rootView.findViewById(R.id.push_repository_name);
		openDirectory = (Button) rootView.findViewById(R.id.open_directory);
		openDirectory.setOnClickListener(new OpenDirectoryOnClickListener());
		return rootView;
	}
	public class OpenDirectoryOnClickListener implements View.OnClickListener
	{

		@Override
		public void onClick(View v)
		{

			final Intent chooserIntent = new Intent(context, DirectoryChooserActivity.class);
			final DirectoryChooserConfig config = DirectoryChooserConfig.builder().newDirectoryName("DirChooserSample").allowReadOnlyDirectory(true).allowNewDirectoryNameModification(true).build();
			chooserIntent.putExtra(DirectoryChooserActivity.EXTRA_CONFIG, config);
			activity.startActivityForResult(chooserIntent, REQUEST_DIRECTORY);

		}
	}
}
