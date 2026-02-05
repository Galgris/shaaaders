package com.paper.shaders

import org.junit.Assert.assertEquals
import org.junit.Test

class DefaultsTest {
  @Test
  fun defaultsMatchWebPreset() {
    val params = PulsingBorderParams()
    assertEquals(0.6f, params.scale)
    assertEquals(0.75f, params.softness)
    assertEquals(0.25f, params.roundness)
    assertEquals(0.1f, params.thickness)
    assertEquals(5, params.spots)
  }
}
