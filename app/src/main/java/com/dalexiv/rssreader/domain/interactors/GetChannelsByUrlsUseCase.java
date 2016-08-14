package com.dalexiv.rssreader.domain.interactors;

import com.dalexiv.rssreader.data.RssChannel;
import com.dalexiv.rssreader.domain.parsers.ChannelParser;

import java.util.ArrayList;
import java.util.List;

import rx.Observable;
import rx.Scheduler;
import rx.schedulers.Schedulers;

/**
 * Created by dalexiv on 8/14/16.
 */

public class GetChannelsByUrlsUseCase extends UseCase<RssChannel> {
    private List<GetChannelByUrlUseCase> useCases;

    // TODO add dagger dep there
    public GetChannelsByUrlsUseCase(ChannelParser parser, Scheduler observeScheduler, List<String> urls) {
        super(Schedulers.io(), observeScheduler);
        useCases = new ArrayList<>();
        for (String url : urls) {
            useCases.add(new GetChannelByUrlUseCase(parser, observeScheduler, url));
        }
    }

    @Override
    protected Observable<RssChannel> buildUseCaseObservable() {
        return Observable.merge(
                Observable.from(useCases)
                        .map(GetChannelByUrlUseCase::buildUseCaseObservable));
    }
}
