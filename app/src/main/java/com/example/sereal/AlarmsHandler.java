package com.example.sereal;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Calendar;
import java.util.List;


// BASED ON CODE AVAILABLE AT https://codinginflow.com/tutorials/android/alarmmanager
class AlarmsHandler {

    // set alarm associated with card id
    public static void SetAlarm(Context context, int id, String title, LocalTime time)
    {
        // setting up time input
        Calendar c = Calendar.getInstance();
        c.set(Calendar.HOUR_OF_DAY, time.getHour());
        c.set(Calendar.MINUTE, time.getMinute());
        c.set(Calendar.SECOND, 0);

        // don't create alarms that have already passed
        if(c.before(Calendar.getInstance()))
        {
            return;
        }

        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent(context, NotificationReceiver.class);

        // Put our alarm's text here
        intent.putExtra("title", title);

        // create our alarm, allowing to break through idle/doze mode to alert user
        PendingIntent pending = PendingIntent.getBroadcast(context, id, intent,0);
        alarmManager.setExactAndAllowWhileIdle(AlarmManager.RTC_WAKEUP, c.getTimeInMillis(), pending);
    }

    // cancel alarm associated with card id
    public static void CancelAlarm(Context context, int id)
    {
        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent(context, NotificationReceiver.class);
        PendingIntent pending = PendingIntent.getBroadcast(context, id, intent,0);

        alarmManager.cancel(pending);
    }

    public static void SetAllAlarms(Context context)
    {
        // reset today's alarms
        List<CardStruct> cards = new CardsDB(context).getCardsOnDay(AlarmsHandler.getTodayOfWeek());

        for (CardStruct c : cards)
        {
            // set/cancel alarms. Doing it here should make most sense
            if(c.isAlarm())
            {
                AlarmsHandler.SetAlarm(context, c.getID(), c.getTitle(),c.getTime());
            } else
            {
                AlarmsHandler.CancelAlarm(context, c.getID());
            }
        }
    }

    // CODE SNIPPET FROM: https://www.baeldung.com/java-get-day-of-week
    // Returns the day of the week as an int 0 (Mon) to 6 (Sun)
    public static int getTodayOfWeek() {
        DayOfWeek day = LocalDate.now().getDayOfWeek();
        return day.getValue() - 1;
    }
    // END OF CODE SNIPPET

}
