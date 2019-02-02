package in.kiran.flashlightdemo;

import android.Manifest;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;

import in.kiran.flashlightdemo.preference.SharedPref;
import in.kiran.flashlightdemo.utils.Utilities;


public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    public static final String TAG = "Kiran-FlashLight-Demo";

    public static final String ACTION_UPDATE_UI = "in.kiran.ACTION_UPDATE_UI";
    public static final String IS_FLASHLIGHT_ON = "is_flashlight_on";
    private static final int CAMERA_REQUEST = 0x001020;

    private ImageView mImageFlashlight;
    protected SharedPref mPreference;

    private BroadcastReceiver mImplicitBroadCast = new BroadcastReceiver() {

        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if (action == null) {
                return;
            }
            switch (action) {
                case ACTION_UPDATE_UI:
                    updateUI();
                    break;
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mImageFlashlight = (ImageView) findViewById(R.id.imageFlashlight);
        mImageFlashlight.setOnClickListener(this);

        mPreference = SharedPref.getInstance(this);
        registerMyOwnReceiver();
    }

    @Override
    protected void onStart() {
        updateUI();
        super.onStart();
    }

    @Override
    protected void onDestroy() {
        if (mPreference.getBooleanValue(IS_FLASHLIGHT_ON)) {
            Utilities.stopFlashlight(this, mPreference);
        }
        unregisterMyOwnReceiver();

        super.onDestroy();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case CAMERA_REQUEST:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    if (!Utilities.startFlashlight(this, mPreference)) {
                        Utilities.showToast(this, getString(R.string.flash_not_supported));
                        finish();
                    }
                } else {
                    Utilities.showToast(this, getString(R.string.no_camera_permission));
                    finish();
                }
                break;
        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.imageFlashlight:
                performFlashLightButtonClick();
                break;
        }
    }

    private void registerMyOwnReceiver() {
        IntentFilter intentFilterLocal = new IntentFilter();
        intentFilterLocal.addAction(ACTION_UPDATE_UI);
        LocalBroadcastManager.getInstance(this).registerReceiver(mImplicitBroadCast, intentFilterLocal);
    }

    private void unregisterMyOwnReceiver() {
        if (mImplicitBroadCast != null) {
            LocalBroadcastManager.getInstance(this).unregisterReceiver(mImplicitBroadCast);
        }
    }

    private void updateUI() {
        if (mPreference.getBooleanValue(IS_FLASHLIGHT_ON)) {
            mImageFlashlight.setImageResource(R.drawable.on);
        } else {
            mImageFlashlight.setImageResource(R.drawable.off);
        }
    }

    private void performFlashLightButtonClick() {
        boolean isEnabled = Utilities.isCameraPermissionGranted(this);
        if (!isEnabled) {
            requestPermissions(new String[]{Manifest.permission.CAMERA}, CAMERA_REQUEST);
            return;
        }

        if (mPreference.getBooleanValue(IS_FLASHLIGHT_ON)) {
            Utilities.stopFlashlight(this, mPreference);
        } else {
            Utilities.startFlashlight(this, mPreference);
        }
    }
}
