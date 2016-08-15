package com.dalexiv.rssreader.presentation.ui.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import com.dalexiv.rssreader.R;
import com.dalexiv.rssreader.domain.RssViewItem;
import com.dalexiv.rssreader.presentation.di.FragmentInjectors;
import com.dalexiv.rssreader.presentation.presenters.PresenterRssList;
import com.dalexiv.rssreader.presentation.ui.adapters.RssListAdapter;
import com.dalexiv.rssreader.presentation.ui.views.RssListView;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import icepick.Icepick;
import icepick.State;

/**
 * Created by dalexiv on 8/13/16.
 */

public class FragmentRssList extends BaseFragment implements RssListView {
    @BindView(R.id.rssRecycler) RecyclerView recyclerView;
    @BindView(R.id.coordinator) CoordinatorLayout coordinatorLayout;
    @BindView(R.id.progressBar) ProgressBar progressBar;
    @BindView(R.id.fab) FloatingActionButton fab;
    RssListAdapter adapter;

    @State
    int firstRecyclerViewVisibleItem;

    @Inject
    PresenterRssList presenterRssList;

    public static FragmentRssList newInstance() {
        return new FragmentRssList();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_rss_list, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        FragmentInjectors.inject(this);

        fab.setOnClickListener(fab -> presenterRssList.handleFabClick());
        setupRecycler();
        presenterRssList.bindView(this);
    }

    @Override
    public void onViewStateRestored(@Nullable Bundle savedInstanceState) {
        super.onViewStateRestored(savedInstanceState);
        Icepick.restoreInstanceState(this, savedInstanceState);

    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        firstRecyclerViewVisibleItem =
                ((LinearLayoutManager) recyclerView.getLayoutManager())
                        .findFirstCompletelyVisibleItemPosition();
        Icepick.saveInstanceState(this, outState);
    }

    private void setupRecycler() {
        adapter = new RssListAdapter(new ArrayList<>(), getActivity().getApplicationContext(),
                rssViewItem -> presenterRssList.handleRecyclerClick(rssViewItem));
        LinearLayoutManager manager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(manager);
        recyclerView.setAdapter(adapter);
        recyclerView.setHasFixedSize(true);
        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                if (dy > 0)
                    fab.hide();
                else if (dy < 0)
                    fab.show();
            }
        });

        // Restore old position
        recyclerView.getLayoutManager()
                .scrollToPosition(firstRecyclerViewVisibleItem);
    }

    @Override
    public void setRefreshing(boolean isRefreshing) {
        if (isRefreshing) {
            progressBar.setVisibility(View.VISIBLE);
            recyclerView.setVisibility(View.GONE);
        } else {
            progressBar.setVisibility(View.GONE);
            recyclerView.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void notifyUser(String message) {
        Snackbar.make(coordinatorLayout, message, Snackbar.LENGTH_LONG).show();
    }

    @Override
    public void displayItems(List<RssViewItem> items) {
        adapter.addItems(items);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        presenterRssList.unbindView(this);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        presenterRssList.onFragmentResult(requestCode, resultCode, data);
    }
}
