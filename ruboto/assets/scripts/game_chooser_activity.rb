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
require 'talker.rb'
require 'launcher.rb'

java_import "android.app.AlertDialog"
ruboto_import "decafbot.ruboto.DialogOnClickListener"
ruboto_import "decafbot.ruboto.GuessTheNumberActivity"
ruboto_import "decafbot.ruboto.GlobalThermonuclearWarActivity"
ruboto_import "decafbot.ruboto.Runnable"

$activity.handle_create do |bundle|
  $activity.content_view = Ruboto::R::layout::game_chooser

  # set up callback for button
  choose_button = $activity.findViewById Ruboto::Id::choose_button
  choose_button.setOnClickListener $activity

  # set up talker and say something the first time
  @talker = Talker.new $activity
  if bundle.nil?
    @talker.say $activity.getString Ruboto::R::string::greetings
  end

  # handles the action from the select button
  handle_click do |view|
    games = $activity.findViewById Ruboto::Id::game_chooser_list
    case games.getCheckedRadioButtonId
    when Ruboto::Id::game_guess_the_number
      launch_activity GuessTheNumberActivity.java_class
    when Ruboto::Id::game_global_thermonuclear_war
      confirm_war
    end
  end

  # confirms whether or not the user really wants to wage global
  # thermonuclear war
  def confirm_war
    # handler that starts 'guess the number'
    start_guess_the_number = DialogOnClickListener.new.handle_click do |view, id|
      launch_activity GuessTheNumberActivity.java_class
    end

    # handler that starts 'global thermonuclear war'
    start_global_thermonuclear_war = DialogOnClickListener.new.handle_click do |view, id|
      launch_activity GlobalThermonuclearWarActivity.java_class
    end

    androidString = JavaUtilities.get_proxy_class( "android.R$string" )
    builder = AlertDialog::Builder.new $activity
    builder.setTitle Ruboto::R::string::game_name_global_thermonuclear_war
    builder.setMessage Ruboto::R::string::chooser_confirm_nuke
    builder.setNegativeButton Ruboto::R::string::later, start_global_thermonuclear_war
    builder.setPositiveButton androidString::yes, start_guess_the_number
    builder.show
    @talker.say $activity.getString Ruboto::R::string::chooser_confirm_nuke
  end

  # Ensure that the talker is cleaned up.
  handle_destroy do
    @talker.shutdown
  end
end

# vim:set et sw=2 ts=2:
