package com.example.dam.ezcloud;
//TODO ADD PERMISSION FOR MARSHMALLOW
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class BlankFragment extends Fragment
{
	private static final String TAG = "BlankFragment";
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
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
	{
		Log.e("Fragment", "onCreateView: Called");
		if (position == 0)
		{
			Log.e(TAG, "onCreateView: password called");
			return new ChangePasswordFragment(inflater, container, savedInstanceState, getContext()).onCreate();
		}
		else if (position == 1)
		{
			Log.e("pos1", "onCreateView: ");
			return new OpenFileFragment(inflater, container, savedInstanceState, getContext(), getActivity()).onCreate();
		}

		else if (position == 2)
		{
			// TODO RECIEVE PUSH REQUESTS ...
			return new PushRequestFragment(inflater, container, savedInstanceState, getContext()).onCreate();
		}
		else if (position == 3)
		{
			//TODO CLONE
			// i.e clone a repository || take a repository and place it in my database
			return new CloneFragment(inflater, container, savedInstanceState, getContext()).onCreate();
		}
		else if (position == 4)
		{
			return new PullOwnVersionFragment(inflater, container, savedInstanceState, getContext()).onCreate();
		}
		else if (position == 5)
		{
			//TODO SEND MERGE REQUEST
			return new SendMergeRequestFragment(inflater, container, savedInstanceState, getContext()).onCreate();
		}
		else if (position == 6)
		{
			return new CreateNewRepoWithPush(inflater, container, savedInstanceState, getContext(),getActivity()).onCreate();
		}
		else
		{
			return new LogoutFragment(inflater, container, savedInstanceState, getContext(), getActivity()).onCreate();
		}
	}
	public interface OnCreateViewCalledListener
	{
		void onCreateViewCalled(int position);
	}
}