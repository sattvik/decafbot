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

import android.app.Activity
import android.os.Bundle
import android.widget.EditText
import android.widget.TextView
import java.util.Random

class GuessTheNumberActivity < Activity
  def onCreate(state)
    super state
    setContentView R::layout::guess_the_number

    @prompt = TextView(findViewById(R::id::prompt))
    @entry = EditText(findViewById(R::id::guess))
    @talker = Talker.new self
    @random = Random.new

    this = self
    findViewById(R::id::submit_button).setOnClickListener do |view|
      this.handle_guess
    end
    
    init_game
  end

  def init_game
    @number = 1 + @random.nextInt(100)
    @guesses_left = 10
    @prompt.setText R::string::gtn_initial_prompt
    clearGuess
    @talker.say getString R::string::gtn_thinking
  end

  def clearGuess
    @entry.setText ""
  end

  def handle_guess
    begin
      analyze_guess guessValue
    rescue NumberFormatException
      provide_feedback R::string::gtn_not_a_number
    end
  end

  def guessValue
    Integer.parseInt @entry.getText.toString, 10
  end

  def analyze_guess(guess: int)
    @guesses_left -= 1
    if guess == @number
      game_end :player_won
    elsif @guesses_left <= 0
      game_end :player_lost
    elsif guess < @number
      bad_guess :too_low
    else
      bad_guess :too_high
    end
  end

  def provide_feedback(id: int)
    audio_str = getString id
    @prompt.setText getString R::string::gtn_feedback_prompt,
      [audio_str, @guesses_left].toArray
    @talker.say audio_str
  end

  def game_end(end_type: String)
    # get win/lose text
    if end_type == :player_won
      speech = getString R::string::gtn_player_won
    else
      speech = getString R::string::gtn_player_lost
    end
    game = self

    builder = AlertDialogBuilder.new self
    builder.title = R::string::game_name_guess_the_number
    builder.message = getString R::string::gtn_end_dialog, [speech].toArray
    builder.positive_text = android::R::string::yes
    builder.on_positive { |d,i| game.init_game }
    builder.negative_text = android::R::string::no
    builder.on_negative { |d,i| game.finish }

    # show and tell
    builder.show
    @talker.say speech
  end

  def bad_guess(guess_type: String)
    clearGuess
    if guess_type == :too_low
      provide_feedback R::string::gtn_too_low
    else
      provide_feedback R::string::gtn_too_high
    end
  end

  def onDestroy
    @talker.shutdown
    super
  end
end

# vim:set ft=ruby sw=2 ts=2 et:
