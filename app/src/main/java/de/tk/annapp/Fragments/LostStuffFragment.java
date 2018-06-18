
package de.tk.annapp.Fragments;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import de.tk.annapp.R;

/**
 * Created by Tobi on 20.09.2017.
 */

public class LostStuffFragment extends Fragment {
    View root;
    EditText lessonSubject;
    Button btn;

    public static final String TAG = "LostStuffFragment";

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        getActivity().setTitle(getString(R.string.lostStuff));
        root = inflater.inflate(R.layout.fragment_loststuff, container, false);

        lessonSubject = (EditText) root.findViewById(R.id.lessonSubject);
        btn = (Button) root.findViewById(R.id.btn);


        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                btnOnClick();
            }
        });



        return root;
    }

    void btnOnClick(){



    }
}

