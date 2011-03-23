/*
 * Copyright © 2011 Sattvik Software & Technology Resources, Ltd. Co.
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 *
 * 1. Redistributions of source code must retain the above copyright notice,
 *    this list of conditions and the following disclaimer.
 * 2. Redistributions in binary form must reproduce the above copyright notice,
 *    this list of conditions and the following disclaimer in the documentation
 *    and/or other materials provided with the distribution.
 * 3. Neither the name of Sattvik Software & Technology Resources, Ltd. Co. nor
 *    the names of its contributors may be used to endorse or promote products
 *    derived from this software without specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS"
 * AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE
 * IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE
 * ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT HOLDER OR CONTRIBUTORS BE
 * LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR
 * CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF
 * SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS
 * INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN
 * CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE)
 * ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE
 * POSSIBILITY OF SUCH DAMAGE.
 */
package decafbot.jni;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.Window;
import android.widget.ArrayAdapter;
import android.widget.ListView;

/**
 * The game, ‘Global thermonuclear war’, modelled from the WOPR.
 *
 * @author Daniel Solano Gómez
 */
public class GlobalThermonuclearWarActivity extends Activity {
    /** Adapter showing failed strategies. */
    private ArrayAdapter<String> adapter;
    private Talker talker;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        requestWindowFeature(Window.FEATURE_PROGRESS);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.global_thermonuclear_war);
        talker=new Talker(this);

        // set up list view
        adapter = new ArrayAdapter<String>(this,
                                           android.R.layout.simple_list_item_1);
        ListView listView = (ListView) findViewById(R.id.gtw_strategy_list);
        listView.setAdapter(adapter);

        // kick off some learning
        LearnTask task = new LearnTask();
        task.execute(getResources().getStringArray(R.array.gtw_strategies));
    }

    @Override
    protected void onDestroy() {
        talker.shutdown();
        super.onDestroy();
    }

    /** Tasks that simulates learning the futility of playing the game. */
    private class LearnTask extends AsyncTask<String, Object, Void> {
        /** The maximum progress value. */
        private static final int MAX_PROGRESS = 10000;

        @Override
        protected void onPostExecute(final Void aVoid) {
            super.onPostExecute(aVoid);
            // hide progress bar
            setProgress(MAX_PROGRESS);
            // notify results
            drawConclusion();
        }

        @Override
        protected native Void doInBackground(final String... strategies);

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            setProgress(0);
        }

        @Override
        protected void onProgressUpdate(final Object... values) {
            final String strategy = (String) values[0];
            final int strategyNumber = (Integer) values[1];
            final int totalStrategies = (Integer) values[2];
            // update progress
            setProgress(MAX_PROGRESS * strategyNumber / totalStrategies);
            // add strategy to view
            adapter.add(strategy);
            super.onProgressUpdate(values);
        }
    }

    /** Notifies the user of the futility of playing the game. */
    private void drawConclusion() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(R.string.game_name_global_thermonuclear_war);
        builder.setMessage(R.string.gtw_conclusion);
        builder.setNegativeButton(R.string.no_thanks,
                                  new DialogInterface.OnClickListener() {
                                      public void onClick(
                                              final DialogInterface dialogInterface,
                                              final int i) {
                                          // nothing else to do, finish the activity
                                          finish();
                                      }
                                  });
        builder.setPositiveButton(R.string.sure,
                                  new DialogInterface.OnClickListener() {
                                      public void onClick(
                                              final DialogInterface dialogInterface,
                                              final int i) {
                                          // launch guess the number
                                          startActivity(new Intent(
                                                  GlobalThermonuclearWarActivity.this,
                                                  GuessTheNumberActivity.class));
                                          // nothing more
                                          finish();
                                      }
                                  });

        builder.show();
        talker.say(getString(R.string.gtw_conclusion));
    }

    static {
        System.loadLibrary("DecafBot");
    }
}