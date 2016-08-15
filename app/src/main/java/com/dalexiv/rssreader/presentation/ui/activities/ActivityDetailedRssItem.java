package com.dalexiv.rssreader.presentation.ui.activities;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;

import com.dalexiv.rssreader.R;
import com.dalexiv.rssreader.domain.RssViewItem;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.dalexiv.rssreader.presentation.ui.ImageUtils.loadBigRssItemImage;

/**
 * Created by dalexiv on 8/15/16.
 */

public class ActivityDetailedRssItem extends AppCompatActivity {
    private static final String ITEM_TAG = "ITEM_TAG";
    @BindView(R.id.description) TextView textView;
    @BindView(R.id.rssImage) ImageView imageView;
    @BindView(R.id.toolbar) Toolbar toolbar;
    @BindView(R.id.fab) FloatingActionButton fab;
    public static Intent makeIntent(Context context, RssViewItem item) {
        Intent intent = new Intent(context, ActivityDetailedRssItem.class);
        intent.putExtra(ITEM_TAG, item);
        return intent;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rss_item_detailed);
        ButterKnife.bind(this);
        setSupportActionBar(toolbar);
        RssViewItem rssViewItem = getIntent().getParcelableExtra(ITEM_TAG);

        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle(rssViewItem.rssItem().getRssData().getTitle());
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        fab.setOnClickListener(view -> startActivity(new Intent(Intent.ACTION_VIEW,
                Uri.parse(rssViewItem.rssItem().getRssData().getLink()))));

        textView.setText(rssViewItem.rssItem().getRssData().getDescription());
        loadBigRssItemImage(this, imageView, rssViewItem);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
