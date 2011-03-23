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

(ns decafbot.clojure.GlobalThermonuclearWarActivity
  "Implementation of global thermonuclear war."
  {:author "Daniel Solano Gómez"}
  (:gen-class :main no
              :extends android.app.Activity
              :exposes-methods {onCreate superOnCreate
                                onDestroy superOnDestroy})
  (:require [decafbot.clojure.talker :as talker])
  (:use decafbot.clojure.utils)
  (:import android.widget.ArrayAdapter
           [decafbot.clojure GuessTheNumberActivity
                             R$array R$id R$layout R$string]))

(def talker (atom nil))

(defn draw-conclusion
  [activity]
  (show-dialog activity
               :title R$string/game_name_global_thermonuclear_war
               :message R$string/gtw_conclusion
               :negative-button [R$string/no_thanks
                                 #(.finish activity)]
               :positive-button [R$string/sure
                                 #(start-activity activity GuessTheNumberActivity)])
  (talker/say @talker (.getString activity R$string/gtw_conclusion)))

(defn learn-task
  [activity adapter strategies]
  (let [max-progress 10000
        num-strategies (count strategies)
        update-factor (/ max-progress num-strategies)
        strategy-num (atom 0)
        update-progress
          (fn [strategy]
            (.runOnUiThread activity
              (reify Runnable
                (run [_]
                  (.setProgress activity (int (* (swap! strategy-num inc)
                                                 update-factor)))
                  (.add adapter strategy)))))]
    (proxy [android.os.AsyncTask] []
      (onPreExecute []
        (.setProgress activity 0))
      (doInBackground [_]
        (doseq [strategy strategies]
          (Thread/sleep 50)
          (update-progress strategy)))
      (onPostExecute [_]
        (.setProgress activity max-progress)
        (draw-conclusion activity)))))

(defn -onCreate [this bundle]
  (doto this
    (.requestWindowFeature android.view.Window/FEATURE_PROGRESS)
    (.superOnCreate bundle)
    (.setContentView R$layout/global_thermonuclear_war))
  (reset! talker (talker/init this))
  (let [adapter (ArrayAdapter. this android.R$layout/simple_list_item_1)
        strategies (.. this getResources (getStringArray R$array/gtw_strategies))]
    (.. this (findViewById R$id/gtw_strategy_list) (setAdapter adapter))
    (.execute (learn-task this adapter strategies) nil)))

(defn -onDestroy
  [this]
  (when @talker
    (talker/shutdown @talker)
    (reset! talker nil))
  (.superOnDestroy this))
