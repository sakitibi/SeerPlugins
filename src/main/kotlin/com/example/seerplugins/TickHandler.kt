package com.example.seerplugins

import net.minecraft.scoreboard.Team
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerTickEvents
import net.minecraft.server.MinecraftServer
import net.minecraft.server.network.ServerPlayerEntity

object TickHandler {

    fun register() {
        ServerTickEvents.END_SERVER_TICK.register(ServerTickEvents.EndTick { server ->
            onServerTick(server)
        })
    }

    private fun onServerTick(server: MinecraftServer) {
        val scoreboard = server.scoreboard
        val meetingObjective = scoreboard.getNullableObjective("meeting") ?: return
        val flashTeamNames = setOf("Seer", "Evilseer", "Madseer") // 必要に応じて追加

        for (player in server.playerManager.playerList) {
            val meetingScore = scoreboard.getPlayerScore(player.entityName, meetingObjective).score

            // 死亡直後かつ meeting が 0 のプレイヤーを検出
            if (player.isDead && player.deathTime == 1 && meetingScore == 0) {
                // チーム名が flash_team のプレイヤーにフラッシュパケット送信
                server.playerManager.playerList
                    .filter { it.scoreboardTeam?.name in flashTeamNames }
                    .forEach { FlashPacketHandler.sendTo(it) }
            }
        }
    }
}