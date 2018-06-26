package com.awais2075gmail.awais2075.util;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.preference.PreferenceManager;
import android.util.Log;


import com.google.gson.Gson;

import java.util.List;
import java.util.Map;

public class FastSave {
    private static FastSave instance;
    private static SharedPreferences mSharedPreferences;

    private FastSave() {
    }

    public static void init(Context context) {
        mSharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
    }

    public static FastSave getInstance() {
        return new FastSave();
    }

    public void saveInt(String key, int value) {
        Editor editor = mSharedPreferences.edit();
        editor.putInt(key, value);
        editor.apply();
    }

    public int getInt(String key) {
        return this.isKeyExists(key) ? mSharedPreferences.getInt(key, 0) : 0;
    }

    public void saveBoolean(String key, boolean value) {
        Editor editor = mSharedPreferences.edit();
        editor.putBoolean(key, value);
        editor.apply();
    }

    public boolean getBoolean(String key) {
        return this.isKeyExists(key) ? mSharedPreferences.getBoolean(key, false) : false;
    }

    public void saveFloat(String key, float value) {
        Editor editor = mSharedPreferences.edit();
        editor.putFloat(key, value);
        editor.apply();
    }

    public float getFloat(String key) {
        return this.isKeyExists(key) ? mSharedPreferences.getFloat(key, 0.0F) : 0.0F;
    }

    public void saveLong(String key, long value) {
        Editor editor = mSharedPreferences.edit();
        editor.putLong(key, value);
        editor.apply();
    }

    public long getLong(String key) {
        return this.isKeyExists(key) ? mSharedPreferences.getLong(key, 0L) : 0L;
    }

    public void saveString(String key, String value) {
        Editor editor = mSharedPreferences.edit();
        editor.putString(key, value);
        editor.apply();
    }

    public String getString(String key) {
        return this.isKeyExists(key) ? mSharedPreferences.getString(key, (String) null) : null;
    }

    public <T> void saveObject(String key, T object) {
        String objectString = (new Gson()).toJson(object);
        Editor editor = mSharedPreferences.edit();
        editor.putString(key, objectString);
        editor.apply();
    }

    public <T> T getObject(String key, Class<T> classType) {
        if (this.isKeyExists(key)) {
            String objectString = mSharedPreferences.getString(key, (String) null);
            if (objectString != null) {
                return (new Gson()).fromJson(objectString, classType);
            }
        }

        return null;
    }

    public <T> void saveObjectsList(String key, List<T> objectList) {
        String objectString = (new Gson()).toJson(objectList);
        Editor editor = mSharedPreferences.edit();
        editor.putString(key, objectString);
        editor.apply();
    }


    public void clearSession() {
        Editor editor = mSharedPreferences.edit();
        editor.clear();
        editor.apply();
    }

    public boolean deleteValue(String key) {
        if (this.isKeyExists(key)) {
            Editor editor = mSharedPreferences.edit();
            editor.remove(key);
            editor.apply();
            return true;
        } else {
            return false;
        }
    }

    public boolean isKeyExists(String key) {
        Map<String, ?> map = mSharedPreferences.getAll();
        if (map.containsKey(key)) {
            return true;
        } else {
            Log.e("FastSave", "No element founded in sharedPrefs with the key " + key);
            return false;
        }
    }
}
