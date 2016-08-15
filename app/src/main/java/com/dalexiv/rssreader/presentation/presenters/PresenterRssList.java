package com.dalexiv.rssreader.presentation.presenters;

import android.app.Activity;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.util.Log;

import com.dalexiv.rssreader.domain.RssViewItem;
import com.dalexiv.rssreader.domain.interactors.GetChannelsByUrlsUseCase;
import com.dalexiv.rssreader.domain.parsers.ChannelParser;
import com.dalexiv.rssreader.presentation.di.DaggerParserComponent;
import com.dalexiv.rssreader.presentation.ui.activities.ActivityDetailedRssItem;
import com.dalexiv.rssreader.presentation.ui.fragments.DialogNewRssLink;
import com.dalexiv.rssreader.presentation.ui.fragments.FragmentRssList;

import java.util.ArrayList;
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

    private List<String> rssLinks = new ArrayList<String>() {{
        add("http://rss.cnn.com/rss/edition.rss");
        add("http://feeds.bbci.co.uk/news/rss.xml");
    }};

    public PresenterRssList() {
        DaggerParserComponent.builder().build().inject(this);
    }

    @Override
    public void bindView(@NonNull FragmentRssList view) {
        super.bindView(view);
        fetchChannels();
    }

    private void fetchChannels() {
        view().setRefreshing(true);
        new GetChannelsByUrlsUseCase(parser,
                AndroidSchedulers.mainThread(),
                rssLinks)
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
                        error -> {
                            Log.e(TAG, "loading rss::", error);
                            view().notifyUser("Bad connection, retry later");
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
                    rssLinks.add(data.getStringExtra(DialogNewRssLink.TAG_LINK_ENTERED));
                    fetchChannels();
            }
        }
    }
}
