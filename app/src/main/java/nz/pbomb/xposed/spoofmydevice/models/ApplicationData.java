package nz.pbomb.xposed.spoofmydevice.models;

import android.graphics.drawable.Drawable;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.SortedSet;

public class ApplicationData {
    private final String appName;
    private final String appPackage;
    private final String appVersion;
    private final Drawable appIconDrawable;

    public ApplicationData(String appName, String appPackage, String appVersion, Drawable appIconDrawable) {
        this.appName = appName;
        this.appPackage = appPackage;
        this.appVersion = appVersion;
        this.appIconDrawable = appIconDrawable;
    }

    public String getAppName() {
        return appName;
    }

    public String getAppPackage() {
        return appPackage;
    }

    public String getAppVersion() {
        return appVersion;
    }

    public Drawable appIconDrawable() {
        return appIconDrawable;
    }

    public static List<String> getSortedPackages(Collection<ApplicationData> applicationDatas) {
        List<String> packages = new ArrayList<>();

        for(ApplicationData applicationData : applicationDatas) {
            packages.add(applicationData.getAppPackage());
        }

        Collections.sort(packages);

        return packages;
    }

    @Override
    public String toString() {
        return "ApplicationData{" +
                "appIconDrawable=" + appIconDrawable +
                ", appName='" + appName + '\'' +
                ", appPackage='" + appPackage + '\'' +
                ", appVersion='" + appVersion + '\'' +
                '}';
    }

    /**
     * Created to sort a data array of ApplicationData objects.
     */
    public static class Comparator implements java.util.Comparator<ApplicationData> {
        @Override
        public int compare(ApplicationData ad1, ApplicationData ad2) {
            return ad1.getAppName().compareTo(ad2.getAppName());
        }
    }
}
