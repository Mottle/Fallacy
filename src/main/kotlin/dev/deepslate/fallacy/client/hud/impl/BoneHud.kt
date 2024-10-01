package dev.deepslate.fallacy.client.hud.impl

import dev.deepslate.fallacy.Fallacy
import dev.deepslate.fallacy.common.data.FallacyAttachments
import dev.deepslate.fallacy.common.data.FallacyAttributes
import dev.deepslate.fallacy.race.Race
import dev.deepslate.fallacy.race.impl.Skeleton
import dev.deepslate.fallacy.util.RGB
import net.minecraft.resources.ResourceLocation
import net.minecraft.world.entity.player.Player

//只在玩家是Skeleton时显示
class BoneHud : SimpleHud("bone") {

    override var isRightHandSide: Boolean = false

    override fun shouldRender(player: Player): Boolean = Race.get(player) is Skeleton

    override val icon: ResourceLocation = Fallacy.id("bone_icon")

    override val barColor: RGB = RGB.fromHex("0xa0a0a0")

    override fun getValue(player: Player): Float = player.getData(FallacyAttachments.BONE)

    override fun getMax(player: Player): Float = player.getAttributeValue(FallacyAttributes.MAX_BONE).toFloat()

    override fun getHudValue(player: Player): Float = getValue(player)

}