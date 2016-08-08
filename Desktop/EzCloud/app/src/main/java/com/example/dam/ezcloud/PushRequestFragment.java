package com.example.dam.ezcloud;

import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.amulyakhare.textdrawable.TextDrawable;
import com.dd.CircularProgressButton;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;

/**
 * Created by dam on 25/7/16.
 */
public class PushRequestFragment extends MyBasicFragment
{
	private static final String TAG = PushRequestFragment.class.getSimpleName();

	public class SingleMessage
	{
		String sender;
		String details;
		String repo_sender;
		String repo_receiver;
		String time;
		String messageId;
		int isRead;
		public SingleMessage(JSONObject jsonObject) throws JSONException
		{
			sender = jsonObject.getString("sender");
			repo_sender = jsonObject.getString("repo_sender_name");
			repo_receiver = jsonObject.getString("repo_receiver_name");
			details = jsonObject.getString("details");
			time = jsonObject.getString("message_time");
			isRead = jsonObject.getInt("isRead");
			messageId = jsonObject.getString("message_id");
		}
	}
	public PushRequestFragment(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState, Context context)
	{
		super(inflater, container, savedInstanceState, context);
	}
	ArrayList<SingleMessage> arrayList;
	RecyclerView listView;
	@Override
	public View onCreate()
	{
		final View rootView = getRootView(R.layout.push_request);
		listView = (RecyclerView) rootView.findViewById(R.id.list_view_container);
		final HashMap<String, String> hashMap = new HashMap<>(1);
		hashMap.put("receiver", Home2.userName);
		arrayList = new ArrayList<>();
		PostRequestSend postRequestSend = new PostRequestSend("http://ezcloud.esy.es/ezCloudWebsite/receiveMessages.php?", hashMap);
		postRequestSend.setContext(context);
		postRequestSend.setTaskDoneListener(new PostRequestSend.TaskDoneListener()
		{
			@Override
			public String onTaskDone(String str) throws JSONException
			{
				if (str.equals(""))
				{
					Toast.makeText(context, "Check your connection", Toast.LENGTH_SHORT).show();
				}
				else
				{
					Log.e(TAG, "onTaskDone: " + str);
					JSONArray jsonArray = new JSONArray(str);
					for (int i = 0; i < jsonArray.length(); i++)
					{
						arrayList.add(new SingleMessage(jsonArray.getJSONObject(i)));
						Log.e(TAG, "onTaskDone: here ?");
					}
					listView.setAdapter(new MyAdapter());
				}
				return null;
			}
		});
		try
		{
			postRequestSend.execute();
		}
		catch (Exception e)
		{
			Toast.makeText(context, "Check your connection", Toast.LENGTH_SHORT).show();
		}
		return rootView;
	}
	public class MyRecyclerViewHolder extends RecyclerView.ViewHolder
	{
		ImageView imageView;
		TextView sender, details;
		CircularProgressButton download;
		public MyRecyclerViewHolder(View convertView)
		{
			super(convertView);
			imageView = (ImageView) convertView.findViewById(R.id.temp_image_view);
			sender = (TextView) convertView.findViewById(R.id.temp_edit_text_from);
			details = (TextView) convertView.findViewById(R.id.temp_edit_text_details);
			download = (CircularProgressButton) convertView.findViewById(R.id.temp_btn_download);
		}
	}

	public class MyAdapter extends RecyclerView.Adapter<MyRecyclerViewHolder>
	{

		@Override
		public MyRecyclerViewHolder onCreateViewHolder(ViewGroup parent, int viewType)
		{
			LayoutInflater li = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			View itemView = li.inflate(R.layout.push_request,null);
			MyRecyclerViewHolder myRecyclerViewHolder = new MyRecyclerViewHolder(itemView);
			return  myRecyclerViewHolder;
		}
		public SingleMessage getItem(int position)
		{
			return arrayList.get(position);
		}
		@Override
		public void onBindViewHolder(final MyRecyclerViewHolder holder, final int position)
		{
			Random rnd = new Random();
			final int color = Color.argb(255, rnd.nextInt(256), rnd.nextInt(256), rnd.nextInt(256));
			TextDrawable drawable = TextDrawable.builder().beginConfig().toUpperCase().endConfig().buildRect("" + getItem(position).sender.charAt(0), color);
			holder.imageView.setImageDrawable(drawable);
			holder.details.setText(getItem(position).details);
			holder.sender.setText(getItem(position).sender);
			holder.download.setProgress(0);
			holder.download.setIndeterminateProgressMode(true);
			holder.download.setOnClickListener(new View.OnClickListener()
			{
				@Override
				public void onClick(View v)
				{
					holder.download.setProgress(50);
					final String[] ver = new String[1];
					HashMap<String, String> hashMap = new HashMap<String, String>();
					hashMap.put("username", holder.sender.getText().toString());
					hashMap.put("reponame", getItem(position).repo_receiver);
					PostRequestSend postRequestSend = new PostRequestSend("http://ezcloud.esy.es/ezCloudWebsite/get_repo_version.php?", hashMap);
					postRequestSend.setContext(context);
					postRequestSend.setTaskDoneListener(new PostRequestSend.TaskDoneListener()
					{
						@Override
						public String onTaskDone(String str) throws JSONException
						{
							ver[0] = str;
							String getFileName = getItem(position).repo_receiver + "/" + str + "/" + getItem(position).repo_receiver;
							DownloadFileFTP downloadFileFTP = new DownloadFileFTP(context, getFileName, holder.sender.getText().toString(), new DownloadFileFTP.OnFileDownloadListener()
							{
								@Override
								public void onFileDownload(String path)
								{
									Log.e(TAG, "onFileDownload: " + path);
									if (path.contains("//"))
									{
										holder.download.setProgress(-1);
										Toast.makeText(context, "Check your Internet", Toast.LENGTH_SHORT).show();
										Handler handler = new Handler();
										handler.postDelayed(new Runnable()
										{
											@Override
											public void run()
											{
												holder.download.setProgress(0);
											}
										}, 2000);
									}
									else
									{
										ZipToDirectory zipToDirectory = new ZipToDirectory();
										zipToDirectory.setOnTaskDoneListener(new ZipToDirectory.OnTaskDoneListener()
										{
											@Override
											public void onTaskDone(boolean isCompleted)
											{
												if (isCompleted)
												{
													holder.download.setProgress(100);
												}
												else
												{
													holder.download.setProgress(-1);
													Handler handler = new Handler();
													handler.postDelayed(new Runnable()
													{
														@Override
														public void run()
														{
															holder.download.setProgress(0);
														}
													}, 2000);
												}
											}
										});
										zipToDirectory.execute(path, Environment.getExternalStorageDirectory().getAbsolutePath() + "/" + getItem(position).repo_receiver);
										Toast.makeText(context, path, Toast.LENGTH_SHORT).show();
									}
								}
							});
							downloadFileFTP.execute();
							return null;
						}
					});
					postRequestSend.execute();
				}
			});
		}
		@Override
		public long getItemId(int position)
		{
			return 0;
		}
		@Override
		public int getItemCount()
		{
			return arrayList.size();
		}

	}
}
