package com.app.vruddy.Adapter;

import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.app.vruddy.R;
import com.app.vruddy.Objects.VideoObject;
import com.app.vruddy.database.Favorite.FavoriteIndex;
import com.facebook.shimmer.Shimmer;
import com.facebook.shimmer.ShimmerDrawable;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import jp.wasabeef.picasso.transformations.CropCircleTransformation;

public class HomeVideoAdapter extends RecyclerView.Adapter<HomeVideoAdapter.MyHolder> {

    private List<VideoObject> videoObjectList = new ArrayList<>();
    private static FavoriteIndex favoriteIndex;

    //interface
    onItemClickListener onItemClickListener;

    public interface onItemClickListener{
        void onItemClick(int position);
        void onDownloadClick(int position);
        void onHeartClick(int position);
    }
    public void setOnItemClickListener(onItemClickListener listener){
        onItemClickListener = listener;
    }

    public HomeVideoAdapter(List<VideoObject> videoObjectList){
        this.videoObjectList = videoObjectList;
    }



    @NonNull
    @Override
    public MyHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.home_video, parent, false);
        MyHolder myHolder = new MyHolder(view, onItemClickListener);
        return myHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull MyHolder holder, int position) {

        Picasso.get()
                .load(videoObjectList.get(position).getThumbnail())
                .into(holder.thumbnail);

        Picasso.get()
                .load(videoObjectList.get(position).getChannelThumbnail())
                .transform(new CropCircleTransformation())
                .into(holder.channelImg);

        //set icons
        //holder.heart.setImageResource(R.drawable.ic_baseline_favorite_24);
        holder.download.setImageResource(R.drawable.ic_download);

        if (favoriteIndex.Id(videoObjectList.get(position).getVideoId())) {
            holder.heart.setImageResource(R.drawable.ic_red_heart);
        } else {
            holder.heart.setImageResource(R.drawable.ic_baseline_favorite_24);
        }

        if(videoObjectList.get(position).isBadge()){
            holder.badge.setImageResource(R.drawable.ic_badge);
        }else {
            holder.badge.setImageResource(0);
        }
        //Shimmer
        Shimmer shimmer = new Shimmer.ColorHighlightBuilder()
                .setBaseColor(Color.parseColor("#696969"))
                .setBaseAlpha(1)
                .setHighlightColor(Color.parseColor("#adaaaa"))
                .setHighlightAlpha(1)
                .setDropoff(50)
                .build();
        ShimmerDrawable shimmerDrawable = new ShimmerDrawable();
        shimmerDrawable.setShimmer(shimmer);


        holder.title.setText(videoObjectList.get(position).getTitle());
        holder.views.setText(videoObjectList.get(position).getViews());
        holder.date.setText(videoObjectList.get(position).getDate());
        holder.channelName.setText(videoObjectList.get(position).getBy());
        holder.showTime.setText(videoObjectList.get(position).getTime());
    }

    @Override
    public int getItemCount() {
        return videoObjectList.size();
    }

    public class MyHolder extends RecyclerView.ViewHolder {
        public ImageView thumbnail;
        public ImageView channelImg;
        public ImageView heart;
        public ImageView download;
        public ImageView badge;
        public TextView title;
        public TextView views;
        public TextView date;
        public TextView channelName;
        public TextView showTime;
        public TextView live;
        public TextView liveText;
        public MyHolder(@NonNull View itemView, onItemClickListener listener) {
            super(itemView);


            thumbnail = itemView.findViewById(R.id.home_video_thumbnails);
            channelImg = itemView.findViewById(R.id.channel_img);
            heart = itemView.findViewById(R.id.heart_icon);
            download = itemView.findViewById(R.id.download_icon);
            badge = itemView.findViewById(R.id.badge_icon);

            title = itemView.findViewById(R.id.title_info);
            views = itemView.findViewById(R.id.views_info);
            date = itemView.findViewById(R.id.date_info);
            channelName = itemView.findViewById(R.id.channel_name);
            showTime = itemView.findViewById(R.id.time_info);
            live = itemView.findViewById(R.id.live_info);
            liveText = itemView.findViewById(R.id.live_video_info);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if(listener != null){
                        int position =  getAdapterPosition();
                        if(position != RecyclerView.NO_POSITION){
                            listener.onItemClick(position);
                        }
                    }
                }
            });
            download.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if(listener != null){
                        int position = getAdapterPosition();
                        if(position != RecyclerView.NO_POSITION){
                            listener.onDownloadClick(position);
                        }
                    }
                }
            });
            heart.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if(listener != null){
                        int position = getAdapterPosition();
                        if(position != RecyclerView.NO_POSITION){
                            listener.onHeartClick(position);
                        }
                    }
                }
            });
        }
    }
}
