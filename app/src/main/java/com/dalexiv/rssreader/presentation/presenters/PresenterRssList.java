package com.dalexiv.rssreader.presentation.presenters;

import android.support.annotation.NonNull;
import android.util.Log;

import com.dalexiv.rssreader.domain.RssViewItem;
import com.dalexiv.rssreader.domain.interactors.GetChannelsByUrlsUseCase;
import com.dalexiv.rssreader.domain.parsers.ChannelParser;
import com.dalexiv.rssreader.presentation.di.DaggerParserComponent;
import com.dalexiv.rssreader.presentation.ui.fragments.FragmentRssList;

import java.util.Arrays;

import javax.inject.Inject;

import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;

/**
 * Created by dalexiv on 8/14/16.
 */

public class PresenterRssList extends Presenter<FragmentRssList> {
    private final String TAG = PresenterRssList.class.getSimpleName();
    @Inject
    ChannelParser parser;

    private String testLink1 = "http://rss.cnn.com/rss/edition.rss";
    private String testLink2 = "http://takiedela.ru/feed/";

    public PresenterRssList() {
        DaggerParserComponent.builder().build().inject(this);
    }

    @Override
    public void bindView(@NonNull FragmentRssList view) {
        super.bindView(view);
        view().setRefreshing(true);
        new GetChannelsByUrlsUseCase(parser,
                AndroidSchedulers.mainThread(),
                Arrays.asList(testLink1, testLink2))
                .getObservable()
                .flatMap(channel -> Observable.zip(
                        Observable.from(channel.getItems()),
                        Observable.just(channel.getRssData().getImageLink()).repeat(),
                        RssViewItem::create))
                .sorted((rssViewItem1, rssViewItem2) ->
                        rssViewItem1.rssItem().getTimestamp()
                                < rssViewItem2.rssItem().getTimestamp() ? 1 : -1)
                .toList()
                .subscribe(rssViewItems -> {
                            view().setRefreshing(false);
                            view().displayItems(rssViewItems);
                        },
                        error -> Log.e(TAG, "loading rss::", error));
    }
}
