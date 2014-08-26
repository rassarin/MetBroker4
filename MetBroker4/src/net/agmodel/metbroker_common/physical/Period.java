package net.agmodel.metbroker_common.physical;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.TimeZone;
import java.text.DateFormat;
import java.io.*;

/**
 * Represents a subset of time with an optional beginning and an optional end.
 * See {@link Interval Interval} for a more useful way of representing Periods
 * which have both beginning and ending times.<br>
 * For convenience, Periods implement the beginning and end of time as an early
 * Date (0 AD), and the latest Date supported by Java respectively. These have
 * the advantage of comparing sensibly with other dates. Calls to getBeginning()
 * and getEnd() will return these dates, but applications can avoid using them
 * with the hasBeginning and hasEnd() functions.<br>
 * Periods are immutable.<br>
 * 
 * @author Matthew Laurenson
 */
public class Period implements Serializable, Comparable, Cloneable {
	private static final long serialVersionUID = -4148421114846406885L;

	private static final DateFormat local = DateFormat.getDateTimeInstance(
			DateFormat.MEDIUM, DateFormat.MEDIUM);

	private Calendar cStart;
	private Calendar cEnd;
	
	/**
	 * 開始カレンダオブジェクトを返す。
	 * @return
	 */
	public Calendar getcStart() {
		return cStart;
	}

	/**
	 * 終了カレンダオブジェクトを返す。
	 * @return
	 */
	public Calendar getcEnd() {
		return cEnd;
	}

	/**
	 * デフォルトのタイムゾーンをシステムのデフォルトとする。
	 */
	private final TimeZone DEFAULT_TIMEZONE = TimeZone.getDefault();

	/** 
	 * 開始カレンダーオブジェクトと終了カレンダーオブジェクトからPeriodを構築する。
	 */
	public Period( Calendar aStart, Calendar anEnd ) {
		set( aStart, anEnd );
	}
	
	/**
	 * Constructs a Period from a starting and an ending Date (both non-null
	 * with ending date after starting date) After some experimentation I've let
	 * startdate==enddate - it is more useful.
	 * 
	 * @throws IllegalArgumentException
	 *             if aStart is after aEnd
	 *
	 * @deprecated
	 * @see #Period(Calendar, Calendar)
	 */
	public Period(Date aStart, Date anEnd) {
		set(aStart, anEnd);
	}

	/**
	 * 終了カレンダオブジェクトと期間情報からPeriodを構築する
	 * @param duration 終了日時までの期間
	 * @param anEnd 終了カレンダ
	 */
	public Period( Duration duration, Calendar anEnd ) {
		set( duration, anEnd );
	}
	
	/**
	 * Constructs a Period from a duration and an ending Date
	 * 
	 * @param duration
	 *            the duration of the period
	 * @param anEnd
	 *            the ending date of the period
	 *            @deprecated
	 *            @see #Period(Duration, Calendar)
	 */
	public Period(Duration duration, Date anEnd) {
		set(duration, anEnd);
	}

	/**
	 * 開始カレンダと期間情報からPeriodを構築する
	 * @param aStart 開始カレンダ
	 * @param duration 開始日時からの期間
	 */
	public Period( Calendar aStart, Duration duration ) {
		Date d = duration.addToDate(aStart.getTime());
		Calendar c = GregorianCalendar.getInstance(aStart.getTimeZone());
		c.setTime(d);
		set( aStart, c);
	}
	/**
	 * Constructs a Period from an starting Date and a duration
	 * 
	 * @param aStart
	 *            the starting date of the period
	 * @param duration
	 *            the duration of the period
	 *            @deprecated
	 *            @see #Period(Calendar, Duration)
	 */
	public Period(Date aStart, Duration duration) {
		Date anEnd = duration.addToDate(aStart);
		set(aStart, anEnd);
	}

	/**
	 * Constructs a Period from a date and an indicator as whether endless.<br>
	 * If endless is true, construct an period starting from aDate but not
	 * ending.<br>
	 * If endless is false, construct a period with no beginning ending at
	 * aDate.<br>
	 * 
	 * @param aDate
	 *            the starting or ending date of a one-ended interval
	 * @param endless
	 *            how to interpret aDate
	 *          
	 * @deprecated 
	 * @see #Period(Calendar, boolean)
	 */
	public Period(Date aDate, boolean endless) {
		if (endless)
			set(aDate, getEndOfTime());
		else
			set(getBeginningOfTime(), aDate);
	}

