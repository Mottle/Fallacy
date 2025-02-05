package dev.deepslate.fallacy.behavior

import dev.deepslate.fallacy.common.data.FallacyAttachments
import net.minecraft.core.Holder
import net.minecraft.world.entity.Entity

interface Behavior {

    companion object {
        fun has(entity: Entity, behavior: Behavior): Boolean {
            if (!entity.hasData(FallacyAttachments.BEHAVIORS)) return false
            return entity.getData(FallacyAttachments.BEHAVIORS).contains(behavior)
        }

        fun has(entity: Entity, behavior: Holder<Behavior>): Boolean = has(entity, behavior.value())

        fun add(entity: Entity, behavior: Behavior) {
            val container = entity.getData(FallacyAttachments.BEHAVIORS)
            container.add(behavior).let { new ->
                entity.setData(FallacyAttachments.BEHAVIORS, new)
            }
        }

        fun add(entity: Entity, behavior: Holder<Behavior>) {
            add(entity, behavior.value())
        }

        private fun addAllRaw(entity: Entity, behaviors: Collection<Behavior>) {
            val container = entity.getData(FallacyAttachments.BEHAVIORS)
            container.addAll(behaviors).let { new ->
                entity.setData(FallacyAttachments.BEHAVIORS, new)
            }
        }

        fun addAll(entity: Entity, behaviors: Collection<Holder<Behavior>>) {
            addAllRaw(entity, behaviors.map { it.value() })
        }

        fun get(entity: Entity): BehaviorContainer {
            if (entity.hasData(FallacyAttachments.BEHAVIORS)) {
                return entity.getData(FallacyAttachments.BEHAVIORS)
            }
            return BehaviorContainer.empty()
        }

        fun remove(entity: Entity, behavior: Behavior) {
            if (!entity.hasData(FallacyAttachments.BEHAVIORS)) return
            val container = entity.getData(FallacyAttachments.BEHAVIORS)
            container.remove(behavior).let { new ->
                entity.setData(FallacyAttachments.BEHAVIORS, new)
            }
        }

        fun remove(entity: Entity, behavior: Holder<Behavior>) {
            remove(entity, behavior.value())
        }

        private fun removeAllRaw(entity: Entity, behaviors: Collection<Behavior>) {
            if (!entity.hasData(FallacyAttachments.BEHAVIORS)) return
            val container = entity.getData(FallacyAttachments.BEHAVIORS)
            container.removeAll(behaviors).let { new ->
                entity.setData(FallacyAttachments.BEHAVIORS, new)
            }
        }

        fun removeAll(entity: Entity, behaviors: Collection<Holder<Behavior>>) {
            removeAllRaw(entity, behaviors.map { it.value() })
        }
    }
}