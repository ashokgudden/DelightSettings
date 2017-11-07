package com.delight.settings.fragments;

import com.android.internal.logging.nano.MetricsProto;

import android.os.Bundle;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.os.UserHandle;
import android.content.ContentResolver;
import android.content.res.Resources;
import android.provider.Settings;
import android.support.v7.preference.ListPreference;
import android.support.v7.preference.Preference;
import android.support.v7.preference.PreferenceScreen;
import android.support.v7.preference.Preference.OnPreferenceChangeListener;
import android.support.v14.preference.SwitchPreference;
import com.delight.settings.preferences.SystemSettingSwitchPreference;
import com.android.settings.R;
import com.android.settings.SettingsPreferenceFragment;
import java.util.Locale;
import android.text.TextUtils;
import android.view.View;

import java.util.List;
import java.util.ArrayList;

public class GestureSettings extends SettingsPreferenceFragment implements
        OnPreferenceChangeListener {

    private static final String KEY_DOUBLE_TAP_SLEEP_NAVBAR = "double_tap_sleep_navbar";

    SystemSettingSwitchPreference mNavbarSleep;

    @Override
    public void onCreate(Bundle icicle) {
        super.onCreate(icicle);
        addPreferencesFromResource(R.xml.gestures_settings);
        ContentResolver resolver = getActivity().getContentResolver();

        mNavbarSleep = (SystemSettingSwitchPreference) findPreference(KEY_DOUBLE_TAP_SLEEP_NAVBAR);
        boolean enabled = Settings.Secure.getIntForUser(
                resolver, Settings.Secure.NAVIGATION_BAR_ENABLED,
                getActivity().getResources().getBoolean(
                com.android.internal.R.bool.config_showNavigationBar) ? 1 : 0,
                UserHandle.USER_CURRENT) == 1;
        mNavbarSleep.setEnabled(enabled);
        }

    @Override
    public boolean onPreferenceChange(Preference preference, Object newValue) {
        ContentResolver resolver = getActivity().getContentResolver();
        return false;
    }

    @Override
    public int getMetricsCategory() {
        return MetricsProto.MetricsEvent.DELIGHT;
    }
}
