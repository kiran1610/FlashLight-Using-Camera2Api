package in.kiran.flashlightdemo.preference;

import android.content.Context;
import android.content.SharedPreferences;

public class SharedPref {

    private static SharedPref mSharedPref;
    private SharedPreferences mSharedPreferences;

    private SharedPref(Context context) {
        mSharedPreferences = context.getSharedPreferences("KF_Pref", Context.MODE_PRIVATE);
    }

    /**
     * Creates single instance of SharedPref
     *
     * @param context context of Activity or Service
     * @return Returns instance of SharedPref
     */
    public static synchronized SharedPref getInstance(Context context) {
        if (mSharedPref == null) {
            mSharedPref = new SharedPref(context.getApplicationContext());
        }
        return mSharedPref;
    }

    /**
     * Stores boolean value in preference
     *
     * @param key   key of preference
     * @param value value for that key
     */
    public synchronized boolean setValue(String key, boolean value) {
        SharedPreferences.Editor editor = mSharedPreferences.edit();
        editor.putBoolean(key, value);
        return editor.commit();
    }

    /**
     * Retrieves boolean value from preference
     *
     * @param keyFlag key of preference
     */
    public boolean getBooleanValue(String keyFlag) {
        return mSharedPreferences.getBoolean(keyFlag, false);
    }
}
