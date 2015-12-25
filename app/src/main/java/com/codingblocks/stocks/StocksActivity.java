package com.codingblocks.stocks;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Date;
import java.util.Scanner;
import java.util.Set;


public class StocksActivity extends AppCompatActivity implements AdapterView.OnItemClickListener, StocksAyncTaskInterface{

    StockArrayAdapter adapter;
    ArrayList<Stock> stocks;
    ProgressDialog progress;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_batch);
        setTitle("Title");
        Date d = new Date();
        stocks = new ArrayList<>();

        SharedPreferences sp = getSharedPreferences("stocks", MODE_PRIVATE);
        Set<String> tickerSet = sp.getStringSet("tickers", null);
        ArrayList<String> ticker = new ArrayList<>();
        if (tickerSet != null)
            ticker.addAll(tickerSet);
        else {
            ticker.add("MSFT");
            ticker.add("fb");
        }

        String urlString = getURLString(ticker);
        StocksFetcherAsyncTask task = new StocksFetcherAsyncTask();
        task.listener = this;
        task.execute(urlString);
        progress = new ProgressDialog(this);
        progress.setTitle("Getting stocks Data");
        progress.setMessage("Wait!");
        progress.show();

        ListView lv = (ListView) findViewById(R.id.batchListView);
        adapter = new StockArrayAdapter(this, stocks);
        lv.setAdapter(adapter);
        lv.setOnItemClickListener(this);
    }

    private String getURLString(ArrayList<String> companyTickers) {
        return "http://query.yahooapis.com/v1/public/yql?q=select%20*%20from%20yahoo.finance.quotes%20where%20symbol%20in%20(%22YHOO%22%2C%22GOOG%22%2C%22MSFT%22)%0A%09%09&env=http%3A%2F%2Fdatatables.org%2Falltables.env&format=json";
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_batch, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            SharedPreferences sp = getSharedPreferences("batch_clicked", MODE_PRIVATE);
            String lastBatch = sp.getString("last_batch", "No Batch clicked so far");
            Toast.makeText(this, lastBatch, Toast.LENGTH_SHORT).show();
            return true;
        } else if (id == R.id.action_call) {
            Intent i = new Intent();
            i.setAction(Intent.ACTION_CALL);
            i.setData(Uri.parse("tel:9971489388"));
            if (i.resolveActivity(getPackageManager()) != null) {
                startActivity(i);
            }
        } else if (id == R.id.action_web)  {
            Intent i = new Intent();
            i.setAction(Intent.ACTION_VIEW);
            i.setData(Uri.parse("http://www.codingblocks.com"));
            if (i.resolveActivity(getPackageManager()) != null) {
                startActivity(i);
            }
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//        Batch b = adapter.getItem(position);
//        SharedPreferences sp = getSharedPreferences("batch_clicked", MODE_PRIVATE);
//        SharedPreferences.Editor editor = sp.edit();
//        editor.putString("last_batch", b.name);
//        editor.commit();
//
//        Intent i = new Intent();
//        i.setClass(this, StudentActivity.class);
//        i.putExtra("numStudents", b.currentlyFilled);
//        i.putExtra("studentArray", new Student[10]);
//        startActivityForResult(i, 0);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
//        if (data == null)
//            return;
//        Bundle b = data.getExtras();
//        String studentName = b.getString("studentName");
//        Toast.makeText(this, "student clicked " + studentName, Toast.LENGTH_LONG).show();
    }

    @Override
    public void stocksTaskOnComplete(Stock[] a) {
        stocks.clear();
        for (Stock s: a) {
            stocks.add(s);
        }
        adapter.notifyDataSetChanged();
        progress.dismiss();
    }
}
