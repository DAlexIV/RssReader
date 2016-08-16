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

    public GetRssSubsUseCase(Context context) {
        super(AndroidSchedulers.mainThread(), AndroidSchedulers.mainThread());
        this.context = context.getApplicationContext();
    }

    @Override
    protected Observable<List<String>> buildUseCaseObservable() {
        Set<String> set = new TreeSet<>();
        PreferenceManager.getDefaultSharedPreferences(context).getStringSet(SET, set);
        List<String> list = new ArrayList<>();
        list.addAll(set);
        return Observable.fromCallable(() -> list);
    }
}
