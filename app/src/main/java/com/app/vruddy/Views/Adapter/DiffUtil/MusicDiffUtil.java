package com.app.vruddy.Views.Adapter.DiffUtil;

import androidx.recyclerview.widget.DiffUtil;

import com.app.vruddy.Views.Adapter.MusicAdapter;
import com.app.vruddy.Data.database.Music.Music;

import java.util.List;

public class MusicDiffUtil extends DiffUtil.Callback{
    private List<Music> oldList;
    private List<Music> newList;

    public MusicDiffUtil(List<Music> oldList, List<Music> newList) {
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
        return MusicAdapter.areContentsTheSame(oldList.get(oldItemPosition), newList.get(newItemPosition));
    }
}
