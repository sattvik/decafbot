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

(ns decafbot.clojure.GuessTheNumberActivity
  "Implementation of guess the number."
  {:author "Daniel Solano Gómez"}
  (:gen-class :main no
              :extends android.app.Activity
              :exposes-methods {onCreate superOnCreate
                                onDestroy superOnDestroy})
  (:require [decafbot.clojure.talker :as talker])
  (:use decafbot.clojure.utils)
  (:import android.view.View$OnClickListener
           [decafbot.clojure R$id R$layout R$string]))

(def activity (atom nil))
(def prompt (atom nil))
(def guess-entry (atom nil))
(def game-state (atom nil))
(def talker (atom nil))

(defn init-game
  "Initialises the game"
  []
  (reset! game-state {:number (inc (rand-int 100))
                      :guesses-left 10})
  (.setText @prompt (.getString @activity R$string/gtn_initial_prompt))
  (.setText @guess-entry "")
  (talker/say @talker (.getString @activity R$string/gtn_thinking)))

(defn parse-int
  [string]
  (try
    (Integer/parseInt string 10)
    (catch NumberFormatException _
      nil)))

(defn provide-feedback
  [id]
  (let [audio-str  (.getString @activity id)
        prompt-str (.getString @activity
                               R$string/gtn_feedback_prompt
                               (to-array [audio-str
                                          (:guesses-left @game-state)]))]
    (.setText @prompt prompt-str)
    (talker/say @talker audio-str)))

(defn end-of-game
  [victory-state]
  (let [speech (.getString @activity
                           (condp = victory-state
                             :player-won R$string/gtn_player_won
                             :player-lost R$string/gtn_player_lost))]
    (show-dialog @activity
                 :title R$string/game_name_guess_the_number
                :message (.getString @activity
                                      R$string/gtn_end_dialog 
                                      (to-array [speech]))
                 :positive-button [android.R$string/yes
                                   #(init-game)]
                 :negative-button [android.R$string/no
                                   #(.finish @activity)])
    (talker/say @talker speech)))

(defn incorrect-guess
  [guess-error]
  (.setText @guess-entry "")
  (swap! game-state (fn [{:keys [guesses-left] :as state}]
                      (assoc state :guesses-left (dec guesses-left))))
  (provide-feedback (condp = guess-error
                      :too-high R$string/gtn_too_high
                      :too-low R$string/gtn_too_low)))

(defn analyze-guess
  "Analyzes the player's guess and handles it appropriately."
  [guess]
  (let [{:keys [number guesses-left]} @game-state]
    (cond
      (= guess number)    (end-of-game :player-won)
      (<= guesses-left 1) (end-of-game :player-lost)
      (< guess number)    (incorrect-guess :too-low)
      :else               (incorrect-guess :too-high))))

(defn handle-guess
  "Handles a player's guess."
  []
  (if-let [guess (parse-int (.. @guess-entry getText toString))]
    (analyze-guess guess)
    (provide-feedback R$string/gtn_not_a_number)))

(defn -onCreate [this bundle]
  (doto this
    (.superOnCreate bundle)
    (.setContentView R$layout/guess_the_number))
  (reset! talker (talker/init this))
  (reset! activity this)
  (reset! prompt (.findViewById this R$id/prompt))
  (reset! guess-entry (.findViewById this R$id/guess))
  (init-game)
  (let [submit (.findViewById this R$id/submit_button)]
    (.setOnClickListener submit
                         (reify View$OnClickListener
                           (onClick [_1 _2]
                             (handle-guess))))))

(defn -onDestroy
  [this]
  (when @talker
    (talker/shutdown @talker)
    (reset! talker nil))
  (.superOnDestroy this))
