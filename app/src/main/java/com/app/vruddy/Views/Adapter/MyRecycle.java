package com.app.vruddy.Views.Adapter;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.app.vruddy.R;
import com.app.vruddy.Models.Objects.VideoObject;
import com.app.vruddy.Data.database.Favorite.FavoriteIndex;
import com.facebook.shimmer.Shimmer;
import com.facebook.shimmer.ShimmerDrawable;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import jp.wasabeef.picasso.transformations.CropCircleTransformation;

public class MyRecycle extends RecyclerView.Adapter<MyRecycle.MyViewHolder> {

    private List<VideoObject> videoObjectList = new ArrayList<>();
    private int dd;
    private static FavoriteIndex favoriteIndex;

    public int getDd() {
        return dd;
    }

    public void setDd(int dd) {
        this.dd = dd;
    }

    //    private ImageView heartImg;
    private Context mContext;
    private OnItemClickListener mListener;


    public interface OnItemClickListener {
        //void onItemClick(int position);
        void onHeartClick(int position);

        void onThumbnailClick(int position);

        void onDownloadClick(int position);
    }

    //    public void setOnItemClickListener(OnItemClickListener listener){
//        mListener = listener;
//    }
    public void setOnHeartClickListener(OnItemClickListener listener) {
        mListener = listener;
    }

    public MyRecycle(Context mContext, List<VideoObject> videoObjectList) {
        this.videoObjectList = videoObjectList;
        this.mContext = mContext;
        favoriteIndex = FavoriteIndex.getInstance();
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.video_list, parent, false);
        return new MyViewHolder(view, mListener);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {


        Picasso.get()
                .load(videoObjectList.get(position).getThumbnail())
                .into(holder.thumbnailsImg);

        Picasso.get()
                .load(videoObjectList.get(position).getChannelThumbnail())
                .transform(new CropCircleTransformation())
                .into(holder.channelProfileImg);

        if (favoriteIndex.Id(videoObjectList.get(position).getVideoId())) {
            holder.heartImg.setImageResource(R.drawable.ic_red_heart);
        } else {
            holder.heartImg.setImageResource(R.drawable.ic_baseline_favorite_24);
        }
        holder.downloadImg.setImageResource(R.drawable.ic_download);

        holder.title.setText(videoObjectList.get(position).getTitle());
        holder.time.setText(videoObjectList.get(position).getTime());
        holder.channelName.setText(videoObjectList.get(position).getBy());
        holder.views.setText(videoObjectList.get(position).getViews());

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

    }

    @Override
    public int getItemCount() {
        return videoObjectList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        ImageView thumbnailsImg;
        ImageView channelProfileImg;
        TextView title;
        TextView time;
        TextView channelName;
        TextView views;
        ImageView heartImg;
        ImageView downloadImg;

        public MyViewHolder(@NonNull View itemView, OnItemClickListener listener) {
            super(itemView);

            thumbnailsImg = itemView.findViewById(R.id.thumbnail);
            channelProfileImg = itemView.findViewById(R.id.imgProfil);
            title = itemView.findViewById(R.id.title);
            time = itemView.findViewById(R.id.trend_showTime);
            channelName = itemView.findViewById(R.id.channelName);
            views = itemView.findViewById(R.id.videoDate);
            heartImg = itemView.findViewById(R.id.heart);
            downloadImg = itemView.findViewById(R.id.download);

//            itemView.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View view) {
//
//                    if(listener != null){
//                        int position = getAdapterPosition();
//                        if(position != RecyclerView.NO_POSITION){
//                            listener.onItemClick(position);
//                        }
//                    }
//
//                }
//            });

            heartImg.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    if (listener != null) {
                        int position = getAdapterPosition();
                        if (position != RecyclerView.NO_POSITION) {
                            listener.onHeartClick(position);
                        }
                    }
                }
            });
            thumbnailsImg.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    if (listener != null) {
                        int position = getAdapterPosition();
                        if (position != RecyclerView.NO_POSITION) {
                            listener.onThumbnailClick(position);
                        }
                    }
                }
            });
            downloadImg.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (listener != null) {
                        int position = getAdapterPosition();
                        if (position != RecyclerView.NO_POSITION) {
                            listener.onDownloadClick(position);
                        }
                    }
                }
            });


        }
    }
}
