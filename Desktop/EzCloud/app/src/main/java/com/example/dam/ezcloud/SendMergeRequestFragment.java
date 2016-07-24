package com.example.dam.ezcloud;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * Created by dam on 25/7/16.
 */
//TODO SEND MERGE REQUEST
public class SendMergeRequestFragment extends  MyBasicFragment
{
	public SendMergeRequestFragment(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState, Context context)
	{
		super(inflater, container, savedInstanceState, context);
	}
	@Override
	public View onCreate()
	{
		return getRootView(R.layout.merge);
	}
}
