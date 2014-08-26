package com.heim.wowauctions.client.ui;

import android.app.ListActivity;
import android.content.Context;
import android.graphics.Color;
import android.util.Log;
import android.util.Pair;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import com.heim.wowauctions.client.R;
import com.heim.wowauctions.client.models.Auction;
import com.heim.wowauctions.client.models.Reply;
import com.heim.wowauctions.client.utils.AuctionsLoader;

import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: sbenner
 * Date: 8/17/14
 * Time: 7:36 PM
 */

public class ItemListAdapter extends BaseAdapter {

    List dataList;
    Context context;
    ListActivity lv;
   String searchString;
    private boolean isLoading=true;
    int page=0;
    private static LayoutInflater inflater = null;
    Pair pair;


    public ItemListAdapter(Context context, List data,Pair pair) {

        this.context = context;
        this.dataList = data;
        lv = (ListActivity) this.context;
        Reply reply = (Reply)lv.getListView().getTag();
        this.pair=pair;
        searchString = reply.getSearchString();
        inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

    }

    public void addAll(List list) {
        page++;
        this.dataList.addAll(list);
    }


    public int getCount() {
        return dataList.size();
    }

    public Object getItem(int position) {
        return position;
    }

    public long getItemId(int position) {
        return position;
    }

    public boolean isLoading() {
        return isLoading;
    }

    public void setLoading(boolean loading) {
        isLoading = loading;
    }

    public static class ViewHolder {

        public TextView tvItemName;
        public TextView tvItemLevel;
        public TextView tvOwner;
        public TextView tvBid;
        public TextView tvBuyout;
        public TextView tvQuantity;
        public long itemId;

    }


    // * 	.border-q0 { border-color: #9d9d9d !important; } /* poor (gray) */
    // .border-q1 { border-color: #ffffff !important; } /* common (white) */
    // .border-q2 { border-color: #1eff00 !important; } /* uncommon (green) */
    // .border-q3 { border-color: #0070dd !important; } /* rare (blue) */
    // .border-q4 { border-color: #a335ee !important; } /* epic (purple) */
    // .border-q5 { border-color: #ff8000 !important; } /* lengendary (orange) */
    // .border-q6 { border-color: #e5cc80 !important; } /* artifact (gold) */
    //  .border-q7 { border-color: #e5cc80 !important; } /* heirloom (gold) */


    public View getView(final int position, View convertView, ViewGroup parent) {

        Log.v("position", Integer.toString(position));


        ViewHolder viewHolder;


        if (convertView == null) {

            convertView = inflater.inflate(R.layout.list_item, parent,false);
            viewHolder = new ViewHolder();
            viewHolder.tvItemName = (TextView) convertView.findViewById(R.id.itemname);
            viewHolder.tvItemLevel = (TextView) convertView.findViewById(R.id.itemlevel);
            viewHolder.tvOwner = (TextView) convertView.findViewById(R.id.owner);
            viewHolder.tvBid = (TextView) convertView.findViewById(R.id.bid);
            viewHolder.tvBuyout = (TextView) convertView.findViewById(R.id.buyout);
            viewHolder.tvQuantity = (TextView) convertView.findViewById(R.id.quantity);
            convertView.setTag(viewHolder);

        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }


        final Auction auction = (Auction) dataList.get(position);

        if (auction != null) {

            int quality = auction.getItem().getQuality();

            viewHolder.tvItemName.setText(auction.getItem().getName());

            if (quality == 1) {
                viewHolder.tvItemName.setTextColor(Color.parseColor("#ffffff"));
            }
            if (quality == 2) {
                viewHolder.tvItemName.setTextColor(Color.parseColor("#1eff00"));
            }
            if (quality == 3) {
                viewHolder.tvItemName.setTextColor(Color.parseColor("#0070dd"));
            }
            if (quality == 4) {
                viewHolder.tvItemName.setTextColor(Color.parseColor("#a335ee"));
            }
            if (quality == 7) {
                viewHolder.tvItemName.setTextColor(Color.parseColor("#e5cc80")); //
            }

            viewHolder.tvItemLevel.setText(String.valueOf("Item Level "+auction.getItem().getItemLevel()));

            viewHolder.tvOwner.setText("Seller: "+auction.getOwner());
            viewHolder.tvBid.setText("Bid: "+String.valueOf(auction.getBid()));
            viewHolder.tvBuyout.setText("Buyout: "+String.valueOf(auction.getBuyout()));
            viewHolder.tvQuantity.setText("Quantity: "+String.valueOf(auction.getQuantity()));
            viewHolder.itemId=auction.getItem().getId();

            convertView.setTag(viewHolder);


        }
//
        if(position>=(getCount()-(getCount()/10))&&!isLoading())
        {
            setLoading(true);
            new AuctionsLoader(this.context,pair).execute(searchString, String.valueOf(page+1));
        }



        return convertView;
    }

    @Override
    public boolean areAllItemsEnabled() {
        // TODO Auto-generated method stub
        return super.areAllItemsEnabled();
    }

    @Override
    public boolean isEnabled(int position) {
        // TODO Auto-generated method stub
        return true;
    }


}
