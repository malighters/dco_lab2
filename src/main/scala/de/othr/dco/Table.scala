package de.othr.dco

object Table {
  def printTable(rows: Array[List[String]]): Unit = {
    val headers = List("Time, ms", "Method", "Threshold", "Steal count")
    val columnWidths = headers.zipWithIndex.map { case (header, i) =>
      math.max(header.length, rows.map(_(i).length).max)
    }

    def formatRow(row: List[String]): String = {
      row.zip(columnWidths).map { case (item, width) =>
        f"%%-${width}s" format item
      }.mkString(" | ")
    }

    // Print headers
    println(columnWidths.map("-" * _).mkString("-+-"))
    println(formatRow(headers))
    println(columnWidths.map("-" * _).mkString("-+-"))

    // Print each row
    rows.foreach { row =>
      println(formatRow(row.toList))
    }
  }
}
