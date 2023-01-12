package com.app.vruddy.Views.Adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.app.vruddy.R;

import java.util.ArrayList;

public class SuggestionAdapter extends RecyclerView.Adapter<SuggestionAdapter.MyHolder>{

    private ArrayList<String> keywords = new ArrayList<>();
    onItemClickListener onItemClickListener;

    public interface onItemClickListener{
        void onItemClick(int position);
    }

    public void setOnItemClickListener(onItemClickListener listener) {
        onItemClickListener = listener;
    }

    public SuggestionAdapter(ArrayList<String> keywords) {
        this.keywords = keywords;
    }

    public class MyHolder extends RecyclerView.ViewHolder{

        TextView keywordsTitle;

        public MyHolder(@NonNull View itemView, onItemClickListener listener) {
            super(itemView);

            keywordsTitle = itemView.findViewById(R.id.suggested_title);

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
        }
    }

    @NonNull
    @Override
    public MyHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.auto_complete_search_layout, parent, false);
        MyHolder myHolder = new MyHolder(view, onItemClickListener);
        return myHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull MyHolder holder, int position) {

        holder.keywordsTitle.setText(keywords.get(position));
    }

    @Override
    public int getItemCount() {
        return keywords.size();
    }


}
