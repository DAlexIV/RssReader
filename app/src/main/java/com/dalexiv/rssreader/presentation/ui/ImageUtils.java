package com.dalexiv.rssreader.presentation.ui;

import android.content.Context;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.dalexiv.rssreader.domain.RssViewItem;

/**
 * Created by dalexiv on 8/15/16.
 */

public class ImageUtils {
    public static final String defaultUrl
            = "http://parmenides.wnyc.org/media/photologue/photos/rss.png";
    public static void loadRssItemImage(Context context, ImageView imageView,
                                        RssViewItem rssViewItem) {
        final String smallImageLink = rssViewItem.rssItem().getRssData().getImageLink();
        String channelImageLink = rssViewItem.channelImageLink();
        if (channelImageLink == null)
            channelImageLink = defaultUrl;
        Glide.with(context)
                .load(smallImageLink == null ? channelImageLink : smallImageLink)
                .into(imageView);
    }

    public static void loadBigRssItemImage(Context context, ImageView imageView, RssViewItem rssViewItem) {
        // At first we will load small cached picture
        loadRssItemImage(context, imageView, rssViewItem);

        // And then we will try to load the big one, if it exists
        final String bigImageLink = rssViewItem.rssItem().getFullImageLink();
        if (bigImageLink != null)
        Glide.with(context)
                .load(bigImageLink)
                .into(imageView);
    }
}
