package com.dalexiv.rssreader.presentation.ui.views;


import com.dalexiv.rssreader.domain.RssViewItem;

import java.util.List;

/**
 * Created by dalexiv on 8/14/16.
 */

public interface RssListView {
    void setRefreshing(boolean isRefreshing);
    void notifyUser(String message);
    void displayItems(List<RssViewItem> items);
}
