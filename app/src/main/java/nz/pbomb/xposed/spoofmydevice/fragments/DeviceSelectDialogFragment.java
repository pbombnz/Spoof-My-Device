package nz.pbomb.xposed.spoofmydevice.fragments;

import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.ButterKnife;
import nz.pbomb.xposed.spoofmydevice.R;


public class DeviceSelectDialogFragment extends DialogFragment {
    @BindView(R.id.spoofDevices_RecyclerView)
    protected RecyclerView recyclerView;

    @BindView(R.id.ndf_textView)
    protected TextView noDeviceFoundTextView;

    @BindView(R.id.title_textView)
    protected TextView titleTextView;

    @BindView(R.id.ok_button_textView)
    protected TextView okayButtonTextView;

    @BindView(R.id.refresh_button_textView)
    protected TextView refreshButtonTextView;


    public static DeviceSelectDialogFragment newInstance() {
        DeviceSelectDialogFragment frag = new DeviceSelectDialogFragment();
        Bundle args = new Bundle();
        frag.setArguments(args);
        return frag;
    }

    public static void show(AppCompatActivity activity, DeviceSelectDialogFragment deviceSelectDialogFragment) {
        FragmentManager fragmentManager = activity.getSupportFragmentManager();
        deviceSelectDialogFragment.show(fragmentManager, "deviceSelectDialog");
        fragmentManager.executePendingTransactions();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_spoofdevice, container);
        ButterKnife.bind(this, view);

        String title = getArguments().getString("title");
        if(title != null) {
            titleTextView.setText(title);
        }

        return view;
    }

    public RecyclerView getRecyclerView() {
        return recyclerView;
    }

    public TextView getOkTextView() {
        return okayButtonTextView;
    }

    public TextView getRefreshButtonTextView() {
        return refreshButtonTextView;
    }

    public TextView getNoDeviceFoundTextView() {
        return noDeviceFoundTextView;
    }
}
