package com.pax.tk.annapp.Adapter;

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
import android.widget.Toast;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

import com.pax.tk.annapp.MainActivity;
import com.pax.tk.annapp.Manager;
import com.pax.tk.annapp.NewsDetailActivity;
import com.pax.tk.annapp.News;
import com.pax.tk.annapp.R;


public class RVAdapterNews extends RecyclerView.Adapter<RVAdapterNews.NewsViewHolder> {

    Context context;
    Manager manager;
    HashMap<Integer, Integer> old = new HashMap<Integer, Integer>();

    /**
     * creates a news adapter
     *
     * @param context ...
     */
    public RVAdapterNews(Context context) {
        this.context = context;
        manager = Manager.getInstance();
    }

    /**
     * does nothing
     */
    public void update() {
        /*ArrayList<News> nn = manager.getNews();
        for (int i = 0; i < nn.size(); i++)
            if (old.keySet().contains(nn.get(i).getLink().hashCode()))
                if (old.get(nn.get(i).getLink().hashCode()).equals(nn.get(i).fullHashCode()))
                    notifyItemChanged(i);
                else
                    notifyItemInserted(i);*/
    }

    /**
     * creates the ViewHolder
     * @param parent parent ViewGroup
     * @param viewType type of the view
     * @return created ViewHolder
     */
    @NonNull
    @Override
    public NewsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context).inflate(R.layout.card_news, parent, false);
        return new NewsViewHolder(v);
    }


    /**
     * binds the ViewHolder
     *
     * @param holder ViewHolder
     * @param position position as int
     */
    @Override
    public void onBindViewHolder(@NonNull NewsViewHolder holder, int position) {
        News news = manager.getOneNews(position);
        holder.setIsRecyclable(true);
        old.put(news.getLink().hashCode(), news.fullHashCode());
        holder.title.setText(news.getTitle());
        holder.description.setText(news.getDescription());

        /*if (news.getImage() == null) {
            if (news.getImageurl() != null) {
                //holder.image.setImageDrawable(manager.getFromURl(news.getImageurl()));
                news.setImage(manager.getFromURl(news.getImageurl()));
            } else{
                holder.image.setVisibility(View.GONE);
            }
        }*/
        holder.image.setImageDrawable(news.getImage());
        System.out.println("use Image: " + news.getImage());

        holder.image.setVisibility(View.GONE);


        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(context, NewsDetailActivity.class);
                intent.putExtra(context.getString(R.string.bundlekey_news), news);
                intent.putExtra(context.getString(R.string.bundleKey_colorThemePosition), ((MainActivity) context).getPreferences(Context.MODE_PRIVATE).getInt("colorSchemePosition", 0));

                try{
                context.startActivity(intent);}
                catch (RuntimeException e){
                    e.printStackTrace();
                    Toast.makeText(context, context.getString(R.string.error), Toast.LENGTH_SHORT).show();
                }
            }
        });

        holder.btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //TODO do menu stuff
            }
        });
    }

    /**
     * gets the news count
     *
     * @return counted news
     */
    @Override
    public int getItemCount() {
        return manager.getNewsCount();
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
