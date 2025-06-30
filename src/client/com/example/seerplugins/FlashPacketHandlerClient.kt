package com.example.seerplugins

import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking

object FlashPacketReceiver {
    fun registerClientReceiver() {
        ClientPlayNetworking.registerGlobalReceiver(FlashPacketSender.ID) { client, _, buf, _ ->
            val r = buf.readInt()
            val g = buf.readInt()
            val b = buf.readInt()
            client.execute {
                FlashEffectHandler.startFlash(r, g, b)
            }
        }
    }
}
