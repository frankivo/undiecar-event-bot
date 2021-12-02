package com.github.frankivo

import java.time.Month
import java.time.format.TextStyle
import java.util.Locale

object dateutil {
  def monthNumber(short: String): Int = {
    Month
      .values()
      .filter(m => short.equalsIgnoreCase(m.getDisplayName(TextStyle.SHORT, Locale.getDefault)))
      .head
      .getValue
  }
}
