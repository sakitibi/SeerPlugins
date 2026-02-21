package com.example.seerplugins

import net.fabricmc.api.ClientModInitializer

class FlashMod : ClientModInitializer {
    override fun onInitializeClient() {
        FlashPacketReceiver.registerClientReceiver()
    }
}
