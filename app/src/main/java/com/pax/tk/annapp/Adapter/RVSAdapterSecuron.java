package com.pax.tk.annapp.Adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.pax.tk.annapp.R;
import com.thegrizzlylabs.sardineandroid.DavResource;

import java.util.ArrayList;
import java.util.List;

public class RVSAdapterSecuron extends RecyclerView.Adapter {

    Context context;
    List<DavResource> resources;

    /**
     * creates a securon adapter
     *
     * @param context ...
     * @param resources resources
     */
    public RVSAdapterSecuron(Context context, List<DavResource> resources ) {
        this.context = context;
        this.resources = resources;
    }

    /**
     * creates the ViewHolder
     *
     * @param parent parent ViewGroup
     * @param viewType type of the View
     * @return created ViewHolder
     */
    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context).inflate(R.layout.item_securon, parent, false);
        return new RecyclerVHSecuron(v);
    }

    /**
     * binds the ViewHolder
     *
     * @param holder ViewHolder
     * @param position position as int
     */
    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        ((RecyclerVHSecuron)holder).name.setText(resources.get(position).getName());
        if (!resources.get(position).isDirectory())
            ((RecyclerVHSecuron) holder).folderView.setVisibility(View.GONE);

    }

    /**
     * returns 0
     *
     * @return 0
     */
    @Override
    public int getItemCount() {
        return 0;
    }


    //Viewholder Class
    public class RecyclerVHSecuron extends RecyclerView.ViewHolder {
        public ImageView folderView;
        TextView name;

        public RecyclerVHSecuron(View itemView) {
            super(itemView);
            folderView = itemView.findViewById(R.id.folderImageView);
            name = itemView.findViewById(R.id.nameTextView);
        }
    }
}