	/**
	 * 開始または終了日時に制限がないPeriodオブジェクトを構築する。<br>
	 * endlessが真の場合、終了日時に制限がない。偽の場合は開始日時に制限がない。
	 * 
	 * @param aDate 開始または終了カレンダ
	 * @param endless 開始または終了いずれかを使用するか制御する
	 */
	public Period( Calendar aDate, boolean endless ) {
		Calendar c = GregorianCalendar.getInstance( aDate.getTimeZone());
		if ( endless ) {
			c.setTime(getEndOfTime());
			set( aDate, c );
		} else{
			c.setTime(getBeginningOfTime());
			set( c, aDate);
		}
			
	}
	/**
	 * デフォルトのタイムゾーン、デフォルトの日時情報でPeriodを構築する。
	 * 
	 */
	public Period() {
		Calendar c1 = GregorianCalendar.getInstance( DEFAULT_TIMEZONE );
		Calendar c2 = GregorianCalendar.getInstance( DEFAULT_TIMEZONE );
		c1.setTime(getBeginningOfTime());
		c2.setTime(getEndOfTime());
		set(c1,c2);
	}

	/**
	 * Changes the Period start date. This method is made protected rather than
	 * private to allow MutableInterval to set the start and end fields.
	 * 
	 * @param aStart
	 *            the new start date for the Period (cloned), which must be
	 *            before the current ending date.
	 * @see #set(Date,Date)
	 * @deprecated
	 * @see #setStart(Calendar)
	 * 
	 */
	protected void setStart(Date aStart) {
		if (aStart.after(end)) {
			String explanation = "Period.setStart called with start date ("
					+ aStart.toString()
					+ ") later than the Period's existing end date ("
					+ end.toString() + ")";
			throw new IllegalArgumentException(explanation);
		}

		start = (Date) aStart.clone();
		cStart.setTime(start);		// sync corresponding calendar
	}
	
	protected void setStart( Calendar aStart) {
		if (aStart.after(cEnd)) {
			String explanation = "Period.setStart called with start date ("
					+ aStart.toString()
					+ ") later than the Period's existing end date ("
					+ end.toString() + ")";
			throw new IllegalArgumentException(explanation);
		}

		cStart = aStart;
		start = aStart.getTime(); // sync corresponging Date
		
	}

	/**
	 * Changes the Period end date. This method is made protected rather than
	 * private to allow MutableInterval to set the start and end fields.
	 * 
	 * @param anEnd
	 *            the new end date for the Period (cloned), which must be after
	 *            the current starting date,
	 * @see #set(Date,Date)
	 * @deprecated
	 * @see #setEnd(Calendar)
	 */
	protected void setEnd(Date anEnd) {
		if (anEnd.before(start)) {
			String explanation = "Period.setEnd called with end date ("
					+ anEnd.toString()
					+ ") earlier than the Period's existing start date ("
					+ start.toString() + ")";
			throw new IllegalArgumentException(explanation);
		}

		end = (Date) anEnd.clone();
		cEnd.setTime(end);
	}

/**
 * 終了カレンダを設定する。<br>
 * 開始カレンダよりも前であった場合は例外を発生する。
 * @param anEnd 終了カレンダ
 */
	protected void setEnd(Calendar anEnd) {
		if (anEnd.before(cStart)) {
			String explanation = "Period.setEnd called with end date ("
					+ anEnd.toString()
					+ ") earlier than the Period's existing start date ("
					+ start.toString() + ")";
			throw new IllegalArgumentException(explanation);
		}

		cEnd = anEnd;
		end = cEnd.getTime();
	}
	/**
	 * Sets the start and end date, ensuring that start is before end Calls
	 * {@link #setStart(Date)} and {@link #setEnd(Date)} which clone the
	 * parameters passed. Note that this method is called by the Period
	 * constructors. It is made protected rather than private to allow
	 * MutableInterval to set the start and end fields.
	 * 
	 * @deprecated 
	 * @see #set(Calendar, Calendar)
	 */
	protected void set(Date aStart, Date anEnd) {
		if (anEnd.before(aStart)) {
			String explanation = "Period constructor called with end date ("
					+ anEnd.toString() + ") earlier than start date ("
					+ aStart.toString() + ")";
			throw new IllegalArgumentException(explanation);
		}
		set( aStart, anEnd, DEFAULT_TIMEZONE);
		// NEW start = (Date) aStart.clone();
		// NEW end = (Date) anEnd.clone();
	}

