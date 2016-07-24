package com.example.dam.ezcloud;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.TextView;

import net.rdrei.android.dirchooser.DirectoryChooserConfig;
import net.rdrei.android.dirchooser.DirectoryChooserFragment;

/**
 * Created by dam on 25/7/16.
 */
public class DirectoryChooserPushSubFragment extends Activity implements DirectoryChooserFragment.OnFragmentInteractionListener
{
	private TextView mDirectoryTextView;
	private DirectoryChooserFragment mDialog;
	protected void onCreate(@Nullable final Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.dialog);
		final DirectoryChooserConfig config = DirectoryChooserConfig.builder()
				.newDirectoryName("DialogSample")
				.build();
		mDialog = DirectoryChooserFragment.newInstance(config);

		mDirectoryTextView = (TextView) findViewById(R.id.textDirectory);

		findViewById(R.id.btnChoose).setOnClickListener(new View.OnClickListener()
		{
			@Override
			public void onClick(View v)
			{
				mDialog.show(getFragmentManager(), null);
			}
		});
	}

	@Override
	public void onSelectDirectory(@NonNull String path)
	{
		mDirectoryTextView.setText(path);
		mDialog.dismiss();
	}
	@Override
	public void onCancelChooser()
	{
		mDialog.dismiss();
	}
}
