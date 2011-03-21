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
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import java.util.Random;

/**
 * Implementation of the ‘Guess the number’ game.
 *
 * @author Daniel Solano Gómez
 */
public class GuessTheNumberActivity extends Activity {
    private Talker talker;
    private TextView prompt;
    private EditText guessEntry;
    private static final int PLAYER_LOST = -2;
    private static final int TOO_LOW = -1;
    private static final int PLAYER_WON = 0;
    private static final int TOO_HIGH = 1;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.guess_the_number);

        prompt = (TextView) findViewById(R.id.prompt);
        guessEntry = (EditText) findViewById(R.id.guess);
        Button submit = (Button) findViewById(R.id.submit_button);
        submit.setOnClickListener(new View.OnClickListener() {
            public void onClick(final View view) {
                handleGuess();
            }
        });
    }

    /**
     * Does all native initialisation of the game.
     *
     * @param newNumber a new number to guess
     */
    private native void nativeInitGame(int newNumber);

    /** Initialises the game. */
    private void initGame() {
        nativeInitGame(1 + new Random().nextInt(100));
        prompt.setText(R.string.gtn_initial_prompt);
        guessEntry.setText("");
        talker.say(getString(R.string.gtn_thinking));
    }

    /** Handles the guess from the user. */
    private void handleGuess() {
        final int guess;
        try {
            guess = Integer.parseInt(guessEntry.getText().toString(), 10);
        } catch (final NumberFormatException ignored) {
            provideFeedback(R.string.gtn_not_a_number);
            return;
        }
        int guessResult = analyzeGuess(guess);
        switch (guessResult) {
            case PLAYER_WON:
                endOfGameDialog(R.string.gtn_player_won);
                break;
            case PLAYER_LOST:
                endOfGameDialog(R.string.gtn_player_lost);
                break;
            default:
                handleIncorrectGuess(guessResult);
                break;
        }
    }

    /**
     * Analyzes whether or not the guess is correct, too high, or too low.
     *
     * @param guess the user’s guess
     *
     * @return one of {@link #TOO_HIGH}, {@link #TOO_LOW}, or {@link
     *         #PLAYER_WON}, or {@link #PLAYER_LOST}
     */
    private native int analyzeGuess(final int guess);

    /**
     * Returns the number of guesses left.
     *
     * @return the number of guesses left
     */
    private native int guessesLeft();

    /**
     * Presents the end-of-game dialogue, allowing the player to start a new
     * game or exit the activity.
     *
     * @param messageID the ID of the string to display naming the game result
     */
    private void endOfGameDialog(final int messageID) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        final String speech = getString(messageID);
        builder.setTitle(R.string.game_name_guess_the_number);
        builder.setMessage(getString(R.string.gtn_end_dialog, speech));
        builder.setPositiveButton(android.R.string.yes,
                                  new DialogInterface.OnClickListener() {
                                      public void onClick(
                                              final DialogInterface dialogInterface,
                                              final int i) {
                                          initGame();
                                      }
                                  });
        builder.setNegativeButton(android.R.string.no,
                                  new DialogInterface.OnClickListener() {
                                      public void onClick(
                                              final DialogInterface dialogInterface,
                                              final int i) {
                                          finish();
                                      }
                                  });
        talker.say(speech);
        builder.show();
    }

    /**
     * Handles an incorrect guess.
     *
     * @param guessResult one of {@link #TOO_LOW} or {@link #TOO_HIGH}
     */
    private void handleIncorrectGuess(final int guessResult) {
        guessEntry.setText("");
        provideFeedback((guessResult == TOO_LOW) ? R.string.gtn_too_low
                                                 : R.string.gtn_too_high);
    }

    /**
     * Provides feedback to the user about their guess.
     *
     * @param stringId the ID of the string that describes the feedbak
     */
    private void provideFeedback(final int stringId) {
        final String audioString = getString(stringId);
        final String promptString =
                getString(R.string.gtn_feedback_prompt, audioString,
                          guessesLeft());
        prompt.setText(promptString);
        talker.say(audioString);
    }

    @Override
    protected void onResume() {
        super.onResume();
        talker = new Talker(this);
        initGame();
    }

    @Override
    protected void onPause() {
        talker.shutdown();
        talker = null;
        super.onPause();
    }

    static {
        System.loadLibrary("DecafBot");
    }
}