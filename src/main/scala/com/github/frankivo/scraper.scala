package com.github.frankivo

import sttp.client3.*

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
        event((e \\ "a").head.text)
      })
  }
}
