package nz.pbomb.xposed.spoofmydevice.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;

import java.util.List;

import nz.pbomb.xposed.spoofmydevice.models.ApplicationData;

public class RetainedFragment extends Fragment {
    private List<ApplicationData> spoofedApplications;
    private List<ApplicationData> nonSpoofedApplications;

    public static RetainedFragment newInstance() {

        Bundle args = new Bundle();

        RetainedFragment fragment = new RetainedFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);
    }

    public List<ApplicationData> getSpoofedApplications() {
        return spoofedApplications;
    }

    public void setSpoofedApplications(List<ApplicationData> spoofedApplications) {
        this.spoofedApplications = spoofedApplications;
    }

    public List<ApplicationData> getNonSpoofedApplications() {
        return nonSpoofedApplications;
    }

    public void setNonSpoofedApplications(List<ApplicationData> nonSpoofedApplications) {
        this.nonSpoofedApplications = nonSpoofedApplications;
    }
}
