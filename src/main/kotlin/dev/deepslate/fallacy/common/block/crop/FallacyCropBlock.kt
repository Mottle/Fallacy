package dev.deepslate.fallacy.common.block.crop

import com.tterrag.registrate.providers.DataGenContext
import com.tterrag.registrate.providers.RegistrateBlockstateProvider
import com.tterrag.registrate.providers.loot.RegistrateBlockLootTables
import dev.deepslate.fallacy.Fallacy
import dev.deepslate.fallacy.common.block.FallacyBlocks
import dev.deepslate.fallacy.common.block.FallacyStateProperties
import dev.deepslate.fallacy.common.block.NPKFarmBlock
import dev.deepslate.fallacy.common.block.data.NPK
import dev.deepslate.fallacy.common.item.FallacyItems
import net.minecraft.advancements.critereon.StatePropertiesPredicate
import net.minecraft.core.BlockPos
import net.minecraft.core.Holder
import net.minecraft.core.registries.Registries
import net.minecraft.server.level.ServerLevel
import net.minecraft.util.RandomSource
import net.minecraft.world.item.Item
import net.minecraft.world.item.enchantment.Enchantments
import net.minecraft.world.level.BlockGetter
import net.minecraft.world.level.ItemLike
import net.minecraft.world.level.Level
import net.minecraft.world.level.block.Block
import net.minecraft.world.level.block.CropBlock
import net.minecraft.world.level.block.state.BlockState
import net.minecraft.world.level.block.state.StateDefinition
import net.minecraft.world.level.block.state.properties.IntegerProperty
import net.minecraft.world.level.storage.loot.LootPool
import net.minecraft.world.level.storage.loot.LootTable
import net.minecraft.world.level.storage.loot.entries.AlternativesEntry
import net.minecraft.world.level.storage.loot.entries.LootItem
import net.minecraft.world.level.storage.loot.functions.ApplyBonusCount
import net.minecraft.world.level.storage.loot.functions.ApplyExplosionDecay
import net.minecraft.world.level.storage.loot.functions.SetItemCountFunction
import net.minecraft.world.level.storage.loot.predicates.LootItemBlockStatePropertyCondition
import net.minecraft.world.level.storage.loot.providers.number.ConstantValue
import net.minecraft.world.level.storage.loot.providers.number.UniformGenerator
import net.neoforged.neoforge.client.model.generators.ConfiguredModel
import net.neoforged.neoforge.common.CommonHooks


