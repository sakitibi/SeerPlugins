package com.example.seerplugins

import com.example.seerplugins.FlashPacketHandler // 修正したインポート
import net.fabricmc.api.ClientModInitializer
import net.fabricmc.api.ModInitializer

object SeerPlugins : ModInitializer, ClientModInitializer {

    override fun onInitialize() {
        // サーバー側初期化処理
        TickHandler.register()
    }

    override fun onInitializeClient() {
        // クライアント側初期化処理
        FlashPacketHandler.registerClientReceiver()  // 修正された名前
        FlashEffectHandler.registerHudRender()
    }
}