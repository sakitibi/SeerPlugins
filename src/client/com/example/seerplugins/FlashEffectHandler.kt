import net.fabricmc.fabric.api.client.rendering.v1.HudRenderCallback
import net.minecraft.client.MinecraftClient
import net.minecraft.client.util.math.MatrixStack
import com.mojang.blaze3d.systems.RenderSystem
import net.minecraft.client.render.GameRenderer
import net.minecraft.client.render.VertexFormat
import net.minecraft.client.render.VertexFormats
import net.minecraft.client.render.Tessellator
import net.minecraft.client.render.BufferBuilder

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

        RenderSystem.enableBlend()
        RenderSystem.disableDepthTest()
        RenderSystem.setShader(GameRenderer.POSITION_TEXTURE_SHADER)
        buffer.begin(VertexFormat.DrawMode.QUADS, VertexFormats.POSITION_COLOR)
        RenderSystem.defaultBlendFunc()

        val tessellator = Tessellator.getInstance()
        val buffer = tessellator.buffer

        buffer.begin(VertexFormat.DrawMode.QUADS, VertexFormats.POSITION_COLOR)

        buffer.vertex(0.0, height.toDouble(), 0.0).color(r, g, b, alpha).next()
        buffer.vertex(width.toDouble(), height.toDouble(), 0.0).color(r, g, b, alpha).next()
        buffer.vertex(width.toDouble(), 0.0, 0.0).color(r, g, b, alpha).next()
        buffer.vertex(0.0, 0.0, 0.0).color(r, g, b, alpha).next()

        tessellator.draw()

        RenderSystem.enableDepthTest()
        RenderSystem.disableBlend()
    }
}
