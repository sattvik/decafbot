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

(ns decafbot.clojure.GameChooserActivity
  "Activity that allows the user to choose a game to play."
  {:author "Daniel Solano Gómez"}
  (:gen-class :main no
              :extends android.app.Activity
              :exposes-methods {onCreate superOnCreate
                                onDestroy superOnDestroy})
  (:require [decafbot.clojure.talker :as talker])
  (:use decafbot.clojure.utils)
  (:import android.view.View$OnClickListener
           [decafbot.clojure GlobalThermonuclearWarActivity
                             GuessTheNumberActivity
                             R$id R$layout R$string]))

(def talker (atom nil))

(defn- confirm-war
  [activity]
  (let [start-activity (partial start-activity activity)]
    (show-dialog activity
                 :title R$string/game_name_global_thermonuclear_war
                 :message R$string/chooser_confirm_nuke
                 :negative-button [R$string/later
                                   #(start-activity GlobalThermonuclearWarActivity)]
                 :positive-button [android.R$string/yes
                                   #(start-activity GuessTheNumberActivity)])
    (talker/say @talker (.getString activity R$string/chooser_confirm_nuke))))

(defn- handle-click
  [selected activity]
  (condp = selected
    R$id/game_guess_the_number
      (start-activity activity GuessTheNumberActivity) 
    R$id/game_global_thermonuclear_war
      (confirm-war activity)))

(defn -onCreate
  [this bundle]
  (doto this
    (.superOnCreate bundle)
    (.setContentView R$layout/game_chooser))
  (reset! talker (talker/init this))
  (when (nil? bundle)
    (talker/say @talker (.getString this R$string/greetings)))
  (let [button (.findViewById this R$id/choose_button)
        radio-group (.findViewById this R$id/game_chooser_list)]
    (.setOnClickListener button
      (reify View$OnClickListener
        (onClick [_ _]
          (handle-click (.getCheckedRadioButtonId radio-group)
                        this))))))

(defn -onDestroy
  [this]
  (when @talker
    (talker/shutdown @talker)
    (reset! talker nil))
  (.superOnDestroy this))
