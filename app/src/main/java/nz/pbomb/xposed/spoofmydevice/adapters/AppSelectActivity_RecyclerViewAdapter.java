package nz.pbomb.xposed.spoofmydevice.adapters;

import android.app.Activity;
import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.NativeExpressAdView;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import butterknife.BindView;
import butterknife.ButterKnife;
import nz.pbomb.xposed.spoofmydevice.Common;
import nz.pbomb.xposed.spoofmydevice.R;
import nz.pbomb.xposed.spoofmydevice.models.ApplicationData;


public class AppSelectActivity_RecyclerViewAdapter extends RecyclerView.Adapter<AppSelectActivity_RecyclerViewAdapter.ViewHolder> {

    static class ViewHolder extends RecyclerView.ViewHolder {
        ViewHolder(View itemView) {
            super(itemView);
        }
    }

    static class ApplicationDataViewHolder extends ViewHolder {

        @BindView(R.id.app_name) TextView appNameTextView;
        @BindView(R.id.app_package) TextView appPackageTextView;
        @BindView(R.id.app_icon_drawable) ImageView appIconDrawable;

        @BindView(R.id.cardView_button_tv) TextView itemViewButton;

        ApplicationDataViewHolder(View itemView) {
            super(itemView);

            ButterKnife.bind(itemView);


            appNameTextView = (TextView) itemView.findViewById(R.id.app_name);
            appPackageTextView = (TextView) itemView.findViewById(R.id.app_package);
            appIconDrawable = (ImageView) itemView.findViewById(R.id.app_icon_drawable);
            itemViewButton = (TextView) itemView.findViewById(R.id.cardView_button_tv);
        }
    }

    static class NativeExpressAdViewHolder extends ViewHolder {
        @BindView(R.id.nativeExpressAdView)
        NativeExpressAdView nativeExpressAdView;

        NativeExpressAdViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(itemView);