	/** 
	 * 指定したタイムゾーン でカレンダを作成し、開始日時、終了日時を設定する。
	 * @param aStart
	 * @param anEnd
	 * @param tz
	 */
	protected void set( Date aStart, Date anEnd, TimeZone tz) {
		cStart = GregorianCalendar.getInstance(tz);
		cEnd = GregorianCalendar.getInstance(tz);
		cStart.setTime(aStart);
		cEnd.setTime(anEnd);
		start = (Date) aStart.clone();
		end = (Date) anEnd.clone();
	}
	
	/** 
	 * 開始日時、終了日時を設定する。
	 * @param aStart
	 * @param anEnd
	 */
	protected void set( Calendar aStart, Calendar anEnd ){
		cStart = aStart;
		cEnd = anEnd;
		start = aStart.getTime();
		end = anEnd.getTime();
	}
	
	/**
	 * Sets Period using an end date and duration Note that this method is
	 * protected rather than private to allow MutableInterval to set the start
	 * and end fields.
	 * @deprecated
	 * @see #set(Duration, Calendar)
	 */
	protected void set(Duration duration, Date anEnd) {
		Date aStart = duration.subtractFromDate(anEnd);
		set(aStart, anEnd, DEFAULT_TIMEZONE);
		// set(aStart, anEnd);
	}
	
	/**
	 * 期間情報と終了カレンダを元に設定する。
	 * @param duration
	 * @param anEnd
	 */
	protected void set( Duration duration, Calendar anEnd ) {
		Date aStart = duration.subtractFromDate(anEnd.getTime());
		Calendar c = (Calendar)anEnd.clone();
		c.setTime(aStart);
		set( c, anEnd);
	}

	/**
	 * Sets Period using an end date and duration Note that this method is
	 * protected rather than private to allow MutableInterval to set the start
	 * and end fields.
	 * @deprecated
	 * @see #set(Calendar, Duration)
	 */
	protected void set(Date aStart, Duration duration) {
		Date anEnd = duration.addToDate(aStart);
		set(aStart, anEnd, DEFAULT_TIMEZONE);
		// set(aStart, anEnd);
	}

	/**
	 * 開始カレンダと期間情報を元に設定する。
	 * @param aStart
	 * @param duration
	 */
	protected void set(Calendar aStart, Duration duration) {
		Date anEnd = duration.addToDate( aStart.getTime());
		Calendar c = (Calendar) aStart.clone();
		c.setTime(anEnd);
		set( aStart, c);
	}

	// The following comments won't display correctly unless a fixed-width font
	// is used).

	/**
	 * Indicates whether this period overlaps in time with another.<br>
	 * Returns true if this period
	 * 
	 * <pre>
	 * |-----|
	 * </pre>
	 * 
	 * overlaps in time with another
	 * 
	 * <pre>
	 * |=======|
	 * </pre>
	 * 
	 * passed as the parameter.<br>
	 * This overlap can take several forms but the method doesn't distinguish
	 * between them.<br>
	 * 
	 * <pre>
	 *   |---|
	 *  |======|
	 * </pre>
	 * 
	 * or
	 * 
	 * <pre>
	 *  |----|
	 *    |=====|
	 * </pre>
	 * 
	 * both return true.<br>
	 * <br>
	 * If the start of the one equals the end of the other, the method returns
	 * false.
	 * 
	 * eg
	 * 
	 * <pre>
	 *  |-----|
	 *        |======|
	 * </pre>
	 * 
	 * returns false, and
	 * 
	 * <pre>
	 *               |-----|
	 *        |======|
	 * 
	 * </pre>
	 * 
	 * returns false.<br>
	 * <br>
	 * This helps to avoid "double counting" of matching periods.
	 * 
	 * @param another
	 *            period to compare with this one
	 * @return true if the period passed overlaps with this period, false
	 *         otherwise
	 */
	public boolean coincidesWith(Period another) {
		if (getStart().before(another.getEnd())
				&& getEnd().after(another.getStart()))
			return (true);
		else
			return (false);
	}

