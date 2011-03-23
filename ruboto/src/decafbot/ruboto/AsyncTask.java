package decafbot.ruboto;

import org.jruby.Ruby;
import org.jruby.javasupport.util.RuntimeHelpers;
import org.jruby.runtime.builtin.IRubyObject;
import org.jruby.javasupport.JavaUtil;
import org.jruby.exceptions.RaiseException;
import org.ruboto.Script;

public class AsyncTask extends android.os.AsyncTask {
  private Ruby __ruby__;

  public static final int CB_POST_EXECUTE = 0;
  public static final int CB_PRE_EXECUTE = 1;
  public static final int CB_PROGRESS_UPDATE = 2;
  public static final int CB_DO_IN_BACKGROUND = 3;
  private IRubyObject[] callbackProcs = new IRubyObject[4];

  public  AsyncTask() {
    super();
  }

  private Ruby getRuby() {
    if (__ruby__ == null) __ruby__ = Script.getRuby();
    return __ruby__;
  }

  public void setCallbackProc(int id, IRubyObject obj) {
    callbackProcs[id] = obj;
  }
	
  public void onPostExecute(Object result) {
    if (callbackProcs[CB_POST_EXECUTE] != null) {
      super.onPostExecute(result);
      try {
        RuntimeHelpers.invoke(getRuby().getCurrentContext(), callbackProcs[CB_POST_EXECUTE], "call" , JavaUtil.convertJavaToRuby(getRuby(), result));
      } catch (RaiseException re) {
        re.printStackTrace();
      }
    } else {
      super.onPostExecute(result);
    }
  }

  public void onPreExecute() {
    if (callbackProcs[CB_PRE_EXECUTE] != null) {
      super.onPreExecute();
      try {
        RuntimeHelpers.invoke(getRuby().getCurrentContext(), callbackProcs[CB_PRE_EXECUTE], "call" );
      } catch (RaiseException re) {
        re.printStackTrace();
      }
    } else {
      super.onPreExecute();
    }
  }

  public void onProgressUpdate(Object... values) {
    if (callbackProcs[CB_PROGRESS_UPDATE] != null) {
      super.onProgressUpdate(values);
      try {
        RuntimeHelpers.invoke(getRuby().getCurrentContext(), callbackProcs[CB_PROGRESS_UPDATE], "call" , JavaUtil.convertJavaToRuby(getRuby(), values));
      } catch (RaiseException re) {
        re.printStackTrace();
      }
    } else {
      super.onProgressUpdate(values);
    }
  }

  public Object doInBackground(Object... params) {
    if (callbackProcs[CB_DO_IN_BACKGROUND] != null) {
      try {
        return RuntimeHelpers.invoke(getRuby().getCurrentContext(), callbackProcs[CB_DO_IN_BACKGROUND], "call" , JavaUtil.convertJavaToRuby(getRuby(), params)).toJava(Object.class);
      } catch (RaiseException re) {
        re.printStackTrace();
        return null;
      }
    } else {
      return null;
    }
  }
}
