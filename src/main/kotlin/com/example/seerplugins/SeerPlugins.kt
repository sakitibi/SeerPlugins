package com.example.seerplugins

import net.fabricmc.api.ModInitializer

class SeerPlugins : ModInitializer {
    override fun onInitialize() {
        TickHandler.register()
    }
}
