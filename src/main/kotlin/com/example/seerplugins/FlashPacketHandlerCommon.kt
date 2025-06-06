package com.example.seerplugins

import io.netty.buffer.Unpooled
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking
import net.minecraft.network.PacketByteBuf
import net.minecraft.server.network.ServerPlayerEntity
import net.minecraft.util.Identifier

object FlashPacketSender {
    val ID = Identifier("seerplugins", "flash")

    fun sendTo(player: ServerPlayerEntity, r: Int, g: Int, b: Int) {
        val buf = PacketByteBuf(Unpooled.buffer())
        buf.writeInt(r)
        buf.writeInt(g)
        buf.writeInt(b)
        ServerPlayNetworking.send(player, ID, buf)
    }
}
