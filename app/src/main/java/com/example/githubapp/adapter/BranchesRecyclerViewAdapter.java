package com.example.githubapp.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.githubapp.R;
import com.example.githubapp.base.BaseAdapter;
import com.example.githubapp.entity.response.repos.BranchBean;
import com.example.githubapp.view.viewholder.BranchViewHolder;

public class BranchesRecyclerViewAdapter extends BaseAdapter<BranchBean, BranchViewHolder> {
    private final String TAG = getClass().getName();

    private String defaultBranch;

    public BranchesRecyclerViewAdapter(Context context) {
        super(context);
    }

    public BranchesRecyclerViewAdapter(Context context, String defaultBranch) {
        this(context);
        this.defaultBranch = defaultBranch;
    }

    @Override
    public BranchViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(getContext()).inflate(R.layout.item_recyclerview_branch, parent, false);
        BranchViewHolder viewHolder = new BranchViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(BranchViewHolder holder, int position) {
        final BranchBean data = getData().get(position);
        if (data.getName().equals(defaultBranch)) {
            holder.mIsDefaultTV.setVisibility(View.VISIBLE);
            holder.mIsDefaultTV.setText("Default");
        }
        holder.mNameTV.setText(data.getName());
    }
}
