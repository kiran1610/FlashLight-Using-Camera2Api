# FlashLight-Using-Camera2Api

FlashLight Demo using Camera 2 api.

This sample code explain how to use Camera 2 api to set Torch mode.

Start Torch mode using below code:
```
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
```
Stop Torch mode using below code:
```
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
```

Please feel free to download and give a try.

You can see fully functioning FlashLight application using above code in XXX.