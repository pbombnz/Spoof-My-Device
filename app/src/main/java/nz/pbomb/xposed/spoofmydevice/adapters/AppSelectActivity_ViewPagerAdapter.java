package nz.pbomb.xposed.spoofmydevice.adapters;

import android.content.Context;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.util.SparseArray;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

import nz.pbomb.xposed.spoofmydevice.BuildConfig;
import nz.pbomb.xposed.spoofmydevice.fragments.RecyclerViewFragment;
import nz.pbomb.xposed.spoofmydevice.models.TabInfo;

public class AppSelectActivity_ViewPagerAdapter extends FragmentStatePagerAdapter {
    private final FragmentManager mFragmentManager;
    private SparseArray<Fragment> mFragments;

    private FragmentTransaction mCurTransaction;

    private List<TabInfo> mTabs;

    private Context mContext;

    private ViewPager mViewPager;
    private TabLayout mTabLayout;

    public AppSelectActivity_ViewPagerAdapter(AppCompatActivity activity, ViewPager viewPager, TabLayout tabLayout) {
        super(activity.getSupportFragmentManager());

        this.mFragmentManager = activity.getSupportFragmentManager();
        this.mFragments = new SparseArray<>();

        this.mTabs = new ArrayList<>();

        this.mContext = activity;

        this.mViewPager = viewPager;
        this.mTabLayout = tabLayout;

        this.mViewPager.setAdapter(this);
        this.mTabLayout.setupWithViewPager(this.mViewPager);
    }

    @Override
    public void startUpdate(ViewGroup container) {
        if (container.getId() == View.NO_ID) {
            throw new IllegalStateException("ViewPager with adapter " + this
                    + " requires a view id");
        }
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        if (mFragments.size() > position) {
            Fragment f = mFragments.get(position);
            if (f != null) {
                return f;
            }
        }

        Fragment fragment = getItem(position);

        if (mCurTransaction == null) {
            mCurTransaction = mFragmentManager.beginTransaction();
        }
        mCurTransaction.add(container.getId(),fragment,"fragment:"+position);
        mFragments.put(position, fragment);
        return fragment;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        if (mCurTransaction == null) {
            mCurTransaction = mFragmentManager.beginTransaction();
        }
        mCurTransaction.remove(mFragments.get(position));
        mFragments.remove(position);
    }

    @Override
    public boolean isViewFromObject(View view, Object fragment) {
        return ((Fragment) fragment).getView() == view;
    }

    @Override
    public void finishUpdate(ViewGroup container) {
        if (mCurTransaction != null) {
            mCurTransaction.commitAllowingStateLoss();
            mCurTransaction = null;
            mFragmentManager.executePendingTransactions();
        }
    }

    @Override
    public int getCount() {
        // Must be mTabs's size as it assists the ViewPager to initial amount of tabs.
        // It cannot be mFragments's size, as this would be zero when we try to create the tabs.
        return mTabs.size();
    }


    /**
     * Generates a new Fragment Instance at the specified position. Only called when the Adapter
     * cannot find a Fragment for this instance which usually occurs when the ViewPager has
     * recently been inflated.
     *
     * @param position the position that a fragment needs to inflated at.
     * @return A fragment at the specified position.
     */
    public Fragment getItem(int position) {
        if(BuildConfig.DEBUG) {
            if(!(position >= 0 && position < mTabs.size())) {
                throw new IllegalArgumentException("position parameter must be in range of the available tabs.");
            }
        }

        // Get Tab information
        TabInfo tabInfo = mTabs.get(position);
        // Instantiate a Fragment using the information for the TabInfo instance
        Fragment fragment = Fragment.instantiate(getContext(), tabInfo.getClassName().getName(), tabInfo.getArgs());

        // If extras contains a ReyclerView.LayoutManager and a Recycler.ViewAdapter, and the
        // fragment instance is a sub-type of RecyclerViewFragment than we can attach theses
        // objects to the recycler view.
        if(tabInfo.getExtras().containsKey("RecyclerViewLayoutManager")
        && tabInfo.getExtras().containsKey("RecyclerViewAdapter")
        && fragment instanceof RecyclerViewFragment) {
            RecyclerView.LayoutManager layoutManager = (RecyclerView.LayoutManager) tabInfo.getExtras().get("RecyclerViewLayoutManager");
            RecyclerView.Adapter adapter = (RecyclerView.Adapter) tabInfo.getExtras().get("RecyclerViewAdapter");

            ((RecyclerViewFragment) fragment).setLayoutManager(layoutManager);
            ((RecyclerViewFragment) fragment).setAdapter(adapter);
        }
        return fragment;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        if(BuildConfig.DEBUG) {
            if(!(position >= 0 && position < mTabs.size())) {
                throw new IllegalArgumentException("position parameter must be in range of the available tabs.");
            }
        }

        // Generate title based on item position
        return mTabs.get(position).getTabName();
    }

    /**
     * Adds new Tabs to the ViewPager and TabLayout views
     *
     * @param tabInfo A series of TabInfo instances that contains enough information to create a
     *                new tab when needed.
     */
    public void addTab(TabInfo... tabInfo) {
        if(tabInfo.length > 0) {
            for (TabInfo aTabInfo : tabInfo) {
                mTabs.add(aTabInfo);
                mTabLayout.addTab(mTabLayout.newTab().setText(aTabInfo.getTabName()));
            }
            // Notify a change to the adapter so they can be created and added to the ViewPager and
            // TabLayout.
            notifyDataSetChanged();
        }
    }

    /**
     *
     * @return The context that the ViewPager and TabLayout are attached to.
     */
    private Context getContext() {
        return mContext;
    }

    /**
     *
     * @return A SparseArray of Fragments loaded in this ViewPager.
     */
    public SparseArray<Fragment> getFragments() {
        return mFragments;
    }

}
