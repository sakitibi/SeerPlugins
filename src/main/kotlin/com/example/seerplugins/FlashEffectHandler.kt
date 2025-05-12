package com.example.seerplugins

import net.fabricmc.fabric.api.client.rendering.v1.HudRenderCallback
import net.minecraft.client.MinecraftClient
import net.minecraft.client.util.math.MatrixStack
import com.mojang.blaze3d.systems.RenderSystem
import net.minecraft.client.render.GameRenderer
import net.minecraft.client.render.VertexFormats
import net.minecraft.client.render.Tessellator
import net.minecraft.client.render.BufferBuilder

object FlashEffectHandler {
    private var flashTicks = 0
    private const val MAX_FLASH_TICKS = 10

    fun startFlash() {
        flashTicks = MAX_FLASH_TICKS
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

        RenderSystem.enableBlend()
        RenderSystem.disableDepthTest()

        // Manually set the shader color
        RenderSystem.setShaderColor(1.0f, 1.0f, 1.0f, 1.0f)

        RenderSystem.defaultBlendFunc()

        val tessellator = Tessellator.getInstance()
        val buffer: BufferBuilder = tessellator.buffer

        buffer.begin(
            net.minecraft.client.render.VertexFormat.DrawMode.QUADS,
            VertexFormats.POSITION_COLOR
        )

        val alpha = (255 * (flashTicks.toFloat() / MAX_FLASH_TICKS)).toInt()

        buffer.vertex(0.0, height.toDouble(), 0.0).color(255, 255, 255, alpha).next()
        buffer.vertex(width.toDouble(), height.toDouble(), 0.0).color(255, 255, 255, alpha).next()
        buffer.vertex(width.toDouble(), 0.0, 0.0).color(255, 255, 255, alpha).next()
        buffer.vertex(0.0, 0.0, 0.0).color(255, 255, 255, alpha).next()

        tessellator.draw()

        RenderSystem.enableDepthTest()
        RenderSystem.disableBlend()
    }
}