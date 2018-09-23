package com.pax.qbt.annapp.Adapter;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashMap;

import com.pax.qbt.annapp.MainActivity;
import com.pax.qbt.annapp.NewsDetailActivity;
import com.pax.qbt.annapp.News;
import com.pax.qbt.annapp.R;
import com.pax.qbt.annapp.SubjectManager;


public class RVAdapterNews extends RecyclerView.Adapter<RVAdapterNews.NewsViewHolder> {

    Context context;
    SubjectManager subjectManager;
    HashMap<Integer,Integer> old = new HashMap<Integer,Integer>();

    public RVAdapterNews(Context context) {
        this.context = context;
        subjectManager = SubjectManager.getInstance();
    }

    public void update(){
        ArrayList<News> nn = subjectManager.getNews();
        for (int i =0; i< nn.size();i++)
            if (old.keySet().contains(nn.get(i).getLink().hashCode()))
                if (old.get(nn.get(i).getLink().hashCode()).equals(nn.get(i).fullHashCode()))
                    notifyItemChanged(i);
            else
                notifyItemInserted(i);
    }

    @NonNull
    @Override
    public NewsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context).inflate(R.layout.card_news, parent, false);
        return new NewsViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull NewsViewHolder holder, int position) {
        News news = subjectManager.getOneNews(position);
        old.put(news.getLink().hashCode(),news.fullHashCode());
        holder.title.setText(news.getTitle());
        holder.description.setText(news.getDiscription());
        holder.image.setImageDrawable(news.getImage());



        if (news.getImage() == null)
            holder.image.setVisibility(View.GONE);

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(context, NewsDetailActivity.class);
                intent.putExtra(context.getString(R.string.bundlekey_news), news);
                intent.putExtra(context.getString(R.string.bundleKey_colorThemePosition), ((MainActivity) context).getPreferences(Context.MODE_PRIVATE).getInt("colorSchemePosition", 0));

                context.startActivity(intent);
            }
        });

        holder.btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //TODO do menu stuff
            }
        });
    }

    @Override
    public int getItemCount() {
        return subjectManager.getNewsCount();
    }

    public static class NewsViewHolder extends RecyclerView.ViewHolder {
        CardView cv;
        TextView title;
        TextView description;
        ImageView image;
        ImageButton btn;
        News news;

        NewsViewHolder(View itemView) {
            super(itemView);
            cv = itemView.findViewById(R.id.card_news_card);
            title = itemView.findViewById(R.id.card_news_titel);
            description = itemView.findViewById(R.id.card_news_discription);
            image = itemView.findViewById(R.id.card_news_image);
            btn = itemView.findViewById(R.id.card_news_btn);
        }
    }

}
