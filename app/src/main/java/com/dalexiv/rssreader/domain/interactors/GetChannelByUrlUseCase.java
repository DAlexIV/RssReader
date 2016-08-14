package com.dalexiv.rssreader.domain.interactors;


import com.dalexiv.rssreader.data.RssChannel;
import com.dalexiv.rssreader.domain.parsers.ChannelParser;

import rx.Observable;
import rx.Scheduler;
import rx.schedulers.Schedulers;

/**
 * Created by dalexiv on 8/14/16.
 */

public class GetChannelByUrlUseCase extends UseCase<RssChannel> {
    private String url;
    private ChannelParser parser;

    // TODO add dagger dep there
    public GetChannelByUrlUseCase(ChannelParser parser, Scheduler observeScheduler, String url) {
        super(Schedulers.io(), observeScheduler);
        this.url = url;
        this.parser = parser;
    }

    @Override
    protected Observable<RssChannel> buildUseCaseObservable() {
        return Observable.fromCallable(() -> parser.parse(url));
    }
}
