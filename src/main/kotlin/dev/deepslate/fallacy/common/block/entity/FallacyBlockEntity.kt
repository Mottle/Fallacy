package dev.deepslate.fallacy.common.block.entity

import net.minecraft.core.BlockPos
import net.minecraft.core.HolderLookup
import net.minecraft.nbt.CompoundTag
import net.minecraft.network.Connection
import net.minecraft.network.protocol.Packet
import net.minecraft.network.protocol.game.ClientGamePacketListener
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket
import net.minecraft.server.level.ServerLevel
import net.minecraft.world.level.ChunkPos
import net.minecraft.world.level.block.entity.BlockEntity
import net.minecraft.world.level.block.entity.BlockEntityType
import net.minecraft.world.level.block.state.BlockState

open class FallacyBlockEntity(type: BlockEntityType<*>, pos: BlockPos, state: BlockState) :
    BlockEntity(type, pos, state) {
    /**
     * @return The packet to send to the client upon block update.
     * This is returned in client in [onDataPacket]
     */
    override fun getUpdatePacket(): Packet<ClientGamePacketListener>? {
        return ClientboundBlockEntityDataPacket.create(this)
    }

    /**
     * Handle a packet sent from [getUpdatePacket]. Delegates to [handleUpdateTag].
     */
    override fun onDataPacket(
        net: Connection, packet: ClientboundBlockEntityDataPacket, lookupProvider: HolderLookup.Provider
    ) {
        val tag = packet.tag

        if (!tag.isEmpty) {
            handleUpdateTag(tag, lookupProvider)
            super.onDataPacket(net, packet, lookupProvider)
        }
    }

    /**
     * Returns the tag containing information needed to send to the client, either on block update or on bulk chunk update.
     * This tag is either returned with the packet in [getUpdatePacket] or [handleUpdateTag] based on where it was called from.
     * Delegates to [saveWithoutMetadata] which calls [saveAdditional]
     */
    override fun getUpdateTag(registries: HolderLookup.Provider): CompoundTag {
        return saveWithoutMetadata(registries)
    }

    /**
     * Handles an update tag sent from the server.
     */
    override fun handleUpdateTag(tag: CompoundTag, lookupProvider: HolderLookup.Provider) {
        loadAdditional(tag, lookupProvider)
        super.handleUpdateTag(tag, lookupProvider)
    }

    override fun loadAdditional(tag: CompoundTag, registries: HolderLookup.Provider) {
        requestModelDataUpdate()
        super.loadAdditional(tag, registries)
    }

    /**
     * Syncs the block entity data to client via means of a block update.
     * Use for stuff that is updated infrequently, for data that is analogous to changing the state.
     */
    open fun markForBlockUpdate() {
        val state = level?.getBlockState(blockPos) ?: return
        level!!.sendBlockUpdated(worldPosition, state, state, 3)
        setChanged()
    }

    /**
     * Marks a block entity as dirty, without updating the comparator output.
     * Use preferentially for updates that want to mark themselves as dirty every tick, and don't require updating comparator output.
     * Reimplements [net.minecraft.world.level.Level.blockEntityChanged] due to trying to avoid comparator updates, called due to MinecraftForge#9169
     */
    open fun markUnsaved() {
        if (level == null) return
        if (level!!.isAreaLoaded(worldPosition, 1)) level!!.getChunkAt(worldPosition).isUnsaved = true
    }

    /**
     * Marks a block entity for syncing without sending a block update. Also, internally marks dirty.
     */
    open fun markForSync() {
        sendVanillaUpdatePacket()
        setChanged()
    }

    open fun sendVanillaUpdatePacket() {
        val packet = updatePacket
        val pos = blockPos

        if (packet == null) return

        val serverLevel = level as? ServerLevel ?: return
        serverLevel.chunkSource.chunkMap.getPlayers(ChunkPos(pos), false).forEach { player ->
            player.connection.send(packet)
        }
    }
}