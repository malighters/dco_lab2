package de.othr.dco

import com.github.kiprobinson.bigfraction.BigFraction

import java.math.BigInteger
import java.util.Random
import java.util.concurrent.ForkJoinPool


object LabApplication extends App {

  System.gc()

  val MaxNumbers = 10000
  var rows: Array[List[String]] = Array()

  val numbers = LazyList.continually( ( new BigInteger(150000, new Random()), new BigInteger(15000, new Random()) ) )
    .take(MaxNumbers)
    .toArray

  val bigFractions: List[BigFraction] = numbers.map(n => BigFraction.valueOf(n._1, n._2)).toList

  // Iterative
  {
    val list: List[BigFraction] = bigFractions
    val startTime = System.currentTimeMillis()
    list.map(fr => fr.intValue())
    val endTime = System.currentTimeMillis()

    rows = rows :+ List(s"${(endTime - startTime)}", s"iterative", "", s"")
  }


  // Fork/join
  val p = Runtime.getRuntime.availableProcessors()

  for (n <- Seq(p/2, p*2, p-1)) {
    for (t <- Seq(1, 10, 100)) {

      val pool: ForkJoinPool = new ForkJoinPool(n)

      val recursiveTask = new BigFractionTask(bigFractions, t)

      val startTime = System.currentTimeMillis()
      val result = pool.invoke(recursiveTask)
      val endTime = System.currentTimeMillis()

      rows = rows :+ List(s"${(endTime - startTime)}", s"fork/join($n)", s"$t", s"${pool.getStealCount}")
      pool.shutdown()
    }
  }

  // Common pool
  for (t <- Seq(1, 10, 100)) {
    val pool: ForkJoinPool = ForkJoinPool.commonPool();

    val recursiveTask = new BigFractionTask(bigFractions, t)

    val startTime = System.currentTimeMillis()
    val result = pool.invoke(recursiveTask)
    val endTime = System.currentTimeMillis()

    rows = rows :+ List(s"${(endTime - startTime)}", s"commonPool", s"$t", s"${pool.getStealCount}")
    pool.shutdown()
  }



  println(s"Number of threads presented in OS: $p")
  Table.printTable(rows)
  // Have fun :-)
}
