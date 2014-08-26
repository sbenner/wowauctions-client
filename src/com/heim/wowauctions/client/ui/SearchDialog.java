package com.heim.wowauctions.client.ui;

/**
 *
 * User: sbenner
 * Date: 8/14/14
 * Time: 9:09 PM
 */

import android.app.AlertDialog;
import android.app.ListActivity;
import android.content.Context;
import android.content.DialogInterface;
import android.util.Pair;
import android.widget.EditText;
import com.heim.wowauctions.client.utils.AuctionsApplication;
import com.heim.wowauctions.client.utils.AuctionsLoader;

public class SearchDialog extends AlertDialog.Builder {


    public SearchDialog(final Context activity,final Pair p) {

        super(activity);


        AlertDialog.Builder confirmationDialog = new AlertDialog.Builder(activity);
        final EditText editText = new EditText(activity);

        confirmationDialog.setMessage("Search current auctions on an item: ")//.setCancelable(false)
                .setView(editText)
                .setPositiveButton("Search", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        new AuctionsLoader(activity,p).execute(editText.getText().toString().trim());

                    }
                })
                .setNeutralButton("Cancel", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {

                        ListActivity lv = (ListActivity)activity;
                        lv.getListView().requestFocus();
                        dialog.cancel();
                    }
                });

        confirmationDialog.create().show();

    }


}
