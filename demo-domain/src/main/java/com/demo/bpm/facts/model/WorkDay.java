package com.demo.bpm.facts.model;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import javax.validation.constraints.NotNull;


import com.demo.bpm.facts.model.validators.ValidWorkDate;
import static com.demo.bpm.shared.DemoBPMConstants.WORK_DATE_FORMAT;

public class WorkDay
        extends AbstractFact
        implements Comparable<WorkDay>
{
    public static final String WORK_DAY = "WorkDay";
    private static final long serialVersionUID = 3108457365704606617L;
    private static final Date DEFAULT_START_TIME;
    private static final Date DEFAULT_END_TIME;
    @NotNull
    @ValidWorkDate
    private Date date;
    private Date systemStartTime;
    private Date systemEndTime;

    static
    {
        try
        {
            DEFAULT_START_TIME = new SimpleDateFormat("HH:mm:ss").parse("09:30:00");
            DEFAULT_END_TIME = new SimpleDateFormat("HH:mm:ss").parse("16:00:00");
        } catch (ParseException e)
        {
            throw new RuntimeException(e);
        }
    }

    // enforce time of day fields to be on the current demo date
    private static Date setTimeOfDayFieldsOnWorkDate(Date sourceDate, Date sourceTimeOfDay)
    {
        Calendar instance = Calendar.getInstance();
        instance.setTime(sourceDate);
        Calendar source = Calendar.getInstance();
        source.setTime(sourceTimeOfDay);

        instance.set(Calendar.HOUR, source.get(Calendar.HOUR));
        instance.set(Calendar.MINUTE, source.get(Calendar.MINUTE));
        instance.set(Calendar.SECOND, source.get(Calendar.SECOND));
        instance.set(Calendar.MILLISECOND, source.get(Calendar.MILLISECOND));
        return instance.getTime();
    }

    public WorkDay()
    {
        super(WORK_DAY);
        this.date = new Date();
        this.systemStartTime = setTimeOfDayFieldsOnWorkDate(this.date, DEFAULT_START_TIME);
        this.systemEndTime = setTimeOfDayFieldsOnWorkDate(this.date, DEFAULT_END_TIME);

    }

    public WorkDay(String dateStr)
    {
        super(dateStr);
        try
        {
            this.date = new SimpleDateFormat(WORK_DATE_FORMAT).parse(dateStr);
        } catch (ParseException e)
        {
            throw new RuntimeException(e);
        }
        this.systemStartTime = setTimeOfDayFieldsOnWorkDate(this.date, DEFAULT_START_TIME);
        this.systemEndTime = setTimeOfDayFieldsOnWorkDate(this.date, DEFAULT_END_TIME);
    }

    public int compareTo(WorkDay o)
    {
        if (o == null)
            return 1;
        String myDate = new SimpleDateFormat(WORK_DATE_FORMAT).format(date);
        String otherDate = new SimpleDateFormat(WORK_DATE_FORMAT).format(o.date);
        return myDate.compareTo(otherDate);
    }

    @Override
    public boolean equals(Object o)
    {
        if ( ! super.equals(o) )
            return false;

        if (!(o instanceof WorkDay)) return false;
        WorkDay workDay = (WorkDay) o;
        if (!date.equals(workDay.date)) return false;

        return true;
    }

    @Override
    public int hashCode()
    {
        return 31 * super.hashCode() + date.hashCode();
    }

    @Override
    public String toString()
    {
        // this is a special, undeletable fact, so I don't expose the ID anywhere
        return "WorkDay[" + new SimpleDateFormat(WORK_DATE_FORMAT).format(date) + ']';
    }

    public Date getDate()
    {
        return date;
    }

    public void setDate(Date date)
    {
        // only the Date fields are relevant, zero out the time fields
        Calendar instance = Calendar.getInstance();
        instance.setTime(date);
        instance.set(Calendar.HOUR, 0);
        instance.set(Calendar.MINUTE, 0);
        instance.set(Calendar.SECOND, 0);
        instance.set(Calendar.MILLISECOND, 0);
        // just with Date fields set
        this.date = instance.getTime();
    }

    public Date getSystemStartTime()
    {
        return systemStartTime;
    }

    public void setSystemStartTime(Date systemStartTime)
    {
        this.systemStartTime = setTimeOfDayFieldsOnWorkDate(date, systemStartTime);
    }

    public Date getSystemEndTime()
    {
        return systemEndTime;
    }

    public void setSystemEndTime(Date systemEndTime)
    {
        this.systemEndTime = setTimeOfDayFieldsOnWorkDate(date, systemEndTime);
    }

}
