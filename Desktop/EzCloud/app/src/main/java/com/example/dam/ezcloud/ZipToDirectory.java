package com.example.dam.ezcloud;

import android.os.AsyncTask;
import android.util.Log;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

/**
 * Created by dam on 8/7/16.
 */
public class ZipToDirectory extends AsyncTask<String, Void, Void>
{
	private static final String TAG = ZipToDirectory.class.getSimpleName();

	@Override
	protected Void doInBackground(String... params)
	{

		try
		{
			unzip(new File(params[0]), new File(params[1]));
			Log.e(TAG, "doInBackground: " + params[0] + " " + params[1]);
		}
		catch (IOException e)
		{
			return null;
		}
		return null;
	}

	private void unzip(File zipFile, File targetDirectory) throws IOException
	{

		ZipInputStream zis = new ZipInputStream(new BufferedInputStream(new FileInputStream(zipFile)));
		try
		{
			ZipEntry ze;
			int count;
			byte[] buffer = new byte[8192];
			while ((ze = zis.getNextEntry()) != null)
			{
				Log.d(TAG, "unzip: " + zipFile.getAbsolutePath() + " " + targetDirectory.getAbsolutePath());
				File file = new File(targetDirectory, ze.getName());
				File dir = ze.isDirectory() ? file : file.getParentFile();
				if (!dir.isDirectory() && !dir.mkdirs())
					throw new FileNotFoundException("Failed to ensure directory: " +
							dir.getAbsolutePath());
				if (ze.isDirectory())
					continue;
				FileOutputStream fout = new FileOutputStream(file);
				try
				{
					while ((count = zis.read(buffer)) != -1)
						fout.write(buffer, 0, count);
				}
				finally
				{
					fout.close();
				}
		    /* if time should be restored as well
            long time = ze.getTime();
            if (time > 0)
                file.setLastModified(time);
            */
			}
		}
		finally
		{
			zis.close();
		}
	}
}