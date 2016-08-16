package com.dalexiv.rssreader.presentation.presenters;

import android.app.Activity;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.util.Log;

import com.dalexiv.rssreader.data.RssChannel;
import com.dalexiv.rssreader.domain.RssViewItem;
import com.dalexiv.rssreader.domain.interactors.GetChannelByUrlUseCase;
import com.dalexiv.rssreader.domain.interactors.GetChannelsByUrlsUseCase;
import com.dalexiv.rssreader.domain.interactors.GetRssSubsUseCase;
import com.dalexiv.rssreader.domain.interactors.SaveRssSubsUseCase;
import com.dalexiv.rssreader.domain.parsers.ChannelParser;
import com.dalexiv.rssreader.presentation.di.DaggerParserComponent;
import com.dalexiv.rssreader.presentation.ui.activities.ActivityDetailedRssItem;
import com.dalexiv.rssreader.presentation.ui.fragments.DialogNewRssLink;
import com.dalexiv.rssreader.presentation.ui.fragments.FragmentRssList;

import java.util.List;

import javax.inject.Inject;

import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;

/**
 * Created by dalexiv on 8/14/16.
 */

public class PresenterRssList extends Presenter<FragmentRssList> {
    private static final int REQUEST_LINK = 1;
    private final String TAG = PresenterRssList.class.getSimpleName();
    @Inject
    ChannelParser parser;

    private List<String> rssLinks;
    private List<RssViewItem> currentViewItems;

    public PresenterRssList() {
        DaggerParserComponent.builder().build().inject(this);
    }

    @Override
    public void bindView(@NonNull FragmentRssList view) {
        super.bindView(view);
        if (currentViewItems == null)
            fetchChannels();
        else
            view().displayItems(currentViewItems);
    }

    private void fetchChannels() {
        view().setRefreshing(true);

        // Load them from prefs
        if (rssLinks == null || rssLinks.isEmpty()) {
            new GetRssSubsUseCase(view().getActivity())
                    .getObservable()
                    .doOnNext(list -> rssLinks = list)
                    .toBlocking()
                    .first();
        }

        new GetChannelsByUrlsUseCase(parser,
                AndroidSchedulers.mainThread(),
                rssLinks)
                .getObservable()
                .compose(transformToList())
                .subscribe(rssViewItems -> {
                            view().setRefreshing(false);
                            view().displayItems(rssViewItems);
                            currentViewItems = rssViewItems;
                        },
                        error -> {
                            Log.e(TAG, "loading all rss::", error);
                            view().notifyUser("Bad connection, retry later");
                        });
    }

    private void fetchChannel(String url) {
        view().setRefreshing(true);

        if (rssLinks.contains(url)) {
            view().setRefreshing(false);
            view().notifyUser("You have already subscribed to " + url);
            return;
        }

        new GetChannelByUrlUseCase(parser, AndroidSchedulers.mainThread(), url)
                .getObservable()
                .compose(transformToList())
                .subscribe(rssViewItems -> {
                    view().setRefreshing(false);

                    rssLinks.add(url);
                    updateCurrentItems(rssViewItems);
                    view().displayItems(currentViewItems);
                    new SaveRssSubsUseCase(view().getActivity(), rssLinks)
                            .getObservable()
                            .toBlocking();
                }, error -> {
                    view().setRefreshing(false);
                    view().displayItems(currentViewItems);

                    Log.e(TAG, "loading rss::", error);
                    view().notifyUser("This rss feed is currently not supported, try another one");
                });
    }

    public void handleFabClick() {
        DialogNewRssLink dialog = DialogNewRssLink.newInstance();
        dialog.setTargetFragment(view(), REQUEST_LINK);
        dialog.show(view().getFragmentManager(), dialog.getClass().getSimpleName());
    }

    public void handleRecyclerClick(RssViewItem rssViewItem) {
        final Intent intent = ActivityDetailedRssItem.makeIntent(view().getContext(), rssViewItem);
        view().getActivity().startActivity(intent);
    }

    public void onFragmentResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == Activity.RESULT_OK) {
            switch (requestCode) {
                case REQUEST_LINK:
                    fetchChannel(data.getStringExtra(DialogNewRssLink.TAG_LINK_ENTERED));
            }
        }
    }

    private Observable.Transformer<RssChannel, List<RssViewItem>> transformToList() {
        return tObservable -> tObservable.flatMap(channel -> Observable.zip(
                Observable.from(channel.getItems()),
                Observable.just(channel.getRssData().getImageLink()).repeat(),
                RssViewItem::create))
                .sorted((rssViewItem1, rssViewItem2) ->
                        rssViewItem1.rssItem().getTimestamp()
                                < rssViewItem2.rssItem().getTimestamp() ? 1 : -1)
                .toList();
    }

    private void updateCurrentItems(List<RssViewItem> viewItems) {
        Observable.merge(Observable.from(currentViewItems),
                Observable.from(viewItems))
                .sorted((rssViewItem1, rssViewItem2) ->
                        rssViewItem1.rssItem().getTimestamp()
                                < rssViewItem2.rssItem().getTimestamp() ? 1 : -1)
                .toList()
                .subscribe(list -> currentViewItems = list);
    }

    @Override
    public void unbindView(@NonNull FragmentRssList view) {
        super.unbindView(view);
    }
}
