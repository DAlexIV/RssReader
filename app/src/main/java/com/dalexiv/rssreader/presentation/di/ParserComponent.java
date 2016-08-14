package com.dalexiv.rssreader.presentation.di;

/**
 * Created by dalexiv on 8/14/16.
 */

import android.support.annotation.NonNull;

import com.dalexiv.rssreader.domain.parsers.ChannelParseModule;
import com.dalexiv.rssreader.presentation.presenters.PresenterRssList;

import javax.inject.Singleton;

import dagger.Component;

@Singleton
@Component(modules = {
        ChannelParseModule.class,
})
public interface ParserComponent {
    void inject(@NonNull PresenterRssList presenterRssList);
}
