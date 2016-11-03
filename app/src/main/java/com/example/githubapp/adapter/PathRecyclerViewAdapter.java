package com.example.githubapp.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.githubapp.R;
import com.example.githubapp.base.BaseAdapter;
import com.example.githubapp.listener.OnDirItemClickListener;
import com.example.githubapp.view.viewholder.PathViewHolder;

import java.util.List;

public class PathRecyclerViewAdapter extends BaseAdapter<String, PathViewHolder> {
    private final String TAG = getClass().getName();

    private OnDirItemClickListener mOnDirItemClickListener;

    public void setOnItemClickListener(OnDirItemClickListener listener) {
        this.mOnDirItemClickListener = listener;
    }

    public PathRecyclerViewAdapter(Context context) {
        super(context);
        getData().add("root system");
    }

    @Override
    public boolean swapAllData(List<String> list) {
        getData().clear();
        boolean result1 = getData().add("root system");
        boolean result2 = getData().addAll(list);
        notifyDataSetChanged();
        return result1 && result2;
    }

    @Override
    public PathViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View mView = LayoutInflater.from(mContext).inflate(R.layout.item_recyclerview_dirpath, parent, false);
        PathViewHolder viewHolder = new PathViewHolder(mView);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(PathViewHolder holder, int position) {
        String file = mData.get(position);
        holder.mPathTV.setText(file);
        final StringBuffer path = new StringBuffer();
        if (mData.size() > 1) {
            for (int i = 1; i <= position; i ++) {
                path.append(mData.get(i));
                if (i != position) {
                    path.append("/");
                }
            }
        }
        holder.mLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mOnDirItemClickListener != null) {
                    mOnDirItemClickListener.onClick(v, path.toString(), null, null);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return mData.size();
    }
}
