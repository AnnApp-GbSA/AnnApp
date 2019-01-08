package com.pax.tk.annapp.Adapter;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.support.annotation.NonNull;
import android.support.constraint.ConstraintLayout;
import android.support.v4.view.PagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.pax.tk.annapp.R;

public class SliderAdapter extends PagerAdapter {

    Context c;
    LayoutInflater layoutInflater;

    /**
     * creates a slider adapter
     *
     * @param c ...
     */
    public SliderAdapter(Context c){
        this.c = c;
        slide_images = c.getResources().obtainTypedArray(R.array.slide_images);
        slide_headings = c.getResources().getStringArray(R.array.slide_headings);
        slide_description = c.getResources().getStringArray(R.array.slide_description);
    }

    //Arrays
    public TypedArray slide_images;

    public String[] slide_headings;

    public String[] slide_description;

    /**
     * gets the slide heading length
     *
     * @return length of the slide heading
     */
    @Override
    public int getCount() {
        return slide_headings.length;
    }

    /**
     * checks if a view is from an object
     *
     * @param view view which shall be checked
     * @param object object to check with
     * @return true if object casted to a View is the view, false if not
     */
    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
        return view == (View) object;
    }

    /**
     * instantiates a item
     *
     * @param container container ViewGroup
     * @param position position of the item as int
     * @return view
     */
    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, int position) {


        layoutInflater = (LayoutInflater) c.getSystemService(c.LAYOUT_INFLATER_SERVICE);

        View view = layoutInflater.inflate(R.layout.slide_layout, container, false);

        TextView header = (TextView) view.findViewById(R.id.tutorial_header);
        TextView description = view.findViewById(R.id.tutorial_description);
        ImageView image = view.findViewById(R.id.boarding_image);

        header.setText(slide_headings[position]);
        description.setText(slide_description[position]);
        image.setImageResource(slide_images.getResourceId(position, -1));

        container.addView(view);

        return view;
    }

    /**
     * destroys a item
     *
     * @param container container ViewGroup
     * @param position position of the item
     * @param object object to destroy
     */
    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
        container.removeView((LinearLayout) object);
    }
}
