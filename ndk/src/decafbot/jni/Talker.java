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

import android.content.Context;
import android.os.Handler;
import android.speech.tts.TextToSpeech;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * Utility class to facilitate use of the TTS engine.
 *
 * @author Daniel Solano Gómez
 */
public final class Talker implements TextToSpeech.OnInitListener {
    /** The TTS synthesiser. */
    private final TextToSpeech tts;
    /** Latch used to ensure TTS is initialised before saying anything. */
    private final CountDownLatch ttsLatch;
    /** Flag for whether or not the talker has been shutdown. */
    private final AtomicBoolean isShutdown;
    /** Handles speech events asynchronously. */
    private final Handler speechHandler = new Handler();

    public Talker(final Context context) {
        tts = new TextToSpeech(context, this);
        ttsLatch = new CountDownLatch(1);
        isShutdown = new AtomicBoolean(false);
    }

    /**
     * Says the speech.
     *
     * @param speech the speech to speak
     */
    public void say(final String speech) {
        sayLater(speech, 0L);
    }

    /**
     * Delays saying the speech in an effort to allow the UI thread to handle
     * things, such as starting up the TTS engine.
     *
     * @param speech what to say
     * @param delay  how long to way before speaking, in milliseconds
     */
    private void sayLater(final String speech, final long delay) {
        speechHandler.postDelayed(new Runnable() {
            public void run() {
                try {
                    if (!isShutdown.get()) {
                        if (ttsLatch.await(10, TimeUnit.MILLISECONDS)) {
                            tts.speak(speech, TextToSpeech.QUEUE_FLUSH, null);
                        } else {
                            sayLater(speech, 100L);
                        }
                    }
                } catch (InterruptedException e) {
                    // restore interrupt
                    Thread.currentThread().interrupt();
                }
            }
        }, delay);
    }

    /**
     * If the TTS has initialised successfully, says what needs to be said.
     *
     * @param status whether or not the TTS initialised successfully
     */
    public void onInit(final int status) {
        ttsLatch.countDown();
    }

    /** Shuts down the TTS engine. */
    public void shutdown() {
        if (isShutdown.compareAndSet(false, true)) {
            tts.shutdown();
        }
    }
}