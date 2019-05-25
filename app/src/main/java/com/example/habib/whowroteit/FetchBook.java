package com.example.habib.whowroteit;

import android.os.AsyncTask;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.ref.WeakReference;

public class FetchBook extends AsyncTask<String, Void, String> {

    private WeakReference<TextView> mTitleText;
    private WeakReference<TextView> mAuthorText;

    FetchBook(TextView titleText, TextView authorText){
        this.mTitleText = new WeakReference <>( titleText );
        this.mAuthorText = new WeakReference <>( authorText );
    }
    @Override
    protected String doInBackground(String... strings) {
        return NetworksUtils.getBookInfo( strings[0] );
    }

    @Override
    protected void onPostExecute(String s) {
        super.onPostExecute( s );
    }
}
