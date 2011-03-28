# Copyright © 2011 Sattvik Software & Technology Resources, Ltd. Co.
# All rights reserved.
#
# Redistribution and use in source and binary forms, with or without
# modification, are permitted provided that the following conditions are met:
#
# 1. Redistributions of source code must retain the above copyright notice,
#    this list of conditions and the following disclaimer.
# 2. Redistributions in binary form must reproduce the above copyright notice,
#    this list of conditions and the following disclaimer in the documentation
#    and/or other materials provided with the distribution.
# 3. Neither the name of Sattvik Software & Technology Resources, Ltd. Co. nor
#    the names of its contributors may be used to endorse or promote products
#    derived from this software without specific prior written permission.
#
# THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS"
# AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE
# IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE
# ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT HOLDER OR CONTRIBUTORS BE
# LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR
# CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF
# SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS
# INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN
# CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE)
# ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE
# POSSIBILITY OF SUCH DAMAGE.

import android.content.Context
import android.os.Handler
import android.speech.tts.TextToSpeech
import java.util.concurrent.atomic.AtomicBoolean

class Talker
  implements android.speech.tts.TextToSpeech.OnInitListener

  def initialize(context: Context)
    @tts = TextToSpeech.new context, self
    @running = AtomicBoolean.new true
    @ready = AtomicBoolean.new false
    @handler = Handler.new
  end

  def onInit(status)
    if status == TextToSpeech.SUCCESS
      @ready.set true
    else
      @running.set false
    end
  end

  def say(speech: String)
    say_later(speech,0)
  end

  def shutdown
    if @running.compareAndSet true, false
      @tts.stop
      @tts.shutdown
    end
  end

  def say_later(speech: String,delay: long)
    talker = self
    running = @running
    ready = @ready
    tts = @tts
    speech_task = to_runnable do
      if running.get
        if ready.get
          tts.speak(speech, TextToSpeech.QUEUE_FLUSH, nil)
        else
          talker.say_later speech, 100
        end
      end
      nil
    end
    @handler.postDelayed speech_task, delay
  end

  def to_runnable(run: Runnable)
    run
  end
end

# vim:set ft=ruby sw=2 ts=2 et:
