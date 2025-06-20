package dev.deepslate.fallacy.common.block.entity

import dev.deepslate.fallacy.common.FallacyFluids
import dev.deepslate.fallacy.util.TickHelper
import dev.deepslate.fallacy.util.getIntrinsicHeat
import net.minecraft.core.BlockPos
import net.minecraft.core.Direction
import net.minecraft.core.HolderLookup
import net.minecraft.nbt.CompoundTag
import net.minecraft.nbt.ListTag
import net.minecraft.nbt.Tag
import net.minecraft.world.item.ItemStack
import net.minecraft.world.item.Items
import net.minecraft.world.level.Level
import net.minecraft.world.level.block.entity.BlockEntityType
import net.minecraft.world.level.block.state.BlockState
import net.neoforged.neoforge.fluids.FluidStack
import kotlin.math.min

class CrucibleEntity(type: BlockEntityType<*>, pos: BlockPos, state: BlockState) :
    FallacyBlockEntity(type, pos, state) {
    companion object {
        val TICK_INTERVAL = TickHelper.second(1)

        const val INVENTORY_SIZE = 9

        const val INCREASE_HEAT = 50

        fun serverTick(level: Level, pos: BlockPos, state: BlockState, entity: CrucibleEntity) {
            if (!TickHelper.checkServerTickRate(TICK_INTERVAL)) return

            val aroundStates = Direction.entries.map(pos::relative).map { p -> p to level.getBlockState(p) }
            val maxHeatCtx = aroundStates.maxBy { (p, state) -> state.getIntrinsicHeat(level, p) }
            val (heatSourcePos, heatSourceState) = maxHeatCtx
            val targetHeat = heatSourceState.getIntrinsicHeat(level, heatSourcePos)

            val detHeat = if (entity.isInventoryEmpty()) INCREASE_HEAT else INVENTORY_SIZE / 4

            if (targetHeat > entity.heat) {
                entity.heat = min(targetHeat, entity.heat + detHeat)
            } else if (targetHeat < entity.heat) {
                entity.heat = min(targetHeat, entity.heat - detHeat)
            }

            entity.inventory.forEachIndexed { idx, material ->
                if (material.`is`(Items.COPPER_INGOT) && (entity.isTankEmpty() || entity.cachedFluid.`is`(FallacyFluids.MOLTEN_COPPER))) {
                    entity.inventory[idx] = ItemStack.EMPTY
                    if (entity.isTankEmpty()) {
                        entity.cachedFluid = FluidStack(FallacyFluids.MOLTEN_COPPER, 144)
                    } else {
                        entity.cachedFluid.amount += 144
                    }
                }
            }
        }
    }

    var heat: Int = 0
        private set

    val inventory: Array<ItemStack> = Array(INVENTORY_SIZE) { ItemStack.EMPTY }

    var cachedFluid: FluidStack = FluidStack.EMPTY

    fun isInventoryEmpty() = inventory.all(ItemStack::isEmpty)

    fun isTankEmpty() = cachedFluid.isEmpty

    override fun loadAdditional(tag: CompoundTag, registries: HolderLookup.Provider) {
        val list = tag.getList("inventory", Tag.TAG_COMPOUND.toInt()).toList()

        if (list.isNotEmpty()) {
            val stacks = list.map {
                ItemStack.parse(registries, it).orElse(ItemStack.EMPTY)
            }
            stacks.forEachIndexed { index, stack -> inventory[index] = stack }
        }

        if (tag.contains("fluid")) {
            val fluidNBT = tag.getCompound("fluid")
            cachedFluid = FluidStack.parse(registries, fluidNBT).orElse(FluidStack.EMPTY)
        }

        heat = tag.getInt("heat")

        super.loadAdditional(tag, registries)
    }

    override fun saveAdditional(tag: CompoundTag, registries: HolderLookup.Provider) {
        val inventoryTag = ListTag()

        //存inventory
        inventory.filter { stack -> !stack.isEmpty }.forEach { stack ->
            inventoryTag.add(stack.save(registries))
        }
        tag.put("inventory", inventoryTag)

        //存fluid
        if (!cachedFluid.isEmpty) tag.put("fluid", cachedFluid.save(registries))

        tag.putInt("heat", heat)

        super.saveAdditional(tag, registries)
    }
}