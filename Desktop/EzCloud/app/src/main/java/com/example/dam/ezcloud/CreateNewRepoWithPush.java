package com.example.dam.ezcloud;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import net.rdrei.android.dirchooser.DirectoryChooserActivity;
import net.rdrei.android.dirchooser.DirectoryChooserConfig;

import java.util.HashMap;

/**
 * Created by dam on 25/7/16.
 */
public class CreateNewRepoWithPush extends MyBasicFragment
{
	public static final int REQUEST_DIRECTORY_PUSH = 4231;
	static EditText path;
	Activity activity;
	public CreateNewRepoWithPush(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState, Context context,Activity activity)
	{
		super(inflater, container, savedInstanceState, context);
		this.activity = activity;
	}
	@Override
	public View onCreate()
	{
		View rootView = getRootView(R.layout.create_new);
		Log.e("Called", "onCreateView: ");
		Toast.makeText(context, "Called", Toast.LENGTH_SHORT).show();
		EditText repoName = ((EditText) rootView.findViewById(R.id.repo_name_create));
		path = ((EditText) rootView.findViewById(R.id.folder_path_create));
		Button btnCreateNewRepo = (Button) rootView.findViewById(R.id.btn_create_new_repo);
		Button chooseDirectory = (Button) rootView.findViewById(R.id.btn_chose_directory);
		chooseDirectory.setOnClickListener(new ChooseDirectoryOnClick());
		btnCreateNewRepo.setOnClickListener(new CreateNewRepoOnClick( repoName));
		return rootView;
	}
	public class ChooseDirectoryOnClick implements View.OnClickListener
	{
		@Override
		public void onClick(View v)
		{
			final Intent chooserIntent = new Intent(context, DirectoryChooserActivity.class);
			final DirectoryChooserConfig config = DirectoryChooserConfig.builder().newDirectoryName("DirChooserSample").allowReadOnlyDirectory(true).allowNewDirectoryNameModification(true).build();
			chooserIntent.putExtra(DirectoryChooserActivity.EXTRA_CONFIG, config);
			activity.startActivityForResult(chooserIntent, REQUEST_DIRECTORY_PUSH);
		}
	}

	public class CreateNewRepoOnClick implements View.OnClickListener
	{
		EditText  repoName;
		public CreateNewRepoOnClick( EditText repoName)
		{
			this.repoName = repoName;
		}
		@Override
		public void onClick(View v)
		{
			DirectoryToZip d2z = new DirectoryToZip(new DirectoryToZip.OnTaskDoneListener()
			{
				@Override
				public void onTaskDone(boolean flag, final String path2)
				{
					Log.e("TAG", "onTaskDone: " + flag);
					if (flag)
					{
						Toast.makeText(context, "Folder Successfully compressed" + Environment.getExternalStorageDirectory(), Toast.LENGTH_SHORT).show();
						HashMap<String, String> hash = new HashMap<>(2);
						hash.put("username", Home.userName);
						hash.put("repoName", repoName.getText().toString());
						PostRequestSend prs = new PostRequestSend("http://ezcloud.esy.es/ezCloudWebsite/commit.php?", hash);
						prs.setTaskDoneListener(new PostRequestSend.TaskDoneListener()
						{
							@Override
							public String onTaskDone(String str)
							{
								Log.e("onTaskDone", "onTaskDone: " + path2 + " " + str);
								new UploadFile(path2, context).execute(str.split("\\s+")[str.split("\\s+").length - 1], repoName.getText().toString());
								return null;
							}
						});
						prs.execute();
					}
					else
					{
						Toast.makeText(context, "Make sure your folder is not empty", Toast.LENGTH_SHORT).show();
					}
				}
			});
			d2z.execute( path.getText().toString(),path.getText().toString()+"/"+ repoName.getText().toString());
		}
	}
}
