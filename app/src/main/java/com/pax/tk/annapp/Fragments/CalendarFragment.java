package com.pax.tk.annapp.Fragments;

import android.app.DatePickerDialog;
import android.app.Fragment;
import android.app.TimePickerDialog;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.BottomSheetDialog;
import android.support.design.widget.FloatingActionButton;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.github.sundeepk.compactcalendarview.CompactCalendarView;
import com.github.sundeepk.compactcalendarview.domain.Event;
import com.pax.tk.annapp.EventHandler;
import com.pax.tk.annapp.MainActivity;
import com.pax.tk.annapp.Manager;
import com.pax.tk.annapp.R;
import com.pax.tk.annapp.Util;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Random;
import java.util.regex.Pattern;


/**
 * Created by Tobi on 20.09.2017.
 */

public class CalendarFragment extends Fragment {
    public static final String TAG = "CalendarFragment";
    View root;
    CompactCalendarView compactCalendarView;
    Manager manager;
    SimpleDateFormat month = new SimpleDateFormat("MMMM");
    SimpleDateFormat year = new SimpleDateFormat("yyyy");
    SimpleDateFormat ddmm = new SimpleDateFormat("dd.MM.");
    SimpleDateFormat kkmm = new SimpleDateFormat("kk:mm");
    SimpleDateFormat ddMMMM = new SimpleDateFormat("dd. MMMM");
    Long currentDay = System.currentTimeMillis();
    Long currentDayPlusOne = currentDay;
    TextView dateIndication;
    ImageButton monthForward;
    ImageButton monthBack;
    FloatingActionButton fabAdd;
    TextView monthIndication;

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
        getActivity().setTitle(getString(R.string.calendar));
        root = inflater.inflate(R.layout.fragment_calendar, container, false);
        dateIndication = root.findViewById(R.id.dateInformation);
        compactCalendarView = root.findViewById(R.id.compactcalendar_view);
        compactCalendarView.setUseThreeLetterAbbreviation(true);
        monthForward = root.findViewById(R.id.monthForward);
        monthBack = root.findViewById(R.id.monthBack);
        fabAdd = root.findViewById(R.id.fabAddCalendar);
        fabAdd.setVisibility(View.GONE);
        monthIndication = root.findViewById(R.id.monthIndication);

        manager = Manager.getInstance();
        manager.setCompactCalendarView(compactCalendarView);

        monthIndication.setText(month.format(compactCalendarView.getFirstDayOfCurrentMonth()) + " - " + year.format(compactCalendarView.getFirstDayOfCurrentMonth()));


        new LoadCalendar().execute((Void) null);
        dateIndication.setText(ddMMMM.format(new Date(System.currentTimeMillis())));

        return root;
    }

    private class LoadCalendar extends AsyncTask<Void, Void, Void>{

        /**
         * Override this method to perform a computation on a background thread. The
         * specified parameters are the parameters passed to {@link #execute}
         * by the caller of this task. Loads the events from the ICS-File into the compactCalendarView
         * and calls methods if any Button on the compactCalendarView is pressed
         * <p>
         * This method can call {@link #publishProgress} to publish updates
         * on the UI thread.
         *
         * @param voids The parameters of the task.
         * @return A result, defined by the subclass of this task.
         * @see #onPreExecute()
         * @see #onPostExecute
         * @see #publishProgress
         */
        @Override
        protected Void doInBackground(Void... voids) {


            EventHandler.loadSchoolEvents();


            for (int i = 0; i<manager.getSchoolEvents().size(); i++){
                try {
                    manager.addSchoolEvent((Event) manager.getSchoolEvents().toArray()[i]);}catch (Exception e){e.printStackTrace();}
            }

            for (int i = 0; i<manager.getPrivateEvents().size(); i++){
                manager.addPrivateEvent((Event) manager.getPrivateEvents().toArray()[i]);
            }

            getActivity().runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    refresh(compactCalendarView.getEvents(new Date(System.currentTimeMillis())));
                }
            });

