package com.github.frankivo

import org.scalatest.funsuite.AnyFunSuite

import scala.io.Source

class scraperTest extends AnyFunSuite :

  lazy val html: String = Source.fromResource("events.html").getLines().mkString("\n")

  test("getBody results in fewer lines") {
    val start = html.split("\n").length
    val body = scraper.getBody(html)
    val parsed = scraper.getBody(html).split("\n").length

    assert(parsed < start)
  }

  test("xml is valid") {
    val body = scraper.getBody(html)
    val xml = scraper.toXML(body)
    assert(xml.getClass == classOf[scala.xml.Elem])
    assert(xml.toString().startsWith("<main"))
  }

  test("amount of events is 12") {
    val body = scraper.getBody(html)
    val xml = scraper.toXML(body)
    val events = scraper.getEvents(xml)
    assert(events.length == 12)
  }

  test("first event is 'Street Stocks and MX-5s at Road America'") {
    val body = scraper.getBody(html)
    val xml = scraper.toXML(body)
    val events = scraper.getEvents(xml)
    assert(events.head.name.equals("Street Stocks and MX-5s at Road America"))
  }

end scraperTest
