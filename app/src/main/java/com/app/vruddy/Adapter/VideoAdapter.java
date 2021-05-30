package com.app.vruddy.Adapter;

import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.PopupMenu;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.RecyclerView;


import com.app.vruddy.Adapter.DiffUtil.VideoDiffUtil;
import com.app.vruddy.R;
import com.app.vruddy.database.Video.Video;
import com.google.android.material.imageview.ShapeableImageView;
import com.squareup.picasso.Picasso;


import java.util.ArrayList;
import java.util.List;

public class VideoAdapter extends RecyclerView.Adapter<VideoAdapter.VideoHolder> {
    private List<Video> videos = new ArrayList<>();
    private onOptionClickListener onOptionClickListener;

    public interface onOptionClickListener{
        void onOptionClick(int position, String video_id, int action);
    }

    public void setOnOptionClickListener(VideoAdapter.onOptionClickListener onOptionClickListener) {
        this.onOptionClickListener = onOptionClickListener;
    }

    @NonNull
    @Override
    public VideoHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.video_modele, parent, false);
        return new VideoAdapter.VideoHolder(view, onOptionClickListener);
    }

    @Override
    public void onBindViewHolder(@NonNull VideoHolder holder, int position) {
        Video current = videos.get(position);

        holder.title.setText(current.getTitle());
        holder.channelName.setText(current.getBy());
        holder.timeLine.setText(current.getTime_line());

        Picasso.get()
                .load(current.getThumbnail())
                .into(holder.thumbnail);
        holder.option.setImageResource(R.drawable.ic_option_black);
    }

    @Override
    public int getItemCount() {
        return videos.size();
    }

    public class VideoHolder extends RecyclerView.ViewHolder{
        private TextView title;
        private TextView channelName;
        private TextView timeLine;
        private ShapeableImageView thumbnail;
        private ImageView option;
        public VideoHolder(@NonNull View itemView, onOptionClickListener onOptionClickListener) {
            super(itemView);
            title = itemView.findViewById(R.id.video_fileTitle);
            channelName = itemView.findViewById(R.id.video_channel_name);
            timeLine = itemView.findViewById(R.id.video_file_time_info);
            thumbnail = itemView.findViewById(R.id.video_thumbnail);
            option = itemView.findViewById(R.id.video_option);

            option.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (onOptionClickListener != null) {
                        int position = getAdapterPosition();
                        Video current = videos.get(position);
                        if (position != RecyclerView.NO_POSITION) {
                            PopupMenu popupMenu = new PopupMenu(view.getContext(), view);
                            popupMenu.inflate(R.menu.music_menu);
                            popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                                @Override
                                public boolean onMenuItemClick(MenuItem item) {
                                    switch (item.getItemId()) {
                                        case R.id.music_play:
                                            System.out.println("Play");
                                            onOptionClickListener.onOptionClick(position, current.getVideo_id(), R.id.music_play);
                                            return true;
                                        case R.id.music_open:
                                            System.out.println("Open in Folder");
                                            onOptionClickListener.onOptionClick(position, current.getVideo_id(), R.id.music_open);
                                            return true;
                                        case R.id.music_delete:
                                            System.out.println("Delete Music");
                                            onOptionClickListener.onOptionClick(position, current.getVideo_id(), R.id.music_delete);
                                            return true;
                                        default:
                                            return false;
                                    }
                                }
                            });
                            popupMenu.show();
                        }
                    }
                }
            });
        }
    }

    public void updateRecycler(List<Video> newVideo){
        VideoDiffUtil videoDiffUtil = new VideoDiffUtil(videos, newVideo);
        DiffUtil.DiffResult diffUtil = DiffUtil.calculateDiff(videoDiffUtil);
        videos = newVideo;
        diffUtil.dispatchUpdatesTo(this);
    }
    public static boolean areContentsTheSame(Video oldData, Video newData) {
        if (oldData.getId() != newData.getId()){
            return false;
        }if (oldData.getTitle() != newData.getTitle()){
            return false;
        }if (oldData.getBy() != newData.getBy()){
            return false;
        }if (oldData.getTime_line() != newData.getTime_line()){
            return false;
        }if (oldData.isBadge() != newData.isBadge()){
            return false;
        }if (oldData.getThumbnail() != newData.getThumbnail()){
            return false;
        }if (oldData.getVideo_id() != newData.getVideo_id()){
            return false;
        }
        return true;
    }
}
