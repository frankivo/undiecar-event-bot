package com.github.frankivo

import org.scalatest.funsuite.AnyFunSuite

import java.time.LocalDate
import scala.io.Source
import scala.util.Random

class scraperTest extends AnyFunSuite :

  lazy val html: String = Source.fromResource("events.html").getLines().mkString("\n")
  lazy val events: Seq[event] = scraper.getEvents(scraper.toXML(scraper.getBody(html)))

  test("body has correct amount of lines") {
    val body = scraper.getBody(html)
    val lines = body.replaceAll("""\s\s+""", "\n").split("\n").length
    assert(lines == 1253)
  }

  test("xml is valid") {
    val body = scraper.getBody(html)
    val xml = scraper.toXML(body)
    assert(xml.getClass == classOf[scala.xml.Elem])
    assert(xml.toString().startsWith("<main"))
  }

  test("amount of events is correct") {
    assert(events.length == 12)
  }

  test("first event has correct name") {
    assert(events.head.name.equals("Street Stocks and MX-5s at Road America"))
  }

  test("events have correct date") {
    val exp = Random.shuffle(
      Seq("2021-11-30", "2021-12-07", "2021-12-14", "2021-12-21", "2021-12-28", "2022-01-04",
        "2022-01-11", "2022-01-18", "2022-01-25", "2022-02-08", "2022-02-15", "2022-02-22")
    ).map(LocalDate.parse)
    val act = events.map(_.date)

    assert(act.length == 12)
    assert(exp.diff(act).isEmpty)
  }

  test("last event has correct url") {
    assert(events.last.url.equals("https://undiecar.com/event/dw12s-at-summit-point/"))
  }

end scraperTest
