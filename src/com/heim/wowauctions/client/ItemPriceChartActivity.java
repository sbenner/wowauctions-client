package com.heim.wowauctions.client;

import android.app.ActionBar;
import android.app.ListActivity;
import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Pair;
import android.view.*;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.SearchView;
import com.heim.wowauctions.client.ui.ItemListAdapter;
import com.heim.wowauctions.client.ui.SearchDialog;
import com.heim.wowauctions.client.ui.WebActivity;
import com.heim.wowauctions.client.utils.AuctionsApplication;
import com.heim.wowauctions.client.utils.AuctionsLoader;
import com.heim.wowauctions.client.utils.ItemStatisticsLoader;

public class ItemPriceChartActivity extends ListActivity {

    Pair p;
    /**
     * Called when the activity is first created.
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_ACTION_BAR);

        ActionBar actionBar = getActionBar();

        actionBar.show();
        String key = ((AuctionsApplication) getApplication()).getPrivateKey();
        String ip =  ((AuctionsApplication) getApplication()).getIpAddress();

        p = new Pair(ip,key);

//        getListView().setOnScrollListener(new InfiniteScrollListener(5) {
//            @Override
//            public void loadMore(int page, int totalItemsCount) {
//                Reply reply = (Reply) getListView().getTag();
//                if (reply != null)
//                    new AuctionsLoader(ItemPriceChartActivity.this).execute(reply.getSearchString(), String.valueOf(reply.getNumber() + 1));
//            }
//        });


        registerForContextMenu(getListView());


        setListAdapter(new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, new String[]{"Search"}));
    }



    @Override
    protected void onListItemClick(ListView l, View v, int position, long id) {
        super.onListItemClick(l, v, position, id);
        l.requestFocus();
        if (v.getTag() instanceof ItemListAdapter.ViewHolder) {
            l.showContextMenuForChild(v);
        } else {
            new SearchDialog(this, p);
        }

    }


    @Override
    public boolean onContextItemSelected(MenuItem item) {
        AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
        Long itemId = ((ItemListAdapter.ViewHolder) info.targetView.getTag()).itemId;
        String name = ((ItemListAdapter.ViewHolder) info.targetView.getTag()).tvItemName.getText().toString();
        switch (item.getItemId()) {
            case R.id.chart:
                new ItemStatisticsLoader(ItemPriceChartActivity.this, p).execute(itemId);
                return true;
            case R.id.item_info:

                Intent myIntent = new Intent(ItemPriceChartActivity.this, WebActivity.class);

                myIntent.putExtra("itemId", itemId);
                myIntent.putExtra("name", name);

                startActivity(myIntent);
                return true;
        }
        return super.onContextItemSelected(item);
    }


    @Override
    public void onCreateContextMenu(ContextMenu menu, View v,
                                    ContextMenu.ContextMenuInfo menuInfo) {

        super.onCreateContextMenu(menu, v, menuInfo);

        if(((AdapterView.AdapterContextMenuInfo) menuInfo).targetView.getTag()==null)
            return;

        MenuInflater m = getMenuInflater();
        m.inflate(R.menu.context_menu, menu);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu, menu);

        SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        final SearchView searchView = (SearchView) menu.findItem(R.id.menu_search).getActionView();

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                searchView.clearFocus();

                new AuctionsLoader(ItemPriceChartActivity.this, p).execute(query.trim());

                return false;  //To change body of implemented methods use File | Settings | File Templates.
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;  //To change body of implemented methods use File | Settings | File Templates.
            }
        });

        searchView.setSearchableInfo(searchManager.getSearchableInfo(getComponentName()));
        searchView.setIconifiedByDefault(false); // Do not iconify the widget; expand it by default


        return true;
    }


    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {

            case R.id.search:
                new SearchDialog(this, p);
                return true;
            default:
                return super.onOptionsItemSelected(item);

        }

    }
}