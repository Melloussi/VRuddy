package com.app.vruddy.Adapter;

import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.app.vruddy.Objects.VideoObject;
import com.app.vruddy.R;
import com.app.vruddy.database.Favorite.FavoriteIndex;
import com.facebook.shimmer.Shimmer;
import com.facebook.shimmer.ShimmerDrawable;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import jp.wasabeef.picasso.transformations.CropCircleTransformation;

public class RelatedVideoAdapter extends RecyclerView.Adapter<RelatedVideoAdapter.MyHolder>  {

//    private ArrayList<String>videoId = new ArrayList<>();
//    private ArrayList<String>thumbnailUrl = new ArrayList<>();
//    private ArrayList<String>videoTitle = new ArrayList<>();
//    private ArrayList<String>viewsInfo = new ArrayList<>();
//    private ArrayList<String>showTime = new ArrayList<>();
//    private ArrayList<String>dateInfo = new ArrayList<>();
//    private ArrayList<String>channelName = new ArrayList<>();
//    private ArrayList<String>channelImgUrl = new ArrayList<>();
//    private ArrayList<Integer>heartIcon = new ArrayList<>();
//    private ArrayList<Integer>DownloadIcon = new ArrayList<>();
//    private ArrayList<Integer>badgeIcon = new ArrayList<>();
    private ArrayList<Boolean>isLive = new ArrayList<>();

    private static FavoriteIndex favoriteIndex;
    private List<VideoObject> videoObjectList;

    //interface
    onItemClickListener onItemClickListener;

    public interface onItemClickListener{
        void onHeartClick(int position);

        void onItemClick(int position);

        void onDownloadClick(int position);
    }
    public void setOnItemClickListener(onItemClickListener listener){
        onItemClickListener = listener;
    }

    public RelatedVideoAdapter(List<VideoObject> localVideoObjectsList, ArrayList<Boolean> localIsLive) {
        videoObjectList = localVideoObjectsList;
        isLive = localIsLive;
    }

    public static class MyHolder extends RecyclerView.ViewHolder{

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

            thumbnail = itemView.findViewById(R.id.videoListThumbnail);
            channelImg = itemView.findViewById(R.id.channelImg);
            heart = itemView.findViewById(R.id.heart);
            download = itemView.findViewById(R.id.download);
            badge = itemView.findViewById(R.id.badge);

            title = itemView.findViewById(R.id.videoTitle);
            views = itemView.findViewById(R.id.views);
            date = itemView.findViewById(R.id.date);
            channelName = itemView.findViewById(R.id.channelname);
            showTime = itemView.findViewById(R.id.showTime);
            live = itemView.findViewById(R.id.live);
            liveText = itemView.findViewById(R.id.isLiveNow);



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

    @NonNull
    @Override
    public MyHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.ralated_videos, parent, false);
        MyHolder myHolder = new MyHolder(view, onItemClickListener);
        return myHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull MyHolder holder, int position) {

        if(!videoObjectList.get(position).getVideoId().isEmpty()) {

            System.out.println("----------- position: "+position);
            System.out.println("----------- Thumbnail: "+videoObjectList.get(position).getThumbnail());
            System.out.println("----------- Size of watch videoObjectList: "+videoObjectList.size());

            Picasso.get()
                    .load(videoObjectList.get(position).getThumbnail())
                    .into(holder.thumbnail);

            Picasso.get()
                    .load(videoObjectList.get(position).getChannelThumbnail())
                    .transform(new CropCircleTransformation())
                    .into(holder.channelImg);

            //set icons
            holder.download.setImageResource(R.drawable.ic_download);

            if (videoObjectList.get(position).isBadge()) {
                holder.badge.setImageResource(R.drawable.ic_badge);
            } else {
                holder.badge.setImageResource(0);
            }

            if (favoriteIndex.Id(videoObjectList.get(position).getVideoId())) {
                holder.heart.setImageResource(R.drawable.ic_red_heart);
            } else {
                holder.heart.setImageResource(R.drawable.ic_baseline_favorite_24);
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

            //check if video is live or not, to hide time and date
//            if (isLive.get(position) == true) {
//                holder.showTime.setVisibility(View.INVISIBLE);
//                holder.date.setVisibility(View.INVISIBLE);
//                holder.live.setVisibility(View.VISIBLE);
//                holder.liveText.setVisibility(View.VISIBLE);
//
//            }

            holder.title.setText(videoObjectList.get(position).getTitle());
            holder.views.setText(videoObjectList.get(position).getViews());
            holder.date.setText(videoObjectList.get(position).getDate());
            holder.channelName.setText(videoObjectList.get(position).getBy());
            holder.showTime.setText(videoObjectList.get(position).getTime());
        }



    }

    @Override
    public int getItemCount() {
        return videoObjectList.size();
    }
}
