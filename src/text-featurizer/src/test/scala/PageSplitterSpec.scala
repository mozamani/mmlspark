// Copyright (C) Microsoft Corporation. All rights reserved.
// Licensed under the MIT License. See LICENSE in project root for information.

package com.microsoft.ml.spark

import org.apache.spark.ml.util.MLReadable

class PageSplitterSpec extends TransformerFuzzing[PageSplitter] {

  import session.implicits._

  lazy val df = Seq(
    "words words  words     wornssaa ehewjkdiw weijnsikjn xnh",
    "s s  s   s     s           s",
    "hsjbhjhnskjhndwjnbvckjbnwkjwenbvfkjhbnwevkjhbnwejhkbnvjkhnbndjkbnd",
    "hsjbhjhnskjhndwjnbvckjbnwkjwenbvfkjhbnwevkjhbnwejhkbnvjkhnbndjkbnd " +
      "190872340870271091309831097813097130i3u709781",
    ""
  ).toDF("text")

  lazy val t = new PageSplitter()
    .setInputCol("text")
    .setMaximumPageLength(20)
    .setMinimumPageLength(10)
    .setOutputCol("pages")

  test("Basic usage") {
    t.transform(df).collect().foreach { row =>
      val pages = row.getSeq[String](1).toList
      val text = row.getString(0)
      assert(pages.mkString("") === text)
      assert(pages.forall(_.length <= 20))
      assert(pages.dropRight(1).forall(_.length >= 10))
    }
  }

  override def testObjects(): Seq[TestObject[PageSplitter]] =
    List(new TestObject(t, df))

  override def reader: MLReadable[_] = PageSplitter

}
