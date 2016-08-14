package com.dalexiv.rssreader.data;

import com.google.auto.value.AutoValue;

import java.util.List;

/**
 * Created by dalexiv on 8/13/16.
 */
@AutoValue
public abstract class RssChannel {
    public static RssChannel create(RssData data, List<RssItem> items) {
        return new AutoValue_RssChannel(data, items);
    }
    public abstract RssData getRssData();
    public abstract List<RssItem> getItems();
}
