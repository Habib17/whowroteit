package com.example.habib.whowroteit;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class MainActivity extends AppCompatActivity  implements LoaderManager.LoaderCallbacks<String>{
    private EditText mBookInput;
    private TextView mTitleText;
    private TextView mAuthortext;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate( savedInstanceState );
        setContentView( R.layout.activity_main );

        mBookInput = findViewById( R.id.bookInput );
        mTitleText = findViewById( R.id.titleText );
        mAuthortext = findViewById( R.id.authorText );

        if(getSupportLoaderManager().getLoader( 0 ) != null){
            getSupportLoaderManager().initLoader( 0, null, this );
        }


    }

    public void searchBook(View view) {
        //mendapatkan string dari input field
        String queryString = mBookInput.getText().toString();
       // new FetchBook( mTitleText, mAuthortext ).execute( queryString );

        InputMethodManager inputManager = (InputMethodManager)
                getSystemService( Context.INPUT_METHOD_SERVICE );
        if(inputManager != null){
            inputManager.hideSoftInputFromWindow (view.getWindowToken(),
                    InputMethodManager.HIDE_NOT_ALWAYS );
        }



        ConnectivityManager connMgr = (ConnectivityManager)
                getSystemService( Context.CONNECTIVITY_SERVICE );
        NetworkInfo networkInfo = null;
        if (networkInfo != null){
            networkInfo = connMgr.getActiveNetworkInfo();

        }

        if(networkInfo != null && networkInfo.isConnected() && queryString.length() != 0){
         //   new FetchBook( mTitleText, mAuthortext ).execute( queryString );
            mAuthortext.setText( "" );
            mTitleText.setText( R.string.loading );
        }
        else {
            if(queryString.length() == 0){
                mAuthortext.setText( "" );
                mTitleText.setText( R.string.no_search_term );
            }
            else {
                mAuthortext.setText( "" );
                mTitleText.setText( R.string.no_network );
            }

        }

        Bundle queryBundle =  new Bundle(  );
        queryBundle.putString( "queryString", queryString );
        getSupportLoaderManager().restartLoader( 0, queryBundle,  this );
    }


    @NonNull
    @Override
    public Loader<String> onCreateLoader(int id, @Nullable Bundle args) {
        String queryString = "";

        if (args != null) {
            queryString = args.getString("queryString");
        }

        return new BookLoader(this, queryString);
    }

    @Override
    public void onLoadFinished(Loader <String> loader, String data) {
        try {
            JSONObject jsonObject = new JSONObject( data );
            JSONArray itemsArray = jsonObject.getJSONArray( "items" );

            int i = 0;
            String title = null;
            String authors = null;
            // Cari hasil di array item, keluar
            // saat judul dan pengarang
            // ditemukan atau ketika semua item sudah diperiksa
            while (i < itemsArray.length() && (authors == null && title == null)){
                //mendapatkan informasi current item
                JSONObject book = itemsArray.getJSONObject(  i);
                JSONObject volumeInfo = book.getJSONObject("volumeInfo");

                //mencoba mendapatkan author dan title dari current item
                //catch jika field lain kosong dan move on
                try {
                    title = volumeInfo.getString( "title" );
                    authors = volumeInfo.getString( "authors" );
                }
                catch (Exception e){
                    e.printStackTrace();
                }

                //pindah ke next item
                i++;




            }

            // If both are found, display the result.
            if (title != null && authors != null) {
                mTitleText.setText(title);
                mAuthortext.setText(authors);
                //mBookInput.setText("");
            } else {
                // If none are found, update the UI to show failed results.
                mTitleText.setText(R.string.no_results);
                mAuthortext.setText("");
            }
        }
        catch (JSONException e){

            e.printStackTrace();

        }
    }

    @Override
    public void onLoaderReset(Loader <String> loader) {

    }
}
