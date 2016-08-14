package com.dalexiv.rssreader.data.parsers;

import com.dalexiv.rssreader.data.RssChannel;
import com.dalexiv.rssreader.domain.parsers.ChannelParserImpl;

import org.junit.Test;

/**
 * Created by dalexiv on 8/14/16.
 */

public class ChannelParserLoadFromNetTest {
    private String link = "http://rss.cnn.com/rss/edition.rss";

    @Test
    public void parse() throws Exception {
        ChannelParserImpl parser = new ChannelParserImpl();
        RssChannel channel = parser.parse(link);
        System.out.println(channel);
    }
}
