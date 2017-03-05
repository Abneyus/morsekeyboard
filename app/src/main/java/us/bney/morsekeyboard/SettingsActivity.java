package us.bney.morsekeyboard;

import android.content.Intent;
import android.os.Bundle;
import android.preference.PreferenceActivity;
import com.android.inputmethodcommon.InputMethodSettingsFragment;

public class SettingsActivity extends PreferenceActivity {
    @Override
    public Intent getIntent() {
        final Intent modIntent = new Intent(super.getIntent());
        modIntent.putExtra(EXTRA_SHOW_FRAGMENT, Settings.class.getName());
        modIntent.putExtra(EXTRA_NO_HEADERS, true);
        return modIntent;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setTitle(R.string.mk_settings);
    }

    @Override
    protected boolean isValidFragment(final String fragmentName) {
        return Settings.class.getName().equals(fragmentName);
    }

    public static class Settings extends InputMethodSettingsFragment {
        @Override
        public void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setInputMethodSettingsCategoryTitle(R.string.st_timing);
            setSubtypeEnablerTitle(R.string.st_dotlength);

            // Load the preferences from an XML resource
            addPreferencesFromResource(R.xml.settings_preference_screen);
        }
    }
}
