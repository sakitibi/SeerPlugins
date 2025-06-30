package com.example.seerplugins

import net.fabricmc.api.ClientModInitializer

object FlashMod : ClientModInitializer {
    override fun onInitializeClient() {
        FlashPacketReceiver.registerClientReceiver()
    }
}
