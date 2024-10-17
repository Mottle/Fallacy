package dev.deepslate.fallacy.mixin;

import dev.deepslate.fallacy.common.biome.data.BiomeSetting;
import dev.deepslate.fallacy.inject.FallacyBiomeExtension;
import net.minecraft.world.level.biome.Biome;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;

@Mixin(Biome.class)
public abstract class BiomeMixin implements FallacyBiomeExtension {
    @Unique
    private BiomeSetting fallacy$setting = new BiomeSetting.Builder().build();

    @Override
    public BiomeSetting fallacy$getSetting() {
        return fallacy$setting;
    }

    @Override
    public void fallacy$setSetting(BiomeSetting setting) {
        fallacy$setting = setting;
    }
}
