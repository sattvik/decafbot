package decafbot.scala.tests

import junit.framework.Assert._
import _root_.android.test.AndroidTestCase

class UnitTests extends AndroidTestCase {
  def testPackageIsCorrect {
    assertEquals("decafbot.scala", getContext.getPackageName)
  }
}