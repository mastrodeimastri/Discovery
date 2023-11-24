package com.example.igproject.RecyclerViewAdapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.igproject.R;

public class NewsRVA extends RecyclerView.Adapter<NewsRVA.MyViewHolder>{
    Context context;

    public NewsRVA(Context context){
        this.context = context;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.recyclerview_news, parent, false);
        return new NewsRVA.MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        holder.title.setText(R.string.hyperlink);
        holder.author.setText("Autore: Redazione Cultura");
        holder.image.setImageResource(R.drawable.clouds_moon);
    }

    @Override
    public int getItemCount() {
        return 5;
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder {
        ImageView image;
        TextView title, author;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            image = itemView.findViewById(R.id.newsImage);
            title = itemView.findViewById(R.id.titleNews);
            author = itemView.findViewById(R.id.authorNews);
        }
    }
}
