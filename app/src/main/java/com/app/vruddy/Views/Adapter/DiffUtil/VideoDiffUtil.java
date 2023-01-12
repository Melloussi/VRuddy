package com.app.vruddy.Views.Adapter.DiffUtil;

import androidx.recyclerview.widget.DiffUtil;
import com.app.vruddy.Views.Adapter.VideoAdapter;
import com.app.vruddy.Data.database.Video.Video;

import java.util.List;

public class VideoDiffUtil extends DiffUtil.Callback{
    private List<Video> oldList;
    private List<Video> newList;

    public VideoDiffUtil(List<Video> oldList, List<Video> newList) {
        this.oldList = oldList;
        this.newList = newList;
    }

    @Override
    public int getOldListSize() {
        return oldList.size();
    }

    @Override
    public int getNewListSize() {
        return newList.size();
    }

    @Override
    public boolean areItemsTheSame(int oldItemPosition, int newItemPosition) {
        return oldList.get(oldItemPosition).getVideo_id().equals(newList.get(newItemPosition).getVideo_id());
    }

    @Override
    public boolean areContentsTheSame(int oldItemPosition, int newItemPosition) {
        return VideoAdapter.areContentsTheSame(oldList.get(oldItemPosition), newList.get(newItemPosition));
    }
}
