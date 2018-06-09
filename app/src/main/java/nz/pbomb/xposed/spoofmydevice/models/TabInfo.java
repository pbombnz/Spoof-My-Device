package nz.pbomb.xposed.spoofmydevice.models;

import android.os.Bundle;

import java.util.HashMap;
import java.util.Map;

public class TabInfo {
    private final Class clazz;
    private final Bundle args;
    private final String tabName;
    private final Map<String, Object> extras;

    public TabInfo(Class clazz, Bundle args, String tabName) {
        this.clazz = clazz;
        this.args = args;
        this.tabName = tabName;
        this.extras = new HashMap<>();
    }


    public TabInfo(Class clazz, Bundle args, String tabName, String[] keys, Object... vals) {
        this.clazz = clazz;
        this.args = args;
        this.tabName = tabName;
        this.extras = new HashMap<>();

        if(keys.length != vals.length) {
            throw new IllegalArgumentException("The number of keys must be equal to the number of values provided.");
        }

        for(int i = 0; i < keys.length; i++) {
            String key = keys[i];
            Object val = vals[i];

            extras.put(key,val);
        }
    }

    public Class getClassName() {
        return clazz;
    }

    public Bundle getArgs() {
        return args;
    }

    public String getTabName() {
        return tabName;
    }

    public Map<String, Object> getExtras() {
        return extras;
    }
}
