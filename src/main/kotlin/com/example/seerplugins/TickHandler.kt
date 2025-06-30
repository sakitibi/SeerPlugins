// Copyright 2025 SKNewRoles
package com.example.seerplugins

import net.minecraft.scoreboard.Team
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerTickEvents
import net.minecraft.server.MinecraftServer
import net.minecraft.server.network.ServerPlayerEntity
import net.minecraft.entity.Entity
import net.minecraft.nbt.NbtCompound
import net.minecraft.nbt.NbtList

object TickHandler {
    fun register() {
        ServerTickEvents.END_SERVER_TICK.register { server ->
            onServerTick(server)
        }
    }


    fun getScoreboardTags(player: ServerPlayerEntity): Set<String> {
        val tags = mutableSetOf<String>()
        val nbt = NbtCompound()
        player.writeCustomDataToNbt(nbt)

        if (nbt.contains("Tags")) {
            val tagList = nbt.getList("Tags", 8) // 8はStringのタイプコード
            for (i in 0 until tagList.size) {
                tags.add(tagList.getString(i))
            }
        }
        return tags
    }

    private fun onServerTick(server: MinecraftServer) {
        val scoreboard = server.scoreboard
        val meetingObjective = scoreboard.getNullableObjective("meeting") ?: return
        val flashTeamNames = setOf("Seer", "Evilseer", "Madseer")

        for (playerEntity in server.playerManager.playerList) {
            val player = playerEntity as? ServerPlayerEntity ?: continue
            val teamName = player.scoreboardTeam?.name ?: continue

            when (teamName) {
                "Seer" -> {
                    // 水色固定フラッシュ
                    FlashPacketSender.sendTo(player, 0, 191, 255)
                }
                "Madseer", "Evilseer" -> {
                    // タグによって色分け
                    val tags = getScoreboardTags(player)

                    val flashColor = when {
                        "camp_red" in tags -> Triple(255, 0, 0)
                        "camp_green" in tags -> Triple(0, 255, 0)
                        "camp_pink" in tags -> Triple(0, 128, 255)
                        else -> null
                    }

                    flashColor?.let { (r, g, b) ->
                        FlashPacketSender.sendTo(player, r, g, b)
                    }
                }
            }
        }
    }
}
