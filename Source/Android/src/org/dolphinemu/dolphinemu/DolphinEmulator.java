/**
 * Copyright 2013 Dolphin Emulator Project
 * Licensed under GPLv2
 * Refer to the license.txt file included.
 */

package org.dolphinemu.dolphinemu;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import org.dolphinemu.dolphinemu.gamelist.GameListActivity;
import org.dolphinemu.dolphinemu.settings.UserPreferences;

import java.io.*;

/**
 * The main activity of this emulator.
 *
 * @param <MainActivity> Main activity.
 */
public final class DolphinEmulator<MainActivity> extends Activity 
{
	private void CopyAsset(String asset, String output)
	{
		InputStream in = null;
		OutputStream out = null;

		try
		{
			in = getAssets().open(asset);
			out = new FileOutputStream(output);
			copyFile(in, out);
			in.close();
			out.close();
		}
		catch (IOException e)
		{
			Log.e("DolphinEmulator", "Failed to copy asset file: " + asset, e);
		}
	}

	private void copyFile(InputStream in, OutputStream out) throws IOException
	{
		byte[] buffer = new byte[1024];
		int read;

		while ((read = in.read(buffer)) != -1)
		{
			out.write(buffer, 0, read);
		}
	}

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		if (savedInstanceState == null)
		{
			Intent ListIntent = new Intent(this, GameListActivity.class);
			startActivityForResult(ListIntent, 1);

			// Make the assets directory
			String BaseDir = Environment.getExternalStorageDirectory()+File.separator+"dolphin-emu";
			File directory = new File(BaseDir);
			directory.mkdirs();

			String ConfigDir = BaseDir + File.separator + "Config";
			directory = new File(ConfigDir);
			directory.mkdirs();

			String GCDir = BaseDir + File.separator + "GC";
			directory = new File(GCDir);
			directory.mkdirs();

			String WiiDir = BaseDir + File.separator + "Wii";
			directory = new File(WiiDir);
			directory.mkdirs();

			// Copy assets if needed
			File file = new File(WiiDir + File.separator + "setting-usa.txt");
			if(!file.exists())
			{
				CopyAsset("ButtonA.png",     BaseDir + File.separator + "ButtonA.png");
				CopyAsset("ButtonB.png",     BaseDir + File.separator + "ButtonB.png");
				CopyAsset("ButtonStart.png", BaseDir + File.separator + "ButtonStart.png");
				CopyAsset("NoBanner.png",    BaseDir + File.separator + "NoBanner.png");
				CopyAsset("GCPadNew.ini",    ConfigDir + File.separator + "GCPadNew.ini");
				CopyAsset("Dolphin.ini",     ConfigDir + File.separator + "Dolphin.ini");
				CopyAsset("dsp_coef.bin",    GCDir + File.separator + "dsp_coef.bin");
				CopyAsset("dsp_rom.bin",     GCDir + File.separator + "dsp_rom.bin");
				CopyAsset("font_ansi.bin",   GCDir + File.separator + "font_ansi.bin");
				CopyAsset("font_sjis.bin",   GCDir + File.separator + "font_sjis.bin");
				CopyAsset("setting-eur.txt", WiiDir + File.separator + "setting-eur.txt");
				CopyAsset("setting-jpn.txt", WiiDir + File.separator + "setting-jpn.txt");
				CopyAsset("setting-kor.txt", WiiDir + File.separator + "setting-kor.txt");
				CopyAsset("setting-usa.txt", WiiDir + File.separator + "setting-usa.txt");
			}

			// Load the configuration keys set in the Dolphin ini and gfx ini files
			// into the application's shared preferences.
			UserPreferences.LoadDolphinConfigToPrefs(this);
		}
	}
}