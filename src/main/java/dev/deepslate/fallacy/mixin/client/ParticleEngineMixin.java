package dev.deepslate.fallacy.mixin.client;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.Maps;
import dev.deepslate.fallacy.client.particle.FallacyParticleRenderTypes;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.Particle;
import net.minecraft.client.particle.ParticleEngine;
import net.minecraft.client.particle.ParticleRenderType;
import net.minecraft.client.renderer.texture.TextureManager;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.List;
import java.util.Map;
import java.util.Queue;

import static net.neoforged.neoforge.client.ClientHooks.makeParticleRenderTypeComparator;

@Mixin(ParticleEngine.class)
public class ParticleEngineMixin {

    @Unique
    private static final List<ParticleRenderType> FALLACY_RENDER_ORDER = ImmutableList.of(
            ParticleRenderType.TERRAIN_SHEET,
            ParticleRenderType.PARTICLE_SHEET_OPAQUE,
            ParticleRenderType.PARTICLE_SHEET_LIT,
            ParticleRenderType.PARTICLE_SHEET_TRANSLUCENT,
            FallacyParticleRenderTypes.INSTANCE.getSORTED_TRANSLUCENT(),
            FallacyParticleRenderTypes.INSTANCE.getSORTED_OPAQUE_BLOCK(),
            ParticleRenderType.CUSTOM //CUSTOM有大病
    );

    @Shadow
    private Map<ParticleRenderType, Queue<Particle>> particles;

    @Inject(method = "<init>", at = @At("TAIL"))
    void injectConstruct(ClientLevel level, TextureManager textureManager, CallbackInfo ci) {
        particles = Maps.newTreeMap(makeParticleRenderTypeComparator(FALLACY_RENDER_ORDER));
    }
}
