package com.infor.wifi;
import android.app.Application;

import cn.bmob.v3.Bmob;
public class WiFiAPP extends Application
{
	private static WiFiAPP instance;
	public static WiFiAPP getContext()
	{
		return instance;
	}
	@Override
	public void onCreate()
	{
		super.onCreate();
		Bmob.initialize(this, "6510177added8afb43d74b6a7bf34560");
		instance = this;
		android.view.ViewConfiguration config = android.view.ViewConfiguration
				.get(this);
		try
		{
			java.lang.reflect.Field menuKeyField = android.view.ViewConfiguration.class
					.getDeclaredField("sHasPermanentMenuKey");
			if (menuKeyField != null)
			{
				menuKeyField.setAccessible(true);
				menuKeyField.setBoolean(config, false);
			}
		} catch (Exception e)
		{
			e.printStackTrace();
		}
	}
}
