package com.app.vruddy.Adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.app.vruddy.R;
import com.app.vruddy.database.Favorite.Favorite;
import com.google.android.material.imageview.ShapeableImageView;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import jp.wasabeef.picasso.transformations.CropCircleTransformation;

public class FavoriteAdapter extends RecyclerView.Adapter<FavoriteAdapter.FavoriteHolder> {

    private List<Favorite> favorites = new ArrayList<>();
    private OnItemClickListener onItemClickListener;

    public interface OnItemClickListener{
        void onRemoveClick(int position);
        void onDownloadClick(int position);
        void onItemClick(int position);
    }

    public List<Favorite> getFavorites() {
        return favorites;
    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    @NonNull
    @Override
    public FavoriteHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.favorite_video_form, parent, false);
        return new FavoriteHolder(view, onItemClickListener);
    }

    @Override
    public void onBindViewHolder(@NonNull FavoriteHolder holder, int position) {
        Favorite currentFavorite = favorites.get(position);

        holder.title.setText(currentFavorite.getTitle());
        holder.channelName.setText(currentFavorite.getBy());
        holder.views.setText(currentFavorite.getViews());
        holder.date.setText(currentFavorite.getDate());
        holder.timeLine.setText(currentFavorite.getTime_line());

        Picasso.get()
                .load(currentFavorite.getThumbnail())
                .into(holder.thumbnail);
        Picasso.get()
                .load(currentFavorite.getChannel_image())
                .transform(new CropCircleTransformation())
                .into(holder.channelImage);

        holder.closeImage.setImageResource(R.drawable.ic_close_24);
        holder.shareImage.setImageResource(R.drawable.ic_share);
        holder.downloadImage.setImageResource(R.drawable.ic_download);
    }

    @Override
    public int getItemCount() {
        return favorites.size();
    }

    public void setFavorites(List<Favorite> favorite) {
        this.favorites = favorite;
        notifyDataSetChanged();
    }

    class FavoriteHolder extends RecyclerView.ViewHolder{
        private TextView  title;
        private TextView  channelName;
        private TextView  views;
        private TextView  date;
        private TextView  timeLine;
        private ShapeableImageView thumbnail;
        private ImageView channelImage;
        private ImageView closeImage;
        private ImageView shareImage;
        private ImageView downloadImage;
        public FavoriteHolder(@NonNull View itemView, OnItemClickListener onItemClickListener) {
            super(itemView);

            title = itemView.findViewById(R.id.favorite_title);
            channelName = itemView.findViewById(R.id.favorite_channel_name);
            views = itemView.findViewById(R.id.favorite_views);
            date = itemView.findViewById(R.id.favorite_date);
            timeLine = itemView.findViewById(R.id.favorite_time_line);
            thumbnail = itemView.findViewById(R.id.fileImage);
            channelImage = itemView.findViewById(R.id.favorite_channel_Image);
            closeImage = itemView.findViewById(R.id.favorite_remove);
            shareImage = itemView.findViewById(R.id.favorite_share);
            downloadImage = itemView.findViewById(R.id.favorite_download);

            closeImage.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if(onItemClickListener != null){
                        int position = getAdapterPosition();
                        if(position != RecyclerView.NO_POSITION){
                            onItemClickListener.onRemoveClick(position);
                        }
                    }
                }
            });
            downloadImage.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if(onItemClickListener != null){
                        int position = getAdapterPosition();
                        if(position != RecyclerView.NO_POSITION){
                            onItemClickListener.onDownloadClick(position);
                        }
                    }
                }
            });
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if(onItemClickListener != null){
                        int position =  getAdapterPosition();
                        if(position != RecyclerView.NO_POSITION){
                            onItemClickListener.onItemClick(position);
                        }
                    }
                }
            });
        }
    }
}
