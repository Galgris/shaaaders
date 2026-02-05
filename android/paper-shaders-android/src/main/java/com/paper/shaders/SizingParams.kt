package com.paper.shaders

data class SizingParams(
  val fit: Fit = Fit.CONTAIN,
  val scale: Float = 1f,
  val rotation: Float = 0f,
  val originX: Float = 0.5f,
  val originY: Float = 0.5f,
  val offsetX: Float = 0f,
  val offsetY: Float = 0f,
  val worldWidth: Float = 0f,
  val worldHeight: Float = 0f
) {
  enum class Fit(val value: Float) {
    NONE(0f),
    CONTAIN(1f),
    COVER(2f)
  }
}
