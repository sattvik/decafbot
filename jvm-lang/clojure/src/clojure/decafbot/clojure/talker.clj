; Copyright © 2011 Sattvik Software & Technology Resources, Ltd. Co.
; All rights reserved.
;
; Redistribution and use in source and binary forms, with or without
; modification, are permitted provided that the following conditions are met:
;
; 1. Redistributions of source code must retain the above copyright notice,
;    this list of conditions and the following disclaimer.
; 2. Redistributions in binary form must reproduce the above copyright notice,
;    this list of conditions and the following disclaimer in the documentation
;    and/or other materials provided with the distribution.
; 3. Neither the name of Sattvik Software & Technology Resources, Ltd. Co. nor
;    the names of its contributors may be used to endorse or promote products
;    derived from this software without specific prior written permission.
;
; THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS"
; AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE
; IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE
; ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT HOLDER OR CONTRIBUTORS BE
; LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR
; CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF
; SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS
; INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN
; CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE)
; ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE
; POSSIBILITY OF SUCH DAMAGE.

(ns decafbot.clojure.talker
  "Text-to-speech utility namespace"
  {:author "Daniel Solano Gómez"}
  (:import android.speech.tts.TextToSpeech))

(defn on-init-listener
  [ready]
  (reify android.speech.tts.TextToSpeech$OnInitListener
    (onInit [_ _]
      (compare-and-set! ready false true))))

(defn init
  [context]
  (let [ready (atom false)]
    {:tts     (TextToSpeech. context (on-init-listener ready))
     :ready   ready
     :running (atom true)
     :handler (android.os.Handler.)}))

(defn shutdown
  [{:keys [tts running]}]
  (when (compare-and-set! running true false)
    (.stop tts)
    (.shutdown tts)))

(defn- say-later
  [{:keys [tts running ready handler] :as talker} speech delay]
  (when @running
    (.postDelayed handler
                  (reify Runnable
                    (run [_]
                      (try
                        (when @running
                          (if @ready
                            (.speak tts speech TextToSpeech/QUEUE_FLUSH nil)
                            (say-later talker speech 100)))
                        (catch InterruptedException _
                          (.interrupt (Thread/currentThread))))))
                  delay)))

(defn say
  [talker speech]
  (say-later talker speech 0))
