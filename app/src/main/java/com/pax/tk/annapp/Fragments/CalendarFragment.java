package com.pax.tk.annapp.Fragments;

import android.app.Fragment;
import android.os.Bundle;
import android.support.design.widget.BottomSheetDialog;
import android.support.design.widget.FloatingActionButton;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.github.sundeepk.compactcalendarview.CompactCalendarView;
import com.pax.tk.annapp.R;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;


/**
 * Created by Tobi on 20.09.2017.
 */

public class CalendarFragment extends Fragment {
    public static final String TAG = "CalendarFragment";
    View root;
    CompactCalendarView compactCalendarView;
    SimpleDateFormat month = new SimpleDateFormat("MMMM");
    SimpleDateFormat year = new SimpleDateFormat("yyyy");
    /**
     * initializing variables and calling methods
     * @param inflater ...
     * @param container ...
     * @param savedInstanceState ...
     * @return root
     */
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
        getActivity().findViewById(R.id.grade).setVisibility(View.GONE);
        getActivity().setTitle(getString(R.string.calendar));
        root = inflater.inflate(R.layout.fragment_calendar, container, false);
        compactCalendarView = root.findViewById(R.id.compactcalendar_view);
        compactCalendarView.setUseThreeLetterAbbreviation(true);

        ImageButton monthForward = root.findViewById(R.id.monthForward);
        ImageButton monthBack = root.findViewById(R.id.monthBack);
        FloatingActionButton fabAdd = root.findViewById(R.id.fabAddCalendar);
        TextView monthIndication = root.findViewById(R.id.monthIndication);

        monthIndication.setText(month.format(compactCalendarView.getFirstDayOfCurrentMonth()) + " - " + year.format(compactCalendarView.getFirstDayOfCurrentMonth()));

        //Listener compactCalendarView
        monthForward.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                compactCalendarView.scrollRight();
            }
        });
        monthBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                compactCalendarView.scrollLeft();
            }
        });
        compactCalendarView.setListener(new CompactCalendarView.CompactCalendarViewListener() {
            @Override
            public void onDayClick(Date dateClicked) {

            }

            @Override
            public void onMonthScroll(Date firstDayOfNewMonth) {
                monthIndication.setText(month.format(compactCalendarView.getFirstDayOfCurrentMonth()) + " - " + year.format(compactCalendarView.getFirstDayOfCurrentMonth()));
            }
        });

        //fabAdd Listener
        fabAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                createInputDialog();
            }
        });

        return root;
    }

    public void createInputDialog() {
        final BottomSheetDialog bsd = new BottomSheetDialog(getContext(), R.style.NewDialog);

        View mView = View.inflate(this.getContext(), R.layout.dialog_new_event, null);

        final EditText eventInput = (EditText) mView.findViewById(R.id.eventInput);
        final Button btnStartDateInput = (Button) mView.findViewById(R.id.startDateInput);
        final Button btnEndDateInput = (Button) mView.findViewById(R.id.endDateInput);
        final EditText locationInput = (EditText) mView.findViewById(R.id.locationInput);
        final Button btnExtra = (Button) mView.findViewById(R.id.btnExtra);
        final LinearLayout extraLayout = (LinearLayout) mView.findViewById(R.id.extraLayout);
        final FloatingActionButton btnOK = (FloatingActionButton) mView.findViewById(R.id.btnOK);
        final FloatingActionButton btnCancel = (FloatingActionButton) mView.findViewById(R.id.btnCancel);

        btnStartDateInput.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        btnEndDateInput.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        btnExtra.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        btnOK.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                bsd.cancel();
            }
        });

        bsd.setContentView(mView);
        bsd.show();
    }
}

