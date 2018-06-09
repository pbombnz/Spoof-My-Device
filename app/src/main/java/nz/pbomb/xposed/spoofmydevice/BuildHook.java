package nz.pbomb.xposed.spoofmydevice;

import android.content.SharedPreferences;
import android.os.Build;

import de.robv.android.xposed.XC_MethodHook;
import de.robv.android.xposed.XposedHelpers;
import de.robv.android.xposed.callbacks.XC_LoadPackage;
import nz.pbomb.xposed.spoofmydevice.preferences.SharedPreferencesKeys;


public class BuildHook  {
    //XC_LoadPackage.LoadPackageParam lpparam;
    //SharedPreferences sharedPreferences;

    public BuildHook(XC_LoadPackage.LoadPackageParam lpparam, SharedPreferences sharedPreferences) {
        //this.lpparam = lpparam;
        //this.sharedPreferences = sharedPreferences;

        Class findClass = XposedHelpers.findClass("android.os.Build", lpparam.classLoader);
        XposedHelpers.setStaticObjectField(findClass, "ID", sharedPreferences.getString(SharedPreferencesKeys.SPOOF_DEVICE__BUILD_ID, Build.UNKNOWN));
        XposedHelpers.setStaticObjectField(findClass, "DISPLAY", sharedPreferences.getString(SharedPreferencesKeys.SPOOF_DEVICE__BUILD_FINGERPRINT, Build.UNKNOWN));
        XposedHelpers.setStaticObjectField(findClass, "PRODUCT", sharedPreferences.getString(SharedPreferencesKeys.SPOOF_DEVICE__BUILD_PRODUCT, Build.UNKNOWN));
        XposedHelpers.setStaticObjectField(findClass, "BOARD", sharedPreferences.getString(SharedPreferencesKeys.SPOOF_DEVICE__BUILD_BOARD, Build.UNKNOWN));
        XposedHelpers.setStaticObjectField(findClass, "DEVICE", sharedPreferences.getString(SharedPreferencesKeys.SPOOF_DEVICE__BUILD_DEVICE, Build.UNKNOWN));
        XposedHelpers.setStaticObjectField(findClass, "CPU_ABI", sharedPreferences.getString(SharedPreferencesKeys.SPOOF_DEVICE__CPU_ABI, Build.UNKNOWN));
        XposedHelpers.setStaticObjectField(findClass, "CPU_ABI2", sharedPreferences.getString(SharedPreferencesKeys.SPOOF_DEVICE__CPU_ABI2, Build.UNKNOWN));
        XposedHelpers.setStaticObjectField(findClass, "MANUFACTURER", sharedPreferences.getString(SharedPreferencesKeys.SPOOF_DEVICE__BUILD_MANUFACTURER, Build.UNKNOWN));
        XposedHelpers.setStaticObjectField(findClass, "BRAND", sharedPreferences.getString(SharedPreferencesKeys.SPOOF_DEVICE__BUILD_BRAND, Build.UNKNOWN));
        //XposedHelpers.setStaticObjectField(findClass, "MANUFACTURER", sharedPreferences.getString(SharedPreferencesKeys.SPOOF_DEVICE__BUILD_MANUFACTURER, Build.UNKNOWN));
        XposedHelpers.setStaticObjectField(findClass, "MODEL", sharedPreferences.getString(SharedPreferencesKeys.SPOOF_DEVICE__BUILD_MODEL, Build.UNKNOWN));
        XposedHelpers.setStaticObjectField(findClass, "BOOTLOADER", sharedPreferences.getString(SharedPreferencesKeys.SPOOF_DEVICE__BUILD_BOOTLOADER, Build.UNKNOWN));
        XposedHelpers.setStaticObjectField(findClass, "RADIO", sharedPreferences.getString(SharedPreferencesKeys.SPOOF_DEVICE__BUILD_RADIO, Build.UNKNOWN));
        final SharedPreferences innerSharedPreferences = sharedPreferences;
        XposedHelpers.findAndHookMethod(findClass, "getRadioVersion", new XC_MethodHook() {
            @Override
            protected void afterHookedMethod(MethodHookParam param) throws Throwable {
                param.setResult(innerSharedPreferences.getString(SharedPreferencesKeys.SPOOF_DEVICE__BUILD_RADIO, Build.UNKNOWN));
            }
        });

        XposedHelpers.setStaticObjectField(findClass, "HARDWARE",  sharedPreferences.getString(SharedPreferencesKeys.SPOOF_DEVICE__BUILD_HARDWARE, Build.UNKNOWN));
        XposedHelpers.setStaticObjectField(findClass, "SERIAL",  sharedPreferences.getString(SharedPreferencesKeys.SPOOF_DEVICE__BUILD_SERIAL, Build.UNKNOWN));

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            XposedHelpers.setStaticObjectField(findClass, "SUPPORTED_ABIS", sharedPreferences.getString(SharedPreferencesKeys.SPOOF_DEVICE__BUILD_SUPPORTED_ABIS, Build.UNKNOWN).split(","));
            XposedHelpers.setStaticObjectField(findClass, "SUPPORTED_32_BIT_ABIS", sharedPreferences.getString(SharedPreferencesKeys.SPOOF_DEVICE__BUILD_SUPPORTED_ABIS_32_BIT, Build.UNKNOWN).split(","));
            XposedHelpers.setStaticObjectField(findClass, "SUPPORTED_64_BIT_ABIS", sharedPreferences.getString(SharedPreferencesKeys.SPOOF_DEVICE__BUILD_SUPPORTED_ABIS_64_BIT, Build.UNKNOWN).split(","));
        }
        XposedHelpers.setStaticObjectField(findClass, "TAGS",  sharedPreferences.getString(SharedPreferencesKeys.SPOOF_DEVICE__BUILD_TAGS, Build.UNKNOWN));
        XposedHelpers.setStaticObjectField(findClass, "TYPE",  sharedPreferences.getString(SharedPreferencesKeys.SPOOF_DEVICE__BUILD_TYPE, Build.UNKNOWN));
        XposedHelpers.setStaticObjectField(findClass, "FINGERPRINT", sharedPreferences.getString(SharedPreferencesKeys.SPOOF_DEVICE__BUILD_FINGERPRINT, Build.UNKNOWN));

