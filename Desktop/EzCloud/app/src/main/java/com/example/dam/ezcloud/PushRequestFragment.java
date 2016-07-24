package com.example.dam.ezcloud;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * Created by dam on 25/7/16.
 */
// TODO PUSH REQUEST
public class PushRequestFragment extends MyBasicFragment
{
	public PushRequestFragment(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState, Context context)
	{
		super(inflater, container, savedInstanceState, context);
	}
	@Override
	public View onCreate()
	{
		return getRootView(R.layout.push_request);
	}
}
