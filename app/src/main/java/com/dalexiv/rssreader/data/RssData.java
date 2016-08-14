package com.dalexiv.rssreader.data;

import android.support.annotation.Nullable;

import com.google.auto.value.AutoValue;

/**
 * Created by dalexiv on 8/14/16.
 */
@AutoValue
public abstract class RssData {
    public static RssData create(String title, String link, String description, String imageLink) {
        return new AutoValue_RssData(title, link, description, imageLink);
    }
    public abstract String getTitle();
    public abstract String getLink();
    public abstract String getDescription();
    @Nullable public abstract String getImageLink();
}
