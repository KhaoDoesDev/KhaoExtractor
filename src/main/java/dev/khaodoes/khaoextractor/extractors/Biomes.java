package dev.khaodoes.khaoextractor.extractors;

import java.util.Optional;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.mojang.serialization.JsonOps;

import dev.khaodoes.khaoextractor.KhaoExtractor.IKhaoExtractor;
import net.minecraft.registry.Registry;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.registry.RegistryOps;
import net.minecraft.server.MinecraftServer;
import net.minecraft.world.biome.Biome;

public class Biomes implements IKhaoExtractor {

    @Override
    public String fileName() {
        return "biomes.json";
    }

    @Override
    public JsonElement extract(MinecraftServer server) throws Exception {
        JsonArray biomesArray = new JsonArray();
     
        var registryManager = server.getRegistryManager();
        RegistryOps<JsonElement> registryOps = RegistryOps.of(JsonOps.INSTANCE, registryManager);

        Optional<Registry<Biome>> biomesRegistry = registryManager.getOptional(RegistryKeys.BIOME);

        if (biomesRegistry.isEmpty()) throw new Exception("We apparently don't have an biome registry on the server.");

        for (Biome biome : biomesRegistry.get()) {
            JsonObject json = Biome.CODEC
                .encodeStart(registryOps, biome)
                .getOrThrow()
                .getAsJsonObject();

            biomesArray.add(json);
        }

        return biomesArray;
    }
}