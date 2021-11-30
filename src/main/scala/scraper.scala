package com.github.frankivo

import sttp.client3._

object scraper {
  def update(): Long = {
    val data = get()
    parse(data)
  }

  def get(): String = {
    val request = basicRequest.get(uri"https://undiecar.com/events/")
    val backend = HttpURLConnectionBackend()
    val response = request.send(backend)
    response.body.getOrElse(throw new Exception("Could not update!"))
  }

  def parse(data: String): Long = {
    0
  }

}
