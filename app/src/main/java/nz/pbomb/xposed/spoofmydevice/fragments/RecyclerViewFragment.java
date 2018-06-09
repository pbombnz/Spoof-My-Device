package nz.pbomb.xposed.spoofmydevice.fragments;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import nz.pbomb.xposed.spoofmydevice.Common;
import nz.pbomb.xposed.spoofmydevice.R;

public class RecyclerViewFragment extends Fragment {
    public enum FragmentType {
        NON_SPOOFED, SPOOFED
    }

    private @Nullable RecyclerView.Adapter mAdapter;
    private @Nullable RecyclerView.LayoutManager mLayoutManager;



    public static RecyclerViewFragment newInstance(FragmentType fragmentType) {
        Bundle args = new Bundle();
        args.putInt("fragmentType", fragmentType.ordinal());

        RecyclerViewFragment fragment = new RecyclerViewFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view;
        int fragmentType = getArguments().getInt("fragmentType");

        if(fragmentType == FragmentType.NON_SPOOFED.ordinal()) {
            view = inflater.inflate(R.layout.fragment_nonspoofed_apps_recycler_view, container, false);
        } else if(fragmentType == FragmentType.SPOOFED.ordinal()) {
            view = inflater.inflate(R.layout.fragment_spoofed_apps_recycler_view, container, false);
        } else {
            view = null;
        }
        return view;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        RecyclerView recyclerView = getRecyclerView();
        if(recyclerView != null) {
            recyclerView.setLayoutManager(getLayoutManager());
            recyclerView.setAdapter(getAdapter());
        }
    }


    /**
     * @return Returns the RecyclerView of Fragment. Returns null if the RecyclerView hasn't been initialised yet.
     */
    public @Nullable RecyclerView getRecyclerView() {
        if(getView() == null) {
            return null;
        }

        int fragmentType = getArguments().getInt("fragmentType");

        if(fragmentType == FragmentType.NON_SPOOFED.ordinal()) {
            return (RecyclerView) getView().findViewById(R.id.nonSpoofedRecyclerView);
        } else if(fragmentType == FragmentType.SPOOFED.ordinal()) {
            return (RecyclerView) getView().findViewById(R.id.spoofedRecyclerView);
        } else {
            return null;
        }
    }

    public @Nullable RecyclerView.Adapter getAdapter() {
        return mAdapter;
    }

    public void setAdapter(@NonNull RecyclerView.Adapter adapter) {
        this.mAdapter = adapter;
    }

    public @Nullable RecyclerView.LayoutManager getLayoutManager() {
        return mLayoutManager;
    }

    public void setLayoutManager(@NonNull RecyclerView.LayoutManager layoutManager) {
        this.mLayoutManager = layoutManager;
    }

}
