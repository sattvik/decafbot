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

java_import "android.os.Handler"
java_import "android.speech.tts.TextToSpeech"
java_import "java.util.concurrent.atomic.AtomicBoolean"
ruboto_import "decafbot.ruboto.TtsInitListener"

# Utility class for interacting with the text to speech capabilities of the
# Android platform.
#
class Talker
  # Initialises the TTS engine using the given context.
  def initialize(context)
    @ready = AtomicBoolean.new false
    @running = AtomicBoolean.new true
    @handler = Handler.new

    init_listener = TtsInitListener.new.handle_init do |status|
      if status == TextToSpeech::SUCCESS
        @ready.set true
      else
        shutdown
      end
    end

    @tts = TextToSpeech.new context, init_listener
  end

  # Says the speech asynchronously.
  def say(speech)
    say_later speech, 0
  end

  # Says the speech asynchronously, with the given delay
  def say_later(speech, delay)
    task=Runnable.new.handle_run do
      # if not running, just end
      if @running.get
        # if ready, speak, otherwise wait some more
        if @ready.get
          @tts.speak speech, TextToSpeech::QUEUE_FLUSH, nil
        else
          say_later speech, 100
        end
      end
    end

    @handler.postDelayed task, delay
  end

  # shuts down the talker, cleaning up all resources
  def shutdown
    if @running.compareAndSet true, false
      @tts.stop
      @tts.shutdown
      @tts=nil
    end
  end
end

# vim:set et ts=2 sw=2:
