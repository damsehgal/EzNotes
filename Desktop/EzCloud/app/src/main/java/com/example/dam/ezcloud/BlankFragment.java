package com.example.dam.ezcloud;
//TODO ADD PERMISSION FOR MARSHMALLOW
import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.dd.CircularProgressButton;

import net.rdrei.android.dirchooser.DirectoryChooserActivity;
import net.rdrei.android.dirchooser.DirectoryChooserConfig;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.HashMap;

public class BlankFragment extends Fragment
{
	private static final String TAG = "BlankFragment";
	public static final int REQUEST_DIRECTORY_PUSH = 4530;
	EditText et1 , path;
	int position;
	static Uri uri;
	OnCreateViewCalledListener ocvcl;
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
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, final Bundle savedInstanceState)
	{
		ocvcl.onCreateViewCalled(position);
		Log.e("Fragment", "onCreateView: Called");
		if (position == 0)
		{
			Log.e(TAG, "onCreateView: password called");
			return new ChangePasswordFragment(inflater, container, savedInstanceState, getContext()).onCreate();
		}
		else if (position == 1)
		{
			Log.e("pos1", "onCreateView: ");
			// Need to implement it here only :-<
			final View rootView = inflater.inflate(R.layout.openfile, container, false);
			CircularProgressButton choseFile = (CircularProgressButton) rootView.findViewById(R.id.btn_chose_file);
			final CircularProgressButton saveFile = (CircularProgressButton) rootView.findViewById(R.id.save_file);
			final CircularProgressButton upLoadFile = (CircularProgressButton) rootView.findViewById(R.id.upload_file);
			et1= (EditText) rootView.findViewById(R.id.opened_file);
			choseFile.setIndeterminateProgressMode(true);
			saveFile.setIndeterminateProgressMode(true);
			upLoadFile.setIndeterminateProgressMode(true);
			choseFile.setProgress(0);
			saveFile.setProgress(0);
			upLoadFile.setProgress(0);
			upLoadFile.setOnClickListener(new View.OnClickListener()
			{
				@Override
				public void onClick(View v)
				{
					upLoadFile.setProgress(50);
					String str = Environment.getExternalStorageDirectory() + "/" + (new File(uri.getPath())).getAbsolutePath().split(":")[1];
					Log.e("Upload", "onClick: " + str);
					UploadFile uploadFile = new UploadFile(str, getContext());
					uploadFile.setOnTaskDoneListener(new UploadFile.OnTaskDoneListener()
					{
						@Override
						public void onTaskComplete(boolean isSuccessful)
						{
							if(isSuccessful)
							{
								upLoadFile.setProgress(100);
								Handler handler = new Handler();
								handler.postDelayed(new Runnable()
								{
									@Override
									public void run()
									{
										upLoadFile.setProgress(0);
									}
								}, 2000);
							}
							else
							{
								upLoadFile.setProgress(-1);
								Handler handler = new Handler();
								handler.postDelayed(new Runnable()
								{
									@Override
									public void run()
									{
										upLoadFile.setProgress(0);
									}
								}, 2000);
							}
						}
					});
					uploadFile.execute();

				}
			});
			saveFile.setOnClickListener(new View.OnClickListener()
			{
				@Override
				public void onClick(View v)
				{
					//TODO Toast.makeText(getContext()(), "Not Supported as of now", Toast.LENGTH_SHORT).show();
					try
					{
						saveFile.setProgress(50);
						getContext().getContentResolver().openOutputStream(uri).write(et1.getText().toString().getBytes());
						saveFile.setProgress(100);
						Handler handler = new Handler();
						handler.postDelayed(new Runnable()
						{
							@Override
							public void run()
							{
								saveFile.setProgress(0);
							}
						}, 2000);
						Toast.makeText(getContext(), "File Saved Successfully", Toast.LENGTH_SHORT).show();
					}
					catch (SecurityException e)
					{
						Log.d("Permission Not Granted", "onClick: " + e);
						saveFile.setProgress(-1);
						Handler handler = new Handler();
						handler.postDelayed(new Runnable()
						{
							@Override
							public void run()
							{
								saveFile.setProgress(0);
							}
						}, 2000);

						Toast.makeText(getContext(), "Permission Not Granted", Toast.LENGTH_SHORT).show();
					}
					catch (IOException e)
					{
						Toast.makeText(getContext(), "IO Exception", Toast.LENGTH_SHORT).show();
						saveFile.setProgress(-1);
						Handler handler = new Handler();
						handler.postDelayed(new Runnable()
						{
							@Override
							public void run()
							{
								saveFile.setProgress(0);
							}
						}, 2000);
					}
					catch (Exception e)
					{
						Toast.makeText(getContext(), "Please chose a file first and don't keep it empty", Toast.LENGTH_SHORT).show();
						saveFile.setProgress(-1);
						Handler handler = new Handler();
						handler.postDelayed(new Runnable()
						{
							@Override
							public void run()
							{
								saveFile.setProgress(0);
							}
						}, 2000);

					}
				}

			});
			choseFile.setOnClickListener(new View.OnClickListener()
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
					startActivityForResult(intent, OpenFileFragment.PICK_FILE_RESULT_CODE);
				}
			});
			return rootView;
		}

		else if (position == 2)
		{
			return new PushRequestFragment(inflater, container, savedInstanceState, getContext()).onCreate();
		}

		else if (position == 3)
		{
			//Todo Pull Version
			return new PullOwnVersionFragment(inflater, container, savedInstanceState, getContext()).onCreate();
		}
		else if (position == 4)
		{
			return new SendMergeRequestFragment(inflater, container, savedInstanceState, getContext()).onCreate();
		}
		else if (position == 5)
		{
			View rootView = inflater.inflate(R.layout.create_new, container, false);
			Log.e("Called", "onCreateView: ");
			//Toast.makeText(getContext(), "Called", Toast.LENGTH_SHORT).show();
			final EditText repoName = ((EditText) rootView.findViewById(R.id.repo_name_create));
			path = ((EditText) rootView.findViewById(R.id.folder_path_create));
			final CircularProgressButton btnCreateNewRepo = (CircularProgressButton) rootView.findViewById(R.id.btn_create_new_repo);
			CircularProgressButton chooseDirectory = (CircularProgressButton) rootView.findViewById(R.id.btn_chose_directory);
			btnCreateNewRepo.setIndeterminateProgressMode(true);
			btnCreateNewRepo.setProgress(0);
			chooseDirectory.setOnClickListener(new View.OnClickListener()
			{
				@Override
				public void onClick(View v)
				{
					final Intent chooserIntent = new Intent(getContext(), DirectoryChooserActivity.class);
					final DirectoryChooserConfig config = DirectoryChooserConfig.builder().newDirectoryName("DirChooserSample").allowReadOnlyDirectory(true).allowNewDirectoryNameModification(true).build();
					chooserIntent.putExtra(DirectoryChooserActivity.EXTRA_CONFIG, config);
					startActivityForResult(chooserIntent, REQUEST_DIRECTORY_PUSH);
				}
			});
			btnCreateNewRepo.setOnClickListener(new View.OnClickListener()
			{
				@Override
				public void onClick(View v)
				{
					btnCreateNewRepo.setProgress(50);
					DirectoryToZip d2z = new DirectoryToZip(new DirectoryToZip.OnTaskDoneListener()
					{
						@Override
						public void onTaskDone(boolean flag, final String path2)
						{
							Log.e("TAG", "onTaskDone: " + flag);
							if (flag)
							{
								Toast.makeText(getContext(), "Folder Successfully compressed" + Environment.getExternalStorageDirectory(), Toast.LENGTH_SHORT).show();
								HashMap<String, String> hash = new HashMap<>(2);
								hash.put("username", Home.userName);
								hash.put("repoName", repoName.getText().toString());
								PostRequestSend prs = new PostRequestSend("http://ezcloud.esy.es/ezCloudWebsite/commit.php?", hash);
								prs.setContext(getContext());
								prs.setTaskDoneListener(new PostRequestSend.TaskDoneListener()
								{
									@Override
									public String onTaskDone(String str)
									{
										Log.e("onTaskDone", "onTaskDone: " + path2 + " " + str);
										new UploadFile(path2, getContext()).execute(str.split("\\s+")[str.split("\\s+").length - 1], repoName.getText().toString());
										btnCreateNewRepo.setProgress(100);
										Handler handler = new Handler();
										handler.postDelayed(new Runnable()
										{
											@Override
											public void run()
											{
												btnCreateNewRepo.setProgress(0);
											}
										}, 2000);
										return null;
									}
								});
								prs.execute();

							}
							else
							{
								Toast.makeText(getContext(), "Make sure your folder is not empty", Toast.LENGTH_SHORT).show();
								btnCreateNewRepo.setProgress(-1);
								Handler handler = new Handler();
								handler.postDelayed(new Runnable()
								{
									@Override
									public void run()
									{
										btnCreateNewRepo.setProgress(0);
									}
								}, 2000);
							}
						}
					});
					d2z.setContext(getContext());
					d2z.execute( path.getText().toString(),path.getText().toString()+"/"+ repoName.getText().toString());
				}
			});
			return rootView;

		}
		else
		{
			return new LogoutFragment(inflater, container, savedInstanceState, getContext(), getActivity()).onCreate();
		}
	}
	public void onActivityResult(int requestCode, int resultCode, Intent data)
	{
		// TODO Auto-generated method stub
		Log.e("plz tell me I am here", "oh");
		switch (requestCode)
		{
			case OpenFileFragment.PICK_FILE_RESULT_CODE:
				if (resultCode == Activity.RESULT_OK)
				{
					uri = data.getData();
					Log.e("URI", "onActivityResult: " + uri.toString());
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
			case REQUEST_DIRECTORY_PUSH:
				if (resultCode == DirectoryChooserActivity.RESULT_CODE_DIR_SELECTED)
					path.setText(data.getStringExtra(DirectoryChooserActivity.RESULT_SELECTED_DIR));
				else
					path.setText("nothing selected");
				break;
		}
	}

	public interface OnCreateViewCalledListener
	{
		void onCreateViewCalled(int position);
	}
}