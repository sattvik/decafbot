/* Copyright © 2011 Sattvik Software & Technology Resources, Ltd. Co.
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
package decafbot.scala

import _root_.android.app.Activity
import _root_.android.app.AlertDialog
import _root_.android.os.Bundle
import _root_.scala.util.Random

/**
 * Allows the user to play the game 'guess the number'.
 *
 * @author Daniel Solano Gómez
 */
class GuessTheNumberActivity extends Activity with TypedActivity
    with EnhancedCallBacks with TalkingActivity {
  /** Used to generate random numbers. */
  val random = new Random
  /** The number to guess */
  var number = 0
  var guessesLeft = 0

  /**
   * Sets up the user interface.
   */
  override def onCreate(savedInstanceState: Bundle) {
    super.onCreate(savedInstanceState)
    setContentView(R.layout.guess_the_number)
    findView(TR.submit_button).onClick { view => handleGuess }
    initGame
  }

  private def initGame {
    number = 1 + random.nextInt(100)
    guessesLeft = 10
    clearGuess
    prompt.setText(R.string.gtn_initial_prompt)
    say(R.string.gtn_thinking)
  }


  private def handleGuess {
    try {
      analyzeGuess(guessValue)
    } catch {
      case e: NumberFormatException => provideFeedback(R.string.gtn_not_a_number)
    }
  }

  private def prompt = findView(TR.prompt)
  private def guessEntry = findView(TR.guess)
  private def guessValue = guessEntry.getText.toString.toInt
  private def clearGuess = guessEntry.setText("")

  private def analyzeGuess(guess: Int) {
    if(number == guess) {
      endGame('playerWon)
    } else if(guessesLeft <= 1) {
      endGame('playerLost)
    } else if(guess < number ) {
      wrongGuess('tooLow)
    } else {
      wrongGuess('tooHigh)
    }
  }

  private def endGame(result: Symbol) {
    val builder = new AlertDialog.Builder(this)
    val speech = getString(result match {
      case 'playerWon => R.string.gtn_player_won
      case 'playerLost => R.string.gtn_player_lost
    })
    builder.setTitle(R.string.game_name_guess_the_number)
    builder.setMessage(getString(R.string.gtn_end_dialog, speech))
    builder.setNegativeButton(android.R.string.no, { finish })
    builder.setPositiveButton(android.R.string.yes, { initGame })
    builder.show
    say(speech)
  }

  private def wrongGuess(badGuess: Symbol) {
    guessesLeft -= 1
    clearGuess
    provideFeedback(badGuess match {
        case 'tooHigh => R.string.gtn_too_high
        case 'tooLow  => R.string.gtn_too_low
      })
  }

  private def provideFeedback(stringId: Int) {
    val audioString = getString(stringId)
    prompt.setText(getString(R.string.gtn_feedback_prompt, audioString,
        guessesLeft.asInstanceOf[AnyRef]))
    say(audioString)
  }
}

// vim:set et ts=2 sw=2:
