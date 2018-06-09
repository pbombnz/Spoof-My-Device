package nz.pbomb.xposed.spoofmydevice.adapters;

import android.support.v7.widget.RecyclerView;
import android.view.View;


public interface RecyclerViewOnClickListener {
    /**
     * This method is called when a RecyclerView item has been clicked regardless of which child
     * view is clicked.
     *
     * @param recyclerViewAdapter The Adapter of the ReyclerView instance that this listener is attached to.
     * @param position The position of the row that have been clicked by the user.
     */
    void onRowClicked(RecyclerView.Adapter recyclerViewAdapter, int position);

    /**
     * This method is called when a RecyclerView item has been clicked where the user clicked a
     * specific view within the item.
     *
     * @param recyclerViewAdapter The Adapter of the ReyclerView instance that this listener is attached to.
     * @param view The specific view or parent of view clicked
     * @param position The position of the row that have been clicked by the user.
     */
    void onViewClicked(RecyclerView.Adapter recyclerViewAdapter, View view, int position);
}
