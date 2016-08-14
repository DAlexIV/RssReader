package com.dalexiv.rssreader.presentation.ui.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.dalexiv.rssreader.R;
import com.dalexiv.rssreader.data.RssItem;
import com.dalexiv.rssreader.domain.RssViewItem;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by dalexiv on 8/14/16.
 */

public class RssListAdapter extends RecyclerView.Adapter<RssListAdapter.RssItemViewHolder> {
    private List<RssViewItem> data;
    private Context context;
    private SimpleDateFormat formatter;

    public RssListAdapter(List<RssViewItem> data, Context context) {
        this.data = data;
        this.context = context;
        formatter = new SimpleDateFormat("d MMMM HH:mm");
    }

    @Override
    public RssListAdapter.RssItemViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        final View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.rss_item, parent, false);
        return new RssItemViewHolder(view);
    }

    @Override
    public void onBindViewHolder(RssItemViewHolder holder, int position) {
        final RssItem rssItem = data.get(position).rssItem();
        holder.textViewTime.setText(formatter.format(new Date(rssItem.getTimestamp())));
        holder.textViewTitle.setText(rssItem.getRssData().getTitle());

        final String smallImageLink = rssItem.getRssData().getImageLink();
        final String channelImageLink = data.get(position).channelImageLink();
        Glide.with(context)
                .load(smallImageLink == null ? channelImageLink : smallImageLink)
                .into(holder.imageView);
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    public void addItems(List<RssViewItem> items) {
        int oldSize = getItemCount();
        data.addAll(items);
        notifyItemRangeInserted(oldSize, getItemCount());
    }

    @Override
    public boolean onFailedToRecycleView(RssItemViewHolder holder) {
        return true;
    }

    public static class RssItemViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.rssImage) ImageView imageView;
        @BindView(R.id.rssTitle) TextView textViewTitle;
        @BindView(R.id.rssTime) TextView textViewTime;

        public RssItemViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
