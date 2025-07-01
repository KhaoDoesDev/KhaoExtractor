package dev.khaodoes.khaoextractor.extractors;

import java.util.Optional;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.mojang.serialization.JsonOps;

import dev.khaodoes.khaoextractor.KhaoExtractor.IKhaoExtractor;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.registry.Registry;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.registry.RegistryOps;
import net.minecraft.server.MinecraftServer;

public class Enchantments implements IKhaoExtractor {

    @Override
    public String fileName() {
        return "enchantments.json";
    }

    @Override
    public JsonElement extract(MinecraftServer server) throws Exception {
        JsonArray enchantmentsArray = new JsonArray();

        var registryManager = server.getRegistryManager();
        RegistryOps<JsonElement> registryOps = RegistryOps.of(JsonOps.INSTANCE, registryManager);

        Optional<Registry<Enchantment>> enchantmentsRegistry = registryManager.getOptional(RegistryKeys.ENCHANTMENT);

        if (enchantmentsRegistry.isEmpty()) throw new Exception("We apparently don't have an enchantment registry on the server.");

        for (Enchantment enchantment : enchantmentsRegistry.get()) {
            JsonObject json = Enchantment.CODEC
                .encodeStart(registryOps, enchantment)
                .getOrThrow()
                .getAsJsonObject();
            // todo: add more details and maybe numeric ids
            enchantmentsArray.add(json);
        };

        return enchantmentsArray;
    }
    
}
