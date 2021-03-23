package com.tomekl007.CH13.functional

import scala.annotation.tailrec

object ReduceScala {
  @tailrec
  def reduce[T] (values: List[T], reducer: (T, T) => T, accumulator:T ): T = values match {
    case Nil => accumulator
    case head :: tail => reduce(tail, reducer, reducer(head, accumulator))
  }
}
