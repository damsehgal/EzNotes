package com.example.dam.ezcloud;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * Created by dam on 25/7/16.
 */
public class CreateNewRepoWithPush extends MyBasicFragment
{
	public CreateNewRepoWithPush(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState, Context context, Activity activity)
	{
		super(inflater, container, savedInstanceState, context);
	}
	@Override
	public View onCreate()
	{
		View rootView = getRootView(R.layout.create_new);
		return rootView;
	}
}
