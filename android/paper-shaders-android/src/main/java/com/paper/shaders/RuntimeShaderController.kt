package com.paper.shaders

import android.graphics.RuntimeShader
import kotlin.math.max

internal class RuntimeShaderController(shaderCode: String) {
  val shader = RuntimeShader(shaderCode)

  private var lastFrameNanos: Long? = null
  private var timeSeconds: Float = 0f
  private var speed: Float = 0f

  fun setMotion(speed: Float, frame: Float) {
    this.speed = speed
    resetTime(frame)
  }

  fun resetTime(frame: Float) {
    timeSeconds = frame
    lastFrameNanos = null
    shader.setFloatUniform("u_time", timeSeconds)
  }

  fun updateTime(frameNanos: Long) {
    val last = lastFrameNanos
    if (last == null) {
      lastFrameNanos = frameNanos
      shader.setFloatUniform("u_time", timeSeconds)
      return
    }
    val deltaSeconds = (frameNanos - last) / 1_000_000_000f
    lastFrameNanos = frameNanos
    timeSeconds += deltaSeconds * speed
    shader.setFloatUniform("u_time", timeSeconds)
  }

  fun setResolution(widthPx: Float, heightPx: Float, pixelRatio: Float) {
    shader.setFloatUniform("u_resolution", max(1f, widthPx), max(1f, heightPx))
    shader.setFloatUniform("u_pixelRatio", max(1f, pixelRatio))
  }
}
