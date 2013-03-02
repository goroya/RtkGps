package ru0xdc.rtkgps.view;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

import ru0xdc.rtkgps.BuildConfig;
import ru0xdc.rtkgps.R;
import ru0xdc.rtklib.GTime;
import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.util.Log;
import android.widget.TextView;

public class GTimeView extends TextView {

	public final static int TIME_FORMAT_GPS = 0;

	public final static int TIME_FORMAT_UTC = 1;

	public final static int TIME_FORMAT_LOCAL = 2;

	public final static int TIME_FORMAT_GPS_TOW = 3;

	private final static int DEFAULT_TIME_FORMAT = TIME_FORMAT_LOCAL;

	private final static String DEFAULT_TIME_TEMPLATE = "yyyy/MM/dd HH:mm:ss.SSS";

	@SuppressWarnings("unused")
	private static final boolean DBG = BuildConfig.DEBUG & true;
	static final String TAG = GTimeView.class.getSimpleName();

	private int mTimeFormat;

	private String mTimeTemplate;

	private final GTime mGTime;

	private final Date mGTimeDate;

	private SimpleDateFormat mGTimeFormatter;

	public GTimeView(Context context) {
		this(context, null);
	}

    public GTimeView(Context context, AttributeSet attrs) {
        super(context, attrs);

        mGTime = new GTime();
        mGTimeDate = new Date();

        // process style attributes
        TypedArray a = context.obtainStyledAttributes(
                attrs, R.styleable.GTimeView, 0, 0);

        try {
        	mTimeFormat = a.getInt(R.styleable.GTimeView_time_format,
        			DEFAULT_TIME_FORMAT
        			);
        	mTimeTemplate = a.getString(R.styleable.GTimeView_time_template);
        	if (mTimeTemplate == null) mTimeTemplate = DEFAULT_TIME_TEMPLATE;
        }finally {
        	a.recycle();
        }

        updateTimeFormatter();
        updateTextViewValue();
    }

    public void setTimeFormat(int timeFormat) {
    	if (timeFormat != mTimeFormat) {
    		mTimeFormat = timeFormat;
    		updateTimeFormatter();
    		updateTextViewValue();
    		invalidate();
    	}
    }

    public void setTimeTemplate(String timeTemplate) {
    	if (timeTemplate != mTimeTemplate) {
    		mTimeTemplate = timeTemplate;
    		updateTimeFormatter();
    		updateTextViewValue();
    		invalidate();
    	}
    }

    public int getTimeFormat() {
    	return mTimeFormat;
    }

    public String getTimeTemplate() {
    	return mTimeTemplate;
    }

    public void setTime(GTime time) {
    	time.copyTo(mGTime);
    	updateTextViewValue();
    	invalidate();
    }

    private void updateTimeFormatter() {
    	switch (mTimeFormat) {
		case TIME_FORMAT_GPS:
			mGTimeFormatter = new SimpleDateFormat(mTimeTemplate, Locale.US);
			mGTimeFormatter.setTimeZone(TimeZone.getTimeZone("UTC"));
			break;
		case TIME_FORMAT_UTC:
			mGTimeFormatter = new SimpleDateFormat(mTimeTemplate, Locale.US);
			mGTimeFormatter.setTimeZone(TimeZone.getTimeZone("UTC"));
			break;
		case TIME_FORMAT_LOCAL:
			mGTimeFormatter = new SimpleDateFormat(mTimeTemplate, Locale.US);
			break;
		case TIME_FORMAT_GPS_TOW:
			mGTimeFormatter = null;
			break;
    	}
    }

    private void updateTextViewValue() {
    	String time;

    	try {
    		switch (mTimeFormat) {
    		case TIME_FORMAT_GPS:
    			mGTimeDate.setTime(mGTime.getGpsTimeMillis());
    			time = mGTimeFormatter.format(mGTimeDate)
    					+ " "
    					+ getResources().getString(R.string.gtime_format_gps)
    					;
    			break;
    		case TIME_FORMAT_UTC:
    			mGTimeDate.setTime(mGTime.getUtcTimeMillis());
    			time = mGTimeFormatter.format(mGTimeDate)
    					+ " "
    					+ getResources().getString(R.string.gtime_format_utc)
    					;
    			break;
    		case TIME_FORMAT_LOCAL:
    			mGTimeDate.setTime(mGTime.getUtcTimeMillis());
    			mGTimeFormatter.setTimeZone(TimeZone.getDefault());
    			time = mGTimeFormatter.format(mGTimeDate)
    					+ " "
    					+ getResources().getString(R.string.gtime_format_local)
    					;
    			break;
    		case TIME_FORMAT_GPS_TOW:
    			time = String.format(Locale.US,
    					"week %04d %.3f s", mGTime.getGpsWeek(),
    					mGTime.getGpsTow()
    					);
    			break;
    		default:
    			throw new IllegalStateException();
    		}
    	} catch (UnsatisfiedLinkError e) {
    		Log.e(TAG, "UnsatisfiedLinkError " + e);
    		time="time time time";
    	}

    	setText(time);
    }
}