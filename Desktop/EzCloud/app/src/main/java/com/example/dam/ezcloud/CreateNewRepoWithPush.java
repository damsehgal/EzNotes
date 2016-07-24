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

import java.util.HashMap;

/**
 * Created by dam on 25/7/16.
 */
public class CreateNewRepoWithPush extends MyBasicFragment
{
	public CreateNewRepoWithPush(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState, Context context)
	{
		super(inflater, container, savedInstanceState, context);
	}
	@Override
	public View onCreate()
	{
		View rootView = getRootView(R.layout.create_new);
		Log.e("Called", "onCreateView: ");
		Toast.makeText(context, "Called", Toast.LENGTH_SHORT).show();
		EditText repoName = ((EditText) rootView.findViewById(R.id.repo_name_create));
		EditText path = ((EditText) rootView.findViewById(R.id.folder_path_create));
		Button btnCreateNewRepo = (Button) rootView.findViewById(R.id.btn_create_new_repo);
		btnCreateNewRepo.setOnClickListener(new CreateNewRepoOnClick(path, repoName));
		return rootView;
	}
	public class CreateNewRepoOnClick implements View.OnClickListener
	{
		EditText path, repoName;
		public CreateNewRepoOnClick(EditText path, EditText repoName)
		{
			this.path = path;
			this.repoName = repoName;
		}
		@Override
		public void onClick(View v)
		{
			DirectoryToZip d2z = new DirectoryToZip(new DirectoryToZip.OnTaskDoneListener()
			{
				@Override
				public void onTaskDone(boolean flag, final String path)
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
								Log.e("onTaskDone", "onTaskDone: " + path + " " + str);
								new UploadFile(path, context).execute(str.split("\\s+")[str.split("\\s+").length - 1], repoName.getText().toString());
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
			String str = "";
			String tempPath = path.getText().toString();
			int i;
			for (i = tempPath.length() - 1; i >= 0; i--)
			{
				if (tempPath.charAt(i) == '/')
					break;
			}
			for (int j = 0; j <= i; j++)
				str += tempPath.charAt(j);
			d2z.execute(Environment.getExternalStorageDirectory() + "/" + tempPath, Environment.getExternalStorageDirectory() + "/" + str + repoName.getText().toString());
		}
	}
}
