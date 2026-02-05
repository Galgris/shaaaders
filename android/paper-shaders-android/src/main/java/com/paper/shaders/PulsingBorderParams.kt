package com.paper.shaders

import com.paper.shaders.SizingParams.Fit

@Suppress("LongParameterList")
data class PulsingBorderParams(
  val colors: List<String> = DEFAULT_COLORS,
  val colorBack: String = "#000000",
  val roundness: Float = 0.25f,
  val thickness: Float = 0.1f,
  val margin: Float? = null,
  val marginLeft: Float = 0f,
  val marginRight: Float = 0f,
  val marginTop: Float = 0f,
  val marginBottom: Float = 0f,
  val aspectRatio: AspectRatio = AspectRatio.AUTO,
  val softness: Float = 0.75f,
  val intensity: Float = 0.2f,
  val bloom: Float = 0.25f,
  val spots: Int = 5,
  val spotSize: Float = 0.5f,
  val pulse: Float = 0.25f,
  val smoke: Float = 0.3f,
  val smokeSize: Float = 0.6f,
  val speed: Float = 1f,
  val frame: Float = 0f,
  val fit: Fit = Fit.CONTAIN,
  val scale: Float = 0.6f,
  val rotation: Float = 0f,
  val originX: Float = 0.5f,
  val originY: Float = 0.5f,
  val offsetX: Float = 0f,
  val offsetY: Float = 0f,
  val worldWidth: Float = 0f,
  val worldHeight: Float = 0f
) {
  enum class AspectRatio(val value: Float) {
    AUTO(0f),
    SQUARE(1f)
  }

  data class ResolvedMargins(
    val left: Float,
    val right: Float,
    val top: Float,
    val bottom: Float
  )

  fun resolveMargins(): ResolvedMargins {
    val m = margin
    return ResolvedMargins(
      left = m ?: marginLeft,
      right = m ?: marginRight,
      top = m ?: marginTop,
      bottom = m ?: marginBottom
    )
  }

  companion object {
    val DEFAULT_COLORS = listOf("#0dc1fd", "#d915ef", "#ff3f2ecc")
  }
}
