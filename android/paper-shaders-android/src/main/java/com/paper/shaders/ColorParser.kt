package com.paper.shaders

import kotlin.math.abs
import kotlin.math.max
import kotlin.math.min

internal object ColorParser {
  fun parse(colorString: String?): FloatArray {
    if (colorString.isNullOrBlank()) return floatArrayOf(0f, 0f, 0f, 1f)
    return when {
      colorString.startsWith("#") -> hexToRgba(colorString)
      colorString.startsWith("rgb", ignoreCase = true) -> parseRgba(colorString)
      colorString.startsWith("hsl", ignoreCase = true) -> hslaToRgba(parseHsla(colorString))
      else -> floatArrayOf(0f, 0f, 0f, 1f)
    }
  }

  private fun hexToRgba(hex: String): FloatArray {
    var value = hex.removePrefix("#")
    if (value.length == 3) {
      value = value.map { "$it$it" }.joinToString("")
    }
    if (value.length == 6) {
      value += "ff"
    }
    if (value.length != 8) return floatArrayOf(0f, 0f, 0f, 1f)

    val r = value.substring(0, 2).toInt(16) / 255f
    val g = value.substring(2, 4).toInt(16) / 255f
    val b = value.substring(4, 6).toInt(16) / 255f
    val a = value.substring(6, 8).toInt(16) / 255f
    return floatArrayOf(clamp(r), clamp(g), clamp(b), clamp(a))
  }

  private fun parseRgba(input: String): FloatArray {
    val regex = Regex("""^rgba?\s*\(\s*(\d+)\s*,\s*(\d+)\s*,\s*(\d+)\s*(?:,\s*([0-9.]+))?\s*\)\s*$""", RegexOption.IGNORE_CASE)
    val match = regex.find(input) ?: return floatArrayOf(0f, 0f, 0f, 1f)
    val r = match.groupValues[1].toFloatOrNull()?.div(255f) ?: 0f
    val g = match.groupValues[2].toFloatOrNull()?.div(255f) ?: 0f
    val b = match.groupValues[3].toFloatOrNull()?.div(255f) ?: 0f
    val a = match.groupValues.getOrNull(4)?.takeIf { it.isNotBlank() }?.toFloatOrNull() ?: 1f
    return floatArrayOf(clamp(r), clamp(g), clamp(b), clamp(a))
  }

  private fun parseHsla(input: String): FloatArray {
    val regex = Regex("""^hsla?\s*\(\s*(\d+)\s*,\s*(\d+)%\s*,\s*(\d+)%\s*(?:,\s*([0-9.]+))?\s*\)\s*$""", RegexOption.IGNORE_CASE)
    val match = regex.find(input) ?: return floatArrayOf(0f, 0f, 0f, 1f)
    val h = match.groupValues[1].toFloatOrNull() ?: 0f
    val s = match.groupValues[2].toFloatOrNull() ?: 0f
    val l = match.groupValues[3].toFloatOrNull() ?: 0f
    val a = match.groupValues.getOrNull(4)?.takeIf { it.isNotBlank() }?.toFloatOrNull() ?: 1f
    return floatArrayOf(h, s, l, a)
  }

  private fun hslaToRgba(hsla: FloatArray): FloatArray {
    val h = (hsla[0] % 360f + 360f) % 360f
    val s = clamp(hsla[1] / 100f)
    val l = clamp(hsla[2] / 100f)
    val a = clamp(hsla[3])

    if (s == 0f) {
      return floatArrayOf(l, l, l, a)
    }

    val q = if (l < 0.5f) l * (1f + s) else l + s - l * s
    val p = 2f * l - q

    val r = hue2rgb(p, q, h / 360f + 1f / 3f)
    val g = hue2rgb(p, q, h / 360f)
    val b = hue2rgb(p, q, h / 360f - 1f / 3f)

    return floatArrayOf(clamp(r), clamp(g), clamp(b), a)
  }

  private fun hue2rgb(p: Float, q: Float, t0: Float): Float {
    var t = t0
    if (t < 0f) t += 1f
    if (t > 1f) t -= 1f
    return when {
      t < 1f / 6f -> p + (q - p) * 6f * t
      t < 1f / 2f -> q
      t < 2f / 3f -> p + (q - p) * (2f / 3f - t) * 6f
      else -> p
    }
  }

  private fun clamp(v: Float, minVal: Float = 0f, maxVal: Float = 1f): Float {
    return min(maxVal, max(minVal, v))
  }
}
