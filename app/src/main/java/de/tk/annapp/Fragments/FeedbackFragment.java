
package de.tk.annapp.Fragments;

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

import de.tk.annapp.R;

/**
 * Created by Tobi on 20.09.2017.
 */

public class FeedbackFragment extends Fragment {
    View root;

    public static final String TAG = "FeedbackFragment";

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        getActivity().findViewById(R.id.grade).setVisibility(View.GONE);

        getActivity().setTitle("Feedback");
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

    void sendFeedback(){

        EditText feedbackText = (EditText) root.findViewById(R.id.feedbackText);

        if(feedbackText.getText().toString().equals("")){
            feedbackText.setError("Input Text");
            return;
        }

        Intent i = new Intent(Intent.ACTION_SENDTO);
        i.setData(Uri.parse("mailto:"));
        i.putExtra(Intent.EXTRA_EMAIL, new String[]{"gbsanna1531@gmail.com"});
        //TODO Set name from actual user into the subject of the feedbackMessage
        i.putExtra(Intent.EXTRA_SUBJECT, "FEEDBACK - AnnApp");
        i.putExtra(Intent.EXTRA_TEXT, feedbackText.getText().toString());

        try{
            startActivity(Intent.createChooser(i, "send mail"));
        } catch (android.content.ActivityNotFoundException e){
            //TODO make it a StringRessource
            Toast.makeText(this.getContext(), "No mail App found!", Toast.LENGTH_SHORT).show();
        } catch (Exception e){
            Toast.makeText(this.getContext(), "Unexpected Error" + e.getMessage(), Toast.LENGTH_SHORT).show();
        }

    }
}

