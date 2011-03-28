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
import android.os.AsyncTask
import android.os.Bundle
import android.view.Window
import android.widget.ArrayAdapter
import android.widget.ListView

class GlobalThermonuclearWarActivity < Activity
  def onCreate(state)
    requestWindowFeature Window.FEATURE_PROGRESS
    super state
    setContentView R::layout::global_thermonuclear_war

    # init list
    @adapter = ArrayAdapter.new self, android::R::layout::simple_list_item_1
    ListView(findViewById R::id::gtw_strategy_list).setAdapter @adapter

    strategies = getResources.getStringArray R::array::gtw_strategies
    LearnTask.new(self, strategies).execute Object[0]

    @talker = Talker.new self
  end

  def add_strategy(strategy: String)
    @adapter.add strategy
  end

  # presents the conclusion about the futility of global thermonuclear war
  def draw_conclusion
    activity = self
    builder = AlertDialogBuilder.new self
    builder.title = R::string::game_name_global_thermonuclear_war
    builder.message = R::string::gtw_conclusion
    builder.negative_text = R::string::no_thanks
    builder.on_negative do |v,i|
      activity.finish
    end
    builder.positive_text = R::string::sure
    builder.on_positive do |v,i|
      activity.startActivity Intent.new activity, GuessTheNumberActivity.class
      activity.finish
    end

    @talker.say getString R::string::gtw_conclusion
    builder.show
  end

  class LearnTask < AsyncTask
    def initialize(activity: GlobalThermonuclearWarActivity,
                   strategies: String[])
      @strategies = strategies
      @activity = activity
    end

    def onPreExecute
      @activity.setProgress 0
    end

    def doInBackground(params)
      i = 0
      @strategies.each do |strategy|
        Thread.sleep 50
        i += 1
        publishProgress [strategy, i].toArray
      end
    end

    def onProgressUpdate(values)
      strategy_num = Integer(values[1]).intValue
      @activity.setProgress (10000 * strategy_num / @strategies.length)
      @activity.add_strategy String(values[0])
    end

    def onPostExecute(result)
      @activity.setProgress 10000
      @activity.draw_conclusion
    end
  end

  def onDestroy
    @talker.shutdown
    super
  end
end

# vim:set ft=ruby sw=2 ts=2 et:
