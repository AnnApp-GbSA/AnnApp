package de.tk.annapp.Adapter;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.support.annotation.NonNull;
import android.support.v4.view.PagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import de.tk.annapp.R;

public class SliderAdapter extends PagerAdapter {

    Context c;
    LayoutInflater layoutInflater;

    public SliderAdapter(Context c){
        this.c = c;

        slide_headings = c.getResources().getStringArray(R.array.slide_headings);
        slide_description = c.getResources().getStringArray(R.array.slide_description);
    }

    //Arrays
    public int[] slide_images = {
            R.mipmap.tutorial_home,
            R.mipmap.tutorial_timetable,
            R.mipmap.tutorial_grade,
            R.mipmap.tutorial_tasks,
            R.mipmap.tutorial_home,
            R.mipmap.tutorial_home,
            R.mipmap.tut_news
    };

    public String[] slide_headings;

    public String[] slide_description;

    @Override
    public int getCount() {
        return slide_headings.length;
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
        return view == (View) object;
    }

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
        image.setImageResource(slide_images[position]);

        container.addView(view);

        return view;
    }


    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
        container.removeView((LinearLayout) object);
    }
}
