package de.othr.dco

import java.math.BigInteger
import java.util.concurrent.RecursiveAction

class BigFractionTask(testData: TestData[(BigInteger,BigInteger)], start:Int, end:Int, threshold:Int) extends RecursiveAction {

  override def compute(): Unit = {
    if((end - start) < threshold) {
      for(i <- (start to end))
        testData.testOperation(testData.values(i))
    }
    else {
      val mid = start + ((end - start) / 2)
      val left = new BigFractionTask(testData, start, mid, threshold).fork()
      val right = new BigFractionTask(testData, mid + 1, end, threshold).fork()
      left.join()
      right.join()
    }
  }
}
