package com.summer.evento;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.CalendarContract;
import android.provider.CalendarContract.Calendars;
import android.provider.CalendarContract.Events;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import java.text.DateFormat;
import java.util.Calendar;
import java.util.TimeZone;


public class CreateEvent extends Activity implements View.OnClickListener{
    private static String calanderURL = "content://com.android.calendar/calendars";
    private static String calanderEventURL = "content://com.android.calendar/events";
    private static String calanderRemiderURL = "content://com.android.calendar/reminders";
    
    DateFormat format_date = DateFormat.getDateInstance();
    DateFormat format_stime=DateFormat.getTimeInstance(DateFormat.SHORT);
    DateFormat format_etime=DateFormat.getTimeInstance(DateFormat.SHORT);
    Calendar calendar=Calendar.getInstance();
    TextView dateLabel;
    TextView stimeLabel, etimeLabel;
    Button btn_date;
    Button btn_stime, btn_etime;

    EditText location;
    EditText description;
    Button btn_create;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_event);

        dateLabel = (TextView)findViewById(R.id.date);
        btn_date=(Button)findViewById(R.id.datePicker);

        btn_date.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                // Perform action on click
                new DatePickerDialog(CreateEvent.this, date, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH),
                        calendar.get(Calendar.DAY_OF_MONTH)).show();
            }
        });

        stimeLabel = (TextView) findViewById(R.id.startTime);
        btn_stime = (Button) findViewById(R.id.startTimePicker);

        btn_stime.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                // Perform action on click
                new TimePickerDialog(CreateEvent.this, stime,
                        calendar.get(Calendar.HOUR_OF_DAY), calendar.get(Calendar.MINUTE), false).show();
            }
        });

        etimeLabel = (TextView) findViewById(R.id.endTime);
        btn_etime = (Button) findViewById(R.id.endTimePicker);

        btn_etime.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                // Perform action on click
                new TimePickerDialog(CreateEvent.this, etime,
                        calendar.get(Calendar.HOUR_OF_DAY), calendar.get(Calendar.MINUTE), false).show();
            }
        });

        location = (EditText)findViewById(R.id.eventLocation);
        description = (EditText)findViewById(R.id.eventDescription);
        btn_create = (Button) findViewById(R.id.createButton);

        // click create event
        btn_create.setOnClickListener(new View.OnClickListener() {

            public void onClick(View v) {

                EditText inputEventName = (EditText) findViewById(R.id.eventName);
                String eventName = inputEventName.getText().toString();
                String eventDescription = description.getText().toString();
                String eventLocation = location.getText().toString();
                String start_time = stimeLabel.getText().toString();
                String end_time = etimeLabel.getText().toString();
                

                String[] startT_fmt1 = start_time.split(" ");
                String[] endT_fmt1 = end_time.split(" ");
                
                String[] startT_fmt2 = startT_fmt1[0].split(":");
                String[] endT_fmt2 = endT_fmt1[0].split(":");
                int start_hour = Integer.parseInt(startT_fmt2[0]);
                int start_min = Integer.parseInt(startT_fmt2[1]);
                int end_hour = Integer.parseInt(endT_fmt2[0]);
                int end_min = Integer.parseInt(endT_fmt2[1]);

                if (startT_fmt1[1].equals("PM")) {
                	start_hour += 12;
                }
                if (endT_fmt1[1].equals("PM")) {
                	end_hour += 12;
                }
                
                Calendar beginTime = Calendar.getInstance();
                beginTime.set(2014, 9, 28, start_hour, start_min);
                Calendar endTime = Calendar.getInstance();
                endTime.set(2014, 9, 28, end_hour, end_min);
                Intent intent = new Intent(Intent.ACTION_INSERT)
                        .setData(Events.CONTENT_URI)
                        .putExtra(CalendarContract.EXTRA_EVENT_BEGIN_TIME, beginTime.getTimeInMillis())
                        .putExtra(CalendarContract.EXTRA_EVENT_END_TIME, endTime.getTimeInMillis())
                        .putExtra(Events.TITLE, eventName)
                        .putExtra(Events.DESCRIPTION, eventDescription)
                        .putExtra(Events.EVENT_LOCATION, eventLocation)
                        .putExtra(Events.AVAILABILITY, Events.AVAILABILITY_BUSY);
                startActivity(intent);
                
                //Toast.makeText(CreateEvent.this, "Event added", Toast.LENGTH_LONG).show();

            }
        });

        // get information from image
        Intent prevIntent = getIntent();
        if (prevIntent.hasExtra("recognizedText")) {
        	//Parse parseTool = new Parse();
        	String recognizedText = prevIntent.getStringExtra("recognizedText");
        	System.out.println(recognizedText);
        	
        	String[] times = Parse.parseTime(recognizedText);
        	stimeLabel.setText(times[0]);
        	etimeLabel.setText(times[1]);
        	location.setText(Parse.parseLocation(recognizedText));
        	dateLabel.setText(Parse.parseDate(recognizedText));
        }
    }


    DatePickerDialog.OnDateSetListener date = new DatePickerDialog.OnDateSetListener() {

        @Override
        public void onDateSet(DatePicker view, int year, int monthOfYear,
                              int dayOfMonth) {
            // TODO Auto-generated method stub
            calendar.set(Calendar.YEAR, year);
            calendar.set(Calendar.MONTH, monthOfYear);
            calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
            dateLabel.setText(format_date.format(calendar.getTime())); //update Date
        }

    };

    TimePickerDialog.OnTimeSetListener stime = new TimePickerDialog.OnTimeSetListener() {

        @Override
        public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
            calendar.set(Calendar.HOUR_OF_DAY, hourOfDay);
            calendar.set(Calendar.MINUTE, minute);
            stimeLabel.setText(format_stime.format(calendar.getTime())); //update Stime
        }
    };

    TimePickerDialog.OnTimeSetListener etime = new TimePickerDialog.OnTimeSetListener() {

        @Override
        public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
            calendar.set(Calendar.HOUR_OF_DAY, hourOfDay);
            calendar.set(Calendar.MINUTE, minute);
            etimeLabel.setText(format_etime.format(calendar.getTime())); //update Etime
        }
    };

    public void onClick(View v) {

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.create_event, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }


    private void initCalendars() {

        TimeZone timeZone = TimeZone.getDefault();
        ContentValues value = new ContentValues();
        value.put(Calendars.NAME, "yy");

        value.put(Calendars.ACCOUNT_NAME, "mengdu@umich.edu");
        value.put(Calendars.ACCOUNT_TYPE, "com.android.exchange");
        value.put(Calendars.CALENDAR_DISPLAY_NAME, "mytt");
        value.put(Calendars.VISIBLE, 1);
        value.put(Calendars.CALENDAR_COLOR, -9206951);
        value.put(Calendars.CALENDAR_ACCESS_LEVEL, Calendars.CAL_ACCESS_OWNER);
        value.put(Calendars.SYNC_EVENTS, 1);
        value.put(Calendars.CALENDAR_TIME_ZONE, timeZone.getID());
        value.put(Calendars.OWNER_ACCOUNT, "mengdu@umich.edu");
        value.put(Calendars.CAN_ORGANIZER_RESPOND, 0);

        Uri calendarUri = Calendars.CONTENT_URI;
        calendarUri = calendarUri.buildUpon()
                .appendQueryParameter(CalendarContract.CALLER_IS_SYNCADAPTER, "true")
                .appendQueryParameter(Calendars.ACCOUNT_NAME, "mengdu@umich.edu")
                .appendQueryParameter(Calendars.ACCOUNT_TYPE, "com.android.exchange")    
                .build();

        getContentResolver().insert(calendarUri, value);
    }




}
