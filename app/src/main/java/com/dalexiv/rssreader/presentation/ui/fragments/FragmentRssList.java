package com.dalexiv.rssreader.presentation.ui.fragments;

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

/**
 * Created by dalexiv on 8/13/16.
 */

public class FragmentRssList extends BaseFragment implements RssListView {
    @BindView(R.id.rssRecycler) RecyclerView recyclerView;
    @BindView(R.id.coordinator) CoordinatorLayout coordinatorLayout;
    @BindView(R.id.progressBar) ProgressBar progressBar;
    RssListAdapter adapter;

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

        initFab(view);
        setupRecycler();
        presenterRssList.bindView(this);
    }

    private void initFab(View view) {
        FloatingActionButton fab = (FloatingActionButton) view.findViewById(R.id.fab);
        fab.setOnClickListener(view1 -> Snackbar.make(view1, "Replace with your own action", Snackbar.LENGTH_LONG)
                .setAction("Action", null).show());
    }

    private void setupRecycler() {
        adapter = new RssListAdapter(new ArrayList<>(), getActivity().getApplicationContext());
        LinearLayoutManager manager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(manager);
        recyclerView.setAdapter(adapter);
    }

    @Override
    public void setRefreshing(boolean isRefreshing) {
        if (isRefreshing) {
            progressBar.setVisibility(View.VISIBLE);
            recyclerView.setVisibility(View.GONE);
        }
        else {
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
    public void addItemsToDisplayed(List<RssViewItem> items) {

    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        presenterRssList.unbindView(this);
    }
}
