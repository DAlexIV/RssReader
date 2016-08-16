package com.dalexiv.rssreader.domain.interactors;

import android.content.Context;
import android.preference.PreferenceManager;

import java.util.List;
import java.util.Set;
import java.util.TreeSet;

import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;

import static com.bumptech.glide.gifdecoder.GifHeaderParser.TAG;

/**
 * Created by dalexiv on 8/15/16.
 */

public class SaveRssSubsUseCase extends UseCase<List<String>> {
    static final String SET = "SET";
    private Context context;
    private List<String> list;

    public SaveRssSubsUseCase(Context context, List<String> list) {
        super(AndroidSchedulers.mainThread(), AndroidSchedulers.mainThread());
        this.context = context.getApplicationContext();
        this.list = list;
    }

    @Override
    protected Observable<List<String>> buildUseCaseObservable() {
        Set<String> set = new TreeSet<>();
        set.addAll(list);
        PreferenceManager
                .getDefaultSharedPreferences(context)
                .edit()
                .putStringSet(TAG, set)
                .apply();
        return Observable.empty();
    }
}

