package com.pax.tk.annapp.Adapter;

import android.content.Context;
import android.os.Bundle;
import android.support.constraint.ConstraintLayout;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;

import com.pax.tk.annapp.Fragments.GradeChildFragment;
import com.pax.tk.annapp.MainActivity;
import com.pax.tk.annapp.Manager;
import com.pax.tk.annapp.R;

import com.pax.tk.annapp.Subject;
import com.pax.tk.annapp.Util;

public class RVAdapterSubjectList extends RecyclerView.Adapter<RVAdapterSubjectList.RecyclerVH> {

    private Context context;
    private ArrayList<Subject> subjects = new ArrayList<>();
    private Manager manager = Manager.getInstance();

    /**
     * creates a subjectList adapter
     *
     * @param context ...
     * @param subjects subjects in the subjectsList
     */
    public RVAdapterSubjectList(Context context,  ArrayList<Subject> subjects){
        this.context = context;
        this.subjects = subjects;
    }

    /**
     * creates the ViewHolder
     *
     * @param parent parent ViewGroup
     * @param viewType type of the view
     * @return created ViewHolder
     */
    @Override
    public RecyclerVH onCreateViewHolder(ViewGroup parent, final int viewType) {
        View v = LayoutInflater.from(context).inflate(R.layout.item_subject_list, parent, false);
        return new RecyclerVH(v);
    }

    /**
     * binds the ViewHolder
     *
     * @param holder ViewHolder
     * @param position position as int
     */
    @Override
    public void onBindViewHolder(RecyclerVH holder, final int position) {
        holder.nameTxt.setText(subjects.get(position).getName());
        holder.cardView.setCardBackgroundColor(Util.getSubjectColor(context, subjects.get(position)));
        holder.nameTxt.setTextColor(context.getColor(android.R.color.white));
        holder.gradeTxt.setTextColor(context.getColor(android.R.color.white));
        holder.constraintLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Bundle args = new Bundle();
                TextView subjectTextName = view.findViewById(R.id.item_subject_name);
                args.putSerializable(context.getString(R.string.bundlekey_subject), subjects.get(position));
                ((MainActivity) context).setFragment(GradeChildFragment.TAG,args);
            }
        });
        holder.gradeTxt.setText( String.valueOf(subjects.get(position).getGradePointAverage()));
    }

    /**
     * gets the subject count
     *
     * @return counted subjects
     */
    @Override
    public int getItemCount() {
        return subjects.size();
    }

    //Viewholder Class
    public class RecyclerVH extends RecyclerView.ViewHolder{
        CardView cardView;
        TextView nameTxt;
        TextView gradeTxt;
        ConstraintLayout constraintLayout;

        public RecyclerVH(View itemView){
            super(itemView);

            cardView = itemView.findViewById(R.id.cardView);

            constraintLayout = itemView.findViewById(R.id.itme_subject_rl_woat_ever);
            nameTxt = itemView.findViewById(R.id.item_subject_name);
            gradeTxt = itemView.findViewById(R.id.item_subject_grade);
        }
    }
}
