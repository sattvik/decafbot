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

require 'ruboto.rb'
require 'launcher.rb'
require 'talker.rb'

java_import "android.app.AlertDialog"
ruboto_import "decafbot.ruboto.DialogOnClickListener"

$activity.handle_create do |bundle|
  # initialises the state of the game
  def init_game
    @guesses_left = 10
    @number = 1 + rand(100)
    @prompt.setText Ruboto::R::string::gtn_initial_prompt
    @guess_entry.setText ""
    @talker.say $activity.getString Ruboto::R::string::gtn_thinking
  end

  $activity.content_view = Ruboto::R::layout::guess_the_number
  @prompt = $activity.findViewById Ruboto::Id::prompt
  @guess_entry = $activity.findViewById Ruboto::Id::guess
  button = $activity.findViewById Ruboto::Id::submit_button
  button.setOnClickListener $activity
  @talker = Talker.new $activity
  init_game

  handle_click do |view|
    begin
      guess = Integer @guess_entry.getText.toString
      analyze_guess guess
    rescue ArgumentError
      provide_feedback Ruboto::R::string::gtn_not_a_number
    end
  end

  # provides feedback on the outcome of a guess
  def provide_feedback(id)
    audio_str = $activity.getString id
    prompt_str = $activity.getString Ruboto::R::string::gtn_feedback_prompt,
      [audio_str, @guesses_left].to_java
    @prompt.setText prompt_str
    @talker.say audio_str
  end

  # enumeration of game outcomes
  module Outcomes
    PlayerWon = 0
    PlayerLost = 1
  end

  # enumeration of bad guess types
  module BadGuesses
    TooLow = -1
    TooHigh = 1
  end

  # analyzes the guess and updates the state of the game
  def analyze_guess(guess)
    @guesses_left -= 1
    if guess == @number
      game_end Outcomes::PlayerWon
    elsif @guesses_left <= 0
      game_end Outcomes::PlayerLost
    elsif guess < @number
      bad_guess BadGuesses::TooLow
    else
      bad_guess BadGuesses::TooHigh
    end
  end

  # handles the end of the game dialog
  def game_end(end_type)
    # handler that restarts the game
    play_again = DialogOnClickListener.new.handle_click do |view, id|
      init_game
    end

    # handler ends the activity
    end_game = DialogOnClickListener.new.handle_click do |view, id|
      finish
    end
    
    # speech to say
    case end_type
    when Outcomes::PlayerWon
      speech = $activity.getString Ruboto::R::string::gtn_player_won
    when Outcomes::PlayerLost
      speech = $activity.getString Ruboto::R::string::gtn_player_lost
    end

    # dialog text
    message = $activity.getString Ruboto::R::string::gtn_end_dialog,
      [speech].to_java


    # build dialog
    androidString = JavaUtilities.get_proxy_class( "android.R$string" )
    builder = AlertDialog::Builder.new $activity
    builder.setTitle Ruboto::R::string::game_name_guess_the_number
    builder.setMessage message
    builder.setPositiveButton androidString::yes, play_again
    builder.setNegativeButton androidString::no, end_game

    # show and tell
    builder.show
    @talker.say speech
  end

  # handle a bad guess
  def bad_guess(guess_type)
    @guess_entry.setText ""
    case guess_type
    when BadGuesses::TooLow
      provide_feedback Ruboto::R::string::gtn_too_low
    when BadGuesses::TooHigh
      provide_feedback Ruboto::R::string::gtn_too_high
    end
  end

  # Ensure that the talker is cleaned up.
  handle_destroy do
    @talker.shutdown
  end
end

# vim:set et sw=2 ts=2:
