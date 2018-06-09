package nz.pbomb.xposed.spoofmydevice.activities;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.preference.Preference;
import android.preference.PreferenceFragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;

import java.util.HashSet;

import butterknife.ButterKnife;
import nz.pbomb.xposed.spoofmydevice.BuildConfig;
import nz.pbomb.xposed.spoofmydevice.Common;
import nz.pbomb.xposed.spoofmydevice.R;
import nz.pbomb.xposed.spoofmydevice.preferences.SharedPreferencesKeys;

public class MainActivity extends AppCompatActivity {
    private SharedPreferences mSharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        // Load User Preferences
        this.mSharedPreferences = getSharedPreferences("SharedPreferences", Context.MODE_PRIVATE);

        // Loading Google AdMob services for whole application
        MobileAds.initialize(getApplicationContext(), getString(R.string.admob_app_id));

        // Loading Banner Ad on MainActivity
        AdView mAdView = (AdView) findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder()
                .addTestDevice("EF959C00FE3BEBB55A6972D73A32BE00")
                .build();
        mAdView.loadAd(adRequest);

        // When the packages key doesn't exist, we can safetly assume this is the first time the
        // application is running, therefore set default values in the saved preferences
        if (!mSharedPreferences.contains(SharedPreferencesKeys.PACKAGES)) {
            setDefaultSharedPreferences();
        }

