package com.example.githubapp.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.githubapp.R;
import com.example.githubapp.entity.response.repos.SingleCommitBean;
import com.example.githubapp.utils.ImageUtil;
import com.example.githubapp.utils.TextUtil;
import com.example.githubapp.view.viewholder.CommitViewHolder;

public class CommitsRecyclerViewAdapter extends LoadMoreRecyclerViewAdapter<SingleCommitBean, CommitViewHolder> {
    private final String TAG = getClass().getName();

    public CommitsRecyclerViewAdapter(Context context) {
        super(context);
    }

    @Override
    public CommitViewHolder createContentViewHolder(ViewGroup parent, int viewType) {
        View mView = LayoutInflater.from(getContext()).inflate(R.layout.item_recyclerview_commit, parent, false);
        CommitViewHolder viewHolder = new CommitViewHolder(mView);
        return viewHolder;
    }

    @Override
    public void bindContentViewHolder(CommitViewHolder holder, final int position) {
        final SingleCommitBean data = getData().get(position);
        if (data.getAuthor() != null) {
            ImageUtil.loadAvatarImageFromUrl(getContext(), data.getAuthor().getAvatar_url(),
                    holder.mAvatarIV);
            holder.mLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (getItemClickedListener() != null) {
                        getItemClickedListener().onItemClicked(v, position);
                    }
                }
            });
        }
        if (data.getCommit() != null) {
            holder.mUsernameTV.setText(data.getCommit().getCommitter().getName());
            holder.mMessageTV.setText(data.getCommit().getMessage());
            holder.mTimeTV.setText(TextUtil.timeConverter(data.getCommit().getCommitter().getDate()));
            holder.mCommentCountTV.setText(String.valueOf(data.getCommit().getComment_count()));
        }
    }
}
