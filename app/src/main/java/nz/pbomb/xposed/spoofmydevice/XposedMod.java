package nz.pbomb.xposed.spoofmydevice;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;

import com.crossbowffs.remotepreferences.RemotePreferences;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import de.robv.android.xposed.IXposedHookLoadPackage;
import de.robv.android.xposed.IXposedHookZygoteInit;
import de.robv.android.xposed.XC_MethodHook;
import de.robv.android.xposed.XSharedPreferences;
import de.robv.android.xposed.XposedBridge;
import de.robv.android.xposed.XposedHelpers;
import de.robv.android.xposed.callbacks.XC_LoadPackage;
import nz.pbomb.xposed.spoofmydevice.preferences.SharedPreferencesKeys;

public class XposedMod implements IXposedHookZygoteInit, IXposedHookLoadPackage {
    private static final String TAG = Common.TAG;
    private static final String APP_PACKAGE = "nz.pbomb.xposed.spoofmydevice";

    public XposedMod mThis = null;
    public XSharedPreferences mXSharedPreferences = null;
    public SharedPreferences mSharedPreferences = null;
    public Context mAppContext = null;

    public XposedMod() {
        mThis = this;

    }



    /**
     * Displays a message in Xposed Logs and logcat if and only if Debug Mode is enabled.
     *
     * @param message The message to be displayed in logs
     */
    @SuppressWarnings("unused")
    public static void logDebug(String message) {
        if (BuildConfig.DEBUG) {
            log(message);
        }
    }

    public static void log(String message) {
        XposedBridge.log("[" + TAG + "] " + message);
    }

    @SuppressWarnings("unused")
    public void refreshSharedPreferences() {
        refreshSharedPreferences(BuildConfig.DEBUG);
    }

    /**
     * Reloads and refreshes the package's shared preferences file to reload new confirgurations
     * that may have changed on runtime.
     *
     * @param displayLogs To show logs or not.
     */
    public void refreshSharedPreferences(boolean displayLogs) {
        mXSharedPreferences = new XSharedPreferences(APP_PACKAGE, "SharedPreferences");
        mXSharedPreferences.makeWorldReadable();
        mXSharedPreferences.reload();

        // Only continue if we want to produce logging
        if(!displayLogs) {
            return;
        }

        // Logging the properties to see if the file is actually readable
        log("Shared Preferences Properties:");
        log("\tWorld Readable: " + mXSharedPreferences.makeWorldReadable());
        log("\tPath: " + mXSharedPreferences.getFile().getAbsolutePath());
        log("\tFile Readable: " + mXSharedPreferences.getFile().canRead());
        log("\tExists: " + mXSharedPreferences.getFile().exists());

        // Display the preferences loaded, only if the file was readable otherwise display an error
        if (mXSharedPreferences.getAll().size() == 0) {
            log("Shared Preferences does not have read permissions most likely due to" +
                    " SELinux enforcing. Loaded Shared Preferences Defaults Instead.");
        } else {
            log("");
            log("Loaded Shared Preferences:");
            Map<String, ?> prefsMap = mXSharedPreferences.getAll();
            for(String key: prefsMap.keySet()) {
                String val = prefsMap.get(key).toString();
                log("\t " + key + ": " + val);
            }
        }
    }


    @Override
    public void initZygote(IXposedHookZygoteInit.StartupParam startupParam) throws Throwable {
        XposedBridge.log("Module Loaded (Debug Mode: " + (BuildConfig.DEBUG ? "ON" : "OFF") + ")");
        refreshSharedPreferences(false);
    }


    @Override
    public void handleLoadPackage(final XC_LoadPackage.LoadPackageParam lpparam) throws Throwable {
        XposedBridge.log(lpparam.packageName);

        if(lpparam.packageName.equals(APP_PACKAGE)) {
            XposedHelpers.findAndHookMethod(APP_PACKAGE+".Common", lpparam.classLoader, "isModuleActive", new XC_MethodHook() {
                    @Override
                    protected void afterHookedMethod(MethodHookParam param) throws Throwable {
                        param.setResult(true);
                    }
                });

        }


        XposedHelpers.findAndHookMethod("android.app.Application", lpparam.classLoader, "onCreate", new XC_MethodHook() {
            @Override
            protected void afterHookedMethod(MethodHookParam param) throws Throwable {
                mAppContext = (Application) param.thisObject;
                mSharedPreferences = new RemotePreferences(mAppContext, "nz.pbomb.xposed.spoofmydevice.provider.preferences", "SharedPreferences");
                //log(lpparam.packageName + " inside:" + mSharedPreferences.getAll().toString());

                Set<String> packageListImmutable = mSharedPreferences.getStringSet(SharedPreferencesKeys.PACKAGES, new HashSet<String>());
                Set<String> packageList = new HashSet<>(packageListImmutable);

                if (!packageList.contains(lpparam.packageName)) {
                    return;
                }

                new BuildHook(lpparam, mSharedPreferences);

                try {
                    Class cls = Class.forName("android.os.SystemProperties");
                    XposedHelpers.findAndHookMethod(cls, "get",String.class, new SystemPropertiesHook(lpparam, mSharedPreferences));
                    XposedHelpers.findAndHookMethod(cls, "get", String.class, String.class, new SystemPropertiesHook(lpparam, mSharedPreferences));
                } catch (ClassNotFoundException e) {
                    e.printStackTrace();
                }
            }
        });
    }
}
