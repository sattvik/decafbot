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
import _root_.android.os.AsyncTask
import _root_.android.os.Bundle
import _root_.android.view.Window
import _root_.android.widget.ArrayAdapter

/**
 * Allows the user to learn the futility of global thermonuclear war.
 *
 * @author Daniel Solano Gómez
 */
class GlobalThermonuclearWarActivity extends Activity with TypedActivity
    with EnhancedCallBacks with TalkingActivity with Launcher {
  private[this] var adapter: ArrayAdapter[String] = null

  /** An asynchronous task that simulates learning. */
  private class LearnTask(val strategies: Array[String])  extends AsyncTask[Object,Object,Object] {
    val MAX_PROGRESS = 10000
    override def onPreExecute() = setProgress(0)

    override def doInBackground(args: Object*): Object = {
      0 until strategies.length foreach { i =>
        Thread.sleep(50)
        publishProgress(strategies(i), (i + 1).asInstanceOf[AnyRef])
      }
      null
    }
    
    override def onProgressUpdate(values: Object*) {
      val strategy = values(0).asInstanceOf[String]
      val strategy_num = values(1).asInstanceOf[Int]

      setProgress(MAX_PROGRESS * strategy_num / strategies.length)
      adapter.add(strategy)
    }

    override def onPostExecute(avoid: Object) {
      setProgress(MAX_PROGRESS)
      drawConclusion
    }
  }

  /**
   * Sets up the user interface.
   */
  override def onCreate(savedInstanceState: Bundle) {
    requestWindowFeature(Window.FEATURE_PROGRESS)
    super.onCreate(savedInstanceState)
    setContentView(R.layout.global_thermonuclear_war)

    // init list
    adapter = new ArrayAdapter(this, android.R.layout.simple_list_item_1)
    findView(TR.gtw_strategy_list).setAdapter(adapter)

    // execute task
    val strategies = getResources().getStringArray(R.array.gtw_strategies)
    new LearnTask(strategies).execute()
  }


  def drawConclusion {
    val builder = new AlertDialog.Builder(this)
    builder.setTitle(R.string.game_name_global_thermonuclear_war)
    builder.setMessage(R.string.gtw_conclusion)
    builder.setNegativeButton(R.string.no_thanks, { finish })
    builder.setPositiveButton(R.string.sure,
      { launch(classOf[GuessTheNumberActivity]) })
    builder.show
    say(R.string.gtw_conclusion)
  }
}

// vim:set et ts=2 sw=2:
