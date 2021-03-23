package com.tomekl007.CH08.joins

import org.apache.spark.sql.functions.broadcast
import org.apache.spark.sql.{Dataset, SparkSession}
import org.scalatest.funsuite.AnyFunSuite


class DataSetJoins extends AnyFunSuite {
  val spark = SparkSession.builder().master("local[2]").getOrCreate()

  test("Should inner join two DS") {
    import spark.sqlContext.implicits._
    val userData =
      spark.sparkContext.makeRDD(List(
        UserData("a", "1"),
        UserData("b", "2"),
        UserData("d", "200")
      )).toDS()

    val clicks =
      spark.sparkContext.makeRDD(List(
        Click("a", "www.page1"),
        Click("b", "www.page2"),
        Click("c", "www.page3")
      )).toDS()

    //when
    val res: Dataset[(UserData, Click)]
    = userData.joinWith(clicks, userData("userId") === clicks("userId"), "inner")


    //then
    res.explain()
    res.show()
    assert(res.count() == 2)

    /**
     * output is following
     * == Physical Plan ==
     * *SortMergeJoin [_1#206.userId], [_2#207.userId], Inner
     * :- *Sort [_1#206.userId ASC], false, 0
     * :  +- Exchange hashpartitioning(_1#206.userId, 200)
     * :     +- *Project [struct(userId#198, data#199) AS _1#206]
     * :        +- Scan ExistingRDD[userId#198,data#199]
     * +- *Sort [_2#207.userId ASC], false, 0
     * +- Exchange hashpartitioning(_2#207.userId, 200)
     * +- *Project [struct(userId#203, url#204) AS _2#207]
     * +- Scan ExistingRDD[userId#203,url#204]
     * +-----+-------------+
     * |   _1|           _2|
     * +-----+-------------+
     * |[b,2]|[b,www.page2]|
     * |[a,1]|[a,www.page1]|
     * +-----+-------------+
     */
  }

  test("Should inner join two DS whereas one of them is broadcasted") {
    import spark.sqlContext.implicits._
    val userData =
      spark.sparkContext.makeRDD(List(
        UserData("a", "1"),
        UserData("b", "2"),
        UserData("d", "200")
      )).toDS()

    val clicks =
      spark.sparkContext.makeRDD(List(
        Click("a", "www.page1"),
        Click("b", "www.page2"),
        Click("c", "www.page3")
      )).toDS()

    //when
    val res: Dataset[(UserData, Click)]
    = userData.joinWith(broadcast(clicks), userData("userId") === clicks("userId"), "inner")


    //then
    res.explain()
    res.show()
    assert(res.count() == 2)

    /**
     * output is following:
     * == Physical Plan ==
     * *BroadcastHashJoin [_1#234.userId], [_2#235.userId], Inner, BuildRight
     * :- *Project [struct(userId#225, data#226) AS _1#234]
     * :  +- Scan ExistingRDD[userId#225,data#226]
     * +- BroadcastExchange HashedRelationBroadcastMode(List(input[0, struct<userId:string,url:string>, false].userId))
     * +- *Project [struct(userId#230, url#231) AS _2#235]
     * +- Scan ExistingRDD[userId#230,url#231]
     * +-----+-------------+
     * |   _1|           _2|
     * +-----+-------------+
     * |[a,1]|[a,www.page1]|
     * |[b,2]|[b,www.page2]|
     * +-----+-------------+
     */
  }
}