open class FallacyCropBlock(
    properties: Properties,
    val npkRequired: NPK,
    val brightnessRange: IntRange = (9..Int.MAX_VALUE)
) :
    CropBlock(properties) {
    companion object {
//        protected val SHAPE_BY_AGE: List<VoxelShape> = listOf(
//            box(0.0, 0.0, 0.0, 16.0, 2.0, 16.0),
//            box(0.0, 0.0, 0.0, 16.0, 3.0, 16.0),
//            box(0.0, 0.0, 0.0, 16.0, 4.0, 16.0),
//            box(0.0, 0.0, 0.0, 16.0, 5.0, 16.0),
//            box(0.0, 0.0, 0.0, 16.0, 8.0, 16.0),
//            box(0.0, 0.0, 0.0, 16.0, 10.0, 16.0),
//            box(0.0, 0.0, 0.0, 16.0, 12.0, 16.0),
//            box(0.0, 0.0, 0.0, 16.0, 16.0, 16.0)
//        )

        val AGE: IntegerProperty = FallacyStateProperties.AGE

        val DYING_COUNTER = FallacyStateProperties.DYING_COUNTER

        fun withVanillaBlockStack(
            context: DataGenContext<Block, FallacyCropBlock>,
            provider: RegistrateBlockstateProvider
        ) {
            val name = context.name
            provider.getVariantBuilder(context.entry).forAllStates { state ->
                val age = state.getValue(AGE)
                val model =
                    provider.models().crop("block/crop/${name}_stage$age", provider.mcLoc("block/${name}_stage$age"))
                        .renderType("cutout")
                return@forAllStates ConfiguredModel.builder().modelFile(model).build()
            }
        }

        fun withVanillaBlockStateAlt(
            context: DataGenContext<Block, FallacyCropBlock>,
            provider: RegistrateBlockstateProvider
        ) {
            val name = context.name
            val models =
                (0..3).map {
                    provider.models().crop("block/crop/${name}_stage$it", provider.mcLoc("block/${name}_stage$it"))
                        .renderType("cutout")
                }
            provider.getVariantBuilder(context.entry).forAllStates { state ->
                val age = state.getValue(AGE)
                val stage = when (age) {
                    in 0..1 -> 0
                    in 2..3 -> 1
                    in 4..6 -> 2
                    else -> 3
                }
                val stateModel = models[stage]
                return@forAllStates ConfiguredModel.builder().modelFile(stateModel).build()
            }
        }

        fun withBlockState(context: DataGenContext<Block, FallacyCropBlock>, provider: RegistrateBlockstateProvider) {
            val name = context.name
            val models =
                (0..3).map {
                    provider.models().crop("block/crop/${name}_stage$it", Fallacy.id("block/crop/${name}_stage$it"))
                        .renderType("cutout")
                }

            provider.getVariantBuilder(context.entry).forAllStates { state ->
                val age = state.getValue(AGE)
                val stage = when (age) {
                    0 -> 0
                    in 1..3 -> 1
                    in 4..6 -> 2
                    else -> 3
                }
                val stateModel = models[stage]
                return@forAllStates ConfiguredModel.builder().modelFile(stateModel).build()
            }
        }

        //存在附魔时若附魔等级为N，则进行(N + extra)次二项分布的掉落尝试
        fun withLoot(gain: Holder<Item>, gainRange: IntRange, seedsRange: IntRange) =
            { provide: RegistrateBlockLootTables, block: FallacyCropBlock ->
                require(gainRange.first > 0)
                require(seedsRange.first > 0)
                val seedItem = block.asItem()
                val fortune = provide.registries.lookup(Registries.ENCHANTMENT).get().get(Enchantments.FORTUNE).get()
                val ageStateCondition = LootItemBlockStatePropertyCondition.hasBlockStateProperties(block)
                    .setProperties(
                        StatePropertiesPredicate.Builder.properties()
                            .hasProperty(AGE, block.maxAge)
                    )

                provide.add(
                    block,
                    LootTable.lootTable().apply(ApplyExplosionDecay.explosionDecay())
                        .withPool(
                            LootPool.lootPool().setRolls(ConstantValue.exactly(1f)).add(
                                AlternativesEntry.alternatives(
                                    LootItem.lootTableItem(gain.value()).`when`(
                                        ageStateCondition
                                    ).apply(
                                        SetItemCountFunction.setCount(
                                            UniformGenerator.between(
                                                gainRange.first.toFloat(),
                                                gainRange.last.toFloat()
                                            )
                                        )
                                    ),
                                    LootItem.lootTableItem(seedItem).apply(
                                        SetItemCountFunction.setCount(ConstantValue.exactly(seedsRange.first.toFloat()))
                                    )
                                )
                            )
                        ).withPool(
                            LootPool.lootPool().setRolls(ConstantValue.exactly(1f)).add(
                                LootItem.lootTableItem(seedItem) // seeds additional
                                    .apply(
                                        ApplyBonusCount.addBonusBinomialDistributionCount(
                                            fortune,
                                            0.57f,
                                            seedsRange.last - seedsRange.first
                                        )
                                    )
                            ).`when`(ageStateCondition)
                        )
                )
            }

        fun withNoSeedsLoot(gain: Holder<Item>, gainRange: IntRange) =
            { provide: RegistrateBlockLootTables, block: FallacyCropBlock ->
                require(gainRange.first > 0)
                val fortune = provide.registries.lookup(Registries.ENCHANTMENT).get().get(Enchantments.FORTUNE).get()
                val ageStateCondition = LootItemBlockStatePropertyCondition.hasBlockStateProperties(block)
                    .setProperties(
                        StatePropertiesPredicate.Builder.properties()
                            .hasProperty(AGE, block.maxAge)
                    )

                provide.add(
                    block,
                    LootTable.lootTable().apply(ApplyExplosionDecay.explosionDecay())
                        .withPool(
                            LootPool.lootPool().setRolls(ConstantValue.exactly(1f)).add(
                                AlternativesEntry.alternatives(
                                    LootItem.lootTableItem(gain.value()).`when`(ageStateCondition).apply(
                                        SetItemCountFunction.setCount(ConstantValue.exactly(gainRange.first.toFloat()))
                                    )
                                )
                            )
                        ).withPool(
                            LootPool.lootPool().setRolls(ConstantValue.exactly(1f)).add(
                                LootItem.lootTableItem(gain.value())
                                    .apply(
                                        ApplyBonusCount.addBonusBinomialDistributionCount(
                                            fortune,
                                            0.57f,
                                            gainRange.last - gainRange.first
                                        )
                                    )
                            ).`when`(ageStateCondition)
                        )
                )
            }
    }

    init {
        registerDefaultState(super.defaultBlockState().setValue(AGE, 0).setValue(DYING_COUNTER, 0))
    }

    override fun getAgeProperty(): IntegerProperty = AGE

    protected open val dead: Holder<Block>
        get() = FallacyBlocks.Crop.DYING_CROP

    override fun createBlockStateDefinition(builder: StateDefinition.Builder<Block?, BlockState?>) {
        super.createBlockStateDefinition(builder)
        builder.add(DYING_COUNTER)
    }

//    override fun getShape(state: BlockState, level: BlockGetter, pos: BlockPos, context: CollisionContext): VoxelShape =
//        SHAPE_BY_AGE[state.getValue(AGE)]

    override fun mayPlaceOn(
        state: BlockState,
        level: BlockGetter,
        pos: BlockPos
    ): Boolean {
        return state.block is NPKFarmBlock
    }

    /**
     * @see net.minecraft.world.level.block.CropBlock.randomTick
     */
    override fun randomTick(
        state: BlockState,
        level: ServerLevel,
        pos: BlockPos,
        random: RandomSource
    ) {
        // Forge: prevent loading unloaded chunks when checking neighbor's light
        if (!level.isAreaLoaded(pos, 1)) return
        if (shouldDie(state)) {
            setDying(level, pos)
            return
        }
        if (!level.canSeeSky(pos)) {
            increaseDyingCounter(state, level, pos)
            return
        }

        val farmland = level.getBlockState(pos.below())
        if (farmland.block !is NPKFarmBlock) {
            setDying(level, pos)
            return
        }

        val isFine = npkRequired.canGrowAt(farmland)

        if (!isFine && level.random.nextInt(0, 1) == 0) {
            increaseDyingCounter(state, level, pos)
            return
        }

        tryGrow(state, level, pos, random, isFine)
    }

    protected open fun tryGrow(
        state: BlockState,
        level: ServerLevel,
        pos: BlockPos,
        random: RandomSource,
        isFine: Boolean
    ) {
        if (level.getRawBrightness(pos, 0) in brightnessRange) {
            val age = this.getAge(state)
            if (age < this.maxAge) {
                val growthSpeed = getGrowthSpeed(state, level, pos)
                if (CommonHooks.canCropGrow(
                        level,
                        pos,
                        state,
                        random.nextInt((25.0F / growthSpeed).toInt() + 1) == 0
                    )
                ) {
                    val dyingCount = state.getValue(DYING_COUNTER)
                    val defaultGrowthState = getStateForAge(age + 1)
                    val newState = if (isFine) defaultGrowthState.setValue(
                        DYING_COUNTER,
                        (dyingCount - 1).coerceAtLeast(0)
                    ) else defaultGrowthState

                    level.setBlock(pos, newState, 2)
                    CommonHooks.fireCropGrowPost(level, pos, state)
                }
            }
        }
    }

    protected open fun setDying(level: Level, pos: BlockPos) {
        level.setBlock(pos, dead.value().defaultBlockState(), 2)
    }

    protected open fun increaseDyingCounter(state: BlockState, level: Level, pos: BlockPos) {
        val count = state.getValue(DYING_COUNTER)
        level.setBlock(pos, state.setValue(DYING_COUNTER, count + 1), 2)
    }

    protected open fun shouldDie(state: BlockState) = state.getValue(DYING_COUNTER) >= 3

    open val canGrowByBoneMeal: Boolean = false

    override fun getBaseSeedId(): ItemLike = FallacyItems.Crop.WHEAT_SEEDS
}