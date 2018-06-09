package nz.pbomb.xposed.spoofmydevice.activities;

import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.support.design.widget.TabLayout;

import android.support.v4.app.FragmentManager;
import android.support.v4.app.NavUtils;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.github.clans.fab.FloatingActionMenu;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import nz.pbomb.xposed.spoofmydevice.Common;
import nz.pbomb.xposed.spoofmydevice.R;
import nz.pbomb.xposed.spoofmydevice.adapters.AppSelectActivity_RecyclerViewAdapter;
import nz.pbomb.xposed.spoofmydevice.adapters.AppSelectActivity_ViewPagerAdapter;
import nz.pbomb.xposed.spoofmydevice.adapters.RecyclerViewOnClickListener;
import nz.pbomb.xposed.spoofmydevice.fragments.RecyclerViewFragment;
import nz.pbomb.xposed.spoofmydevice.fragments.RetainedFragment;
import nz.pbomb.xposed.spoofmydevice.models.ApplicationData;
import nz.pbomb.xposed.spoofmydevice.models.TabInfo;
import nz.pbomb.xposed.spoofmydevice.utils.PackageInfoComparator;


public class AppSelectActivity extends AppCompatActivity implements RecyclerViewOnClickListener {
    private SharedPreferences mSharedPreferences;

    @BindView(R.id.activity_app_select_progressMessage_TextView)     protected TextView mTextViewProgress;
    @BindView(R.id.activity_app_select_progressBar)     protected ProgressBar mProgressBar;
    @BindView(R.id.activity_app_select_fab_main)        protected FloatingActionMenu mFAM;
    //@BindView(R.id.activity_app_select_fab_spoofAll)    protected FloatingActionButton mFAB_spoofAll;
    //@BindView(R.id.activity_app_select_fab_unSpoofAll)  protected FloatingActionButton mFAB_unSpoofAll;

    @BindView(R.id.activity_app_select_viewpager) ViewPager mViewPager;
    @BindView(R.id.activity_app_select_sliding_tabs) TabLayout mTabLayout;

    private AppSelectActivity_ViewPagerAdapter mViewPagerAdapter;

    private RetainedFragment mFragment_retained;

    private RecyclerView.Adapter mRecyclerViewAdapter_NonSpoofedApps;
    private RecyclerView.Adapter mRecyclerViewAdapter_SpoofedApps;

    private RecyclerView.LayoutManager mRecyclerViewLayoutManager_NonSpoofedApps;
    private RecyclerView.LayoutManager mRecyclerViewLayoutManager_SpoofedApps;

    private AsyncTask mAsyncTask;