        // If the module is not activated in Xposed, tell the user to do so.
        if (!BuildConfig.DEBUG && !Common.isModuleActive()) {
            Drawable alertIcon = ContextCompat.getDrawable(this, R.drawable.ic_warning_black_24dp);
            alertIcon.setAlpha(127);

            new AlertDialog.Builder(this)
                    .setTitle(getString(R.string.MainActivity_AlertDialog_ModuleActivition_title))
                    .setCancelable(false)
                    .setMessage(getString(R.string.MainActivity_AlertDialog_ModuleActivition_message))
                    .setNegativeButton(getString(R.string.AlertDialog_Okay), new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            finish();
                        }
                    })
                    .setIcon(alertIcon)
                    .show();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // show menu when menu button is pressed
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        Intent intent = null;
        switch (item.getItemId()) {
            /*case R.id.menu_main_donate:
                intent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.paypal.com/cgi-bin/webscr?cmd=_s-xclick&hosted_button_id=QNQDESEMGWDPY"));
                break;
            */
            case R.id.menu_main_contact_me:
                intent = new Intent(getApplicationContext(), ContactActivity.class);
                break;
            /*case R.id.menu_main_xda:
                intent = new Intent(Intent.ACTION_VIEW, Uri.parse("http://XDA-HOLDER"));
                break;
             */
            case R.id.menu_main_about:
                intent = new Intent(getApplicationContext(), AboutActivity.class);
                break;
        }
        startActivity(intent);
        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("deprecation")
    private void setDefaultSharedPreferences() {
        SharedPreferences.Editor sharedPrefEditor = mSharedPreferences.edit();

        sharedPrefEditor.putStringSet(SharedPreferencesKeys.PACKAGES, new HashSet<String>());

        sharedPrefEditor.putString(SharedPreferencesKeys.SPOOF_DEVICE__BUILD_BOARD, Build.BOARD);
        sharedPrefEditor.putString(SharedPreferencesKeys.SPOOF_DEVICE__BUILD_ID, Build.ID);
        sharedPrefEditor.putString(SharedPreferencesKeys.SPOOF_DEVICE__BUILD_DISPLAY, Build.DISPLAY);
        sharedPrefEditor.putString(SharedPreferencesKeys.SPOOF_DEVICE__BUILD_PRODUCT, Build.PRODUCT);
        sharedPrefEditor.putString(SharedPreferencesKeys.SPOOF_DEVICE__BUILD_DEVICE, Build.ID);
        sharedPrefEditor.putString(SharedPreferencesKeys.SPOOF_DEVICE__CPU_ABI, Build.CPU_ABI);
        sharedPrefEditor.putString(SharedPreferencesKeys.SPOOF_DEVICE__CPU_ABI2, Build.CPU_ABI2);
        sharedPrefEditor.putString(SharedPreferencesKeys.SPOOF_DEVICE__BUILD_MANUFACTURER, Build.MANUFACTURER);
        sharedPrefEditor.putString(SharedPreferencesKeys.SPOOF_DEVICE__BUILD_BRAND, Build.BRAND);
        sharedPrefEditor.putString(SharedPreferencesKeys.SPOOF_DEVICE__BUILD_MODEL, Build.MODEL);
        sharedPrefEditor.putString(SharedPreferencesKeys.SPOOF_DEVICE__BUILD_BOOTLOADER, Build.BOOTLOADER);
        sharedPrefEditor.putString(SharedPreferencesKeys.SPOOF_DEVICE__BUILD_RADIO, Build.getRadioVersion());
        sharedPrefEditor.putString(SharedPreferencesKeys.SPOOF_DEVICE__BUILD_HARDWARE, Build.HARDWARE);
        sharedPrefEditor.putString(SharedPreferencesKeys.SPOOF_DEVICE__BUILD_SERIAL, Build.SERIAL);


        StringBuilder supportedAbis32bit = new StringBuilder();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            if (Build.SUPPORTED_32_BIT_ABIS.length > 0) {
                for (String abi : Build.SUPPORTED_32_BIT_ABIS) {
                    supportedAbis32bit.append(abi).append(",");
                }
                supportedAbis32bit.deleteCharAt(supportedAbis32bit.length() - 1);
            }
        }

        StringBuilder supportedAbis64bit = new StringBuilder();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            if (Build.SUPPORTED_64_BIT_ABIS.length > 0) {
                for (String abi : Build.SUPPORTED_64_BIT_ABIS) {
                    supportedAbis64bit.append(abi).append(",");
                }
                supportedAbis64bit.deleteCharAt(supportedAbis64bit.length() - 1);
            }
        }

        String supportedAbis;
        if (supportedAbis64bit.toString().equals("") && supportedAbis32bit.toString().equals("")) {
            supportedAbis = "";
        } else if (supportedAbis64bit.toString().equals("") && !supportedAbis32bit.toString().equals("")) {
            supportedAbis = new String(supportedAbis32bit);
        } else if (!supportedAbis64bit.toString().equals("") && supportedAbis32bit.toString().equals("")) {
            supportedAbis = new String(supportedAbis64bit);
        } else if (!supportedAbis64bit.toString().equals("") && !supportedAbis32bit.toString().equals("")) {
            supportedAbis = String.valueOf(supportedAbis64bit) + "," + supportedAbis32bit;
        } else {
            supportedAbis = null;
        }


        sharedPrefEditor.putString(SharedPreferencesKeys.SPOOF_DEVICE__BUILD_SUPPORTED_ABIS_32_BIT, supportedAbis32bit.toString());
        sharedPrefEditor.putString(SharedPreferencesKeys.SPOOF_DEVICE__BUILD_SUPPORTED_ABIS_64_BIT, supportedAbis64bit.toString());
        sharedPrefEditor.putString(SharedPreferencesKeys.SPOOF_DEVICE__BUILD_SUPPORTED_ABIS, supportedAbis);

        sharedPrefEditor.putString(SharedPreferencesKeys.SPOOF_DEVICE__BUILD_TYPE, Build.TYPE);
        sharedPrefEditor.putString(SharedPreferencesKeys.SPOOF_DEVICE__BUILD_TAGS, Build.TAGS);
        sharedPrefEditor.putString(SharedPreferencesKeys.SPOOF_DEVICE__BUILD_FINGERPRINT, Build.FINGERPRINT);

        sharedPrefEditor.putString(SharedPreferencesKeys.SPOOF_DEVICE__VERSION_INCREMENTAL, Build.VERSION.INCREMENTAL);
        sharedPrefEditor.putString(SharedPreferencesKeys.SPOOF_DEVICE__VERSION_RELEASE, Build.VERSION.RELEASE);
        sharedPrefEditor.putString(SharedPreferencesKeys.SPOOF_DEVICE__VERSION_SDK, Build.VERSION.SDK);
        sharedPrefEditor.putString(SharedPreferencesKeys.SPOOF_DEVICE__VERSION_SDK_INT, String.valueOf(Build.VERSION.SDK_INT));
        sharedPrefEditor.putString(SharedPreferencesKeys.SPOOF_DEVICE__VERSION_CODENAME, Build.VERSION.CODENAME);

        sharedPrefEditor.apply();
    }

    public static class MainActivity_MainPreferenceFragement extends PreferenceFragment {
        @Override
        public void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            addPreferencesFromResource(R.xml.preferences_main);

            findPreference("deviceSelectPref").setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
                @Override
                public boolean onPreferenceClick(Preference preference) {
                    getActivity().startActivity(new Intent(getActivity(), DeviceSelectActivity.class));
                    return true;
                }
            });


            findPreference("appSelectPref").setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
                @Override
                public boolean onPreferenceClick(Preference preference) {
                    getActivity().startActivity(new Intent(getActivity(), AppSelectActivity.class));
                    return true;
                }
            });
        }
    }
}
