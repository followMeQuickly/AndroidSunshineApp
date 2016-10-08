package com.example.android.sunshine;

import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.widget.ShareActionProvider;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.android.sunshine.data.WeatherContract;

/**
 * Created by Chase on 9/26/2016.
 */
public class DetailFragment extends Fragment implements LoaderManager.LoaderCallbacks<Cursor>{

    private static final String LOG_TAG = DetailFragment.class.getSimpleName();
    private static final String FORECAST_SHARE_HASHTAG ="#SSSUUUNNNNSSHHHIIIINNNNNEEE";

    static String DETAIL_URI = "URI";
    private ShareActionProvider mShareActionProvider;
    private static final String[] FORECAST_COLUMNS = {
            WeatherContract.WeatherEntry.TABLE_NAME + "." + WeatherContract.WeatherEntry._ID,
            WeatherContract.WeatherEntry.COLUMN_DATE,
            WeatherContract.WeatherEntry.COLUMN_SHORT_DESC,
            WeatherContract.WeatherEntry.COLUMN_MAX_TEMP,
            WeatherContract.WeatherEntry.COLUMN_MIN_TEMP,
            WeatherContract.WeatherEntry.COLUMN_HUMIDITY,
            WeatherContract.WeatherEntry.COLUMN_WIND_SPEED,
            WeatherContract.WeatherEntry.COLUMN_DEGREES,
            WeatherContract.WeatherEntry.COLUMN_PRESSURE,
            WeatherContract.WeatherEntry.COLUMN_WEATHER_ID

    };

    private static final int DETAIL_LOADER = 0;

    private static final int COL_WEATHER_ID = 0;
    private static final int COL_WEATHER_DATE = 1;
    private static final int COL_WEATHER_DESC = 2;
    private static final int COL_WEATHER_MAX_TEMP = 3;
    private static final int COL_WEATHER_MIN_TEMP = 4;
    private static final int COL_HUMIDITY = 5;
    private static final int COL_WIND_SPEED = 6;
    private static final int COL_DEGREES = 7;
    private static final int COL_PRESSURE = 8;
    private static final int COL_WEATHER_DESC_ID = 9;




    private TextView dateView;
    private TextView highDegreeView;
    private ImageView image;
    private TextView descriptionView;
    private TextView lowDegreeView;
    private TextView humidityView;
    private TextView windSpeedView;
    private TextView degreesView;
    private TextView pressureView;
    private TextView friendlyDateView;
    private String forecast;

    private Uri mUri;

    public DetailFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        Bundle arguments = getArguments();
        if(arguments != null)
        {
            mUri = arguments.getParcelable(DETAIL_URI);
        }
        View rootView = inflater.inflate(R.layout.fragment_detail, container, false);



        dateView = (TextView)rootView.findViewById(R.id.selected_date);
        highDegreeView = (TextView)rootView.findViewById(R.id.selected_high_temp);
        descriptionView = (TextView) rootView.findViewById(R.id.selected_forecast);
        image = (ImageView) rootView.findViewById(R.id.selected_forecast_imageview);
        lowDegreeView = (TextView) rootView.findViewById(R.id.selected_low_temp);
        humidityView = (TextView) rootView.findViewById(R.id.selected_humidity);
        pressureView = (TextView) rootView.findViewById(R.id.selected_pressure);
        friendlyDateView = (TextView) rootView.findViewById(R.id.selected_day);
        windSpeedView = (TextView) rootView.findViewById(R.id.selected_wind);



        setHasOptionsMenu(true);

        return rootView;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.detail_fragment, menu);
        MenuItem item = menu.findItem(R.id.action_share);
        mShareActionProvider = (ShareActionProvider) MenuItemCompat.getActionProvider(item);

        if (mShareActionProvider != null) {
            mShareActionProvider.setShareIntent(createShareForecastIntent());
        } else {
            //logging should be here.
        }
    }

    private Intent createShareForecastIntent() {

        Intent shareIntent = new Intent(Intent.ACTION_SEND);
        shareIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_WHEN_TASK_RESET);
        shareIntent.setType("text/plain");
        shareIntent.putExtra(Intent.EXTRA_TEXT, forecast + FORECAST_SHARE_HASHTAG);
        return shareIntent;

    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        getLoaderManager().initLoader(DETAIL_LOADER, null, this);
        super.onActivityCreated(savedInstanceState);
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        if(mUri != null)
        {
            return new CursorLoader(
                    getActivity(),
                    mUri,
                    FORECAST_COLUMNS,
                    null,
                    null,
                    null
            );
        }
        return null;
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        Log.v(LOG_TAG, "In onLoadFinished");
        if (!data.moveToFirst()) { return; }

        String dateString = Utility.formatDate(
                data.getLong(COL_WEATHER_DATE));


        dateView.setText(dateString);

        String weatherDescription =
                data.getString(COL_WEATHER_DESC);

        boolean isMetric = Utility.isMetric(getActivity());

        String high = Utility.formatTemperature(
                data.getDouble(COL_WEATHER_MAX_TEMP), isMetric);

        String low = Utility.formatTemperature(
                data.getDouble(COL_WEATHER_MIN_TEMP), isMetric);


        Float humidity = data.getFloat(COL_HUMIDITY);
        humidityView.setText(humidity.toString());
        long date = data.getLong(COL_WEATHER_DATE);
        friendlyDateView.setText(Utility.getFriendlyDayFormat(getActivity(), date));
        highDegreeView.setText(high);


        lowDegreeView.setText(low);


        descriptionView.setText(weatherDescription);

        image.setImageResource(Utility.getArtResourceForWeatherCondition(data.getInt(COL_WEATHER_DESC_ID)));


        float windSpeedStr = data.getFloat(COL_WIND_SPEED);
                    float windDirStr = data.getFloat(COL_DEGREES);
                   windSpeedView.setText(Utility.getFormattedWind(getActivity(), windSpeedStr, windDirStr));


        Float pressure = data.getFloat(COL_PRESSURE);
                   pressureView.setText(pressure.toString());
        //TextView detailTextView = (TextView)getView().findViewById(R.id.detail_text_view);
        //detailTextView.setText(forecast);

        // If onCreateOptionsMenu has already happened, we need to update the share intent now.
        if (mShareActionProvider != null) {
            mShareActionProvider.setShareIntent(createShareForecastIntent());
        }
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {

    }

    void onLocationChanged( String newLocation ) {
        // replace the uri, since the location has changed
        Uri uri = mUri;
        if (null != uri) {
            long date = WeatherContract.WeatherEntry.getDateFromUri(uri);
            Uri updatedUri = WeatherContract.WeatherEntry.buildWeatherLocationWithDate(newLocation, date);
            mUri = updatedUri;
            getLoaderManager().restartLoader(DETAIL_LOADER, null, this);
        }
    }

}