package nz.pbomb.xposed.spoofmydevice.utils;


import android.os.Bundle;

import com.google.gson.annotations.SerializedName;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Arrays;

public class SpoofDevice {

    @SerializedName("humanDeviceName")
    private String humanDeviceName;

    @SerializedName("VERSION")
    public VERSION VERSION;

    @SerializedName("Build")
    public Build Build;

    public SpoofDevice() {
    }

    public SpoofDevice(String humanDeviceName, String BOARD, String INCREMENTAL, String RELEASE, String SDK, int SDK_INT, String CODENAME, String ID, String DISPLAY, String PRODUCT, String DEVICE, String CPU_ABI, String CPU_ABI2, String MANUFACTURER, String BRAND, String MODEL, String BOOTLOADER, String RADIO, String HARDWARE, String SERIAL, String[] SUPPORTED_ABIS, String[] SUPPORTED_32_BIT_ABIS, String[] SUPPORTED_64_BIT_ABIS, String TYPE, String TAGS, String FINGERPRINT) {
        this.Build = new Build(BOARD,ID,DISPLAY,PRODUCT,DEVICE,CPU_ABI,CPU_ABI2,MANUFACTURER,BRAND,MODEL,BOOTLOADER,RADIO,HARDWARE,SERIAL,SUPPORTED_ABIS,SUPPORTED_32_BIT_ABIS,SUPPORTED_64_BIT_ABIS,TYPE,TAGS,FINGERPRINT);
        this.VERSION = new VERSION(CODENAME,INCREMENTAL,RELEASE,SDK,SDK_INT);
        this.humanDeviceName = humanDeviceName;
    }

    public SpoofDevice(JSONObject jsonObject) {
        try {
            JSONObject sp_json = jsonObject.getJSONObject("Build");
            JSONObject sp_ver_json = jsonObject.getJSONObject("VERSION");
            String sp_humanDeviceName = jsonObject.getString("humanDeviceName");

            this.Build = new Build(sp_json.getString("BOARD"),
                    sp_json.getString("ID"),
                    sp_json.getString("DISPLAY"),
                    sp_json.getString("PRODUCT"),
                    sp_json.getString("DEVICE"),
                    sp_json.getString("CPU_ABI"),
                    sp_json.getString("CPU_ABI2"),
                    sp_json.getString("MANUFACTURER"),
                    sp_json.getString("BRAND"),
                    sp_json.getString("MODEL"),
                    sp_json.getString("BOOTLOADER"),
                    sp_json.getString("RADIO"),
                    sp_json.getString("HARDWARE"),
                    sp_json.getString("SERIAL"),
                    sp_json.getJSONArray("SUPPORTED_ABIS").join(",").split(","),
                    sp_json.getJSONArray("SUPPORTED_32_BIT_ABIS").join(",").split(","),
                    sp_json.getJSONArray("SUPPORTED_64_BIT_ABIS").join(",").split(","),
                    sp_json.getString("TYPE"),
                    sp_json.getString("TAGS"),
                    sp_json.getString("FINGERPRINT"));

            this.VERSION = new VERSION(sp_ver_json.getString("CODENAME"),sp_ver_json.getString("INCREMENTAL"),
                    sp_ver_json.getString("RELEASE"),sp_ver_json.getString("SDK"),sp_ver_json.getInt("SDK_INT"));

            this.humanDeviceName = sp_humanDeviceName;
        } catch (JSONException e) {
           throw new RuntimeException(e);
        }
    }

    public String getHumanDeviceName() {
        return humanDeviceName;
    }


    /*
        Static Inner Classes
     */

    public static class VERSION {
        @SerializedName("INCREMENTAL")
        public final String INCREMENTAL;

        @SerializedName("RELEASE")
        public final String RELEASE;

        @SerializedName("SDK")
        public final String SDK;

        @SerializedName("SDK_INT")
        public final int SDK_INT;

        @SerializedName("CODENAME")
        public final String CODENAME;

        public VERSION(String CODENAME, String INCREMENTAL, String RELEASE, String SDK, int SDK_INT) {
            this.CODENAME = CODENAME;
            this.INCREMENTAL = INCREMENTAL;
            this.RELEASE = RELEASE;
            this.SDK = SDK;
            this.SDK_INT = SDK_INT;
        }

        @Override
        public String toString() {
            return "VERSION{" +
                    "CODENAME='" + CODENAME + '\'' +
                    ", INCREMENTAL='" + INCREMENTAL + '\'' +
                    ", RELEASE='" + RELEASE + '\'' +
                    ", SDK='" + SDK + '\'' +
                    ", SDK_INT=" + SDK_INT +
                    '}';
        }
    }

    public static class Build {
        @SerializedName("ID")
        public final String ID;

