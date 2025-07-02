package dev.khaodoes.khaoextractor.extractors;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import dev.khaodoes.khaoextractor.KhaoExtractor.IKhaoExtractor;
import net.minecraft.particle.ParticleType;
import net.minecraft.registry.Registries;
import net.minecraft.server.MinecraftServer;

public class Particles implements IKhaoExtractor {

    @Override
    public String fileName() {
        return "particles.json";
    }

    @Override
    public JsonElement extract(MinecraftServer server) throws Exception {
        JsonArray particlesArray = new JsonArray();

        for (ParticleType<?> particleType : Registries.PARTICLE_TYPE) {
            JsonObject particleJson = new JsonObject();
            particleJson.addProperty("id", Registries.PARTICLE_TYPE.getRawId(particleType));
            particleJson.addProperty("name", Registries.PARTICLE_TYPE.getId(particleType).getPath());
            particleJson.addProperty("should_always_spawn", particleType.shouldAlwaysSpawn());

            particlesArray.add(particleJson);
        }

        return particlesArray;
    }
}