//            dateIndication.setText(ddMMMM.format(new Date(System.currentTimeMillis())));
            //TODO refresh if new event is loaded at this date

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
                    refresh(compactCalendarView.getEvents(dateClicked));
                    dateIndication.setText(ddMMMM.format(dateClicked));
                    currentDay = dateClicked.getTime();
                    currentDayPlusOne = dateClicked.getTime() + 86400000;
                }

                @Override
                public void onMonthScroll(Date firstDayOfNewMonth) {
                    monthIndication.setText(month.format(compactCalendarView.getFirstDayOfCurrentMonth()) + " - " + year.format(compactCalendarView.getFirstDayOfCurrentMonth()));
                    refresh(compactCalendarView.getEvents(firstDayOfNewMonth));
                    dateIndication.setText(ddMMMM.format(firstDayOfNewMonth));
                    currentDay = firstDayOfNewMonth.getTime();
                    currentDayPlusOne = firstDayOfNewMonth.getTime() + 86400000;
                }
            });


            //fabAdd Listener
            fabAdd.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    createInputDialog();
                }
            });

            return null;
        }
    }

    /**
     * displays a message on the screen if eventsThisDay is empty
     * displays all important information for every Event in eventsThisDay on the screen if not
     *
     * @param eventsThisDay List of Events which includes all events for this day
     * @return false if eventsThisDay is empty and true if not
     */
    private boolean refresh(List<Event> eventsThisDay) {
        TextView event = (TextView) root.findViewById(R.id.Event);
        LinearLayout eventList = (LinearLayout) root.findViewById(R.id.eventList);
        eventList.removeAllViews();
        if (eventsThisDay.isEmpty()) {
            event.setVisibility(View.VISIBLE);
            event.setText("    "+R.string.noEventMessage);
            return false;
        }
        event.setVisibility(View.GONE);
        for (int x = 0; x < eventsThisDay.size(); x++) {
            View eventView = LayoutInflater.from(this.getContext()).inflate(
                    R.layout.event_layout, null);
            String[] split = eventsThisDay.get(x).getData().toString().split(Pattern.quote("°°"));
            String startDate = ddmm.format(new Date(eventsThisDay.get(x).getTimeInMillis()));
            String startTime = kkmm.format(new Date(eventsThisDay.get(x).getTimeInMillis()));
            String endDate = "";
            String endTime = "";
            try {
                endDate = ddmm.format(new Date(Long.valueOf(split[0])));
                endTime = kkmm.format(new Date(Long.valueOf(split[0])));
            } catch (Exception e) {
            }
            String[] splitEnd = endDate.split(Pattern.quote(". "));
            String[] splitStart = startDate.split(Pattern.quote(". "));
            try {
                if (Integer.valueOf(splitEnd[0]) > Integer.valueOf(splitStart[0]) || Integer.valueOf(splitEnd[1]) > Integer.valueOf(splitStart[1])) {
                    endTime = ddmm.format(new Date(Long.valueOf(split[0]))) + " " + kkmm.format(new Date(Long.valueOf(split[0])));
                }
            } catch (Exception e) {
            }
            String location = split[1];
            String summary = split[2];

            /*if(location.length() > 13 && summary.length() > 40){
                String[] splitLocation = location.split(Pattern.quote(" "));
                location = splitLocation[0];
                if(location.length() > 13){
                    location = "";
                }
            }*/

            String[] splitStartTime = startTime.split(Pattern.quote(" "));
            if (splitStartTime.length > 1) {
                startTime = splitStartTime[0] + monthToDate(splitStartTime[1]) + ".";
            }

            String[] splitEndTime = endTime.split(Pattern.quote(" "));
            if (splitEndTime.length > 1) {
                endTime = splitEndTime[0] + monthToDate(splitEndTime[1]) + ".";
            }


            TextView startTimeTxt = eventView.findViewById(R.id.startTime);
            TextView endTimeTxt = eventView.findViewById(R.id.endTime);

            if (startTime.equals(endTime) && startTime.contains("24:00") || endTime.contains("24:30")) {
                startTimeTxt.setText(getString(R.string.allday));
                endTimeTxt.setVisibility(View.GONE);
            } else {

                startTimeTxt.setText("Beginn: " + startTime);

                endTimeTxt.setText("Ende: " + endTime);
            }

            TextView locationTxt = eventView.findViewById(R.id.location);
            if (location.equals(""))
                locationTxt.setVisibility(View.GONE);
            else
                locationTxt.setText("Ort: " + location);

            TextView descriptionTxt = eventView.findViewById(R.id.description);
            descriptionTxt.setText(summary);

            TextView color = eventView.findViewById(R.id.colorMarker);
            color.setBackgroundColor(eventsThisDay.get(x).getColor());

            eventList.addView(eventView);

        }
        return true;
    }

    /**
     * transforms a monthName like Januar in a number like 01
     *
     * @param monthName name of the month as a String
     * @return number of month as a String or null if monthName is not a name of a month
     */
    private String monthToDate(String monthName) {
        String month;
        switch (monthName) {
            case "Januar":
                month = "01";
                break;
            case "Februar":
                month = "02";
                break;
            case "März":
                month = "03";
                break;
            case "April":
                month = "04";
                break;
            case "Mai":
                month = "05";
                break;
            case "Juni":
                month = "06";
                break;
            case "Juli":
                month = "07";
                break;
            case "August":
                month = "08";
                break;
            case "September":
                month = "09";
                break;
            case "Oktober":
                month = "10";
                break;
            case "November":
                month = "11";
                break;
            case "Dezember":
                month = "12";
                break;
            default:
                month = null;
        }
        return month;
    }


    public void createInputDialog() {
        final BottomSheetDialog bsd = new BottomSheetDialog(getContext(), R.style.NewDialog);

        View mView = View.inflate(this.getContext(), R.layout.dialog_new_event, null);

        final EditText eventInput = (EditText) mView.findViewById(R.id.eventInput);
        final Button btnStartDateInput = (Button) mView.findViewById(R.id.startDateInput);
        final Button btnStartTimeInput = (Button) mView.findViewById(R.id.startTimeInput);
        final Button btnEndDateInput = (Button) mView.findViewById(R.id.endDateInput);
        final Button btnEndTimeInput = (Button) mView.findViewById(R.id.endTimeInput);
        final EditText locationInput = (EditText) mView.findViewById(R.id.locationInput);
        final Button btnExtra = (Button) mView.findViewById(R.id.btnExtra);
        final LinearLayout extraLayout = (LinearLayout) mView.findViewById(R.id.extraLayout);
        final FloatingActionButton btnOK = (FloatingActionButton) mView.findViewById(R.id.btnOK);
        final FloatingActionButton btnCancel = (FloatingActionButton) mView.findViewById(R.id.btnCancel);
        final CheckBox allDayEvent = (CheckBox) mView.findViewById(R.id.allDayEvent);

        btnStartDateInput.setText(ddMMMM.format(currentDay));
        Date start = new Date(currentDayPlusOne);
        System.out.println("start: " + currentDay);
        Date end = new Date(currentDayPlusOne);
        final boolean[] change = {false};

        allDayEvent.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    TypedValue a = new TypedValue();
                    getContext().getTheme().resolveAttribute(R.attr.colorTimetableHeader, a, true);
                    btnStartTimeInput.setEnabled(false);
                    btnStartTimeInput.setTextColor(a.data);
                    btnEndTimeInput.setEnabled(false);
                    btnEndTimeInput.setTextColor(a.data);
                } else {
                    TypedValue a = new TypedValue();
                    getContext().getTheme().resolveAttribute(R.attr.colorAccent, a, true);

                    btnStartTimeInput.setEnabled(true);
                    btnStartTimeInput.setTextColor(a.data);
                    btnEndTimeInput.setEnabled(true);
                    btnEndTimeInput.setTextColor(a.data);
                }
            }
        });

        DatePickerDialog.OnDateSetListener onStartDateSetListener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                String hour = new java.text.SimpleDateFormat("hh").format(new java.util.Date(currentDay));
                String minute = new java.text.SimpleDateFormat("mm").format(new java.util.Date(currentDay));
                start.setYear(year - 1900);
                start.setMonth(month);
                start.setDate(dayOfMonth);


                btnStartDateInput.setText(ddMMMM.format(start.getTime()));

                if (end.getTime() < start.getTime()) {
                    start.setTime(end.getTime());
                    Toast.makeText(getContext(), "Die Startzeit darf nicht nach der Endzeit liegen.", Toast.LENGTH_SHORT).show();
                    btnStartDateInput.setText(ddMMMM.format(start.getTime()));
                }
                if (end.getTime() > start.getTime()) {
                    allDayEvent.setChecked(true);
                    allDayEvent.setEnabled(false);
                } else {
                    allDayEvent.setEnabled(true);
                }

                start.setTime(start.getTime() + 86400000);
            }
        };

        TimePickerDialog.OnTimeSetListener onStartTimeSetListener = new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                start.setHours(hourOfDay);
                start.setMinutes(minute);
                btnStartTimeInput.setText(kkmm.format(start.getTime()));
            }
        };

        DatePickerDialog.OnDateSetListener onEndDateSetListener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                String hour = new java.text.SimpleDateFormat("hh").format(start);
                String minute = new java.text.SimpleDateFormat("mm").format(start);
                end.setYear(year - 1900);
                end.setMonth(month);
                end.setDate(dayOfMonth);
                btnEndDateInput.setText(ddMMMM.format(end.getTime()));
                if (end.getTime() < start.getTime()) {
                    end.setTime(start.getTime());
                    if (currentDay == currentDayPlusOne)
                        btnEndDateInput.setText(ddMMMM.format(currentDay));
                    else
                        btnEndDateInput.setText(ddMMMM.format(end.getTime() - 86400000));
                    Toast.makeText(getContext(), "Die Endzeit darf nicht vor der Startzeit liegen.", Toast.LENGTH_SHORT).show();
                }
                if (end.getTime() > start.getTime()) {
                    allDayEvent.setChecked(true);
                    allDayEvent.setEnabled(false);
                } else {
                    allDayEvent.setEnabled(true);
                }
            }


        };

        TimePickerDialog.OnTimeSetListener onEndTimeSetListener = new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                end.setHours(hourOfDay);
                end.setMinutes(minute);
                btnEndTimeInput.setText(kkmm.format(end.getTime()));
                change[0] = true;
            }
        };

        btnStartDateInput.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String day = new java.text.SimpleDateFormat("dd").format(new java.util.Date(currentDay));
                String month = new java.text.SimpleDateFormat("MM").format(new java.util.Date(currentDay));
                String year = new java.text.SimpleDateFormat("yyyy").format(new java.util.Date(currentDay));
                DatePickerDialog datePickerDialog = new DatePickerDialog(
                        getContext(), R.style.DatePickerTheme, onStartDateSetListener, Integer.valueOf(year), Integer.valueOf(month) - 1, Integer.valueOf(day));
                datePickerDialog.setTitle(getString(R.string.chooseDate));
                datePickerDialog.setCanceledOnTouchOutside(false);
                datePickerDialog.show();
            }
        });

        btnStartTimeInput.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int hour = Integer.valueOf(new SimpleDateFormat("kk").format(new java.util.Date(currentDay)));
                int minute = Integer.valueOf(new SimpleDateFormat("mm").format(new java.util.Date(currentDay)));


                TimePickerDialog timePickerDialog = new TimePickerDialog(
                        getContext(), R.style.DatePickerTheme, onStartTimeSetListener, hour, minute, true);
                timePickerDialog.setTitle(getString(R.string.chooseTime));
                timePickerDialog.setCanceledOnTouchOutside(false);
                timePickerDialog.show();
            }
        });

        btnEndDateInput.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String day = new java.text.SimpleDateFormat("dd").format(start);
                String month = new java.text.SimpleDateFormat("MM").format(start);
                String year = new java.text.SimpleDateFormat("yyyy").format(start);
                DatePickerDialog datePickerDialog = new DatePickerDialog(
                        getContext(), R.style.DatePickerTheme, onEndDateSetListener, Integer.valueOf(year), Integer.valueOf(month) - 1, Integer.valueOf(day));
                datePickerDialog.setTitle(getString(R.string.chooseDate));
                datePickerDialog.setCanceledOnTouchOutside(false);
                datePickerDialog.show();
            }
        });

        btnEndTimeInput.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int hour = Integer.valueOf(new SimpleDateFormat("kk").format(new java.util.Date(currentDay)));
                int minute = Integer.valueOf(new SimpleDateFormat("mm").format(new java.util.Date(currentDay)));


                TimePickerDialog timePickerDialog = new TimePickerDialog(
                        getContext(), MainActivity.colorScheme, onEndTimeSetListener, hour, minute, true);
                timePickerDialog.setTitle(getString(R.string.chooseTime));
                timePickerDialog.setCanceledOnTouchOutside(false);
                timePickerDialog.show();
            }
        });

        btnExtra.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (extraLayout.getVisibility() != View.VISIBLE)
                    extraLayout.setVisibility(View.VISIBLE);
                else
                    extraLayout.setVisibility(View.GONE);
            }
        });

        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                bsd.cancel();
            }
        });

        bsd.setTitle(R.string.addEvent);

        btnOK.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (eventInput.getText().toString().isEmpty()) {
                    Util.createAlertDialog(getString(R.string.warning), getString(R.string.warningMessage), 0, getContext());
                    return;
                }

                int color = Color.argb(255, randomNumberGenerator(0, 200), randomNumberGenerator(50, 255), randomNumberGenerator(50, 255));

                String uid = String.valueOf(eventInput.getText().toString().hashCode());
                Event ev1;
                if (!allDayEvent.isChecked()) {
                    if (change[0] == false) {
                        ev1 = new Event(color, start.getTime(), "" + "°°" + locationInput.getText().toString() + "°°" + eventInput.getText().toString() + "°°" + uid);
                    } else {
                        ev1 = new Event(color, start.getTime(), end.getTime() + "°°" + locationInput.getText().toString() + "°°" + eventInput.getText().toString() + "°°" + uid);
                    }
                } else {
                    if (currentDay == currentDayPlusOne)
                        ev1 = new Event(color, start.getTime() - (start.getTime() % 86400000L) - 3600000, ((end.getTime() - (end.getTime() % 86400000L) - 3600000) + "°°" + locationInput.getText().toString() + "°°" + eventInput.getText().toString() + "°°" + uid));
                    else
                        ev1 = new Event(color, start.getTime() - (start.getTime() % 86400000L) - 3600000, ((end.getTime() - (end.getTime() % 86400000L) - 3600000+86400000) + "°°" + locationInput.getText().toString() + "°°" + eventInput.getText().toString() + "°°" + uid));

                }

                //compactCalendarView.addEvent(ev1);

                //TODO add to list
                manager.addPrivateEvent(ev1);

                bsd.cancel();
                refresh(compactCalendarView.getEvents(currentDay));
            }
        });
        bsd.setContentView(mView);
        bsd.show();
    }

    /**
     * creates a random number between two numbers
     *
     * @param rangeMin smallest possible number
     * @param rangeMax biggest possible number
     * @return random number between rangeMin and rangeMax
     */
    public static int randomNumberGenerator(int rangeMin, int rangeMax) {
        Random r = new Random();
        int createdRanNum = (int) Math.round(rangeMin + (rangeMax - rangeMin) * r.nextDouble());
        return (createdRanNum);
    }

}

