package dev.deepslate.fallacy.alloy

import net.minecraft.core.Holder
import net.minecraft.resources.ResourceLocation
import net.minecraft.world.level.material.Fluid

interface Alloy {
    //流体id : 数量
    val source: Map<ResourceLocation, Int>

    val result: Holder<Fluid>
}