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
import _root_.android.os.Bundle
import _root_.android.os.Handler
import _root_.android.speech.tts.TextToSpeech
import _root_.java.util.concurrent.atomic.AtomicBoolean

/**
 * Trait that adds a handy [[#say]] method for text-to-speech.
 */
trait TalkingActivity extends Activity {
  private[this] var tts: Option[TextToSpeech] = None
  private[this] var handler: Option[Handler] = None
  private[this] val ready = new AtomicBoolean(false)

  /** Ensures that the TTS engine is initialised */
  override def onCreate(savedInstance: Bundle) {
    super.onCreate(savedInstance)
    tts = Some(new TextToSpeech(this, new TextToSpeech.OnInitListener {
        override def onInit(status: Int): Unit =
          if(status == TextToSpeech.SUCCESS)
            ready.compareAndSet(false, true)
          else
            tts = None
      }));
    handler = Some(new Handler)
  }

  def say(speechId: Int) {
    say(getString(speechId))
  }

  /**
   * Says something asynchronously.
   *
   * @param speech what to say
   */
  def say(speech: String) {
    def sayLater(delay: Long) {
      handler.get.postDelayed(new Runnable {
        override def run: Unit =
          tts.map { tts =>
            if(ready.get)
              tts.speak(speech, TextToSpeech.QUEUE_FLUSH, null)
            else
              sayLater(100)
          }
        }, delay);
    }
    sayLater(0)
  }
  
  /**
   * Ensures that the TTS engine is shut down.
   */
  override def onDestroy {
    super.onDestroy
    tts.map { tts => tts.stop; tts.shutdown }
  }
}

// vim:set et ts=2 sw=2:
