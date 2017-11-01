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
import android.os.Bundle;
import android.os.UserHandle;
import android.support.v7.preference.ListPreference;
import android.support.v7.preference.Preference;
import android.support.v7.preference.PreferenceScreen;
import android.provider.Settings;

import com.android.internal.logging.nano.MetricsProto;
import com.android.internal.util.delight.DelightUtils;

import com.android.settings.R;
import com.android.settings.SettingsPreferenceFragment;

public class PowerSettings extends SettingsPreferenceFragment implements
        Preference.OnPreferenceChangeListener {

    private static final String TORCH_POWER_BUTTON_GESTURE = "torch_power_button_gesture";
    private static final String KEY_TORCH_LONG_PRESS_POWER_TIMEOUT = "torch_long_press_power_timeout";

    private ListPreference mTorchPowerButton;
    private ListPreference mTorchLongPressPowerTimeout;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.delight_power_settings);
        ContentResolver resolver = getActivity().getContentResolver();
        final PreferenceScreen prefScreen = getPreferenceScreen();

        if (!DelightUtils.deviceHasFlashlight(getContext())) {
            Preference toRemove = prefScreen.findPreference(TORCH_POWER_BUTTON_GESTURE);
            Preference toRemove1 = prefScreen.findPreference(KEY_TORCH_LONG_PRESS_POWER_TIMEOUT);
            if (toRemove != null) {
                prefScreen.removePreference(toRemove);
            }
            if (toRemove1 != null) {
                prefScreen.removePreference(toRemove1);
            }
        } else {
            mTorchPowerButton = (ListPreference) findPreference(TORCH_POWER_BUTTON_GESTURE);
            int mTorchPowerButtonValue = Settings.Secure.getInt(resolver,
                    Settings.Secure.TORCH_POWER_BUTTON_GESTURE, 0);
            mTorchPowerButton.setValue(Integer.toString(mTorchPowerButtonValue));
            mTorchPowerButton.setSummary(mTorchPowerButton.getEntry());
            mTorchPowerButton.setOnPreferenceChangeListener(this);

            if (mTorchPowerButtonValue == 2) {
                mTorchLongPressPowerTimeout = (ListPreference) findPreference(KEY_TORCH_LONG_PRESS_POWER_TIMEOUT);
                int torchLongPressPowerTimeout = Settings.System.getInt(resolver,
                        Settings.System.TORCH_LONG_PRESS_POWER_TIMEOUT, 0);
                mTorchLongPressPowerTimeout.setEnabled(true);
                mTorchLongPressPowerTimeout.setValue(Integer.toString(torchLongPressPowerTimeout));
                mTorchLongPressPowerTimeout.setSummary(mTorchLongPressPowerTimeout.getEntry());
                mTorchLongPressPowerTimeout.setOnPreferenceChangeListener(this);
            } else if ((mTorchPowerButtonValue == 1) || (mTorchPowerButtonValue == 0)) {
                mTorchLongPressPowerTimeout.setEnabled(false);
            }
        }
    }

    @Override
    public int getMetricsCategory() {
        return MetricsProto.MetricsEvent.DELIGHT;
    }

    public boolean onPreferenceChange(Preference preference, Object newValue) {
        ContentResolver resolver = getActivity().getContentResolver();

        if (preference == mTorchPowerButton) {
            int mTorchPowerButtonValue = Integer.valueOf((String) newValue);
            int index = mTorchPowerButton.findIndexOfValue((String) newValue);
            mTorchPowerButton.setSummary(
                    mTorchPowerButton.getEntries()[index]);
            Settings.Secure.putInt(resolver, Settings.Secure.TORCH_POWER_BUTTON_GESTURE,
                    mTorchPowerButtonValue);
            if (mTorchPowerButtonValue == 1) {
                //if doubletap for torch is enabled, switch off double tap for camera
                Settings.Secure.putInt(resolver, Settings.Secure.CAMERA_DOUBLE_TAP_POWER_GESTURE_DISABLED,
                        1);
                mTorchLongPressPowerTimeout.setEnabled(false);
            } else if (mTorchPowerButtonValue == 2) {
                mTorchLongPressPowerTimeout.setEnabled(true);
            } else if (mTorchPowerButtonValue == 0) {
                mTorchLongPressPowerTimeout.setEnabled(false);
            }
            return true;
        } else if (preference == mTorchLongPressPowerTimeout) {
            int mTorchLongPressPowerTimeoutValue = Integer.valueOf((String) newValue);
            int index = mTorchLongPressPowerTimeout.findIndexOfValue((String) newValue);
            mTorchLongPressPowerTimeout.setSummary(
                    mTorchLongPressPowerTimeout.getEntries()[index]);
            Settings.System.putInt(resolver, Settings.System.TORCH_LONG_PRESS_POWER_TIMEOUT, mTorchLongPressPowerTimeoutValue);
            return true;
        }

        return false;
    }
}
