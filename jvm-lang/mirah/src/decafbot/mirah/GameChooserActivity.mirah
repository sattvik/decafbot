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
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.RadioGroup

class GameChooserActivity < Activity
  def onCreate(state)
    super state
    setContentView R::layout::game_chooser

    activity = self
    findViewById(R.id.choose_button).setOnClickListener do |view|
      activity.handle_selection
    end

    @talker = Talker.new self
    if state.nil?
      @talker.say getString R::string::greetings
    end
  end

  def onDestroy
    @talker.shutdown
    super
  end

  def handle_selection
    radioGroup = RadioGroup(findViewById R::id::game_chooser_list)
    checkedId = radioGroup.getCheckedRadioButtonId
    if checkedId == R::id::game_guess_the_number
      launch GuessTheNumberActivity.class
    elsif checkedId == R::id::game_global_thermonuclear_war
      confirm_war
    end
  end


  def confirm_war: void
    activity = self

    builder = AlertDialogBuilder.new self
    builder.title = R::string::game_name_global_thermonuclear_war
    builder.message = R::string::chooser_confirm_nuke

    builder.negative_text = R::string::later
    builder.on_negative do |v,i|
      activity.launch GlobalThermonuclearWarActivity.class
    end

    builder.positive_text = android::R::string::yes
    builder.on_positive do |v,i|
      activity.launch GuessTheNumberActivity.class
    end

    builder.show
    @talker.say getString R::string::chooser_confirm_nuke
  end

  def launch(activity: Class)
    startActivity Intent.new(self, activity)
    finish
  end
end

# vim:set ft=ruby sw=2 ts=2 et:
