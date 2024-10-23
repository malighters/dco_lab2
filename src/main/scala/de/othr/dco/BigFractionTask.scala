package de.othr.dco

import com.github.kiprobinson.bigfraction.BigFraction

import java.util.concurrent.RecursiveTask

class BigFractionTask(list: List[BigFraction], threshold: Int) extends RecursiveTask[List[Int]] {

  def iterative(list: List[BigFraction]): List[Int] = {
    list.map(fr => fr.intValue())
  }

  override def compute: List[Int] = {
    if(list.length <= threshold){
      iterative(list)
    }
    else {
      val (leftList, rightList) = list.splitAt(list.length / 2)
      val left = new BigFractionTask(leftList, threshold)
      val right = new BigFractionTask(rightList, threshold)
      left.fork()
      right.fork()

      val leftResult = left.join()
      val rightResult = right.join()
      leftResult ++ rightResult
    }
  }
}
