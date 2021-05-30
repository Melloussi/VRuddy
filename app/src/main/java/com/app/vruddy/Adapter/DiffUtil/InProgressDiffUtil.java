package com.app.vruddy.Adapter.DiffUtil;

import androidx.recyclerview.widget.DiffUtil;

import com.app.vruddy.database.InProgress.InProgressFile;

import java.util.List;

public class InProgressDiffUtil extends DiffUtil.Callback {
    List<InProgressFile> oldList;
    List<InProgressFile> newList;

    public InProgressDiffUtil(List<InProgressFile> oldList, List<InProgressFile> newList) {
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
        return InProgressFile.areContentsTheSame(oldList.get(oldItemPosition), newList.get(newItemPosition));
    }
}
