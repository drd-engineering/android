package com.makaya.drd;

import android.app.Dialog;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TimePicker;
import android.widget.Toast;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * Created by xbudi on 16/09/2016.
 */
public class PopupDateTime {
    private View view;
    int fieldId;
    public boolean isBackDated=false;
    public long maxDate=0;
    public String dateFormat="dd/MM/yyyy";

    public PopupDateTime(View view, int fieldId)
    {
        this.view=view;
        this.fieldId=fieldId;
    }

    public void Show(int templateId)
    {
        final Dialog dialog = new Dialog(view.getContext());
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(templateId);
        Button btnCancel = (Button)dialog.findViewById(R.id.btn_cancel);
        Button btnSet = (Button)dialog.findViewById(R.id.btn_set);
        final EditText field=(EditText)view.findViewById(fieldId);

        final int tmpId=templateId;
        if (tmpId == R.layout.popup_calendar) {
            DatePicker datePicker = (DatePicker) dialog.findViewById(R.id.datePicker);
            if (!isBackDated)
                datePicker.setMinDate(System.currentTimeMillis() - 1000);
            else if (maxDate!=0)
                datePicker.setMaxDate(maxDate);

        }else if (tmpId == R.layout.popup_time){
            TimePicker timePicker = (TimePicker) dialog.findViewById(R.id.timePicker);
            timePicker.setIs24HourView(true);
        }

        if (btnSet!=null) {
            btnSet.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {

                    if (tmpId == R.layout.popup_calendar) {
                        DatePicker datePicker = (DatePicker) dialog.findViewById(R.id.datePicker);

                        int day = datePicker.getDayOfMonth();
                        int month = datePicker.getMonth();
                        int year = datePicker.getYear();

                        Calendar cal = Calendar.getInstance();
                        cal.set(Calendar.YEAR, year);
                        cal.set(Calendar.MONTH, month);
                        cal.set(Calendar.DAY_OF_MONTH, day);

                        if (!isBackDated && cal.compareTo(Calendar.getInstance()) < 0) {
                            Toast.makeText(v.getContext(), "Date should be greater than or equal to today!",
                                    Toast.LENGTH_LONG).show();
                            return;
                        }

                        SimpleDateFormat sdf = new SimpleDateFormat(dateFormat);
                        String formatedDate = sdf.format(cal.getTime());
                        field.setText(formatedDate);
                    } else if (tmpId == R.layout.popup_time) {
                        TimePicker timePicker = (TimePicker) dialog.findViewById(R.id.timePicker);
                        int hour = timePicker.getCurrentHour();
                        int minute = timePicker.getCurrentMinute();

                        Calendar cal = Calendar.getInstance();
                        cal.set(Calendar.HOUR_OF_DAY, hour);
                        cal.set(Calendar.MINUTE, minute);

                        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm");
                        String formatedTime = sdf.format(cal.getTime());//hour+":"+minute;
                        field.setText(formatedTime);
                    }
                    dialog.dismiss();
                }

            });
        }
        if (btnCancel!=null) {
            btnCancel.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    dialog.dismiss();
                }
            });
        }
        if (!field.getText().toString().equals("")) {
            if (tmpId == R.layout.popup_calendar) {
                DatePicker datePicker = (DatePicker) dialog.findViewById(R.id.datePicker);
                SimpleDateFormat sdf = new SimpleDateFormat(dateFormat);
                Date date=new Date();
                try {
                    date = sdf.parse(field.getText().toString());
                }catch (ParseException ex){}


                datePicker.init(date.getYear()+1900, date.getMonth(), date.getDate(),null);
            } else if (tmpId == R.layout.popup_time) {
                TimePicker timePicker = (TimePicker) dialog.findViewById(R.id.timePicker);
                SimpleDateFormat sdf = new SimpleDateFormat("HH:mm");
                Date date = null;
                try {
                    date = sdf.parse(field.getText().toString());
                } catch (ParseException e) {
                }
                Calendar c = Calendar.getInstance();
                c.setTime(date);

                timePicker.setCurrentHour(c.get(Calendar.HOUR_OF_DAY));
                timePicker.setCurrentMinute(c.get(Calendar.MINUTE));
            }
        }
        dialog.show();
    }

    

}
