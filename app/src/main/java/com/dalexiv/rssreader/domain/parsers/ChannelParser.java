package com.dalexiv.rssreader.domain.parsers;


import com.dalexiv.rssreader.data.RssChannel;

import java.util.Arrays;
import java.util.List;

/**
 * Created by dalexiv on 8/13/16.
 */

public interface ChannelParser {
    String ITEM = "item";
    String TITLE = "title";
    String LINK = "link";
    String DESCRIPTION = "description";
    String IMAGE_URL_TAG = "url";
    String CHANNEL_IMAGE = "image";
    String PUB_DATE = "pubDate";
    List<String> IMAGE_TAGS = Arrays.asList("media:thumbnail", "media:content", "image");

    RssChannel parse(String xml);
}
