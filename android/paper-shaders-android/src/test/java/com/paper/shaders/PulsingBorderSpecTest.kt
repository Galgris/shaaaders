package com.paper.shaders

import org.junit.Assert.assertEquals
import org.junit.Test

class PulsingBorderSpecTest {
  @Test
  fun parsesJsonSpec() {
    val json = """
      {
        "width": 1280,
        "height": 720,
        "colors": ["#0dc1fd", "#d915ef", "#ff3f2ecc"],
        "colorBack": "#000000",
        "roundness": 1,
        "thickness": 0,
        "softness": 0.75,
        "aspectRatio": "square",
        "intensity": 0.2,
        "bloom": 0.45,
        "spots": 3,
        "spotSize": 0.4,
        "pulse": 0.5,
        "smoke": 1,
        "smokeSize": 0,
        "speed": 1,
        "scale": 0.6,
        "marginLeft": 0,
        "marginRight": 0,
        "marginTop": 0,
        "marginBottom": 0
      }
    """.trimIndent()

    val spec = PulsingBorderSpec.fromJson(json)
    assertEquals(1280, spec.width)
    assertEquals(720, spec.height)
    assertEquals(3, spec.params.colors.size)
    assertEquals(PulsingBorderParams.AspectRatio.SQUARE, spec.params.aspectRatio)
  }

  @Test
  fun parsesJsxSnippet() {
    val snippet = """
      <PulsingBorder
        width={1280}
        height={720}
        colors={["#0dc1fd", "#d915ef", "#ff3f2ecc"]}
        colorBack="#000000"
        roundness={1}
        thickness={0}
        softness={0.75}
        aspectRatio="square"
        intensity={0.2}
        bloom={0.45}
        spots={3}
        spotSize={0.4}
        pulse={0.5}
        smoke={1}
        smokeSize={0}
        speed={1}
        scale={0.6}
        marginLeft={0}
        marginRight={0}
        marginTop={0}
        marginBottom={0}
      />
    """.trimIndent()

    val spec = PulsingBorderSpec.fromJsxSnippet(snippet)
    assertEquals(1280, spec.width)
    assertEquals(720, spec.height)
    assertEquals(3, spec.params.colors.size)
    assertEquals(PulsingBorderParams.AspectRatio.SQUARE, spec.params.aspectRatio)
  }
}
