package com.codingblocks.stocks;

import android.os.AsyncTask;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Scanner;

/**
 * Created by nagarro on 12/09/15.
 */
public class StocksFetcherAsyncTask extends AsyncTask<String, Void, Stock[]>{

    StocksAyncTaskInterface listener;

    @Override
    protected Stock[] doInBackground(String... params) {
        String urlString = params[0];
        try {
            URL url = new URL(urlString);
            HttpURLConnection urlConnection = (HttpURLConnection)url.openConnection();
            urlConnection.setRequestMethod("GET");
            urlConnection.connect();

            InputStream data = urlConnection.getInputStream();
            Scanner s = new Scanner(data);
            StringBuffer output = new StringBuffer();
            while (s.hasNext()) {
                output.append(s.nextLine());
            }
            Log.i("output", output.toString());
            s.close();
            urlConnection.disconnect();

            return parseStocksJson(output.toString());

        } catch(MalformedURLException e) {
            return null;
        } catch (IOException e) {
            return null;
        }
    }

    @Override
    protected void onPostExecute(Stock[] stocks) {
        if (listener != null)
            listener.stocksTaskOnComplete(stocks);
    }

    private Stock[] parseStocksJson(String jsonString) {
        try {
            JSONObject object = new JSONObject(jsonString);
            JSONObject query = object.getJSONObject("query");
            JSONObject results = query.getJSONObject("results");
            JSONArray quotes = results.getJSONArray("quote");

            Stock[] output = new Stock[quotes.length()];
            for (int i = 0; i < quotes.length(); i++) {
                JSONObject company = quotes.getJSONObject(i);
                Stock s = new Stock();
                s.companyName = company.getString("Name");
                s.ticker = company.getString("symbol");
                s.currentPrice = Double.parseDouble(company.getString("Bid"));
                String change = company.getString("Change");
                s.percentChange = Double.parseDouble(change);
                output[i] = s;
            }
            return output;
        } catch (JSONException e) {
            return null;
        }
    }

}
