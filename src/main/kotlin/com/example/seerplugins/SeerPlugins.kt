package com.example.seerplugins

import net.fabricmc.api.ModInitializer

object SeerPlugins : ModInitializer {
    override fun onInitialize() {
        TickHandler.register()
    }
}
