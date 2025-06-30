package dev.deepslate.fallacy.trait

import dev.deepslate.fallacy.common.data.FallacyAttachments
import net.minecraft.core.Holder
import net.minecraft.world.entity.Entity

interface Trait {

    companion object {
        fun has(entity: Entity, trait: Trait): Boolean {
            if (!entity.hasData(FallacyAttachments.BEHAVIORS)) return false
            return entity.getData(FallacyAttachments.BEHAVIORS).contains(trait)
        }

        fun has(entity: Entity, trait: Holder<Trait>): Boolean = has(entity, trait.value())

        fun add(entity: Entity, trait: Trait) {
            val container = entity.getData(FallacyAttachments.BEHAVIORS)
            container.add(trait).let { new ->
                entity.setData(FallacyAttachments.BEHAVIORS, new)
            }
        }

        fun add(entity: Entity, trait: Holder<Trait>) {
            add(entity, trait.value())
        }

        private fun addAllRaw(entity: Entity, traits: Collection<Trait>) {
            val container = entity.getData(FallacyAttachments.BEHAVIORS)
            container.addAll(traits).let { new ->
                entity.setData(FallacyAttachments.BEHAVIORS, new)
            }
        }

        fun addAll(entity: Entity, behaviors: Collection<Holder<Trait>>) {
            addAllRaw(entity, behaviors.map { it.value() })
        }

        fun get(entity: Entity): BehaviorContainer {
            if (entity.hasData(FallacyAttachments.BEHAVIORS)) {
                return entity.getData(FallacyAttachments.BEHAVIORS)
            }
            return BehaviorContainer.empty()
        }

        fun remove(entity: Entity, trait: Trait) {
            if (!entity.hasData(FallacyAttachments.BEHAVIORS)) return
            val container = entity.getData(FallacyAttachments.BEHAVIORS)
            container.remove(trait).let { new ->
                entity.setData(FallacyAttachments.BEHAVIORS, new)
            }
        }

        fun remove(entity: Entity, trait: Holder<Trait>) {
            remove(entity, trait.value())
        }

        private fun removeAllRaw(entity: Entity, traits: Collection<Trait>) {
            if (!entity.hasData(FallacyAttachments.BEHAVIORS)) return
            val container = entity.getData(FallacyAttachments.BEHAVIORS)
            container.removeAll(traits).let { new ->
                entity.setData(FallacyAttachments.BEHAVIORS, new)
            }
        }

        fun removeAll(entity: Entity, behaviors: Collection<Holder<Trait>>) {
            removeAllRaw(entity, behaviors.map { it.value() })
        }
    }
}