package nz.pbomb.xposed.spoofmydevice.utils;

import android.os.Build;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class SpoofDevices {
    private static List<SpoofDevice> devices;

    /*public enum DEVICE {
        SAMSUNG_GALAXY_NOTE_3("samsung", "samsung", "SM-N9005");

        private String manufacturer;
        private String brand;
        private String model;

        DEVICE(String manufacturer, String brand, String model) {
            this.manufacturer = manufacturer;
            this.brand = brand;
            this.model = model;
        }

        public String getBrand() {
            return brand;
        }

        public String getManufacturer() {
            return manufacturer;
        }

        public String getModel() {
            return model;
        }
    }*/

    static {
        devices = new ArrayList<>();

        SpoofDevice samsung_galaxy_note_3 = new SpoofDevice(
                "Samsung Galaxy Note 3 (4.4.2)",
                "MSM8974",
                "N9005XXUGNG1",
                "4.4.2",
                "19",
                19,
                "REL",
                "KOT49H",
                "KOT49H.N9005XXUGNG1",
                "hltexx",
                "hlte",
                "armeabi-v7a",
                "armeabi",
                "samsung",
                "samsung",
                "SM-N9005",
                "N9005XXUGNG1",
                "N9005XXUGNG1",
                "qcom",
                Build.UNKNOWN,
                new String[] { "armeabi-v7a", "armeabi" },
                new String[] { "armeabi-v7a", "armeabi" },
                new String[0],
                "user",
                "release-keys",
                "samsung/hltexx/hlte:4.4.2/KOT49H/N9005XXUGNG1:user/release-keys");

        SpoofDevice samsung_galaxy_s7_edge = new SpoofDevice("Samsung Galaxy S7 Edge (6.0.1)",
                "universal8890",
                "G935FXXU1APD1",
                "6.0.1",
                "23",
                23,
                "REL",
                "MMB29K",
                "MMB29K.G935FXXU1APD1",
                "hero2ltexx",
                "hero2lte",
                "arm64-v8a",
                "armeabi-v7a",
                "samsung",
                "samsung",
                "SM-G935F",
                "G935FXXU1APD1",
                "G935FXXU1APD1",
                "qcom",
                Build.UNKNOWN,
                new String[] { "arm64-v8a","armeabi-v7a","armeabi" },
                new String[] { "armeabi-v7a", "armeabi" },
                new String[] { "arm64-v8a" },
                "user",
                "release-keys",
                "samsung/hero2ltexx/hero2lte:6.0.1/MMB29K/G935FXXU1APD1:user/release-keys");

        SpoofDevice huawei_p8 = new SpoofDevice("Huawei P8 (5.0)",
                "GRA-L09",
                "C900B162",
                "5.0",
                "21",
                21,
                "REL",
                "HUAWEIGRA-L09",
                "GRA-L09C900B162",
                "GRA-L09",
                "HWGRA",
                "arm64-v8a",
                "armeabi-v7a",
                "HUAWEI",
                "HUAWEI",
                "HUAWEI GRA-L09",
                "C900B162",
                "C900B162",
                "Hisilicon Kirin 935", //?
                Build.UNKNOWN,
                new String[] {"arm64-v8a","armeabi-v7a","armeabi" },
                new String[] {"arm64-v8a" },
                new String[] {"armeabi-v7a","armeabi" },
                "user",
                "release-keys",
                "HUAWEI/GRA-L09/HWGRA:5.0/HUAWEIGRA-L09/C900B162:user/release-keys");


        devices.add(samsung_galaxy_note_3);
        devices.add(samsung_galaxy_s7_edge);
        devices.add(huawei_p8);
    }

    /*public static SpoofDevice getDeviceInfo(DEVICE device) {
        return getDeviceInfo(device.getManufacturer(), device.getBrand(), device.getModel());
    }*/

    public static SpoofDevice getDeviceByManufacturerBrandModel(String manufacturer, String brand, String model) {
        for(SpoofDevice device: devices) {
            if(device.Build.MANUFACTURER.equals(manufacturer)
                || device.Build.BRAND.equals(brand)
                || device.Build.MODEL.equals(model)) {
                return device;
            }
        }
        return null;
    }

    public static SpoofDevice getDeviceInfoByHumanDeviceName(String humanDeviceName) {
        for(SpoofDevice device: devices) {
            if(device.getHumanDeviceName().equals(humanDeviceName)) {
                return device;
            }
        }
        return null;
    }

    public static List<SpoofDevice> getDevices() {
        return Collections.unmodifiableList(devices);
    }

    public static String covertSpoofDeviceToJson(SpoofDevice sp) {
        final Gson gson = new GsonBuilder().setPrettyPrinting().create();
        return gson.toJson(sp);
    }
}
