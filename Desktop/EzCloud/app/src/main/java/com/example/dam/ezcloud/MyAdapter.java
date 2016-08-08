package com.example.dam.ezcloud;

import android.content.Context;
import android.graphics.Color;
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

import org.json.JSONException;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;

import co.dift.ui.SwipeToAction;

/**
 * Created by dam on 8/8/16.
 */
public class MyAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>
{
	private static final String TAG = MyAdapter.class.getSimpleName();
	public ArrayList<SingleMessage> arrayList;
	public Context context;

	public class MyItemViewHolder extends SwipeToAction.ViewHolder<SingleMessage>
	{
		ImageView imageView;
		TextView sender, details;
		CircularProgressButton download;
		public MyItemViewHolder(View convertView)
		{
			super(convertView);
			imageView = (ImageView) convertView.findViewById(R.id.temp_image_view);
			sender = (TextView) convertView.findViewById(R.id.temp_edit_text_from);
			details = (TextView) convertView.findViewById(R.id.temp_edit_text_details);
			download = (CircularProgressButton) convertView.findViewById(R.id.temp_btn_download);
		}
	}
	public MyAdapter(ArrayList<SingleMessage> arrayList, Context context)
	{
		this.arrayList = arrayList;
		this.context = context;
	}
	@Override
	public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType)
	{
		View view = LayoutInflater.from(context).inflate(R.layout.messages_received1, null);
		return new MyItemViewHolder(view);
	}
	@Override
	public void onBindViewHolder(final RecyclerView.ViewHolder holder, final int position)
	{
		final SingleMessage item = arrayList.get(position);
		final MyItemViewHolder myItemViewHolder = (MyItemViewHolder) holder;
		Random rnd = new Random();
		final int color = Color.argb(255, rnd.nextInt(256), rnd.nextInt(256), rnd.nextInt(256));
		TextDrawable drawable = TextDrawable.builder().beginConfig().toUpperCase().endConfig().buildRect("" + item.sender.charAt(0), color);
		myItemViewHolder.imageView.setImageDrawable(drawable);
		myItemViewHolder.details.setText(item.details);
		myItemViewHolder.sender.setText(item.sender);
		myItemViewHolder.download.setProgress(0);
		myItemViewHolder.download.setIndeterminateProgressMode(true);
		myItemViewHolder.download.setOnClickListener(new View.OnClickListener()
		{
			@Override
			public void onClick(View v)
			{
				myItemViewHolder.download.setProgress(50);
				final String[] ver = new String[1];
				HashMap<String, String> hashMap = new HashMap<String, String>();
				hashMap.put("username", myItemViewHolder.sender.getText().toString());
				hashMap.put("reponame", item.repo_receiver);
				PostRequestSend postRequestSend = new PostRequestSend("http://ezcloud.esy.es/ezCloudWebsite/get_repo_version.php?", hashMap);
				postRequestSend.setContext(context);
				postRequestSend.setTaskDoneListener(new PostRequestSend.TaskDoneListener()
				{
					@Override
					public String onTaskDone(String str) throws JSONException
					{
						ver[0] = str;
						String getFileName = item.repo_receiver + "/" + str + "/" + item.repo_receiver;
						DownloadFileFTP downloadFileFTP = new DownloadFileFTP(context, getFileName, myItemViewHolder.sender.getText().toString(), new DownloadFileFTP.OnFileDownloadListener()
						{
							@Override
							public void onFileDownload(String path)
							{
								Log.e(TAG, "onFileDownload: " + path);
								if (path.contains("//"))
								{
									myItemViewHolder.download.setProgress(-1);
									Toast.makeText(context, "Check your Internet", Toast.LENGTH_SHORT).show();
									Handler handler = new Handler();
									handler.postDelayed(new Runnable()
									{
										@Override
										public void run()
										{
											myItemViewHolder.download.setProgress(0);
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
												myItemViewHolder.download.setProgress(100);
											}
											else
											{
												myItemViewHolder.download.setProgress(-1);
												Handler handler = new Handler();
												handler.postDelayed(new Runnable()
												{
													@Override
													public void run()
													{
														myItemViewHolder.download.setProgress(0);
													}
												}, 2000);
											}
										}
									});
									zipToDirectory.execute(path, Environment.getExternalStorageDirectory().getAbsolutePath() + "/" + item.repo_receiver);
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
		myItemViewHolder.data = item;
	}
	@Override
	public int getItemCount()
	{
		return arrayList.size();
	}
}
