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

/**
 * The main activity for the DecafBot demo.  Allows the user to choose a
 * game to play.
 *
 * @author Daniel Solano Gómez
 */
class GameChooserActivity extends Activity with TypedActivity
    with EnhancedCallBacks with TalkingActivity with Launcher {

  /**
   * Sets up the user interface.
   */
  override def onCreate(savedInstanceState: Bundle) {
    super.onCreate(savedInstanceState)
    setContentView(R.layout.game_chooser)
    findView(TR.choose_button).onClick { view => handleSelection }
    if(savedInstanceState == null)
      say(R.string.greetings)
  }

  def handleSelection =
    findView(TR.game_chooser_list).getCheckedRadioButtonId match {
      case R.id.game_guess_the_number         => launch(classOf[GuessTheNumberActivity])
      case R.id.game_global_thermonuclear_war => confirmWar
    }

  def confirmWar {
    val builder = new AlertDialog.Builder(this)
    builder.setTitle(R.string.game_name_global_thermonuclear_war)
    builder.setMessage(R.string.chooser_confirm_nuke)
    builder.setNegativeButton(R.string.later,
      { launch(classOf[GlobalThermonuclearWarActivity]) })
    builder.setPositiveButton(android.R.string.yes,
      { launch(classOf[GuessTheNumberActivity]) })
    builder.show
    say(R.string.chooser_confirm_nuke)
  }
}

// vim:set et ts=2 sw=2:
