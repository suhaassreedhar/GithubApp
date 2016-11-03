package com.example.githubapp.view.viewholder;

import android.support.v7.widget.AppCompatTextView;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.LinearLayout;

import com.example.githubapp.R;



public class BranchViewHolder extends RecyclerView.ViewHolder {
    public final LinearLayout mLayout;

    public final AppCompatTextView mNameTV;

    public final AppCompatTextView mIsDefaultTV;

    public BranchViewHolder(View itemView) {
        super(itemView);

        mLayout = (LinearLayout) itemView.findViewById(R.id.branch_item_layout);
        mNameTV = (AppCompatTextView) itemView.findViewById(R.id.branch_name_TV);
        mIsDefaultTV = (AppCompatTextView) itemView.findViewById(R.id.branch_is_default_TV);
    }
}
