package com.example.wayup.adapter;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.wayup.LoadMore;
import com.example.wayup.R;
import com.example.wayup.model.Company;
import com.example.wayup.model.Job;

import java.util.List;

public class CompanyAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private final int VIEW_TYPE_ITEM = 0, VIEW_TYPE_LOADING = 1;
    LoadMore loadMore;
    boolean isLoading;
    Activity activity;
    List<Company> items;
    int visibleThreshold = 5;
    int lastVisibleItem, totalItemCount;
    private static OnClickItemListener clickItemListener;

    public interface OnClickItemListener {
        void onItemClick(View view, int position);
    }

    public void setOnItemClickListener(OnClickItemListener listener) {
        this.clickItemListener = listener;
    }

    public CompanyAdapter(RecyclerView recyclerView, Activity activity, List<Company> items) {
        this.activity = activity;
        this.items = items;

        final LinearLayoutManager linearLayoutManager = (LinearLayoutManager) recyclerView.getLayoutManager();
        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                totalItemCount = linearLayoutManager.getItemCount();
                lastVisibleItem = linearLayoutManager.findLastVisibleItemPosition();
                if (!isLoading && totalItemCount <= (lastVisibleItem + visibleThreshold)) {
                    if (loadMore != null)
                        loadMore.onLoadMore();
                    isLoading = true;
                }

            }
        });

        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                totalItemCount = linearLayoutManager.getItemCount(); // Lấy tổng số lượng item đang có
                lastVisibleItem = linearLayoutManager.findLastVisibleItemPosition(); // Lấy vị trí của item cuối cùng
                if (!isLoading && totalItemCount <= (lastVisibleItem + visibleThreshold)) // Nếu không phải trạng thái loading và tổng số lượng item bé hơn hoặc bằng vị trí item cuối + số lượng item tối đa hiển thị
                {
                    if (loadMore != null)
                        loadMore.onLoadMore(); // Gọi interface Loadmore
                    isLoading = true;
                }

            }
        });
    }


    @Override
    public int getItemViewType(int position) {
        return items.get(position) == null ? VIEW_TYPE_LOADING : VIEW_TYPE_ITEM;
    }

    public void setLoadMore(LoadMore loadMore) {
        this.loadMore = loadMore;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == VIEW_TYPE_ITEM) {
            View view = LayoutInflater.from(activity)
                    .inflate(R.layout.item_company, parent, false);
            return new ItemViewHolder(view);
        } else if (viewType == VIEW_TYPE_LOADING) {
            View view = LayoutInflater.from(activity)
                    .inflate(R.layout.item_loading, parent, false);
            return new LoadingViewHolder(view);
        }
        return null;
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {

        if (holder instanceof ItemViewHolder) {
            Company item = items.get(position);
            ItemViewHolder viewHolder = (ItemViewHolder) holder;
            viewHolder.tvName.setText(item.getName());
            viewHolder.tvSmallInfo.setText(item.getTitle());
            Glide.with(activity).load(item.getThumbnail()).into(viewHolder.imLogo);
            Glide.with(activity).load(item.getImage()).into(viewHolder.imThumbnail);
        } else if (holder instanceof LoadingViewHolder) {
            LoadingViewHolder loadingViewHolder = (LoadingViewHolder) holder;
            loadingViewHolder.progressBar.setIndeterminate(true);
        }

    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    public void setLoaded() {
        isLoading = false;
    }

    class LoadingViewHolder extends RecyclerView.ViewHolder {

        public ProgressBar progressBar;

        public LoadingViewHolder(View itemView) {
            super(itemView);
            progressBar = itemView.findViewById(R.id.progressBar);
        }
    }

    class ItemViewHolder extends RecyclerView.ViewHolder {

        public TextView tvName, tvSmallInfo;
        public ImageView imLogo, imThumbnail;

        public ItemViewHolder(final View itemView) {
            super(itemView);
            tvName = itemView.findViewById(R.id.tvNameItemCompany);
            tvSmallInfo = itemView.findViewById(R.id.tvSmallInfoItemCompany);
            imLogo = itemView.findViewById(R.id.imLogoItemCompany);
            imThumbnail = itemView.findViewById(R.id.imThumbnail);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (clickItemListener != null) {
                        clickItemListener.onItemClick(itemView, getLayoutPosition());
                    }
                }
            });
        }
    }
}
