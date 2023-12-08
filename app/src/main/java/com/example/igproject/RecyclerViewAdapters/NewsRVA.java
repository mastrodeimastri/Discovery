package com.example.igproject.RecyclerViewAdapters;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.igproject.LocalData.NewsArticle;
import com.example.igproject.R;

import com.squareup.picasso.Picasso;

public class NewsRVA extends RecyclerView.Adapter<NewsRVA.MyViewHolder>{
    Context context;
    NewsArticle[] newsArticles;

    public NewsRVA(Context context, NewsArticle[] newsArticles){
        this.context = context;
        this.newsArticles = newsArticles;
    }

    public void openLink(int pos){
        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(newsArticles[pos].articleLink));
        context.startActivity(intent);
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
        holder.title.setText(newsArticles[position].title);
        holder.author.setText(newsArticles[position].author);
        //holder.image.setImageResource(R.drawable.clouds_moon);
        Picasso.get().load(newsArticles[position].imageLink).into(holder.image);
    }

    @Override
    public int getItemCount() {
        return newsArticles.length;
    }

    public class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        ImageView image;
        TextView title, author;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            image = itemView.findViewById(R.id.newsImage);
            title = itemView.findViewById(R.id.titleNews);
            author = itemView.findViewById(R.id.authorNews);

            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            int position = getAdapterPosition();
            if (position != RecyclerView.NO_POSITION)
                openLink(getAdapterPosition());
        }
    }
}
