package com.example.seerplugins

import net.fabricmc.fabric.api.client.rendering.v1.HudRenderCallback
import net.minecraft.client.MinecraftClient
import net.minecraft.client.util.math.MatrixStack
import com.mojang.blaze3d.systems.RenderSystem
import net.minecraft.client.render.*
import net.minecraft.client.render.GameRenderer

object FlashEffectHandler {
    private var flashTicks = 0
    private const val MAX_FLASH_TICKS = 10
    private var flashColor = Triple(255, 255, 255)

    fun startFlash(r: Int, g: Int, b: Int) {
        flashTicks = MAX_FLASH_TICKS
        flashColor = Triple(r, g, b)
    }

    fun registerHudRender() {
        HudRenderCallback.EVENT.register(HudRenderCallback { matrices: MatrixStack, _ ->
            if (flashTicks > 0) {
                drawWhiteOverlay(matrices)
                flashTicks--
            }
        })
    }

    private fun drawWhiteOverlay(matrices: MatrixStack) {
        val client = MinecraftClient.getInstance()
        val window = client.window
        val width = window.scaledWidth
        val height = window.scaledHeight
        val (r, g, b) = flashColor
        val alpha = (255 * (flashTicks.toFloat() / MAX_FLASH_TICKS)).toInt()

        // レンダリング設定
        RenderSystem.enableBlend()
        RenderSystem.defaultBlendFunc()
        RenderSystem.disableDepthTest()
        
        // 1.19.4 のシェーダー指定 (テクスチャなし、位置と色のみ)
        RenderSystem.setShader(GameRenderer::getPositionColorProgram)

        val tessellator = Tessellator.getInstance()
        val buffer = tessellator.buffer

        // 描画開始 (POSITION_COLOR を使用)
        buffer.begin(VertexFormat.DrawMode.QUADS, VertexFormats.POSITION_COLOR)

        // 行列を適用して描画
        val matrix = matrices.peek().positionMatrix
        buffer.vertex(matrix, 0.0f, height.toFloat(), 0.0f).color(r, g, b, alpha).next()
        buffer.vertex(matrix, width.toFloat(), height.toFloat(), 0.0f).color(r, g, b, alpha).next()
        buffer.vertex(matrix, width.toFloat(), 0.0f, 0.0f).color(r, g, b, alpha).next()
        buffer.vertex(matrix, 0.0f, 0.0f, 0.0f).color(r, g, b, alpha).next()

        tessellator.draw()

        RenderSystem.enableDepthTest()
        RenderSystem.disableBlend()
    }
}