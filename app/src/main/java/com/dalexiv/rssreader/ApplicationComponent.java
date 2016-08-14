package com.dalexiv.rssreader;

import android.os.Handler;
import android.support.annotation.NonNull;

import com.dalexiv.rssreader.presentation.di.PresenterModule;
import com.dalexiv.rssreader.presentation.ui.fragments.FragmentRssList;

import javax.inject.Named;
import javax.inject.Singleton;

import dagger.Component;

@Singleton
@Component(modules = {
        ApplicationModule.class,
        PresenterModule.class
})
public interface ApplicationComponent {
    @NonNull
    @Named(ApplicationModule.MAIN_THREAD_HANDLER)
    Handler mainThreadHandler();

    void inject(@NonNull FragmentRssList fragmentRssList);


}
