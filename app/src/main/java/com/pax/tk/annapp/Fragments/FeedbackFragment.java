
package com.pax.tk.annapp.Fragments;

import android.app.Fragment;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.pax.tk.annapp.R;

/**
 * Created by Tobi on 20.09.2017.
 */

public class FeedbackFragment extends Fragment {
    View root;

    public static final String TAG = "FeedbackFragment";

    /**
     * initializing variables and calling methods
     *
     * @param inflater           ...
     * @param container          ...
     * @param savedInstanceState ...
     * @return root
     */
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        getActivity().findViewById(R.id.grade).setVisibility(View.GONE);
        getActivity().findViewById(R.id.syncWithCalendar).setVisibility(View.GONE);

        getActivity().setTitle(getString(R.string.feedback));
        root = inflater.inflate(R.layout.fragment_feedback, container, false);

        Button btnSend = (Button) root.findViewById(R.id.btnSend);

        btnSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sendFeedback();
            }
        });


        return root;
    }

    /**
     * checks if text is in the EditText and opens a mail-app to send the text as an e-mail to gbsanna1531@gmail.com
     * makes a toast if an exception is caught
     */
    void sendFeedback(){

        EditText feedbackText = (EditText) root.findViewById(R.id.feedbackText);

        if(feedbackText.getText().toString().equals("")){
            feedbackText.setError("Input Text");
            return;
        }

        Intent i = new Intent(Intent.ACTION_SENDTO);
        i.setData(Uri.parse("mailto:"));
        i.putExtra(Intent.EXTRA_EMAIL, new String[]{"gbsanna1531@gmail.com"});
        i.putExtra(Intent.EXTRA_SUBJECT, "FEEDBACK - AnnApp");
        i.putExtra(Intent.EXTRA_TEXT, feedbackText.getText().toString());

        try{
            startActivity(Intent.createChooser(i, "send mail"));
        } catch (android.content.ActivityNotFoundException e){
            Toast.makeText(this.getContext(), R.string.noMailApp, Toast.LENGTH_SHORT).show();
        } catch (Exception e){
            Toast.makeText(this.getContext(), getString(R.string.error) + e.getMessage(), Toast.LENGTH_SHORT).show();
        }

    }
}

