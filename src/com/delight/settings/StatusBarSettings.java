/*
 * Copyright (C) 2017 The ABC rom
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.delight.settings;

import android.content.ContentResolver;
import android.content.res.Resources;
import android.net.TrafficStats;
import android.os.Bundle;
import android.support.v14.preference.SwitchPreference;
import android.support.v7.preference.ListPreference;
import android.support.v7.preference.Preference;
import android.support.v7.preference.PreferenceGroup;
import android.support.v7.preference.PreferenceScreen;
import android.support.v7.preference.PreferenceCategory;
import android.provider.Settings;

import com.android.internal.logging.nano.MetricsProto;

import com.android.settings.R;
import com.android.settings.SettingsPreferenceFragment;

import com.android.internal.util.omni.DeviceUtils;

public class StatusBarSettings extends SettingsPreferenceFragment implements
        Preference.OnPreferenceChangeListener {

    private static final String NETWORK_TRAFFIC_ROOT = "category_network_traffic";

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.delight_statusbar_settings);

        PreferenceScreen prefScreen = getPreferenceScreen();

        // TrafficStats will return UNSUPPORTED if the device does not support it.
        if (TrafficStats.getTotalTxBytes() == TrafficStats.UNSUPPORTED ||
                TrafficStats.getTotalRxBytes() == TrafficStats.UNSUPPORTED) {
            prefScreen.removePreference(findPreference(NETWORK_TRAFFIC_ROOT));
        }
    }

    @Override
    public int getMetricsCategory() {
        return MetricsProto.MetricsEvent.DELIGHT;
    }

    public boolean onPreferenceChange(Preference preference, Object newValue) {
        return false;
    }
}
