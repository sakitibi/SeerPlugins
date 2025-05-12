package com.example.seerplugins

import io.netty.buffer.Unpooled
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking
import net.minecraft.network.PacketByteBuf
import net.minecraft.server.network.ServerPlayerEntity
import net.minecraft.util.Identifier

object FlashPacketHandler {
    private val ID = Identifier("seerplugins", "flash") // パケットID

    // サーバー → クライアント にパケット送信
    fun sendTo(player: ServerPlayerEntity) {
        val buf = PacketByteBuf(Unpooled.buffer())
        // 今回は中身のデータが不要なので空パケットでOK
        ServerPlayNetworking.send(player, ID, buf)
    }

    // クライアント側でパケットを受信してフラッシュ処理を実行
    fun registerClientReceiver() {
        ClientPlayNetworking.registerGlobalReceiver(ID) { client, _, _, _ ->
            client.execute {
                FlashEffectHandler.startFlash()
            }
        }
    }
}