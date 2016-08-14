package com.dalexiv.rssreader.presentation.di;

import com.dalexiv.rssreader.ApplicationComponent;
import com.dalexiv.rssreader.RssReaderApp;
import com.dalexiv.rssreader.presentation.ui.fragments.FragmentRssList;

/**
 * Created by dalexiv on 8/14/16.
 */

public class FragmentInjectors {
    public static void inject(FragmentRssList rssList) {
        getAppDiComponent(rssList).inject(rssList);
    }

    public static ApplicationComponent getAppDiComponent(FragmentRssList rssList) {
        return RssReaderApp.from(rssList.getActivity()).getApplicationComponent();
    }
}
