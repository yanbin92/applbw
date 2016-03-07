package com.chinausky.lanbowan.evideo;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

import android.os.Environment;

import com.chinausky.lanbowan.R;
import com.evideo.voip.core.EvideoVoipCore;

public class SampleUtils {
	
	public static boolean isSdCardExist() {
		return Environment.getExternalStorageState().equals(
				Environment.MEDIA_MOUNTED);
	}

	public static String getSdCardWorkPath() {
		String workPath = null;
		if (isSdCardExist())
			workPath = Environment.getExternalStorageDirectory().getPath()
					+ File.separator + "lanbowan" + File.separator;
		return workPath;
	}

	public static String formatCurrentTimeFilename(long seconds) {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd_HH-mm-ss");
		sdf.setTimeZone(TimeZone.getDefault());
		Date date = new Date(seconds);
		return sdf.format(date);
	}

	public static int switchRegistrationState(
			EvideoVoipCore.RegistrationState status) {
		int resID = R.string.sip_registration_none;
		if (status == EvideoVoipCore.RegistrationState.RegistrationProgress) {
			resID = R.string.sip_registration_progress;
		} else if (status == EvideoVoipCore.RegistrationState.RegistrationOk) {
			resID = R.string.sip_registration_ok;
		} else if (status == EvideoVoipCore.RegistrationState.RegistrationCleared) {
			resID = R.string.sip_registration_cleared;
		} else if (status == EvideoVoipCore.RegistrationState.RegistrationFailed) {
			resID = R.string.sip_registration_failed;
		}
		return resID;
	}
}
