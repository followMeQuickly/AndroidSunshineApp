package com.example.android.sunshine;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.os.DeadObjectException;
import android.support.v4.widget.CursorAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.android.sunshine.ForecastFragment;
import com.example.android.sunshine.R;
import com.example.android.sunshine.Utility;
import com.example.android.sunshine.data.WeatherContract;

/**
 * {@link ForecastAdapter} exposes a list of weather forecasts
 * from a {@link android.database.Cursor} to a {@link android.widget.ListView}.
 */
public class ForecastAdapter extends CursorAdapter {
    public ForecastAdapter(Context context, Cursor c, int flags) {
        super(context, c, flags);
    }

    /**
     * Prepare the weather high/lows for presentation.
     */
    private String formatHighLows(double high, double low) {
        boolean isMetric = Utility.isMetric(mContext);
        String highLowStr = Utility.formatTemperature(high, isMetric) + "/" + Utility.formatTemperature(low, isMetric);
        return highLowStr;
    }

    /*
        This is ported from FetchWeatherTask --- but now we go straight from the cursor to the
        string.
     */
    private String convertCursorRowToUXFormat(Cursor cursor) {
        // get row indices for our cursor


        String highAndLow = formatHighLows(
                cursor.getDouble(ForecastFragment.COL_WEATHER_MAX_TEMP),
                cursor.getDouble(ForecastFragment.COL_WEATHER_MIN_TEMP));

        return Utility.formatDate(cursor.getLong(ForecastFragment.COL_WEATHER_DATE)) +
                " - " + cursor.getString(ForecastFragment.COL_WEATHER_DESC) +
                " - " + highAndLow;
    }

    private static final int VIEW_TYPE_COUNT = 2;
    private static final int VIEW_TYPE_TODAY = 0;
    private static final int VIEW_TYPE_fUTURE_DAY = 1;

    @Override
    public int getItemViewType(int position) {
        return position == 0 ? VIEW_TYPE_TODAY : VIEW_TYPE_fUTURE_DAY;
    }

    @Override
    public int getViewTypeCount()
    {
        return VIEW_TYPE_COUNT;
    }
    /*
        Remember that these views are reused as needed.
     */
    @Override
    public View newView(Context context, final Cursor cursor, ViewGroup parent) {

        int viewType = getItemViewType(cursor.getPosition());
        int layoutId = -1;

        switch (viewType)
        {
            case VIEW_TYPE_TODAY:
            {
                layoutId = R.layout.list_item_forecast_today;
                break;
            }
            case VIEW_TYPE_fUTURE_DAY:
            {
                layoutId = R.layout.list_item_forecast;
                break;
            }
        }
        View view =   LayoutInflater.from(context).inflate(layoutId, parent, false);
        ViewHolder viewHolder = new ViewHolder(view);
        view.setTag(viewHolder);
        return view;

    }

    public static class ViewHolder {
        public final ImageView iconView;
        public final TextView dateView;
        public final TextView descriptionView;
        public final TextView highTempView;
        public final TextView lowTempView;

        public ViewHolder(View view) {
            iconView = (ImageView) view.findViewById(R.id.list_item_icon);
            dateView = (TextView) view.findViewById(R.id.list_item_date_textview);
            descriptionView = (TextView) view.findViewById(R.id.list_item_forecast_textview);
            highTempView = (TextView) view.findViewById(R.id.list_item_high_textview);
            lowTempView = (TextView) view.findViewById(R.id.list_item_low_textview);
        }
    }

    /*
        This is where we fill-in the views with the contents of the cursor.
     */
    @Override
    public void bindView(View view, Context context, Cursor cursor) {
        // our view is pretty simple here --- just a text view
        // we'll keep the UI functional with a simple (and slow!) binding
        ViewHolder viewHolder = (ViewHolder) view.getTag();


        ImageView iconImage = viewHolder.iconView;
        int viewType = getItemViewType(cursor.getPosition());
        switch (viewType)
        {
            case VIEW_TYPE_TODAY:
                iconImage.setImageResource(
                        Utility.getArtResourceForWeatherCondition(
                                cursor.getInt(ForecastFragment.COL_WEATHER_CONDITION_ID)));
                break;
            case VIEW_TYPE_fUTURE_DAY:
                iconImage.setImageResource(
                        Utility.getIconResourceForWeatherCondition(
                                cursor.getInt(ForecastFragment.COL_WEATHER_CONDITION_ID)));

                break;
        }


        TextView dateView = viewHolder.dateView;
        long dateInMillis = cursor.getLong(ForecastFragment.COL_WEATHER_DATE);
        String x = Utility.getFriendlyDayFormat(context, dateInMillis);
        dateView.setText(x);

        TextView forecastView = viewHolder.descriptionView;
        String forecastValue = cursor.getString(ForecastFragment.COL_WEATHER_DESC);
        forecastView.setText(forecastValue);

        TextView highView = viewHolder.highTempView;
        String highValue = cursor.getString(ForecastFragment.COL_WEATHER_MAX_TEMP);
        highView.setText(highValue);

        TextView lowView = viewHolder.lowTempView;
        String lowValue = cursor.getString(ForecastFragment.COL_WEATHER_MIN_TEMP);
        lowView.setText(lowValue);


        //tv.setText(convertCursorRowToUXFormat(cursor));
    }
}