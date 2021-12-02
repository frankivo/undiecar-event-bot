package com.github.frankivo

object bot {
  @main def run() = {
    val count = scraper.update()
    println(s"Got $count results")
  }
}
