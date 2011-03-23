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
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.RadioGroup;

/**
 * Activity that allows the user to choose a game to play.
 *
 * @author Daniel Solano Gómez
 */
public class GameChooserActivity extends Activity {
    /** Utility for talking. */
    private Talker talker;

    @Override
    public void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.game_chooser);
        Button chooseButton = (Button) findViewById(R.id.choose_button);
        chooseButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(final View view) {
                handleSelection();
            }
        });
        talker = new Talker(this);
        // greet the first time
        if (savedInstanceState == null) {
            talker.say(getString(R.string.greetings));
        }
    }

    /**
     * Called when the user clicks the ‘choose’ button.  Handles launching of
     * the desired game.
     */
    private void handleSelection() {
        RadioGroup games = (RadioGroup) findViewById(R.id.game_chooser_list);
        switch (games.getCheckedRadioButtonId()) {
            case R.id.game_guess_the_number:
                launchGuessTheNumber();
                break;
            case R.id.game_global_thermonuclear_war:
                confirmGlobalThermonuclearWar();
                break;
        }
    }

    /** Starts the game ‘Choose the number’. */
    private void launchGuessTheNumber() {
        startActivity(new Intent(this, GuessTheNumberActivity.class));
        finish();
    }

    /** Confirm that the user wants to play ‘Global thermonuclear war’. */
    private void confirmGlobalThermonuclearWar() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(R.string.game_name_global_thermonuclear_war);
        builder.setMessage(R.string.chooser_confirm_nuke);
        builder.setNegativeButton(R.string.later,
                                  new DialogInterface.OnClickListener() {
                                      public void onClick(
                                              final DialogInterface dialogInterface,
                                              final int i) {
                                          launchGlobalThermonuclearWar();
                                      }
                                  });
        builder.setPositiveButton(android.R.string.yes,
                                  new DialogInterface.OnClickListener() {
                                      public void onClick(
                                              final DialogInterface dialogInterface,
                                              final int i) {
                                          launchGuessTheNumber();
                                      }
                                  });
        builder.show();
        talker.say(getString(R.string.chooser_confirm_nuke));
    }

    /** Launches the game ‘Global thermonuclear war’ */
    private void launchGlobalThermonuclearWar() {
        startActivity(new Intent(this, GlobalThermonuclearWarActivity.class));
        finish();
    }
    @Override
    protected void onDestroy() {
        talker.shutdown();
        super.onDestroy();
    }
}