    @Override
    @SuppressWarnings("ConstantConditions")
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_app_select);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setElevation(0); // Removes shadow under ActionBar for seamless look.
        ButterKnife.bind(this);

        // Get packages from SharedPreferences that are already spoofed
        mSharedPreferences = getSharedPreferences("SharedPreferences", Context.MODE_PRIVATE);

        // Display all packages and ads in RecyclerView on seperate thread
        mAsyncTask = new onCreateASyncTask(savedInstanceState).execute();
    }

    @OnClick(R.id.activity_app_select_fab_spoofAll)
    public void mFAB_spoofAll_onClick() {

        new AlertDialog.Builder(this)
            .setTitle(getString(R.string.AppSelectActivity_alertDialog_spoofAllWarning_title))
            .setMessage(getString(R.string.AppSelectActivity_alertDialog_spoofAllWarning_message))
            .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int which) {
                    AppSelectActivity_RecyclerViewAdapter recyclerViewAdapter_NonSpoofedApps = (AppSelectActivity_RecyclerViewAdapter) mRecyclerViewAdapter_NonSpoofedApps;
                    AppSelectActivity_RecyclerViewAdapter recyclerViewAdapter_SpoofedApps = (AppSelectActivity_RecyclerViewAdapter) mRecyclerViewAdapter_SpoofedApps;

                    List<ApplicationData> allNonSpoofedApplications = recyclerViewAdapter_NonSpoofedApps.getApplicationDataList();
                    List<ApplicationData> allNonSpoofedApplications_Spoofables = new ArrayList<>();
                    List<ApplicationData> allNonSpoofedApplications_Unspoofables = new ArrayList<>();
                    int allNonSpoofedApplicationsSize = allNonSpoofedApplications.size();

                    if(allNonSpoofedApplicationsSize == 0) {
                        mFAM.close(true);
                        return;
                    }

                    for (int i = 0; i < allNonSpoofedApplications.size(); i++) {
                        ApplicationData a = allNonSpoofedApplications.get(i);

                        if (AppSelectActivity_RecyclerViewAdapter.isPackageSpoofable(a.getAppPackage())) {
                            allNonSpoofedApplications_Spoofables.add(a);
                        } else {
                            allNonSpoofedApplications_Unspoofables.add(a);
                        }

                    }

                    recyclerViewAdapter_SpoofedApps.getApplicationDataList().addAll(allNonSpoofedApplications_Spoofables);
                    recyclerViewAdapter_NonSpoofedApps.getApplicationDataList().clear();
                    recyclerViewAdapter_NonSpoofedApps.getApplicationDataList().addAll(allNonSpoofedApplications_Unspoofables);

                    Collections.sort(((AppSelectActivity_RecyclerViewAdapter) mRecyclerViewAdapter_SpoofedApps).getApplicationDataList(), new ApplicationData.Comparator());
                    Collections.sort(((AppSelectActivity_RecyclerViewAdapter) mRecyclerViewAdapter_NonSpoofedApps).getApplicationDataList(), new ApplicationData.Comparator());


                    mRecyclerViewAdapter_SpoofedApps.notifyDataSetChanged();
                    mRecyclerViewAdapter_NonSpoofedApps.notifyDataSetChanged();
                    //mRecyclerViewAdapter_NonSpoofedApps.notifyItemRangeRemoved(0, allNonSpoofedApplicationsSize);

                    mFAM.close(true);
                }
            })
            .setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int which) {
                    mFAM.close(true);
                }
            })
            .setIcon(R.drawable.ic_warning_black_24dp)
            .show();
    }

    @OnClick(R.id.activity_app_select_fab_unSpoofAll)
    public void mFAB_unSpoofAll_onClick() {
        AppSelectActivity_RecyclerViewAdapter recyclerViewAdapter_NonSpoofedApps = ((AppSelectActivity_RecyclerViewAdapter) mRecyclerViewAdapter_NonSpoofedApps);
        AppSelectActivity_RecyclerViewAdapter recyclerViewAdapter_SpoofedApps = ((AppSelectActivity_RecyclerViewAdapter) mRecyclerViewAdapter_SpoofedApps);


        List<ApplicationData> allSpoofedApplications = recyclerViewAdapter_SpoofedApps.getApplicationDataList();
        int allSpoofedApplicationsSize = allSpoofedApplications.size();
        int oldItemCount = recyclerViewAdapter_SpoofedApps.getItemCount();

        if(allSpoofedApplicationsSize == 0) {
            mFAM.close(true);
            return;
        }

        recyclerViewAdapter_NonSpoofedApps.getApplicationDataList().addAll(allSpoofedApplications);
        recyclerViewAdapter_SpoofedApps.getApplicationDataList().clear();
        Collections.sort(((AppSelectActivity_RecyclerViewAdapter) mRecyclerViewAdapter_NonSpoofedApps).getApplicationDataList(), new ApplicationData.Comparator());

        mRecyclerViewAdapter_NonSpoofedApps.notifyDataSetChanged();
        mRecyclerViewAdapter_SpoofedApps.notifyItemRangeRemoved(0, oldItemCount);

        mFAM.close(true);
    }

    @Override
    public void onBackPressed() {
        if(mFAM.isOpened()) {
            mFAM.close(true);
        } else {
            super.onBackPressed();
        }

    }

    @Override
    @SuppressWarnings("unchecked")
    public void onResume() {
        super.onResume();

        // If the app was pushed to the background then an app could be unistalled or installed by
        // the user. If those scenerio was true, we need to remove the uninstalled package
        // from SharedPreferences and the RecyclerView's to avoid future errors and add newly
        // installed apps to the RecyclerView

        // Initialise all the variables we need access

        PackageManager pm = getPackageManager();

        // Get both RecyclerViewAdapters so we can get the Application Data lists.
        AppSelectActivity_RecyclerViewAdapter recyclerViewAdapter_NonSpoofedApps = ((AppSelectActivity_RecyclerViewAdapter) mRecyclerViewAdapter_NonSpoofedApps);
        AppSelectActivity_RecyclerViewAdapter recyclerViewAdapter_SpoofedApps = ((AppSelectActivity_RecyclerViewAdapter) mRecyclerViewAdapter_SpoofedApps);

        if(recyclerViewAdapter_NonSpoofedApps == null || recyclerViewAdapter_SpoofedApps == null) {
            return;
        }

        // Get references to ApplicationData list of both RecyclerViewAdapters for future manipulation
        List<ApplicationData> recyclerViewAdapter_NonSpoofedApps_AppList = recyclerViewAdapter_NonSpoofedApps.getApplicationDataList();
        List<ApplicationData>  recyclerViewAdapter_SpoofedApps_AppList = recyclerViewAdapter_SpoofedApps.getApplicationDataList();

        // A list for each recycler so we know which items to remove (if any)
        List<Integer> NonSpoofedApps_AppIndexRemovalList = new ArrayList<>();
        List<Integer> SpoofedApps_AppIndexRemovalList = new ArrayList<>();


        // Check if all ApplicationData instances should continue their existence by checking
        // if the package name can be used to retrieve the app information from the PackageManager.
        // If an error is produced, then the application of the ApplicationData instance no longer
        // exists, so the ApplicationData can be removed as well.


        for(int i = 0; i < recyclerViewAdapter_NonSpoofedApps_AppList.size(); i++) {
            ApplicationData applicationData = recyclerViewAdapter_NonSpoofedApps_AppList.get(i);
            String packageName = applicationData.getAppPackage();

            try {
                pm.getPackageInfo(packageName, 0);
            } catch (PackageManager.NameNotFoundException e) {
                Log.i(Common.TAG, "The package '"+packageName+"' has been recently uninstalled when this application was paused.");
                NonSpoofedApps_AppIndexRemovalList.add(i);
                break;
            }
        }

        for(int i = 0; i < recyclerViewAdapter_SpoofedApps_AppList.size(); i++) {
            ApplicationData applicationData = recyclerViewAdapter_SpoofedApps_AppList.get(i);
            String packageName = applicationData.getAppPackage();

            try {
                pm.getPackageInfo(packageName, 0);
            } catch (PackageManager.NameNotFoundException e) {
                SpoofedApps_AppIndexRemovalList.add(i);
                Log.i(Common.TAG, "The package '"+packageName+"' has been recently uninstalled when this application was paused.");
                break;
            }
        }

        // Remove all ApplicationData instances that no longer have their assoicated applications
        // installed on the device.

        for(Integer i : NonSpoofedApps_AppIndexRemovalList) {
            recyclerViewAdapter_NonSpoofedApps_AppList.remove((int) i);
        }
        for(Integer i : SpoofedApps_AppIndexRemovalList) {
            recyclerViewAdapter_SpoofedApps_AppList.remove((int) i);
        }

        recyclerViewAdapter_NonSpoofedApps.notifyDataSetChanged();
        recyclerViewAdapter_SpoofedApps.notifyDataSetChanged();

        List<String> recyclerViewAdapter_NonSpoofedApps_AppList_PackagesOnly = ApplicationData.getSortedPackages(recyclerViewAdapter_NonSpoofedApps_AppList);
        List<String> recyclerViewAdapter_SpoofedApps_AppList_PackagesOnly = ApplicationData.getSortedPackages(recyclerViewAdapter_SpoofedApps_AppList);

        // Check and add for newly installed applications
        for(PackageInfo pInfo : pm.getInstalledPackages(0)) {
            // Initialise a variable to indicate if the package is a newly installed application.
            boolean isNewApp = true;

            if(recyclerViewAdapter_NonSpoofedApps_AppList_PackagesOnly.contains(pInfo.packageName)) {
                isNewApp = false;
            } else if(recyclerViewAdapter_SpoofedApps_AppList_PackagesOnly.contains(pInfo.packageName)) {
                isNewApp = false;
            }

            if(isNewApp) {
                String appPackage = pInfo.packageName;
                String appName = pInfo.applicationInfo.loadLabel(getPackageManager()).toString();
                String appVersion = pInfo.versionName;
                Drawable appIconDrawable = pInfo.applicationInfo.loadIcon(pm);

                Log.i(Common.TAG, "The package '"+appPackage+"' has been recently installed when this application was paused.");

                ApplicationData applicationData = new ApplicationData(appName, appPackage ,appVersion, appIconDrawable);
                recyclerViewAdapter_NonSpoofedApps_AppList.add(applicationData);
                Collections.sort(recyclerViewAdapter_NonSpoofedApps_AppList, new ApplicationData.Comparator());
            }
        }

        recyclerViewAdapter_NonSpoofedApps.notifyDataSetChanged();
        recyclerViewAdapter_SpoofedApps.notifyDataSetChanged();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            // Respond to the action bar's Up/Home button
            case android.R.id.home:
                NavUtils.navigateUpFromSameTask(this);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onStop() {
        if(mAsyncTask != null) {
            if(mAsyncTask.getStatus() == AsyncTask.Status.PENDING || mAsyncTask.getStatus() == AsyncTask.Status.RUNNING) {
                mAsyncTask.cancel(true);
                super.onStop();
                return;
            }
        }

        // Save all spoofed applications
        removeAllPackageFromSharedPreferences();
        addSpoofedApplicationDateToSharedPreferences();

        super.onStop();
    }

    @Override
    protected void onDestroy() {
        AppSelectActivity_RecyclerViewAdapter recyclerViewAdapter_NonSpoofedApps = ((AppSelectActivity_RecyclerViewAdapter) mRecyclerViewAdapter_NonSpoofedApps);
        AppSelectActivity_RecyclerViewAdapter recyclerViewAdapter_SpoofedApps = ((AppSelectActivity_RecyclerViewAdapter) mRecyclerViewAdapter_SpoofedApps);

        if(recyclerViewAdapter_NonSpoofedApps == null || recyclerViewAdapter_SpoofedApps == null) {
            super.onDestroy();
            return;
        }

        mFragment_retained.setNonSpoofedApplications(recyclerViewAdapter_NonSpoofedApps.getApplicationDataList());
        mFragment_retained.setSpoofedApplications(recyclerViewAdapter_SpoofedApps.getApplicationDataList());

        super.onDestroy();
    }


    @Override
    protected void onSaveInstanceState(Bundle outState) {
        outState.putInt("mViewPager_currentItem", mViewPager.getCurrentItem());
        super.onSaveInstanceState(outState);

    }

    @Override
    public void onRowClicked(RecyclerView.Adapter rva, int position) {

    }

    @Override
    public void onViewClicked(RecyclerView.Adapter recyclerViewAdapter, View view, int position) {
        if(recyclerViewAdapter instanceof AppSelectActivity_RecyclerViewAdapter) {
            AppSelectActivity_RecyclerViewAdapter appSelectActivityRecyclerViewAdapter = (AppSelectActivity_RecyclerViewAdapter) recyclerViewAdapter;
            int applicationDatasPositionWithOffset = AppSelectActivity_RecyclerViewAdapter.getApplicationDataListPosition(position);

            if (appSelectActivityRecyclerViewAdapter == mRecyclerViewAdapter_NonSpoofedApps) {
                ApplicationData applicationData = appSelectActivityRecyclerViewAdapter.getApplicationDataList().remove(applicationDatasPositionWithOffset);
                ((AppSelectActivity_RecyclerViewAdapter) mRecyclerViewAdapter_SpoofedApps).getApplicationDataList().add(applicationData);
                Collections.sort(((AppSelectActivity_RecyclerViewAdapter) mRecyclerViewAdapter_SpoofedApps).getApplicationDataList(), new ApplicationData.Comparator());

                mRecyclerViewAdapter_NonSpoofedApps.notifyItemRemoved(position);
                mRecyclerViewAdapter_NonSpoofedApps.notifyItemRangeChanged(position, mRecyclerViewAdapter_NonSpoofedApps.getItemCount());
                mRecyclerViewAdapter_SpoofedApps.notifyDataSetChanged();
            } else if (appSelectActivityRecyclerViewAdapter == mRecyclerViewAdapter_SpoofedApps) {
                ApplicationData applicationData = appSelectActivityRecyclerViewAdapter.getApplicationDataList().remove(applicationDatasPositionWithOffset);
                ((AppSelectActivity_RecyclerViewAdapter) mRecyclerViewAdapter_NonSpoofedApps).getApplicationDataList().add(applicationData);
                Collections.sort(((AppSelectActivity_RecyclerViewAdapter) mRecyclerViewAdapter_NonSpoofedApps).getApplicationDataList(), new ApplicationData.Comparator());

                mRecyclerViewAdapter_SpoofedApps.notifyItemRemoved(position);
                mRecyclerViewAdapter_SpoofedApps.notifyItemRangeChanged(position, mRecyclerViewAdapter_SpoofedApps.getItemCount());
                mRecyclerViewAdapter_NonSpoofedApps.notifyDataSetChanged();
            }
        }

    }

    /* SharedPreferences Methods */

    protected void addPackageToSharedPreferences(String packageName) {
        // Safety checking that your adding a package is already saved in SharedPreferences.
        List<String> packageList = reloadPackagesFromSharedPreferences();
        if(packageList.contains(packageName)) {
            return;
        }

        SharedPreferences.Editor editor = mSharedPreferences.edit();
        packageList.add(packageName);
        editor.putStringSet("packages", new HashSet<>(packageList));
        editor.apply();
    }

    protected void addSpoofedApplicationDateToSharedPreferences() {
        // Get RecyclerViewAdapters so we can get the Application Data lists.
        AppSelectActivity_RecyclerViewAdapter recyclerViewAdapter_SpoofedApps = ((AppSelectActivity_RecyclerViewAdapter) mRecyclerViewAdapter_SpoofedApps);

        if(recyclerViewAdapter_SpoofedApps == null) {
            return;
        }

        List<ApplicationData>  recyclerViewAdapter_SpoofedApps_AppList = recyclerViewAdapter_SpoofedApps.getApplicationDataList();
        List<String> packageNames = ApplicationData.getSortedPackages(recyclerViewAdapter_SpoofedApps_AppList);

        SharedPreferences.Editor editor = mSharedPreferences.edit();
        editor.putStringSet("packages", new HashSet<>(packageNames));
        editor.apply();

    }

    /**
     * Removes all application packages from SharedPreferences by rewriting the saved Set with an
     * empty set.
     */
    protected void removeAllPackageFromSharedPreferences() {
        SharedPreferences.Editor editor = mSharedPreferences.edit();
        editor.putStringSet("packages", new HashSet<String>());
        editor.apply();
    }

    protected void removePackageFromSharedPreferences(String packageName) {
        // Safety checking that your not removing a package is not saved in SharedPreferences.
        List<String> packageList = reloadPackagesFromSharedPreferences();
        if(!packageList.contains(packageName)) {
            return;
        }

        SharedPreferences.Editor editor = mSharedPreferences.edit();
        packageList.remove(packageName);
        editor.putStringSet("packages", new HashSet<>(packageList));
        editor.apply();
    }

    /**
     * @return A list of application packages that the users would like spoofing enabled.
     */
    protected List<String> reloadPackagesFromSharedPreferences() {
        Set<String> packageListImmutable = mSharedPreferences.getStringSet("packages", new HashSet<String>());
        return new ArrayList<>(packageListImmutable);
    }

    /* Async Initial Service */

    /**
     * Async Initial Service - Loads all Application and Already Spoofed Applications
     * from SharedPreferences.
     */
    protected class onCreateASyncTask extends AsyncTask<Void, Integer, Map<String, List<ApplicationData>>> {
        private Bundle savedInstanceState;
        private FragmentManager fragmentManager;

        onCreateASyncTask(Bundle savedInstanceState) {
            this.savedInstanceState = savedInstanceState;
            this.fragmentManager = getSupportFragmentManager();
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            mTextViewProgress.setVisibility(View.VISIBLE);
            mProgressBar.setVisibility(View.VISIBLE);
            mProgressBar.setProgress(0);
        }

        @Override
        protected Map<String, List<ApplicationData>> doInBackground(Void ... v) {
            // When teh RetainedFragment exists, we can assume that this is because the view
            // has rotated, hence we simply reload the existing data rather than create new data
            // which is time-consuming.
            RetainedFragment retainedFragment = (RetainedFragment) fragmentManager.findFragmentByTag("data");

            if(retainedFragment != null) {
                Map<String, List<ApplicationData>> object = new HashMap<>();
                object.put("spoofed", retainedFragment.getSpoofedApplications());
                object.put("nonSpoofed", retainedFragment.getNonSpoofedApplications());
                return object;
            }

            // Get applications packages that have already been spoofed
            final List<String> packageList = reloadPackagesFromSharedPreferences();

            // Get all packages information and sort them out alphabetically
            PackageManager pm = getPackageManager();
            List<PackageInfo> allPackages = pm.getInstalledPackages(0);
            List<PackageInfo> spoofedPackages = new ArrayList<>();
            List<PackageInfo> nonSpoofedPackages = new ArrayList<>(allPackages);

            // Retrieve each package information bundle that has already been activated for spoofing and
            // configure the information to be used correclty with the RecyclerView.
            for (int i = 0; i < packageList.size(); i++) {
                String packageName = packageList.get(i);
                PackageInfo pInfo;

                try {
                    pInfo = pm.getPackageInfo(packageName, 0);
                } catch (PackageManager.NameNotFoundException e) {
                    // Failure to get the package information means that the app has been removed
                    // from the system therefore remove the package from the SharedPreference file
                    removePackageFromSharedPreferences(packageName);
                    continue;
                }

                // Add the package information to the spoofedPackages list so it visually appears on the
                // screen in the correct section
                spoofedPackages.add(pInfo);

                // Remove the package information from the list of non-spoofed apps as it is known
                // that spoofing is already active for this application
                for (int j = 0; j < nonSpoofedPackages.size(); j++) {
                    if (nonSpoofedPackages.get(j).packageName.equals(packageName)) {
                        nonSpoofedPackages.remove(j);
                        break;
                    }
                }
            }
            publishProgress(25);

            // Generate the data to be placed in the RecyclerView
            final List<ApplicationData> applicationDatas_Spoofed = new ArrayList<>();
            final List<ApplicationData> applicationDatas_NonSpoofed = new ArrayList<>();

            // Alphabetically sort the spoofed list before we add the list to RecyclerView data
            Collections.sort(spoofedPackages, new PackageInfoComparator(pm));

            publishProgress(50);
            // Add each element of spoofedPackages to RecyclerView data
            for (PackageInfo pInfo : spoofedPackages) {
                String pkgName = pInfo.packageName;
                String appName = pInfo.applicationInfo.loadLabel(getPackageManager()).toString();
                String appVer = pInfo.versionName;
                Drawable icon_drawable = pInfo.applicationInfo.loadIcon(pm);

                applicationDatas_Spoofed.add(new ApplicationData(appName, pkgName, appVer, icon_drawable));
            }

            // Alphabetically sort the non-spoofed list
            Collections.sort(nonSpoofedPackages, new PackageInfoComparator(pm));
            publishProgress(75);

            // Add each element of nonSpoofedPackages to RecyclerView data
            for (PackageInfo pInfo : nonSpoofedPackages) {
                String pkgName = pInfo.packageName;
                String appName = pInfo.applicationInfo.loadLabel(getPackageManager()).toString();
                String appVer = pInfo.versionName;
                Drawable icon_drawable = pInfo.applicationInfo.loadIcon(pm);

                applicationDatas_NonSpoofed.add(new ApplicationData(appName, pkgName, appVer, icon_drawable));
                //break;
            }

            Map<String, List<ApplicationData>> object = new HashMap<>();
            object.put("spoofed", applicationDatas_Spoofed);
            object.put("nonSpoofed", applicationDatas_NonSpoofed);

            publishProgress(100);
            return object;
        }

        @Override
        protected void onProgressUpdate(Integer... values) {
            super.onProgressUpdate(values);
            mProgressBar.setProgress(values[0]);
    }

        @Override
        protected void onPostExecute(Map<String, List<ApplicationData>> appDataList) {
            if(isCancelled()) {
                return;
            }


            mProgressBar.setVisibility(View.INVISIBLE);
            mTextViewProgress.setVisibility(View.INVISIBLE);

            // Configure the RecyclerView with the custom Adapter a Linear Layout Manager
            mFAM.setVisibility(View.VISIBLE);
            mFAM.setClosedOnTouchOutside(true);

            mFragment_retained = (RetainedFragment)  fragmentManager.findFragmentByTag("data");

            if(mFragment_retained == null) {
                mFragment_retained = RetainedFragment.newInstance();
                fragmentManager.beginTransaction().add(mFragment_retained, "data").commit();
            }

            mRecyclerViewAdapter_NonSpoofedApps = new AppSelectActivity_RecyclerViewAdapter(AppSelectActivity.this, appDataList.get("nonSpoofed"), AppSelectActivity_RecyclerViewAdapter.APP_SELECT_TYPE.NON_SPOOFED);
            mRecyclerViewAdapter_SpoofedApps = new AppSelectActivity_RecyclerViewAdapter(AppSelectActivity.this, appDataList.get("spoofed"), AppSelectActivity_RecyclerViewAdapter.APP_SELECT_TYPE.SPOOFED);

            mViewPager = (ViewPager) findViewById(R.id.activity_app_select_viewpager);
            mTabLayout = (TabLayout) findViewById(R.id.activity_app_select_sliding_tabs);

            mTabLayout.setupWithViewPager(mViewPager);

            mRecyclerViewLayoutManager_NonSpoofedApps = new LinearLayoutManager(AppSelectActivity.this);
            mRecyclerViewLayoutManager_SpoofedApps = new LinearLayoutManager(AppSelectActivity.this);

            // Get the ViewPager and set it's PagerAdapter so that it can display items
            mViewPagerAdapter = new AppSelectActivity_ViewPagerAdapter(AppSelectActivity.this, mViewPager, mTabLayout);
            Bundle Fragment1 = new Bundle();
            Bundle Fragment2 = new Bundle();

            Fragment1.putInt("fragmentType", RecyclerViewFragment.FragmentType.NON_SPOOFED.ordinal());
            Fragment2.putInt("fragmentType", RecyclerViewFragment.FragmentType.SPOOFED.ordinal());

            mViewPagerAdapter.addTab(new TabInfo(RecyclerViewFragment.class, Fragment1, "Non-Spoofed",
                    new String[] {"RecyclerViewLayoutManager", "RecyclerViewAdapter"  },
                    mRecyclerViewLayoutManager_NonSpoofedApps, mRecyclerViewAdapter_NonSpoofedApps ));
            mViewPagerAdapter.addTab(new TabInfo(RecyclerViewFragment.class, Fragment2, "Spoofed",
                    new String[] {"RecyclerViewLayoutManager", "RecyclerViewAdapter"  },
                    mRecyclerViewLayoutManager_SpoofedApps, mRecyclerViewAdapter_SpoofedApps ));

            ((AppSelectActivity_RecyclerViewAdapter) mRecyclerViewAdapter_NonSpoofedApps).setListener(AppSelectActivity.this);
            ((AppSelectActivity_RecyclerViewAdapter) mRecyclerViewAdapter_SpoofedApps).setListener(AppSelectActivity.this);

            if(savedInstanceState != null) {
                mViewPager.setCurrentItem(savedInstanceState.getInt("mViewPager_currentItem"));
            }
        }
    }
}
