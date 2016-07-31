package com.example.dam.ezcloud;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

/**
 * Created by dam on 30/7/16.
 */
public class CheckNetConnection
{
	private Context _context;
	public CheckNetConnection(Context context)
	{
		this._context = context;
	}
	/**
	 * Checking for all possible internet providers
	 **/
	public boolean isConnectingToInternet()
	{
		ConnectivityManager cm = (ConnectivityManager) _context.getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo netInfo = cm.getActiveNetworkInfo();
		//should check null because in airplane mode it will be null
		return (netInfo != null && netInfo.isConnected());
	}
}