	/**
	 * Indicates whether the input date falls within the partially closed
	 * interval (start,end] This method is consistent with coincides(Period);
	 * See
	 * {@link Period#coincidesWith(Date, boolean, boolean) coincidesWith(Date boolean, boolean)}
	 * for a more flexible version
	 * 
	 * @param input
	 *            the Date in question
	 * @return true if input is within the exclusive interval (start,end)
	 * @deprecated
	 * @see #coincidesWith(Calendar)
	 */
	public boolean coincidesWith(Date input) {
		if (getStart().before(input) && !getEnd().before(input))
			return true;
		else
			return false;
	}

	/**
	 * Indicates whether the input date falls within the partially closed
	 * interval (start,end] This method is consistent with coincides(Period);
	 * See
	 * {@link Period#coincidesWith(Date, boolean, boolean) coincidesWith(Date boolean, boolean)}
	 * for a more flexible version
	 * 
	 * @param input
	 *            the Date in question
	 * @return true if input is within the exclusive interval (start,end)
	 */
	public boolean coincidesWith( Calendar input ){
		if (getcStart().before(input) && !getcEnd().before(input))
			return true;
		else
			return false;
		
	}
	/**
	 * Indicates whether the input date falls within the interval See
	 * {@link Period#coincidesWith(Date) coincidesWith(Date)} for a simpler
	 * version
	 * 
	 * @param input
	 *            the Date in question
	 * @param includeStart
	 *            if input=Interval.start then also return true
	 * @param includeEnd
	 *            if input=Interval.end then also return true
	 * @return true if input is within the interval (start,end), (start,end],
	 *         [start,end) or [start,end]
	 *        @deprecated
	 *        @see #coincidesWith(Calendar, boolean, boolean)
	 */
	public boolean coincidesWith(Date input, boolean includeStart,
			boolean includeEnd) {
		if (getStart().before(input) && getEnd().after(input))
			return true;
		else if (includeStart && input.equals(getStart()))
			return true;
		else if (includeEnd && input.equals(getEnd()))
			return true;
		else
			return false;
	}

	/**
	 * Indicates whether the input date falls within the interval See
	 * {@link Period#coincidesWith(Date) coincidesWith(Date)} for a simpler
	 * version
	 * 
	 * @param input
	 *            the Date in question
	 * @param includeStart
	 *            if input=Interval.start then also return true
	 * @param includeEnd
	 *            if input=Interval.end then also return true
	 * @return true if input is within the interval (start,end), (start,end],
	 *         [start,end) or [start,end]
	 */
	public boolean coincidesWith(Calendar input, boolean includeStart,
			boolean includeEnd) {
		if (getcStart().before(input) && getcEnd().after(input))
			return true;
		else if (includeStart && input.equals(getcStart()))
			return true;
		else if (includeEnd && input.equals(getcEnd()))
			return true;
		else
			return false;
		
	}
	/**
	 * Indicates whether the Period begins
	 * 
	 * @return true if the Period has a beginning date, false if it doesn't
	 */
	public boolean hasBeginning() {
		return !start.equals(getBeginningOfTime());
	}

	/**
	 * Indicates whether the Period ends
	 * 
	 * @return true if the Period has an ending date, false if it doesn't
	 */
	public boolean hasEnd() {
		return !end.equals(getEndOfTime());
	}

	/**
	 * @return the starting date of the period. See
	 *         {@link Period#hasBeginning() hasBeginning} for a nice way to
	 *         handle periods with no end.
	 * @deprecated
	 * @see #getcStart()
	 */
	public Date getStart() {
		return (Date) start.clone();
	}

	/**
	 * @return the end of the period. See {@link Period#hasEnd() hasEnd} for a
	 *         nice way to handle periods with no end.
	 * @deprecated
	 * @see #getcEnd()
	 */
	public Date getEnd() {
		return (Date) end.clone();
	}

