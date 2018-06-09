package nz.pbomb.xposed.spoofmydevice.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.firebase.crash.FirebaseCrash;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import nz.pbomb.xposed.spoofmydevice.Common;
import nz.pbomb.xposed.spoofmydevice.R;
import nz.pbomb.xposed.spoofmydevice.utils.SpoofDevice;


public class DeviceSelectFragment_RecyclerViewAdapter extends RecyclerView.Adapter<DeviceSelectFragment_RecyclerViewAdapter.ViewHolder> {

    private RecyclerViewOnClickListener listener;


    static class ViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.device_name_textView) TextView humanReadableDeviceNameTextView;

        ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

    private List<SpoofDevice> spoofDevices;

    public DeviceSelectFragment_RecyclerViewAdapter(List<SpoofDevice> spoofDevices) {
        this.spoofDevices = spoofDevices;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);

        View cardView = inflater.inflate(R.layout.fragment_spoofdevice_card_view, parent, false);
        return new ViewHolder(cardView);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {
        //FirebaseCrash.logcat(Log.DEBUG, Common.TAG, "onBindViewHolder(...): spoofDevices.size(): "+spoofDevices.size()+" position: "+position);

        SpoofDevice spoofDevice = spoofDevices.get(position);

        //Log.i(Common.TAG, spoofDevice.toString());

        holder.humanReadableDeviceNameTextView.setText(spoofDevice.getHumanDeviceName());
        holder.humanReadableDeviceNameTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(listener != null) {
                    listener.onViewClicked(DeviceSelectFragment_RecyclerViewAdapter.this, view, position);
                }
            }
        });

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(listener != null) {
                    listener.onRowClicked(DeviceSelectFragment_RecyclerViewAdapter.this, position);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return spoofDevices.size();
    }

    public List<SpoofDevice> getSpoofDevices() {
        return spoofDevices;
    }

    public RecyclerViewOnClickListener getListener() {
        return listener;
    }

    public void setListener(RecyclerViewOnClickListener listener) {
        this.listener = listener;
    }

}

