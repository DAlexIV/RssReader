package com.dalexiv.rssreader;

import android.app.Application;
import android.content.Context;
import android.support.annotation.NonNull;

public class RssReaderApp extends Application {
    private ApplicationComponent applicationComponent;

    // Prevent need in a singleton (global) reference to the application object.
    @NonNull
    public static RssReaderApp from(@NonNull Context context) {
        return (RssReaderApp) context.getApplicationContext();
    }

    @Override
    public void onCreate() {
        super.onCreate();
        applicationComponent = prepareApplicationComponent().build();
    }

    @NonNull
    protected DaggerApplicationComponent.Builder prepareApplicationComponent() {
        return DaggerApplicationComponent.builder()
                .applicationModule(new ApplicationModule(this));
    }

    @NonNull
    public ApplicationComponent getApplicationComponent() {
        return applicationComponent;
    }
}
