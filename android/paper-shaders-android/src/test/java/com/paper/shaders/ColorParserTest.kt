package com.paper.shaders

import org.junit.Assert.assertArrayEquals
import org.junit.Test

class ColorParserTest {
  @Test
  fun parsesHexColors() {
    val rgba = ColorParser.parse("#fff")
    assertArrayEquals(floatArrayOf(1f, 1f, 1f, 1f), rgba, 0.001f)
  }

  @Test
  fun parsesRgba() {
    val rgba = ColorParser.parse("rgba(255, 0, 0, 0.5)")
    assertArrayEquals(floatArrayOf(1f, 0f, 0f, 0.5f), rgba, 0.001f)
  }
}
