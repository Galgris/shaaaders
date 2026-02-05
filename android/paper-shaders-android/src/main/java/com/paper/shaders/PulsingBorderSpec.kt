package com.paper.shaders

import com.paper.shaders.PulsingBorderParams.AspectRatio
import com.paper.shaders.SizingParams.Fit
import org.json.JSONArray
import org.json.JSONObject

class SpecParseException(val token: String, message: String) : IllegalArgumentException(message)

data class PulsingBorderSpec(
  val width: Int? = null,
  val height: Int? = null,
  val params: PulsingBorderParams = PulsingBorderParams()
) {
  companion object {
    fun fromJson(json: String): PulsingBorderSpec {
      val obj = JSONObject(json)
      val map = mutableMapOf<String, Any?>()
      val keys = obj.keys()
      while (keys.hasNext()) {
        val key = keys.next()
        map[key] = obj.get(key)
      }
      return fromMap(map)
    }

    fun fromJsxSnippet(snippet: String): PulsingBorderSpec {
      val start = snippet.indexOf("<PulsingBorder")
      if (start < 0) {
        throw SpecParseException("<PulsingBorder", "Missing <PulsingBorder> tag")
      }
      val afterStart = start + "<PulsingBorder".length
      val end = snippet.indexOf("/>", afterStart).takeIf { it >= 0 }
        ?: snippet.indexOf(">", afterStart).takeIf { it >= 0 }
        ?: throw SpecParseException("/>", "Missing closing /> or > for <PulsingBorder>")

      val attrs = snippet.substring(afterStart, end)
      val map = parseAttributes(attrs)
      return fromMap(map)
    }

    private fun parseAttributes(attrs: String): Map<String, Any?> {
      val map = mutableMapOf<String, Any?>()
      var i = 0
      while (i < attrs.length) {
        while (i < attrs.length && attrs[i].isWhitespace()) i++
        if (i >= attrs.length) break

        val keyStart = i
        while (i < attrs.length && (attrs[i].isLetterOrDigit() || attrs[i] == '_' || attrs[i] == '-')) i++
        if (keyStart == i) {
          val token = attrs.substring(i).take(16)
          throw SpecParseException(token, "Expected prop name")
        }
        val key = attrs.substring(keyStart, i)

        while (i < attrs.length && attrs[i].isWhitespace()) i++
        if (i >= attrs.length || attrs[i] != '=') {
          val token = attrs.substring(i).take(16)
          throw SpecParseException(token, "Expected '=' after prop name '$key'")
        }
        i++
        while (i < attrs.length && attrs[i].isWhitespace()) i++
        if (i >= attrs.length) {
          throw SpecParseException(key, "Missing value for prop '$key'")
        }

        val value: Any? = when (attrs[i]) {
          '"' -> {
            val endQuote = attrs.indexOf('"', i + 1)
            if (endQuote < 0) {
              throw SpecParseException(attrs.substring(i).take(16), "Unterminated string for '$key'")
            }
            val raw = attrs.substring(i + 1, endQuote)
            i = endQuote + 1
            raw
          }
          '{' -> {
            val endBrace = attrs.indexOf('}', i + 1)
            if (endBrace < 0) {
              throw SpecParseException(attrs.substring(i).take(16), "Unterminated brace for '$key'")
            }
            val raw = attrs.substring(i + 1, endBrace).trim()
            i = endBrace + 1
            parseBraceValue(raw, key)
          }
          else -> {
            val token = attrs.substring(i).take(16)
            throw SpecParseException(token, "Unexpected value for '$key'")
          }
        }
        map[key] = value
      }
      return map
    }

    private fun parseBraceValue(raw: String, key: String): Any? {
      if (raw.isBlank()) return null
      if (raw.startsWith("[")) {
        return try {
          val array = JSONArray(raw)
          List(array.length()) { idx -> array.get(idx) }
        } catch (ex: Exception) {
          throw SpecParseException(raw.take(16), "Invalid array value for '$key'")
        }
      }
      if (raw.startsWith("\"") && raw.endsWith("\"")) {
        return raw.removePrefix("\"").removeSuffix("\"")
      }
      if (raw.equals("true", ignoreCase = true)) return true
      if (raw.equals("false", ignoreCase = true)) return false
      raw.toFloatOrNull()?.let { return it }
      throw SpecParseException(raw.take(16), "Unsupported value for '$key'")
    }

    private fun fromMap(map: Map<String, Any?>): PulsingBorderSpec {
      val defaults = PulsingBorderParams()

      val width = map["width"].asInt()
      val height = map["height"].asInt()

      val colors = map["colors"].asStringList() ?: defaults.colors
      val colorBack = map["colorBack"].asString() ?: defaults.colorBack
      val roundness = map["roundness"].asFloat() ?: defaults.roundness
      val thickness = map["thickness"].asFloat() ?: defaults.thickness
      val margin = map["margin"].asFloat() ?: defaults.margin
      val marginLeft = map["marginLeft"].asFloat() ?: defaults.marginLeft
      val marginRight = map["marginRight"].asFloat() ?: defaults.marginRight
      val marginTop = map["marginTop"].asFloat() ?: defaults.marginTop
      val marginBottom = map["marginBottom"].asFloat() ?: defaults.marginBottom
      val aspectRatio = map["aspectRatio"].asString()?.let { parseAspectRatio(it) } ?: defaults.aspectRatio
      val softness = map["softness"].asFloat() ?: defaults.softness
      val intensity = map["intensity"].asFloat() ?: defaults.intensity
      val bloom = map["bloom"].asFloat() ?: defaults.bloom
      val spots = map["spots"].asInt() ?: defaults.spots
      val spotSize = map["spotSize"].asFloat() ?: defaults.spotSize
      val pulse = map["pulse"].asFloat() ?: defaults.pulse
      val smoke = map["smoke"].asFloat() ?: defaults.smoke
      val smokeSize = map["smokeSize"].asFloat() ?: defaults.smokeSize
      val speed = map["speed"].asFloat() ?: defaults.speed
      val frame = map["frame"].asFloat() ?: defaults.frame
      val fit = map["fit"].asString()?.let { parseFit(it) } ?: defaults.fit
      val scale = map["scale"].asFloat() ?: defaults.scale
      val rotation = map["rotation"].asFloat() ?: defaults.rotation
      val originX = map["originX"].asFloat() ?: defaults.originX
      val originY = map["originY"].asFloat() ?: defaults.originY
      val offsetX = map["offsetX"].asFloat() ?: defaults.offsetX
      val offsetY = map["offsetY"].asFloat() ?: defaults.offsetY
      val worldWidth = map["worldWidth"].asFloat() ?: defaults.worldWidth
      val worldHeight = map["worldHeight"].asFloat() ?: defaults.worldHeight

      val params = PulsingBorderParams(
        colors = colors,
        colorBack = colorBack,
        roundness = roundness,
        thickness = thickness,
        margin = margin,
        marginLeft = marginLeft,
        marginRight = marginRight,
        marginTop = marginTop,
        marginBottom = marginBottom,
        aspectRatio = aspectRatio,
        softness = softness,
        intensity = intensity,
        bloom = bloom,
        spots = spots,
        spotSize = spotSize,
        pulse = pulse,
        smoke = smoke,
        smokeSize = smokeSize,
        speed = speed,
        frame = frame,
        fit = fit,
        scale = scale,
        rotation = rotation,
        originX = originX,
        originY = originY,
        offsetX = offsetX,
        offsetY = offsetY,
        worldWidth = worldWidth,
        worldHeight = worldHeight
      )

      return PulsingBorderSpec(width = width, height = height, params = params)
    }

    private fun parseAspectRatio(value: String): AspectRatio {
      return when (value.lowercase()) {
        "square" -> AspectRatio.SQUARE
        else -> AspectRatio.AUTO
      }
    }

    private fun parseFit(value: String): Fit {
      return when (value.lowercase()) {
        "none" -> Fit.NONE
        "cover" -> Fit.COVER
        else -> Fit.CONTAIN
      }
    }

    private fun Any?.asString(): String? = when (this) {
      is String -> this
      else -> null
    }

    private fun Any?.asFloat(): Float? = when (this) {
      is Number -> this.toFloat()
      is String -> this.toFloatOrNull()
      else -> null
    }

    private fun Any?.asInt(): Int? = when (this) {
      is Number -> this.toInt()
      is String -> this.toIntOrNull()
      else -> null
    }

    private fun Any?.asStringList(): List<String>? {
      return when (this) {
        is JSONArray -> List(this.length()) { idx -> this.getString(idx) }
        is List<*> -> this.mapNotNull { it?.toString() }
        else -> null
      }
    }
  }
}
