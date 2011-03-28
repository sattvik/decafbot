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

import android.app.AlertDialog
import android.content.Context

# Utility used to build alert dialogs easily
class AlertDialogBuilder
  def initialize(context: Context)
    @builder = AlertDialog.Builder.new context
  end

  def title_set(title: int)
    @builder.setTitle title
  end

  def message_set(message: int)
    @builder.setMessage message
  end

  def message_set(message: String)
    @builder.setMessage message
  end

  def negative_text_set(text: int)
    @has_neg_button = true
    @negative_text = text
  end

  def on_negative(listener: android.content.DialogInterface.OnClickListener)
    @negative_listener = listener
  end

  def positive_text_set(text: int)
    @has_pos_button = true
    @positive_text = text
  end

  def on_positive(listener: android.content.DialogInterface.OnClickListener)
    @positive_listener = listener
  end

  def show
    if @has_pos_button
      @builder.setPositiveButton @positive_text, @positive_listener
    end
    if @has_neg_button
      @builder.setNegativeButton @negative_text, @negative_listener
    end
    @builder.show
  end
end

# vim:set ft=ruby sw=2 ts=2 et:
