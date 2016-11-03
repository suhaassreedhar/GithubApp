package com.example.githubapp.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.githubapp.R;
import com.example.githubapp.base.BaseAdapter;
import com.example.githubapp.entity.response.repos.RepositoryContentBean;
import com.example.githubapp.listener.OnDirItemClickListener;
import com.example.githubapp.view.viewholder.FileDirViewHolder;

public class FileDirRecyclerViewAdapter extends BaseAdapter<RepositoryContentBean, FileDirViewHolder> {
    private final String TAG = getClass().getName();

    private OnDirItemClickListener mOnDirItemClickListener;

    public FileDirRecyclerViewAdapter(Context context) {
        super(context);
    }

    public void setOnItemClickListener(OnDirItemClickListener listener) {
        mOnDirItemClickListener = listener;
    }

    @Override
    public FileDirViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View mView = LayoutInflater.from(getContext()).inflate(R.layout.item_recyclerview_filedir, parent, false);
        FileDirViewHolder viewHolder = new FileDirViewHolder(mView);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(FileDirViewHolder holder, int position) {
        final RepositoryContentBean data = getData().get(position);
        String[] strs = data.getPath().split("/");
        holder.mFileNameTV.setText(strs[strs.length - 1]);
        if (data.getType().equals("dir")) {
            holder.mTypeIV.setImageResource(R.mipmap.ic_dir);
        } else if (data.getType().equals("file") || data.getType().equals("symlink")) {
            holder.mTypeIV.setImageResource(R.mipmap.ic_file);
        }
        holder.mLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mOnDirItemClickListener != null) {
                    mOnDirItemClickListener.onClick(v, data.getPath(), data.getType(), data.getSha());
                }
            }
        });
    }
}
