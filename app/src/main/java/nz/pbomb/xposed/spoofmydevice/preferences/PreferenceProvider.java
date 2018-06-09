package nz.pbomb.xposed.spoofmydevice.preferences;

import com.crossbowffs.remotepreferences.RemotePreferenceProvider;

public class PreferenceProvider extends RemotePreferenceProvider {
    public PreferenceProvider() {
        super("nz.pbomb.xposed.spoofmydevice.provider.preferences", new String[] {"SharedPreferences"});
    }
}
