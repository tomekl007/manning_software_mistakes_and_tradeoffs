package com.tomekl007.CH13.functional

import org.scalatest.funsuite.AnyFunSuite


class ReduceScalaTest extends AnyFunSuite {

  test("should reduce two values"){
    // given
    val input = List(1,2)

    // when
    val result = ReduceScala.reduce(input, (v:Int, a:Int) => v+a, 0)

    // then
    assert(result == 3)
  }

  test("should reduce ten values"){
    // given
    val input = List(0,1,2,3,4,5,6,7,8,9)

    // when
    val result = ReduceScala.reduce(input, (v:Int, a:Int) => v+a, 0)

    // then
    assert(result == 45)
  }

  test("should reduce a lot of values without the stack overflow"){
    // given
    val input = Range.inclusive(0, 100000).toList

    // when
    val result = ReduceScala.reduce(input, (v:Int, a:Int) => v+a, 0)

    // then
    assert(result == 705082704)
  }
}
