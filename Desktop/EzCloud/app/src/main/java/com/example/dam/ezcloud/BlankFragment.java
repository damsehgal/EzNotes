package com.example.dam.ezcloud;
//TODO ADD PERMISSION FOR MARSHMALLOW

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Path;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
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
import java.util.HashMap;

public class BlankFragment extends Fragment
{
	int position;
	public static final int PICKFILE_RESULT_CODE = 1, REQUEST_DIRECTORY = 2;
	Button choseFile, saveFile;
	Button upLoadFile;
	EditText et1;
	static String prev = "";
	static Uri uri;
	private OnCreateViewCalledListener ocvcl;

	public void setOnCreateViewCalledListener(OnCreateViewCalledListener ocvcListener)
	{
		ocvcl = ocvcListener;
	}


	public void setPosition(int position)
	{
		this.position = position;
	}

	public Uri getUri()
	{
		return uri;
	}

	@Override
	public void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);

	}

	public void onActivityResult(int requestCode, int resultCode, Intent data)
	{
		// TODO Auto-generated method stub
		Log.e("isItCalled", "onActivityResult: ");
		switch (requestCode)
		{
			case PICKFILE_RESULT_CODE:
				if (resultCode == Activity.RESULT_OK)
				{
					uri = data.getData();
					Log.e("URI", "onActivityResult: " + uri.toString());
					File f = new File(uri.getPath());
					String f2 = "";
					try
					{
						BufferedReader buff = new BufferedReader(new InputStreamReader(getContext().getContentResolver().openInputStream(uri), "UTF-8"));
						String line;
						while ((line = buff.readLine()) != null)
						{
							f2 += line + "\n";
						}
						et1.setText(f2);
					}
					catch (FileNotFoundException e)
					{
						Toast.makeText(getContext(), "File Not Found", Toast.LENGTH_SHORT).show();
					}
					catch (IOException e)
					{
						Toast.makeText(getContext(), "IO Exception", Toast.LENGTH_SHORT).show();
					}
					Log.e("activity", "onActivityResult: " + f2);
				}
				else
				{
					Log.e("dsaasd", "onActivityResult: ");
				}

				break;

		}
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
	{

		Log.e("Fragment", "onCreateView: Called");
		if (position == 0)
		{
			//TODO change password in current session .... in PHP
			View rootView = inflater.inflate(R.layout.changepass, container, false);
			final EditText currentPassword = (EditText) (rootView).findViewById(R.id.curr_pass);
			final EditText newPassword = (EditText) (rootView).findViewById(R.id.new_pass);
			final EditText confirmPassword = (EditText) (rootView).findViewById(R.id.confirm_password);
			Button btn1 = (Button) (rootView).findViewById(R.id.change_password);
			btn1.setOnClickListener(new View.OnClickListener()
			{
				@Override
				public void onClick(View v)
				{
					Log.e("called", "onClick: ");
					if (currentPassword.getText().toString().equals(Home.passWord) && newPassword.getText().toString().equals(confirmPassword.getText().toString()))
					{
						Home.passWord = changePassword(Home.userName, newPassword.getText().toString());
						Toast.makeText(getContext(), "Password changed successfully", Toast.LENGTH_SHORT).show();
						Log.e("changed", "onClick: ");
					}
					else
					{
						Log.e("unchanged", "onClick: ");
						Toast.makeText(getContext(), "Please Refill the form", Toast.LENGTH_SHORT).show();
					}
				}
			});
			return rootView;
		}
		else if (position == 1)
		{
			Log.e("pos1", "onCreateView: ");
			final View rootView = inflater.inflate(R.layout.openfile, container, false);
			choseFile = (Button) rootView.findViewById(R.id.btn_chose_file);
			saveFile = (Button) rootView.findViewById(R.id.save_file);
			upLoadFile = (Button) rootView.findViewById(R.id.upload_file);
			et1 = (EditText) rootView.findViewById(R.id.opened_file);

			choseFile.setOnClickListener(new ChoseFileOnClick());
			saveFile.setOnClickListener(new SaveFileOnClick());
			upLoadFile.setOnClickListener(new UpLoadFileOnClick());

			return rootView;
		}
		else if (position == 2)
		{
			//TODO PUSH
			final View rootView = inflater.inflate(R.layout.push, container, false);
			return rootView;

		}
		else if (position == 3)
		{
			// TODO PUSH REQUEST
			final View rootView = inflater.inflate(R.layout.push_request, container, false);
			return rootView;

		}
		else if (position == 4)
		{
			//TODO FORK
			final View rootView = inflater.inflate(R.layout.fork, container, false);
			return rootView;

		}
		else if (position == 5)
		{
			// TODO PULL VERSION
			final View rootView = inflater.inflate(R.layout.pull_version, container, false);
			final EditText editText1 = (EditText) rootView.findViewById(R.id.select_repository);
			final EditText editText = (EditText) rootView.findViewById(R.id.select_version);
			Button btn1 = (Button) rootView.findViewById(R.id.btn_pull_version);
			btn1.setOnClickListener(new View.OnClickListener()
			{
				@Override
				public void onClick(View v)
				{
					String path = editText1.getText().toString();
					int cunt = 0;
					if((new File(path)).exists())
					{
						path+="-";
					}
					while ((new File(path+cunt)).exists())
					{
						cunt++;
					}
					DownloadFileFTP downloadFileFTP = new DownloadFileFTP(getContext(), path);
					downloadFileFTP.getData();
					ZipToDirectory zipToDirectory = new ZipToDirectory();
					zipToDirectory.execute(Environment.getExternalStorageDirectory()+"/"+path+".zip",Environment.getExternalStorageDirectory()+"/"+path);
					Toast.makeText(getContext(), "Folder successfuly saved with path= "+path, Toast.LENGTH_SHORT).show();
				}
			});
			return rootView;

		}
		else if (position == 6)
		{
			//TODO SEND MERGE REQUEST
			final View rootView = inflater.inflate(R.layout.merge, container, false);

			return rootView;

		}
		else if (position == 7)
		{
			//TODO CREATE NEW REPOSITORY WITH PUSH

			final View rootView = inflater.inflate(R.layout.create_new, container, false);
			Log.e("Called", "onCreateView: ");
			Toast.makeText(getContext(), "Called", Toast.LENGTH_SHORT).show();
			EditText repoName = ((EditText) rootView.findViewById(R.id.repo_name_create));
			EditText path = ((EditText) rootView.findViewById(R.id.folder_path_create));
			Button btnCreateNewRepo = (Button) rootView.findViewById(R.id.btn_create_new_repo);
			btnCreateNewRepo.setOnClickListener(new CreateNewRepoOnClick(path, repoName));

			return rootView;
		}
		else
		{
			// LOGOUT
			HashMap <String , String> hashMap = new HashMap<>();
			hashMap.put("sess_ID" ,PreferenceManager.getDefaultSharedPreferences(getContext()).getString("sess_ID", ""));
			PostRequestSend prs = new PostRequestSend( "http://ezcloud.esy.es/ezCloudWebsite/logout_app.php?", hashMap);
			prs.setTaskDoneListener(new PostRequestSend.TaskDoneListener()
			{
				@Override
				public String onTaskDone(String str)
				{
					SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(getContext());
					SharedPreferences.Editor ed = preferences.edit();
					ed.putString("sess_ID", "");
					ed.commit();
					Log.e("PostResponse", "onTaskDone: " + str);
					Log.e("LOGOUT", "onCreateView: " + preferences.getString("sess_ID", ""));
					Intent intent = new Intent(getContext(), MainActivity.class);
					startActivity(intent);
					return null;

				}
			});
			prs.execute();

			return inflater.inflate(R.layout.openfile, container, false);

		}

	}

	public String changePassword(String userName, String password)
	{
		Log.e("dsasd", "changePassword: ");
		HashMap<String, String> hash = new HashMap<>(5);
		hash.put("sess_ID", PreferenceManager.getDefaultSharedPreferences(getContext()).getString("sess_ID", ""));
		hash.put("username", userName);
		hash.put("password", password);
		PostRequestSend changePass = new PostRequestSend("http://ezcloud.esy.es/ezCloudWebsite/change_pass_app.php?", hash);
		changePass.setTaskDoneListener(new PostRequestSend.TaskDoneListener()
		{
			@Override
			public String onTaskDone(String str)
			{
				Log.e("Change Pass", "onTaskDone: " + str);
				return str;
			}
		});
		changePass.execute();
		return password;
	}

	public interface OnCreateViewCalledListener
	{

		void onCreateViewCalled(int position);
	}

	public class UpLoadFileOnClick implements View.OnClickListener
	{
		@Override
		public void onClick(View v)
		{
			String str = Environment.getExternalStorageDirectory() + "/" + (new File(uri.getPath())).getAbsolutePath().split(":")[1];
			Log.e("Upload", "onClick: " + str);
			UploadFile uploadFile = new UploadFile(str, getContext());
			uploadFile.execute();
		}
	}

	public class SaveFileOnClick implements View.OnClickListener
	{
		@Override
		public void onClick(View v)
		{
			//TODO Toast.makeText(getContext(), "Not Supported as of now", Toast.LENGTH_SHORT).show();
			try
			{
				getContext().getContentResolver().openOutputStream(uri).write(et1.getText().toString().getBytes());
				Toast.makeText(getContext(), "File Saved Successfully", Toast.LENGTH_SHORT).show();
			}
			catch (SecurityException e)
			{
				Log.d("Permission Not Granted", "onClick: " + e);
				Toast.makeText(getContext(), "Permission Not Granted", Toast.LENGTH_SHORT).show();
			}
			catch (IOException e)
			{
				Toast.makeText(getContext(), "IO Exception", Toast.LENGTH_SHORT).show();
			}
			catch (Exception e)
			{
				Toast.makeText(getContext(), "Please chose a file first and don't keep it empty", Toast.LENGTH_SHORT).show();
			}
		}
	}

	public class ChoseFileOnClick implements View.OnClickListener
	{
		@Override
		public void onClick(View v)
		{
			Intent intent;
			if (Build.VERSION.SDK_INT < 19)
				intent = new Intent(Intent.ACTION_GET_CONTENT);
			else
				intent = new Intent(Intent.ACTION_OPEN_DOCUMENT);
			intent.setType("*/*");
			startActivityForResult(intent, PICKFILE_RESULT_CODE);
		}
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

					Log.e("TAG", "onTaskDone: " + flag );
					if (flag)
					{
						Toast.makeText(getContext(), "Folder Successfully compressed" + Environment.getExternalStorageDirectory(), Toast.LENGTH_SHORT).show();
						HashMap<String,String> hash = new HashMap<>(2);
						hash.put("username",Home.userName);
						hash.put("repoName",repoName.getText().toString());
						PostRequestSend prs = new PostRequestSend("http://ezcloud.esy.es/ezCloudWebsite/commit.php?",hash);
						prs.setTaskDoneListener(new PostRequestSend.TaskDoneListener()
						{
							@Override
							public String onTaskDone(String str)
							{

								Log.e("onTaskDone", "onTaskDone: "+ path + " " + str  );
								new UploadFile(path, getContext()).execute(str.split("\\s+")[str.split("\\s+").length-1], repoName.getText().toString());
								return null;
							}
						});
						prs.execute();

					}
					else
					{
						Toast.makeText(getContext(), "Make sure your folder is not empty", Toast.LENGTH_SHORT).show();
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