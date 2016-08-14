package com.dalexiv.rssreader.domain.parsers;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

/**
 * Created by dalexiv on 8/13/16.
 */

@Module
public class ChannelParseModule {
    @Provides
    @Singleton
    ChannelParser provideChannelParser() {
        return new ChannelParserImpl();
    }
}
