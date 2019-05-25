package com.example.habib.whowroteit;

import android.net.Uri;
import android.util.Log;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;

import javax.net.ssl.HttpsURLConnection;

public class NetworksUtils {
    private static final String LOG_TAG = NetworksUtils.class.getSimpleName();
    //base url untuk api book
    private static final String BOOK_BASE_URL = "https://www.googleapis.com/books/v1/volumes?";
    //parameter untuk search string
    private static final String QUERY_PARAM = "q";
    //parameter untuk batas search
    private static final String MAX_RESULT = "maxResults";
    //parameter untuk memfilter print type
    private static  final String PRINT_TYPE = "printType";
    static String getBookInfo(String queryString) {
        HttpsURLConnection urlConnection = null;
        BufferedReader reader = null;
        String bookJSONString = null;
        try {
            Uri builtURI = Uri.parse( BOOK_BASE_URL ).buildUpon()
                    .appendQueryParameter( QUERY_PARAM, queryString )
                    .appendQueryParameter( MAX_RESULT, "10")
                    .appendQueryParameter( PRINT_TYPE, "books")
                    .build();
            URL requestURL = new URL( builtURI.toString() );

            urlConnection = (HttpsURLConnection) requestURL.openConnection();
            urlConnection.setRequestMethod( "GET" );
            urlConnection.connect();

            //mendapatkan inputStream
            InputStream inputStream = urlConnection.getInputStream();

            //membuat buffer reader dari input stream
            reader = new BufferedReader( new InputStreamReader( inputStream ) );

            //memakai input stream reader untuk hold incoming response
            StringBuilder builder = new StringBuilder(  );

            String line;
            while ((line= reader.readLine()) != null) {
            builder.append( line );

            // Karena ini JSON, menambahkan baris baru tidak perlu (tidak akan
                // memengaruhi parsing) tetapi itu membuat debugging * banyak * lebih mudah
                // jika Anda mencetak buffer yang lengkap untuk debugging.
                builder.append( "\n" );
            }

            if(builder.length()==0){
            //stream empty maka no point to parsing
                return  null;
            }

            bookJSONString = builder.toString();
        }
        catch (IOException e){
            e.printStackTrace();
        }
        finally {
            if(urlConnection != null){
                urlConnection.disconnect();
            }
            if(reader!= null){
                try {
                    reader.close();
                }
                catch (IOException e){
                    e.printStackTrace();
                }
            }
        }

        Log.d(LOG_TAG, bookJSONString);
        return bookJSONString;
    }
}
