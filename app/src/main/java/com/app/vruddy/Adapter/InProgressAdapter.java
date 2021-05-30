package com.app.vruddy.Adapter;

import android.annotation.SuppressLint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.app.vruddy.Adapter.DiffUtil.InProgressDiffUtil;
import com.app.vruddy.R;
import com.app.vruddy.database.InProgress.InProgressFile;
import com.google.android.material.imageview.ShapeableImageView;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class InProgressAdapter extends RecyclerView.Adapter<InProgressAdapter.FileHolder> {

    private List<InProgressFile> inProgressFiles = new ArrayList<>();
    private OnItemClickListener onItemClickListener;


    public interface OnItemClickListener {
        void onCancelClick(int position, String video_id, int downloadId);

        void onPauseClick(int position, String video_id, int downloadId);

        void onResumeClick(int position, String video_id, int downloadId);

    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    public List<InProgressFile> getInProgressFiles() {
        return inProgressFiles;
    }

    public void setInProgressFiles(List<InProgressFile> inProgressFiles) {
        this.inProgressFiles = inProgressFiles;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public FileHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.downloading_video_form, parent, false);
        return new FileHolder(view, onItemClickListener);
    }

    @SuppressLint("ResourceAsColor")
    @Override
    public void onBindViewHolder(@NonNull FileHolder holder, int position) {
        InProgressFile currentInProgressFile = inProgressFiles.get(position);


        holder.title.setText(currentInProgressFile.getTitle());
        holder.channelName.setText(currentInProgressFile.getBy());
        holder.timeLine.setText(currentInProgressFile.getTime_line());
        holder.progress_text.setText(currentInProgressFile.getProgress() + "% -");
        holder.progress.setProgress(currentInProgressFile.getProgress());
        holder.total.setText(currentInProgressFile.getTotal_size() + "MB");
        holder.current_download.setText(currentInProgressFile.getCurrent_size() + "MB/");
        holder.file_type.setText(currentInProgressFile.getFile_type());

        Picasso.get()
                .load(currentInProgressFile.getThumbnail())
                .into(holder.thumbnail);
        holder.cancel_file.setImageResource(R.drawable.ic_delete_10);
        holder.pause_file.setImageResource(currentInProgressFile.getPause());
        holder.resume_file.setImageResource(currentInProgressFile.getResume());

        if (!currentInProgressFile.isFileCompleted()) {
            holder.resume_file.setVisibility(View.VISIBLE);
            holder.pause_file.setVisibility(View.VISIBLE);
            holder.progress.setVisibility(View.VISIBLE);
            holder.failMsg.setVisibility(View.GONE);
        }
        if (currentInProgressFile.isPaused()) {
            holder.pause_file.setVisibility(View.INVISIBLE);
            holder.resume_file.setVisibility(View.VISIBLE);
        } else {
            holder.resume_file.setVisibility(View.INVISIBLE);
            holder.pause_file.setVisibility(View.VISIBLE);
        }
        System.out.println("---------- is complete: " + currentInProgressFile.isFileCompleted());
        System.out.println("---------- file Type  : " + currentInProgressFile.getFile_type());
        if (currentInProgressFile.isFileCompleted() && currentInProgressFile.getFile_type().equals("video")) {
            //
            holder.resume_file.setVisibility(View.GONE);
            holder.pause_file.setVisibility(View.GONE);
            holder.progress.setVisibility(View.GONE);
            holder.infinity_progress.setVisibility(View.VISIBLE);
            holder.total.setVisibility(View.GONE);
            holder.current_download.setText("Converting...");
            holder.current_download.setTextColor(R.color.lightGreen);
        }
        if (currentInProgressFile.isFileFail() || currentInProgressFile.isAudioFail()) {
            holder.resume_file.setVisibility(View.GONE);
            holder.pause_file.setVisibility(View.GONE);
            holder.progress.setVisibility(View.GONE);
            holder.infinity_progress.setVisibility(View.GONE);
            holder.failMsg.setVisibility(View.VISIBLE);

        }
    }

    @Override
    public int getItemCount() {
        return inProgressFiles.size();
    }

    public void updateRecycler(List<InProgressFile> newInProgressFiles) {
        //
        InProgressDiffUtil inProgressDiffUtil = new InProgressDiffUtil(inProgressFiles, newInProgressFiles);
        DiffUtil.DiffResult diffUtil = DiffUtil.calculateDiff(inProgressDiffUtil);
        inProgressFiles = newInProgressFiles;
        diffUtil.dispatchUpdatesTo(this);
    }

    class FileHolder extends RecyclerView.ViewHolder {
        private TextView failMsg;
        private TextView title;
        private TextView channelName;
        private TextView timeLine;
        private TextView progress_text;
        private ProgressBar progress;
        private ProgressBar infinity_progress;
        private TextView total;
        private TextView current_download;
        private TextView file_type;
        private ShapeableImageView thumbnail;
        private ImageView cancel_file;
        private ImageView pause_file;
        private ImageView resume_file;
        private ImageView retry_file;

        public FileHolder(@NonNull View itemView, OnItemClickListener onItemClickListener) {
            super(itemView);
            failMsg = itemView.findViewById(R.id.downloading_fail_msg);
            title = itemView.findViewById(R.id.downloading_fileTitle);
            channelName = itemView.findViewById(R.id.downloading_channel_name);
            timeLine = itemView.findViewById(R.id.downloading_file_time_info);
            progress_text = itemView.findViewById(R.id.downloading_progressPercentage);
            progress = itemView.findViewById(R.id.downloading_progress_bar);
            infinity_progress = itemView.findViewById(R.id.downloading_infinity_progress_bar);
            total = itemView.findViewById(R.id.downloading_total_size);
            current_download = itemView.findViewById(R.id.downloading_current_size);
            file_type = itemView.findViewById(R.id.downloading_file_type);
            thumbnail = itemView.findViewById(R.id.downloading_thumbnail);
            cancel_file = itemView.findViewById(R.id.downloading_cancelDownloading);
            pause_file = itemView.findViewById(R.id.downloading_pause);
            resume_file = itemView.findViewById(R.id.downloading_resume);
            retry_file = itemView.findViewById(R.id.downloading_retry);

            cancel_file.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (onItemClickListener != null) {
                        int position = getAdapterPosition();
                        InProgressFile currentInProgressFile = inProgressFiles.get(position);
                        if (position != RecyclerView.NO_POSITION) {
                            onItemClickListener.onCancelClick(position, currentInProgressFile.getVideo_id(), currentInProgressFile.getDownloadFileId());
                        }
                    }
                }
            });
            pause_file.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (onItemClickListener != null) {
                        int position = getAdapterPosition();
                        InProgressFile currentInProgressFile = inProgressFiles.get(position);
                        if (position != RecyclerView.NO_POSITION) {
                            onItemClickListener.onPauseClick(position, currentInProgressFile.getVideo_id(), currentInProgressFile.getDownloadFileId());
                            pause_file.setVisibility(View.INVISIBLE);
                            resume_file.setVisibility(View.VISIBLE);
                        }
                    }
                }
            });
            resume_file.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (onItemClickListener != null) {
                        int position = getAdapterPosition();
                        InProgressFile currentInProgressFile = inProgressFiles.get(position);
                        if (position != RecyclerView.NO_POSITION) {
                            onItemClickListener.onResumeClick(position, currentInProgressFile.getVideo_id(), currentInProgressFile.getDownloadFileId());
                            resume_file.setVisibility(View.INVISIBLE);
                            pause_file.setVisibility(View.VISIBLE);
                        }
                    }
                }
            });
        }
    }
}
