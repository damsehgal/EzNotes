package com.example.dam.ezcloud;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

public class Home2 extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener
{
	static String userName, passWord;
	static ProgressDialog pd;
	Uri uri;
	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_home2);
		Intent intent = getIntent();
		userName = intent.getStringExtra(MainActivity.USERNAME_KEY);
		passWord = intent.getStringExtra(MainActivity.PASSWORD_KEY);
		Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
		toolbar.setTitle(userName);
		setSupportActionBar(toolbar);
		DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
		ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
			this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
		drawer.setDrawerListener(toggle);
		toggle.syncState();
		NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
		navigationView.setNavigationItemSelectedListener(this);
		navigationView.getMenu().getItem(2).setChecked(true);
		navigationView.setCheckedItem(navigationView.getMenu().getItem(2).getItemId());
		onNavigationItemSelected(navigationView.getMenu().findItem(R.id.nav_push_request));
	}
	@Override
	public void onBackPressed()
	{
		DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
		if (drawer.isDrawerOpen(GravityCompat.START))
		{
			drawer.closeDrawer(GravityCompat.START);
		}
		else
		{
			super.onBackPressed();
		}
	}
	@Override
	public boolean onCreateOptionsMenu(Menu menu)
	{
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.home2, menu);
		return true;
	}
	public void setFragment(int position)
	{
		BlankFragment blankFragment = new BlankFragment();
		Bundle bundle = new Bundle();
		bundle.putInt("key", position);
		blankFragment.setArguments(bundle);
		if (position == 1)
		{
			uri = blankFragment.getUri();
		}
		FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
		fragmentTransaction.replace(R.id.frame_layout_contains_fragment, BlankFragment.newInstance(position));
		pd = new ProgressDialog(Home2.this);
		pd.setMessage("loading");
		pd.show();
		fragmentTransaction.commit();
	}
	@SuppressWarnings("StatementWithEmptyBody")
	@Override
	public boolean onNavigationItemSelected(MenuItem item)
	{
		// Handle navigation view item clicks here.
		int id = item.getItemId();
		if (id == R.id.nav_change_password)
		{
			setFragment(0);
		}
		else if (id == R.id.nav_open_file)
		{
			setFragment(1);
		}
		else if (id == R.id.nav_push_request)
		{
			setFragment(2);
		}
		else if (id == R.id.nav_pull_version)
		{
			setFragment(3);
		}
		else if (id == R.id.nav_merge_req)
		{
			setFragment(4);
		}
		else if (id == R.id.nav_create_new)
		{
			setFragment(5);
		}
		else if (id == R.id.nav_logout)
		{
			setFragment(6);
		}
		DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
		drawer.closeDrawer(GravityCompat.START);
		return true;
	}
}