	public String toString() {
		String result = null;
		if (hasBeginning())
			result = local.format(getStart());
		else
			result = "<";

		result = result + " - ";

		if (hasEnd())
			result = result + local.format(getEnd());
		else
//			result = result = result + ">";
			result = result + ">";
		return result;
	}

	/**
	 * Compares two periods based on start time, and then end time if the start
	 * times are equal
	 * 
	 * @param a
	 *            Object to compare - assumed to be a Period
	 * @return -1 if this period starts before the other, or if the start times
	 *         are equal and this period ends sooner.
	 */
	public int compareTo(Object a) {
		Period aPeriod = (Period) a;
		if (getStart().before(aPeriod.getStart()))
			return -1;
		else if (getStart().after(aPeriod.getStart()))
			return 1;
		else if (getEnd().before(aPeriod.getEnd()))
			return -1;
		else if (getEnd().after(aPeriod.getEnd()))
			return 1;
		else
			return 0;
	}

	public Object clone() {
		// Interval i= new Interval(start,end);
		Period i = new Period(start, end);
		// I don't use getStart and getEnd because they clone the returned
		// dates, and the constructor clones the input Date parameters;
		return i;
	}

	/**
	 * A Date which will suffice in most situations as the beginning of time
	 * This implementation may change to an earlier date at a later date.
	 * 
	 * @return an early date (the birth of Christ approximately)
	 */
	public static Date getBeginningOfTime() {
		return new Date(earliest);
	}

	/**
	 * A Date corresponding to the end of time
	 * 
	 * @return latest date currently supported by Java
	 */
	public static Date getEndOfTime() {
		return new Date(latest);
	}

	/**
	 * Selects the earlier of two Dates Both Dates must be non-null. The dates
	 * aren't cloned.
	 * 
	 * @param a
	 *            one date
	 * @param b
	 *            another date
	 * @return the date which is before the other
	 */
	public static Date getEarlierOf(Date a, Date b) {
		if (a.before(b))
			return a;
		else
			return b;
	}

	/**
	 * Selects the later of two Dates Both Dates must be non-null. The dates
	 * aren't cloned.
	 * 
	 * @param a
	 *            one date
	 * @param b
	 *            another date
	 * @return the date which is after the other
	 */
	public static Date getLaterOf(Date a, Date b) {
		if (a.after(b))
			return a;
		else
			return b;
	}

	/**
	 * Creates a new Period which minimally encompasses both this Period and the
	 * other The new period starts from the earlier of this period.getStart()
	 * and another.getStart() and ends at the later of thisPeriod.getEnd() and
	 * another.getEnd();
	 * 
	 * @param another
	 *            the period to combine with this one
	 * @return a new Period covering the same period as both this and the other.
	 */
	public Period unionWith(Period another) {
		Date earliest = null;
		if (this.hasBeginning() && another.hasBeginning())
			earliest = getEarlierOf(start, another.getStart());

		Date latest = null;
		if (this.hasEnd() && another.hasEnd())
			latest = getLaterOf(end, another.getEnd());

		if (earliest == null)
			if (latest == null)
				return new Period();
			else
				return new Period(latest, false);
		else // earliest date exists
		if (latest == null)
			return new Period(earliest, true);
		else
			return new Period(earliest, latest);

	}

	/**
	 * Determines whether another Period lies within this one.
	 * 
	 * @param another
	 *            the period to test against this one
	 * @return true if the other period doesn't start earlier nor end later than
	 *         this one.
	 */
	public boolean encompasses(Period another) {
		return !(getEarlierOf(start, another.getStart()).before(start) || getLaterOf(
				end, another.getEnd()).after(end));

	}

	// I originally used null values for start and end, but think Mark II is an
	// improvement
	private static final long earliest;

	// set to the birth of Christ below because =0x8000000000000000L didn't seem
	// to work;
	private static final long latest = 0x7FFFFFFFFFFFFFFFL;

	private Date end;

	private Date start;

	static {
		java.util.Calendar c = java.util.Calendar.getInstance();
		c.clear();
		c.setTimeZone(java.util.TimeZone.getTimeZone("UTC"));
		c.set(0, 0, 1);
		earliest = c.getTime().getTime();
	}

}
