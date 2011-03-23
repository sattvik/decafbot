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
java_import "android.view.Window"
java_import "android.widget.ArrayAdapter"
ruboto_import "decafbot.ruboto.AsyncTask"
ruboto_import "decafbot.ruboto.Runnable"


$activity.handle_create do |bundle|
  androidLayout = JavaUtilities.get_proxy_class( "android.R$layout" )
  $activity.requestWindowFeature Window::FEATURE_PROGRESS
  $activity.content_view = Ruboto::R::layout::global_thermonuclear_war
  @talker = Talker.new $activity
  @adapter = ArrayAdapter.new $activity, androidLayout.simple_list_item_1
  $activity.findViewById(Ruboto::Id::gtw_strategy_list).adapter = @adapter

  # set up background task
  learn_task = AsyncTask.new

  learn_task.handle_pre_execute do 
    # start showing the progress bar
    $activity.setProgress 0
  end

  learn_task.handle_do_in_background do |strategies|
    Array(strategies).each_index do |i|
      strategy = strategies[i]
      activity = $activity

      # code that should run in the UI thread
      update_ui = Runnable.new.handle_run do 
        activity.setProgress (10000 * i / strategies.length)
        @adapter.add strategy
      end

      # think
      sleep 0.05
      # publish
      $activity.runOnUiThread update_ui
    end
  end

  learn_task.handle_post_execute do 
    # stop showing the progress bar
    $activity.setProgress 10000
    draw_conclusion
  end

  # execute the task
  learn_task.execute $activity.getResources.getStringArray Ruboto::R::array::gtw_strategies


  # present conclusion
  def draw_conclusion
    # handler that starts 'guess the number'
    start_guess_the_number = DialogOnClickListener.new.handle_click do |view, id|
      launch_activity GuessTheNumberActivity.java_class
    end

    # handler that starts 'global thermonuclear war'
    end_game = DialogOnClickListener.new.handle_click do |view, id|
      finish
    end

    builder = AlertDialog::Builder.new $activity
    builder.setTitle Ruboto::R::string::game_name_global_thermonuclear_war
    builder.setMessage Ruboto::R::string::gtw_conclusion
    builder.setNegativeButton Ruboto::R::string::no_thanks, end_game
    builder.setPositiveButton Ruboto::R::string::sure, start_guess_the_number
    builder.show
    @talker.say $activity.getString Ruboto::R::string::gtw_conclusion
  end

  # clean up the talker
  handle_destroy do
    @talker.shutdown
  end
end

# vim:set et sw=2 ts=2:
