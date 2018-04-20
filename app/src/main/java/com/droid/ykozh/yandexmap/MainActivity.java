package com.droid.ykozh.yandexmap;

import android.os.Bundle;
import android.preference.PreferenceActivity;

public class MainActivity extends PreferenceActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(com.droid.ykozh.yandexmap.R.xml.main);
    }
}
