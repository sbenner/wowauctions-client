package com.heim.wowauctions.client.ui;

import android.util.Log;
import android.widget.AbsListView;

/**
 * taken from http://www.avocarrot.com/blog/implement-infinitely-scrolling-list-android/
 * User: sbenner
 * Date: 8/16/14
 * Time: 3:15 AM
 */
public abstract class InfiniteScrollListener implements AbsListView.OnScrollListener {
    private int bufferItemCount = 5;
    private int currentPage = 0;
    private int itemCount = 0;
    private boolean isLoading = true;

    public InfiniteScrollListener(int bufferItemCount) {
        this.bufferItemCount = bufferItemCount;
    }

    public abstract void loadMore(int page, int totalItemsCount);

    @Override
    public void onScrollStateChanged(AbsListView view, int scrollState) {
        // Do Nothing
    }

    @Override
    public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount)
    {

        if (totalItemCount < itemCount) {
            this.itemCount = totalItemCount;
            if (totalItemCount == 0) {
                this.isLoading = true; }
        }

        if (isLoading && (totalItemCount > itemCount)) {
            isLoading = false;
            itemCount = totalItemCount;
            currentPage++;
        }

         if (!isLoading && (view.getLastVisiblePosition()>=(totalItemCount-(totalItemCount/3)))) {
            loadMore(currentPage + 1, totalItemCount);
            isLoading = true;
        }
    }
}
