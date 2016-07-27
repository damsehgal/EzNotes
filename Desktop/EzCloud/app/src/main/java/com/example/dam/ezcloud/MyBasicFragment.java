package com.example.dam.ezcloud;

import android.app.Fragment;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * Created by dam on 24/7/16.
 */
public abstract class MyBasicFragment
{

	LayoutInflater inflater;
	ViewGroup container;
	Bundle savedInstanceState;
	Context context;
	public MyBasicFragment(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState, Context context)
	{

		this.inflater = inflater;
		this.container = container;
		this.savedInstanceState = savedInstanceState;
		this.context = context;
	}
	public View getRootView(int layout)
	{
		return inflater.inflate(layout, container, false);
	}
	public abstract View onCreate();
}
