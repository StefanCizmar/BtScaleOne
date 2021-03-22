package com.kiwi.btscaleone.utils;

import android.content.Context;
import android.widget.Toast;

//Toast统一管理类
public class T {

	private T() {
		/* cannot be instantiated */
		throw new UnsupportedOperationException("cannot be instantiated");
	}

	private static Toast mToast;

	/**
	 * Display Toast for a short time
	 * 
	 * @param context
	 * @param message
	 */
	public static void showShort(Context context, CharSequence message) {
		if (mToast == null) {
			mToast = Toast.makeText(context, message, Toast.LENGTH_SHORT);
		} else {
			mToast.setText(message);
			mToast.setDuration(Toast.LENGTH_SHORT);
		}
		mToast.show();
	}

	/**
	 * Display Toast for a short time
	 * 
	 * @param context
	 * @param message
	 */
	public static void showShort(Context context, int message) {
		if (mToast == null) {
			mToast = Toast.makeText(context, message, Toast.LENGTH_SHORT);
		} else {
			mToast.setText(message);
			mToast.setDuration(Toast.LENGTH_SHORT);
		}
		mToast.show();
	}

	/**
	 * Display Toast for a long time
	 * 
	 * @param context
	 * @param message
	 */
	public static void showLong(Context context, CharSequence message) {
		if (mToast == null) {
			mToast = Toast.makeText(context, message, Toast.LENGTH_LONG);
		} else {
			mToast.setText(message);
			mToast.setDuration(Toast.LENGTH_LONG);
		}
		mToast.show();
	}

	/**
	 * Display Toast for a long time
	 * 
	 * @param context
	 * @param message
	 */
	public static void showLong(Context context, int message) {
		if (mToast == null) {
			mToast = Toast.makeText(context, message, Toast.LENGTH_LONG);
		} else {
			mToast.setText(message);
			mToast.setDuration(Toast.LENGTH_LONG);
		}
		mToast.show();
	}

	/**
	 * Custom display Toast time
	 * 
	 * @param context
	 * @param message
	 * @param duration
	 */
	public static void show(Context context, CharSequence message, int duration) {
		if (mToast == null) {
			mToast = Toast.makeText(context, message, duration);
		} else {
			mToast.setText(message);
			mToast.setDuration(duration);
		}
		mToast.show();
	}

	/**
	 * Custom display Toast time
	 * 
	 * @param context
	 * @param message
	 * @param duration
	 */
	public static void show(Context context, int message, int duration) {
		if (mToast == null) {
			mToast = Toast.makeText(context, message, duration);
		} else {
			mToast.setText(message);
			mToast.setDuration(duration);
		}
		mToast.show();
	}

}