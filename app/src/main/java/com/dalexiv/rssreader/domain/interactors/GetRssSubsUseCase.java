package com.dalexiv.rssreader.domain.interactors;

import android.content.Context;
import android.preference.PreferenceManager;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;

import static com.dalexiv.rssreader.domain.interactors.SaveRssSubsUseCase.SET;

/**
 * Created by dalexiv on 8/15/16.
 */

public class GetRssSubsUseCase extends UseCase<List<String>> {
    private Context context;
    private Set<String> defaultUrls;

    public GetRssSubsUseCase(Context context) {
        this.defaultUrls = new TreeSet<>();
        defaultUrls.add("http://rss.cnn.com/rss/edition.rss");
        this.context = context.getApplicationContext();
    }

    @Override
    protected Observable<List<String>> buildUseCaseObservable() {
        Set<String> set = PreferenceManager.getDefaultSharedPreferences(context)
                .getStringSet(SET, defaultUrls);
        List<String> list = new ArrayList<>();
        list.addAll(set);
        return Observable.fromCallable(() -> list);
    }
}