            nativeExpressAdView = (NativeExpressAdView) itemView.findViewById(R.id.nativeExpressAdView);
        }
    }

    // Intialise a list of applications packages that are guaranteed to brick or bootloop
    // the Android device/ROM and should not be allowed to be spoofed to protect the users
    private static List<Pattern> shouldNotBeSpoofedPackageApplications;

    static {
        // Intialise a list of applications packages that are guaranteed to brick or bootloop
        // the Android device/ROM and should not be allowed to be spoofed to protect the users
        shouldNotBeSpoofedPackageApplications = new ArrayList<>();
        shouldNotBeSpoofedPackageApplications.add(Pattern.compile("nz\\.pbomb\\.xposed\\.spoofmydevice"));
        shouldNotBeSpoofedPackageApplications.add(Pattern.compile("android"));
        shouldNotBeSpoofedPackageApplications.add(Pattern.compile("com\\.android\\.(?!((vending)|(chrome))).*"));
    }

    /**
     * Determine whether the application package is safe to spoof or not.
     *
     * @param applicationPackage The package to check whether it is safe to spoof or not.
     * @return false, if the package is unsafe otherwise if the package is safe to enable spoofing,
     * then return true.
     */
    public static boolean isPackageSpoofable(String applicationPackage) {
        for(Pattern p : shouldNotBeSpoofedPackageApplications) {
            Matcher m = p.matcher(applicationPackage);
            if(m.matches()) {
                return false;
            }
        }
        return true;
    }

    // For every X RecyclerView items, an Ad is shown.
    private final static int AD_SHOWN_PER_ITEMS = 10;

    public enum APP_SELECT_TYPE {
        /*AD(-1), */NON_SPOOFED, SPOOFED
    }

    private final APP_SELECT_TYPE ApplicationSelectionType;
    private RecyclerViewOnClickListener listener;
    private List<ApplicationData> applicationDataList;

    private AdRequest request;

    private Activity activity;

    public AppSelectActivity_RecyclerViewAdapter(Activity activity,
                                                 List<ApplicationData> applicationDataList,
                                                 APP_SELECT_TYPE applicationSelectionType) {
        this.request = new AdRequest.Builder()
                .addTestDevice("EF959C00FE3BEBB55A6972D73A32BE00")
                .build();

        this.activity = activity;
        this.applicationDataList = applicationDataList;
        this.ApplicationSelectionType = applicationSelectionType;
    }

    @Override
    public int getItemViewType(int position) {
        boolean isItemViewAd = position % AD_SHOWN_PER_ITEMS == 0;
        if(isItemViewAd && position != 0) {
            return -1; // Ad Item View Type
        } else {
            return this.ApplicationSelectionType.ordinal();
        }
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);

        // Create Native AdView
        if(viewType == -1) {
            View adView = inflater.inflate(R.layout.fragment_card_view_admob, parent, false);
            NativeExpressAdViewHolder adViewHolder = new NativeExpressAdViewHolder(adView);
            adViewHolder.nativeExpressAdView.loadAd(request);
            return adViewHolder;
        } else {
            // Create a ApplicationData cardView for either the spoofed or unspoofed RecyclerView
            View applicationDataView = inflater.inflate(R.layout.fragment_card_view, parent, false);
            ApplicationDataViewHolder applicationDataViewHolder = new ApplicationDataViewHolder(applicationDataView);

            // When the RecyclerView is for already Spoofed application, we change the button text to a
            // minus symbol to visually show to the user we a removing it from this spoofed list
            if (viewType == APP_SELECT_TYPE.SPOOFED.ordinal()) {
                applicationDataViewHolder.itemViewButton.setText("−");
            }
            return applicationDataViewHolder;
        }
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {
        // Initialise a offset so get the Application data from the ArrayList taking into account
        // the Ads shown in the RecyclerView
        int listOffset = getApplicationDataListPosition(position);
        //Log.d(Common.TAG, "getItemCOunt:"+getItemCount()+" size:" +getApplicationDataList().size() + "  pos:"+position+" offset:"+listOffset);

        boolean isItemViewAd = position % AD_SHOWN_PER_ITEMS == 0;
        // Do not bind anything if the item at position is a native ad.
        if(isItemViewAd && position != 0) {
            return;
        }

        ApplicationDataViewHolder applicationDataViewHolder = (ApplicationDataViewHolder) holder;


        ApplicationData applicationData = applicationDataList.get(/*position - */listOffset);

        Log.d(Common.TAG, "pos: "+position);
        Log.d(Common.TAG, "offset : "+ listOffset);
        Log.d(Common.TAG, "appData: "+ applicationData.toString());

        applicationDataViewHolder.appNameTextView.setText(applicationData.getAppName());
        applicationDataViewHolder.appPackageTextView.setText(applicationData.getAppPackage());
        applicationDataViewHolder.appIconDrawable.setImageDrawable(applicationData.appIconDrawable());

        // Disable the button and do not add click listeners if the app is unsafe to spoof. That
        // way the user cannot screw up their device.
        if(!isPackageSpoofable(applicationData.getAppPackage())) {
            applicationDataViewHolder.itemViewButton.setVisibility(View.GONE);
            return;
        }
        applicationDataViewHolder.itemViewButton.setVisibility(View.VISIBLE);

        applicationDataViewHolder.itemViewButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(listener != null) {
                    listener.onViewClicked(AppSelectActivity_RecyclerViewAdapter.this, view, position);
                }
            }
        });

        applicationDataViewHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(listener != null) {
                    listener.onRowClicked(AppSelectActivity_RecyclerViewAdapter.this, position);
                }
            }
        });

    }

    @Override
    public int getItemCount() {
        return (int) (applicationDataList.size() + (Math.floor((applicationDataList.size()/ AD_SHOWN_PER_ITEMS))));
    }

    public static int getApplicationDataListPosition(int position) {
        return position - (int) Math.floor((position / AD_SHOWN_PER_ITEMS));
    }

    public RecyclerViewOnClickListener getListener() {
        return listener;
    }

    public void setListener(RecyclerViewOnClickListener listener) {
        this.listener = listener;
    }

    public List<ApplicationData> getApplicationDataList() {
        return applicationDataList;
    }

}

