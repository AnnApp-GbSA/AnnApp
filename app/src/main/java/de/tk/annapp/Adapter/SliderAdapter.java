package de.tk.annapp.Adapter;

import android.content.Context;
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

import de.tk.annapp.R;

public class SliderAdapter extends PagerAdapter {

    Context c;
    LayoutInflater layoutInflater;

    public SliderAdapter(Context c){
        this.c = c;
    }

    //Arrays
    public int[] slide_images = {
            R.mipmap.tutorial_button,
            R.mipmap.tutorial_homescreen
    };

    public String[] slide_headings = {
        "Note Einfügen",
        "Home Screen",
    };

    public String[] slide_description = {
            "Unter dem Reiter Noten können sie alle ihre Noten für jedes einzelen Fach eintragen. Klicken sie hierzu auf den Kopf im rechten unterem Eck. Geben sie alle notwendigen Informationen bezüglich der Note an und drücken sie auf den OK Knopf",
            "Auf dem Home Screen kann der tägliche Stundenplan auuf der linken Seite eingesehen werden und auf der rechten Seite noch zu erledigende Aufgaben.",
    };

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
        Drawable drawable = c.getDrawable(slide_images[position]);
        image.setImageDrawable(drawable);

        container.addView(view);

        return view;
    }


    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
        container.removeView((LinearLayout) object);
    }
}
