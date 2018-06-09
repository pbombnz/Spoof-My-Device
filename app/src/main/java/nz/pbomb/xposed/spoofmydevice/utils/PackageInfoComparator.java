package nz.pbomb.xposed.spoofmydevice.utils;

import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;

import java.util.Comparator;


public class PackageInfoComparator implements Comparator<PackageInfo> {
    private PackageManager pm;

    public PackageInfoComparator(PackageManager pm) {
        this.pm = pm;
    }

    @Override
    public int compare(PackageInfo p1, PackageInfo p2) {
        String p1AppName = p1.applicationInfo.loadLabel(pm).toString();
        String p2AppName = p2.applicationInfo.loadLabel(pm).toString();
        return p1AppName.compareTo(p2AppName);
    }
}
