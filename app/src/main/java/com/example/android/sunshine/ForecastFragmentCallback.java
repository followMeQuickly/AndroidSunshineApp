package com.example.android.sunshine;

import android.net.Uri;

/**
 * Created by chaseland on 10/8/16.
 */

public interface ForecastFragmentCallback {

    void onItemSelected(Uri dateUri);
}
