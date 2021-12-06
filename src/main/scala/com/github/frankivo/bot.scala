package com.github.frankivo

object bot {
  @main def run(): Unit = {
    val count = scraper.update()
    println(s"Got $count results")
  }
}
