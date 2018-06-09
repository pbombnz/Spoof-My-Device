package nz.pbomb.xposed.spoofmydevice;

import android.content.SharedPreferences;
import android.os.Build;

import de.robv.android.xposed.XC_MethodHook;
import de.robv.android.xposed.callbacks.XC_LoadPackage;
import nz.pbomb.xposed.spoofmydevice.preferences.SharedPreferencesKeys;


public class SystemPropertiesHook extends XC_MethodHook {
    private XC_LoadPackage.LoadPackageParam llparam;
    private SharedPreferences sharedPreferences;

    public SystemPropertiesHook(XC_LoadPackage.LoadPackageParam llparam, SharedPreferences sharedPreferences) {
        this.llparam = llparam;
        this.sharedPreferences = sharedPreferences;
    }

    @Override
    protected void afterHookedMethod(MethodHookParam param) throws Throwable {
        String str = (String) param.args[0];
        String str2 = param.args.length == 2 ? (String) param.args[1] : Build.UNKNOWN;

        switch (str) {
            case "ro.build.id":
                param.setResult(sharedPreferences.getString(SharedPreferencesKeys.SPOOF_DEVICE__BUILD_ID, str2));
                break;
            case "ro.build.display.id":
                param.setResult(sharedPreferences.getString(SharedPreferencesKeys.SPOOF_DEVICE__BUILD_DISPLAY, str2));
                break;
            case "ro.product.name":
                param.setResult(sharedPreferences.getString(SharedPreferencesKeys.SPOOF_DEVICE__BUILD_DEVICE, str2));
                break;
            case "ro.product.device":
                param.setResult(sharedPreferences.getString(SharedPreferencesKeys.SPOOF_DEVICE__BUILD_DEVICE, str2));
                break;
            case "ro.product.board":
                param.setResult(sharedPreferences.getString(SharedPreferencesKeys.SPOOF_DEVICE__BUILD_BOARD, str2));
                break;
            case "ro.product.cpu.abi":
                param.setResult(sharedPreferences.getString(SharedPreferencesKeys.SPOOF_DEVICE__CPU_ABI, str2));
                break;
            case "ro.product.cpu.abi2":
                param.setResult(sharedPreferences.getString(SharedPreferencesKeys.SPOOF_DEVICE__CPU_ABI2, str2));
                break;
            case "ro.product.manufacturer":
                param.setResult(sharedPreferences.getString(SharedPreferencesKeys.SPOOF_DEVICE__BUILD_MANUFACTURER, str2));
                break;
            case "ro.product.brand":
                param.setResult(sharedPreferences.getString(SharedPreferencesKeys.SPOOF_DEVICE__BUILD_BRAND, str2));
                break;
            case "ro.product.model":
                param.setResult(sharedPreferences.getString(SharedPreferencesKeys.SPOOF_DEVICE__BUILD_MODEL, str2));
                break;
            case "ro.boot.bootloader":
                param.setResult(sharedPreferences.getString(SharedPreferencesKeys.SPOOF_DEVICE__BUILD_BOOTLOADER, str2));
                break;
            case "ro.bootloader":
                param.setResult(sharedPreferences.getString(SharedPreferencesKeys.SPOOF_DEVICE__BUILD_BOOTLOADER, str2));
                break;
            case "ro.boot.hardware":
                param.setResult(sharedPreferences.getString(SharedPreferencesKeys.SPOOF_DEVICE__BUILD_HARDWARE, str2));
                break;
            case "ro.hardware":
                param.setResult(sharedPreferences.getString(SharedPreferencesKeys.SPOOF_DEVICE__BUILD_HARDWARE, str2));
                break;
            case "ro.boot.serial":
                param.setResult(sharedPreferences.getString(SharedPreferencesKeys.SPOOF_DEVICE__BUILD_SERIAL, str2));
                break;
            case "ro.serial":
                param.setResult(sharedPreferences.getString(SharedPreferencesKeys.SPOOF_DEVICE__BUILD_SERIAL, str2));
                break;
            case "ro.product.cpu.abilist":
                param.setResult(sharedPreferences.getString(SharedPreferencesKeys.SPOOF_DEVICE__BUILD_SUPPORTED_ABIS, str2));
                break;
            case "ro.product.cpu.abilist32":
                param.setResult(sharedPreferences.getString(SharedPreferencesKeys.SPOOF_DEVICE__BUILD_SUPPORTED_ABIS_32_BIT, str2));
                break;
            case "ro.product.cpu.abilist64":
                param.setResult(sharedPreferences.getString(SharedPreferencesKeys.SPOOF_DEVICE__BUILD_SUPPORTED_ABIS_64_BIT, str2));
                break;
            case "ro.build.tags":
                param.setResult(sharedPreferences.getString(SharedPreferencesKeys.SPOOF_DEVICE__BUILD_TAGS, str2));
                break;
            case "ro.build.type":
                param.setResult(sharedPreferences.getString(SharedPreferencesKeys.SPOOF_DEVICE__BUILD_TYPE, str2));
                break;
            case "ro.build.fingerprint":
                param.setResult(sharedPreferences.getString(SharedPreferencesKeys.SPOOF_DEVICE__BUILD_FINGERPRINT, str2));
                break;
            case "ro.vendor.build.fingerprint":
                param.setResult(sharedPreferences.getString(SharedPreferencesKeys.SPOOF_DEVICE__BUILD_FINGERPRINT, str2));
                break;
            case "ro.build.version.incremental":
                param.setResult(sharedPreferences.getString(SharedPreferencesKeys.SPOOF_DEVICE__VERSION_INCREMENTAL, str2));
                break;
            case "ro.build.version.release":
                param.setResult(sharedPreferences.getString(SharedPreferencesKeys.SPOOF_DEVICE__VERSION_RELEASE, str2));
                break;
            case "ro.build.version.sdk":
                param.setResult(sharedPreferences.getString(SharedPreferencesKeys.SPOOF_DEVICE__VERSION_SDK_INT, str2));
                break;
            case "ro.build.version.codename":
                param.setResult(sharedPreferences.getString(SharedPreferencesKeys.SPOOF_DEVICE__VERSION_CODENAME, str2));
                break;
            case "ro.build.version.all_codenames":
                param.setResult(sharedPreferences.getString(SharedPreferencesKeys.SPOOF_DEVICE__VERSION_CODENAME, str2));
                break;
            default:
                break;
        }
    }
}
