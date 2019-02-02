package in.kiran.flashlightdemo.utils;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.hardware.camera2.CameraAccessException;
import android.hardware.camera2.CameraManager;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;
import android.widget.Toast;

import in.kiran.flashlightdemo.R;
import in.kiran.flashlightdemo.preference.SharedPref;

import static in.kiran.flashlightdemo.MainActivity.ACTION_UPDATE_UI;
import static in.kiran.flashlightdemo.MainActivity.IS_FLASHLIGHT_ON;
import static in.kiran.flashlightdemo.MainActivity.TAG;

public class Utilities {

    private static CameraManager mCameraManager;

    public static boolean startFlashlight(Context context, SharedPref preference) {
        if (isCameraSupportedForFlash(context)) {
            if (flashLightOn(context)) {
                return preference.setValue(IS_FLASHLIGHT_ON, true);
            }
        }
        return false;
    }

    public static boolean stopFlashlight(Context context, SharedPref preference) {
        if (flashLightOff(context)) {
            return preference.setValue(IS_FLASHLIGHT_ON, false);
        }
        return false;
    }

    private static void initCameraManagerInstance(Context context) {
        if (mCameraManager == null) {
            mCameraManager = (CameraManager) context.getSystemService(Context.CAMERA_SERVICE);
        }
    }

    private static boolean flashLightOn(Context context) {
        try {
            if (mCameraManager == null) {
                initCameraManagerInstance(context);
            }
            String cameraId = mCameraManager.getCameraIdList()[0];
            mCameraManager.setTorchMode(cameraId, true);
            // Sending broadcast to update the UI
            LocalBroadcastManager.getInstance(context).sendBroadcast(new Intent(ACTION_UPDATE_UI));
        } catch (CameraAccessException e) {
            Log.e(TAG, "flashLightOn(): " + e);
            return false;
        }
        return true;
    }

    private static boolean flashLightOff(Context context) {
        try {
            if (mCameraManager == null) {
                initCameraManagerInstance(context);
            }
            String cameraId = mCameraManager.getCameraIdList()[0];
            mCameraManager.setTorchMode(cameraId, false);
            // Sending broadcast to update the UI
            LocalBroadcastManager.getInstance(context).sendBroadcast(new Intent(ACTION_UPDATE_UI));
        } catch (CameraAccessException e) {
            Log.e(TAG, "flashLightOff(): " + e);
            return false;
        }
        return true;
    }

    public static boolean isCameraPermissionGranted(Context context) {
        return ContextCompat.checkSelfPermission(context, Manifest.permission.CAMERA)
                == PackageManager.PERMISSION_GRANTED;
    }

    public static boolean isCameraSupportedForFlash(Context context) {
        final boolean hasCameraFlash
                = context.getPackageManager().hasSystemFeature(PackageManager.FEATURE_CAMERA_FLASH);

        if (!hasCameraFlash) {
            String message = context.getString(R.string.no_flash_available);
            showToast(context, message);
        }
        return  hasCameraFlash;
    }

    public static void showToast (Context context, String message) {
        Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
    }
}