package com.dalexiv.rssreader.presentation.ui.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.dalexiv.rssreader.R;
import com.dalexiv.rssreader.domain.RssViewItem;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.dalexiv.rssreader.presentation.ui.ImageUtils.loadRssItemImage;


/**
 * Created by dalexiv on 8/14/16.
 */

public class RssListAdapter extends RecyclerView.Adapter<RssListAdapter.RssItemViewHolder> {
    private List<RssViewItem> data;
    private Context context;
    private IRssItemClicked iRssItemClicked;
    private SimpleDateFormat formatter;

    public RssListAdapter(List<RssViewItem> data, Context context, IRssItemClicked iRssItemClicked) {
        this.data = data;
        this.context = context;
        this.iRssItemClicked = iRssItemClicked;
        formatter = new SimpleDateFormat("d MMMM HH:mm");
        setHasStableIds(true);
    }

    @Override
    public RssListAdapter.RssItemViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        final View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_rss, parent, false);
        final RssItemViewHolder rssItemViewHolder = new RssItemViewHolder(view);
        rssItemViewHolder.itemView.setOnClickListener(view1
                -> iRssItemClicked.onClick(data.get(rssItemViewHolder.getAdapterPosition())));
        return rssItemViewHolder;
    }

    @Override
    public void onBindViewHolder(RssItemViewHolder holder, int position) {
        final RssViewItem rssViewItem = data.get(position);
        holder.textViewTime.setText(formatter.format(
                new Date(rssViewItem.rssItem().getTimestamp())));
        holder.textViewTitle.setText(rssViewItem.rssItem().getRssData().getTitle());
        loadRssItemImage(context, holder.imageView, rssViewItem);
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    @Override
    public long getItemId(int position) {
        return data.get(position).rssItem().getTimestamp();
    }

    public void addItems(List<RssViewItem> items) {
        data = items;
        notifyDataSetChanged();
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
