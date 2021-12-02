package com.github.frankivo

import org.scalatest.funsuite.AnyFunSuite

import scala.io.Source

class scraperTest extends AnyFunSuite :

  lazy val html: String = Source.fromResource("events.html").getLines().mkString("\n")

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
    val body = scraper.getBody(html)
    val xml = scraper.toXML(body)
    val events = scraper.getEvents(xml)
    assert(events.length == 12)
  }

  test("first event has correct name") {
    val body = scraper.getBody(html)
    val xml = scraper.toXML(body)
    val events = scraper.getEvents(xml)
    assert(events.head.name.equals("Street Stocks and MX-5s at Road America"))
  }

end scraperTest
