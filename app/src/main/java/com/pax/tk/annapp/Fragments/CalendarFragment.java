package com.pax.tk.annapp.Fragments;

import android.app.DatePickerDialog;
import android.app.Fragment;
import android.app.TimePickerDialog;
import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.BottomSheetDialog;
import android.support.design.widget.FloatingActionButton;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.TimePicker;

import com.github.sundeepk.compactcalendarview.CompactCalendarView;
import com.github.sundeepk.compactcalendarview.domain.Event;
import com.pax.tk.annapp.EventHandler;
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
        compactCalendarView = root.findViewById(R.id.compactcalendar_view);
        compactCalendarView.setUseThreeLetterAbbreviation(true);

        ImageButton monthForward = root.findViewById(R.id.monthForward);
        ImageButton monthBack = root.findViewById(R.id.monthBack);
        FloatingActionButton fabAdd = root.findViewById(R.id.fabAddCalendar);
        TextView monthIndication = root.findViewById(R.id.monthIndication);
        TextView dateIndication = root.findViewById(R.id.dateInformation);

        manager = Manager.getInstance();
        manager.setCompactCalendarView(compactCalendarView);

        monthIndication.setText(month.format(compactCalendarView.getFirstDayOfCurrentMonth()) + " - " + year.format(compactCalendarView.getFirstDayOfCurrentMonth()));

        EventHandler.loadSchoolEvents();


        for (Event e :
                manager.getSchoolEvents()) {
            manager.addSchoolEvent(e);
        }

        dateIndication.setText(ddMMMM.format(new Date(System.currentTimeMillis())));
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
                currentDay=dateClicked.getTime();
            }

            @Override
            public void onMonthScroll(Date firstDayOfNewMonth) {
                monthIndication.setText(month.format(compactCalendarView.getFirstDayOfCurrentMonth()) + " - " + year.format(compactCalendarView.getFirstDayOfCurrentMonth()));
                refresh(compactCalendarView.getEvents(firstDayOfNewMonth));
                dateIndication.setText(ddMMMM.format(firstDayOfNewMonth));
                currentDay=firstDayOfNewMonth.getTime();
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

    /*public void createInputDialog() {
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
    }*/

    private boolean refresh(List<Event> eventsThisDay) {
        TextView event = (TextView) root.findViewById(R.id.Event);
        LinearLayout eventList = (LinearLayout) root.findViewById(R.id.eventList);
        eventList.removeAllViews();
        if (eventsThisDay.isEmpty()) {
            event.setVisibility(View.VISIBLE);
            event.setText(R.string.noEventMessage);
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

            /*ImageButton deleteButton = eventView.findViewById(R.id.item_event_button_delete);
            int i;
            int y;
            int z = -1;
            for (i = 0; i < ownEvents.size(); i++) {
                if (eventsThisDay.get(x).equals(ownEvents.get(i))) {
                    deleteButton.setVisibility(View.VISIBLE);
                    for (y = 0; y < events.size(); y++) {
                        if (eventsThisDay.get(x).equals(events.get(y))) {
                            z = y;
                        }
                    }
                }
            }
            int posEv1 = z;
            deleteButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    askDelete(posEv1);
                }
            });*/
            eventList.addView(eventView);

        }
        return true;
    }

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
        final Button btnEndDateInput = (Button) mView.findViewById(R.id.endDateInput);
        final EditText locationInput = (EditText) mView.findViewById(R.id.locationInput);
        final Button btnExtra = (Button) mView.findViewById(R.id.btnExtra);
        final LinearLayout extraLayout = (LinearLayout) mView.findViewById(R.id.extraLayout);
        final FloatingActionButton btnOK = (FloatingActionButton) mView.findViewById(R.id.btnOK);
        final FloatingActionButton btnCancel = (FloatingActionButton) mView.findViewById(R.id.btnCancel);

        btnStartDateInput.setText(ddMMMM.format(currentDay));
        Date start = new Date(currentDay);
        Date end = new Date(currentDay);
        final boolean[] change = {false};

        DatePickerDialog.OnDateSetListener onStartDateSetListener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                String hour = new java.text.SimpleDateFormat("hh").format(new java.util.Date(currentDay));
                String minute = new java.text.SimpleDateFormat("mm").format(new java.util.Date(currentDay));
                start.setYear(year - 1900);
                start.setMonth(month);
                start.setDate(dayOfMonth);
                TimePickerDialog timePickerDialog = new TimePickerDialog(
                        getContext(), R.style.TimePickerTheme, onStartTimeSetListener, Integer.valueOf(hour), Integer.valueOf(minute), true);
                timePickerDialog.setTitle("Uhrzeit auswählen");
                timePickerDialog.setCanceledOnTouchOutside(false);
                timePickerDialog.show();
            }

            TimePickerDialog.OnTimeSetListener onStartTimeSetListener = new TimePickerDialog.OnTimeSetListener() {
                @Override
                public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                    start.setHours(hourOfDay);
                    start.setMinutes(minute);
                    btnStartDateInput.setText(ddMMMM.format(start.getTime()) + " " + kkmm.format(start.getTime()));
                }
            };
        };

        DatePickerDialog.OnDateSetListener onEndDateSetListener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                String hour = new java.text.SimpleDateFormat("hh").format(start);
                String minute = new java.text.SimpleDateFormat("mm").format(start);
                end.setYear(year - 1900);
                end.setMonth(month);
                end.setDate(dayOfMonth);
                TimePickerDialog timePickerDialog = new TimePickerDialog(
                        getContext(), R.style.TimePickerTheme, onEndTimeSetListener, Integer.valueOf(hour), Integer.valueOf(minute), true);
                timePickerDialog.setTitle("Uhrzeit auswählen");
                timePickerDialog.setCanceledOnTouchOutside(false);
                timePickerDialog.show();
            }

            TimePickerDialog.OnTimeSetListener onEndTimeSetListener = new TimePickerDialog.OnTimeSetListener() {
                @Override
                public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                    end.setHours(hourOfDay);
                    end.setMinutes(minute);
                    btnEndDateInput.setText(ddMMMM.format(end.getTime()) + " " + kkmm.format(end.getTime()));
                    change[0] = true;
                }
            };
        };

        btnStartDateInput.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String day = new java.text.SimpleDateFormat("dd").format(new java.util.Date(currentDay));
                String month = new java.text.SimpleDateFormat("MM").format(new java.util.Date(currentDay));
                String year = new java.text.SimpleDateFormat("yyyy").format(new java.util.Date(currentDay));
                DatePickerDialog datePickerDialog = new DatePickerDialog(
                        getContext(), R.style.TimePickerTheme, onStartDateSetListener, Integer.valueOf(year), Integer.valueOf(month) - 1, Integer.valueOf(day));
                datePickerDialog.setTitle(getString(R.string.chooseDate));
                datePickerDialog.setCanceledOnTouchOutside(false);
                datePickerDialog.show();
            }
        });

        btnEndDateInput.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String day = new java.text.SimpleDateFormat("dd").format(start);
                String month = new java.text.SimpleDateFormat("MM").format(start);
                String year = new java.text.SimpleDateFormat("yyyy").format(start);
                DatePickerDialog datePickerDialog = new DatePickerDialog(
                        getContext(), R.style.TimePickerTheme, onEndDateSetListener, Integer.valueOf(year), Integer.valueOf(month) - 1, Integer.valueOf(day));
                datePickerDialog.setTitle(getString(R.string.chooseDate));
                datePickerDialog.setCanceledOnTouchOutside(false);
                datePickerDialog.show();
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

                Random rnd = new Random();
                int color = Color.argb(255, rnd.nextInt(256), rnd.nextInt(256), rnd.nextInt(256));

                String uid = String.valueOf(eventInput.getText().toString().hashCode());
                Event ev1;
                if (change[0] == false) {
                    ev1 = new Event(color, start.getTime(), "" + "°°" + locationInput.getText().toString() + "°°" + eventInput.getText().toString() + "°°" + uid);
                } else {
                    ev1 = new Event(color, start.getTime(), end.getTime() + "°°" + locationInput.getText().toString() + "°°" + eventInput.getText().toString() + "°°" + uid);
                }
                compactCalendarView.addEvent(ev1);

                //TODO add to list

                bsd.cancel();
            }
        });
        bsd.setContentView(mView);
        bsd.show();
    }
}

