package com.dalexiv.rssreader.presentation.di;

import com.dalexiv.rssreader.presentation.presenters.PresenterRssList;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

/**
 * Created by dalexiv on 8/14/16.
 */
@Module
public class PresenterModule {
    @Singleton
    @Provides
    PresenterRssList providePresenterRssList() {
        return new PresenterRssList();
    }
}
