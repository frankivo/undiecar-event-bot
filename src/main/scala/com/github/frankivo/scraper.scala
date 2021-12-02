package com.github.frankivo

import sttp.client3.*

import java.text.SimpleDateFormat
import java.time.format.TextStyle
import java.time.{DayOfWeek, LocalDate, Month}
import java.util.Locale
import scala.io.Source

object scraper {
  def update(): Long = {
    val data = download()
    val body = getBody(data)
    val xml = toXML(body)
    getEvents(xml).length
  }

  private def download(): String = {
    val request = basicRequest.get(uri"https://undiecar.com/events/")
    val backend = HttpURLConnectionBackend()
    val response = request.send(backend)
    response.body.getOrElse(throw new Exception("Could not update!"))
  }

  def getBody(html: String): String = {
    val lines = html.split("\n").filterNot(_.isEmpty)
    val start = lines.indexWhere(_.startsWith("<main"))
    val end = lines.indexWhere(_.startsWith("</main>")) + 1
    lines.slice(start, end).mkString
  }

  def toXML(data: String): scala.xml.Elem = scala.xml.XML.loadString(data)

  def getEvents(node: xml.Elem): Seq[event] = {
    ((node \\ "table").head \ "tbody" \ "tr")
      .map(e => {
        val date = getDate((e \\ "td") (1).text)
        val name = (e \\ "a").head.text
        val url = (e \\ "@href").text

        event(name, date, url)
      })
  }

  def getDate(raw: String): LocalDate = {
    val withoutDay = DayOfWeek
      .values()
      .map(_.toString)
      .foldLeft(raw) { (a, b) => a.toUpperCase().replace(b, "") }

    val monthRaw = withoutDay.split(" ").head
    val monthNum = dateutil.monthNumber(monthRaw).toString

    val nums = """([\d]+)""".r.findAllMatchIn(raw).toSeq

    val dow = nums.head.matched
    val year = nums(1).matched

    val dateRaw = s"$year-${padLeft(monthNum)}-${padLeft(dow)}"

    LocalDate.parse(dateRaw)
  }

  private def padLeft(string: String) = string.reverse.padTo(2, "0").mkString.reverse
}
