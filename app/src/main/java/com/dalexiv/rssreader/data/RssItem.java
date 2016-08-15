package com.dalexiv.rssreader.data;

import android.os.Parcelable;
import android.support.annotation.Nullable;

import com.google.auto.value.AutoValue;

/**
 * Created by dalexiv on 8/13/16.
 */
@AutoValue
public abstract class RssItem implements Parcelable {
    public static RssItem create(RssData rssData, String fullImageLink, long timestamp) {
        return new AutoValue_RssItem(rssData, fullImageLink, timestamp);
    }
    public abstract RssData getRssData();
    @Nullable public abstract String getFullImageLink();
    public abstract long getTimestamp();
}