        // Host -> XposedHelpers.setStaticObjectField(findClass, "HOST", d.a("user", "unknown"));
        // USER -> XposedHelpers.setStaticObjectField(findClass, "USER", d.a("user", "unknown"));

        findClass = XposedHelpers.findClass("android.os.Build$VERSION", lpparam.classLoader);
        XposedHelpers.setStaticObjectField(findClass, "INCREMENTAL",  sharedPreferences.getString(SharedPreferencesKeys.SPOOF_DEVICE__VERSION_INCREMENTAL, Build.UNKNOWN));
        XposedHelpers.setStaticObjectField(findClass, "RELEASE", sharedPreferences.getString(SharedPreferencesKeys.SPOOF_DEVICE__VERSION_RELEASE, Build.UNKNOWN));
        XposedHelpers.setStaticObjectField(findClass, "SDK", sharedPreferences.getString(SharedPreferencesKeys.SPOOF_DEVICE__VERSION_SDK, Build.UNKNOWN));
        XposedHelpers.setStaticObjectField(findClass, "SDK_INT", Integer.parseInt(sharedPreferences.getString(SharedPreferencesKeys.SPOOF_DEVICE__VERSION_SDK_INT, Build.UNKNOWN)));
        XposedHelpers.setStaticObjectField(findClass, "CODENAME", sharedPreferences.getString(SharedPreferencesKeys.SPOOF_DEVICE__VERSION_CODENAME, Build.UNKNOWN));
    }
}