        @SerializedName("DISPLAY")
        public final String DISPLAY;

        @SerializedName("PRODUCT")
        public final String PRODUCT;

        @SerializedName("Build")
        public final String DEVICE;

        @SerializedName("Build")
        public final String BOARD;

        @SerializedName("CPU_ABI")
        public final String CPU_ABI;

        @SerializedName("CPU_ABI2")
        public final String CPU_ABI2;


        @SerializedName("MANUFACTURER")
        public final String MANUFACTURER;

        @SerializedName("BRAND")
        public final String BRAND;

        @SerializedName("MODEL")
        public final String MODEL;

        @SerializedName("BOOTLOADER")
        public final String BOOTLOADER;

        @SerializedName("RADIO")
        public final String RADIO;

        @SerializedName("HARDWARE")
        public final String HARDWARE;

        @SerializedName("SERIAL")
        public final String SERIAL;

        @SerializedName("SUPPORTED_ABIS")
        public final String[] SUPPORTED_ABIS;

        @SerializedName("SUPPORTED_32_BIT_ABIS")
        public final String[] SUPPORTED_32_BIT_ABIS;

        @SerializedName("SUPPORTED_64_BIT_ABIS")
        public final String[] SUPPORTED_64_BIT_ABIS;


        @SerializedName("TYPE")
        public final String TYPE;

        @SerializedName("TAGS")
        public final String TAGS;

        @SerializedName("FINGERPRINT")
        public final String FINGERPRINT;

        public Build(String BOARD, String ID, String DISPLAY, String PRODUCT, String DEVICE, String CPU_ABI, String CPU_ABI2, String MANUFACTURER, String BRAND, String MODEL, String BOOTLOADER, String RADIO, String HARDWARE, String SERIAL, String[] SUPPORTED_ABIS, String[] SUPPORTED_32_BIT_ABIS, String[] SUPPORTED_64_BIT_ABIS, String TYPE, String TAGS, String FINGERPRINT) {
            this.BOARD = BOARD;
            this.ID = ID;
            this.DISPLAY = DISPLAY;
            this.PRODUCT = PRODUCT;
            this.DEVICE = DEVICE;
            this.CPU_ABI = CPU_ABI;
            this.CPU_ABI2 = CPU_ABI2;
            this.MANUFACTURER = MANUFACTURER;
            this.BRAND = BRAND;
            this.MODEL = MODEL;
            this.BOOTLOADER = BOOTLOADER;
            this.RADIO = RADIO;
            this.HARDWARE = HARDWARE;
            this.SERIAL = SERIAL;
            this.SUPPORTED_ABIS = SUPPORTED_ABIS;
            this.SUPPORTED_32_BIT_ABIS = SUPPORTED_32_BIT_ABIS;
            this.SUPPORTED_64_BIT_ABIS = SUPPORTED_64_BIT_ABIS;
            this.TYPE = TYPE;
            this.TAGS = TAGS;
            this.FINGERPRINT = FINGERPRINT;
        }

        @Override
        public String toString() {
            return "Build{" +
                    "BOARD='" + BOARD + '\'' +
                    ", ID='" + ID + '\'' +
                    ", DISPLAY='" + DISPLAY + '\'' +
                    ", PRODUCT='" + PRODUCT + '\'' +
                    ", DEVICE='" + DEVICE + '\'' +
                    ", CPU_ABI='" + CPU_ABI + '\'' +
                    ", CPU_ABI2='" + CPU_ABI2 + '\'' +
                    ", MANUFACTURER='" + MANUFACTURER + '\'' +
                    ", BRAND='" + BRAND + '\'' +
                    ", MODEL='" + MODEL + '\'' +
                    ", BOOTLOADER='" + BOOTLOADER + '\'' +
                    ", RADIO='" + RADIO + '\'' +
                    ", HARDWARE='" + HARDWARE + '\'' +
                    ", SERIAL='" + SERIAL + '\'' +
                    ", SUPPORTED_ABIS=" + Arrays.toString(SUPPORTED_ABIS) +
                    ", SUPPORTED_32_BIT_ABIS=" + Arrays.toString(SUPPORTED_32_BIT_ABIS) +
                    ", SUPPORTED_64_BIT_ABIS=" + Arrays.toString(SUPPORTED_64_BIT_ABIS) +
                    ", TYPE='" + TYPE + '\'' +
                    ", TAGS='" + TAGS + '\'' +
                    ", FINGERPRINT='" + FINGERPRINT + '\'' +
                    '}';
        }
    }

    @Override
    public String toString() {
        return "SpoofDevice{" +
                "Build=" + Build +
                ", humanDeviceName='" + humanDeviceName + '\'' +
                ", VERSION=" + VERSION +
                '}';
    }
}
