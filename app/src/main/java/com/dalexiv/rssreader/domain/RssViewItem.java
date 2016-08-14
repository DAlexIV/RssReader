package com.dalexiv.rssreader.domain;

import android.support.annotation.Nullable;

import com.dalexiv.rssreader.data.*;
import com.google.auto.value.AutoValue;

/**
 * Created by dalexiv on 8/14/16.
 */
@AutoValue
public abstract class RssViewItem {
    public static RssViewItem create(RssItem rssItem, String channelImageLink) {
        return new AutoValue_RssViewItem(rssItem, channelImageLink);
    }
    public abstract RssItem rssItem();
    @Nullable public abstract String channelImageLink();
}
