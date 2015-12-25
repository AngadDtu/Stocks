package com.codingblocks.stocks;

import android.content.Context;
import android.graphics.Color;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.util.List;

/**
 * Created by nagarro on 06/09/15.
 */
public class StockArrayAdapter extends ArrayAdapter<Stock> {

    Context context;

    public StockArrayAdapter(Context context, List<Stock> objects) {
        super(context, 0, objects);
        this.context = context;
    }

    public static class StockViewHolder {
        TextView companyName;
        TextView ticker;
        TextView price;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = View.inflate(context, R.layout.batch_item_layout, null);
            StockViewHolder vh = new StockViewHolder();
            vh.companyName = (TextView)convertView.findViewById(R.id.batchName);
            vh.ticker = (TextView)convertView.findViewById(R.id.courseName);
            vh.price = (TextView)convertView.findViewById(R.id.seats);
            convertView.setTag(vh);
        }

        Stock s = getItem(position);
        StockViewHolder vh = (StockViewHolder)convertView.getTag();
        vh.price.setText(s.currentPrice + "/" + s.percentChange);
        vh.companyName.setText(s.companyName);
        vh.ticker.setText(s.ticker);

        return convertView;
    }
}
