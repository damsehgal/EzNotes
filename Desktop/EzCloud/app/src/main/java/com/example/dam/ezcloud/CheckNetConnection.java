package com.example.dam.ezcloud;

import java.net.InetAddress;

/**
 * Created by dam on 30/7/16.
 */
public class CheckNetConnection
{
	public static boolean isInternetAvailable()
	{
		try
		{
			InetAddress ipAddr = InetAddress.getByName("google.com");
			return !ipAddr.equals("");
		}
		catch (Exception e)
		{
			return false;
		}
	}
}
