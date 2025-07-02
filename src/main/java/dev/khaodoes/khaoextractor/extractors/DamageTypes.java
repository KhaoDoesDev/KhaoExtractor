package dev.khaodoes.khaoextractor.extractors;

import java.util.Optional;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.mojang.serialization.JsonOps;

import dev.khaodoes.khaoextractor.KhaoExtractor.IKhaoExtractor;
import net.minecraft.entity.damage.DamageType;
import net.minecraft.registry.Registry;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.registry.RegistryOps;
import net.minecraft.server.MinecraftServer;

public class DamageTypes implements IKhaoExtractor {

    @Override
    public String fileName() {
        return "damage_types.json";
    }

    @Override
    public JsonElement extract(MinecraftServer server) throws Exception {
        JsonArray damageTypesArray = new JsonArray();

        var registryManager = server.getRegistryManager();
        RegistryOps<JsonElement> registryOps = RegistryOps.of(JsonOps.INSTANCE, registryManager);

        Optional<Registry<DamageType>> damageTypesRegistry = registryManager.getOptional(RegistryKeys.DAMAGE_TYPE);

        if (damageTypesRegistry.isEmpty())
            throw new Exception("We apparently don't have a damage type registry on the server.");

        for (DamageType damageType : damageTypesRegistry.get()) {
            JsonObject encodedJson = DamageType.CODEC
                    .encodeStart(registryOps, damageType)
                    .getOrThrow()
                    .getAsJsonObject();

            JsonObject json = new JsonObject();

            json.addProperty("id", damageTypesRegistry.get().getId(damageType).getPath());
            json.addProperty("num_id", damageTypesRegistry.get().getRawId(damageType));
            json.addProperty("death_message_type", damageType.deathMessageType().asString());
            json.addProperty("damage_effects", damageType.effects().asString());

            for (String key : encodedJson.keySet()) {
                json.add(key, encodedJson.get(key));
            }
        }
        ;

        return damageTypesArray;
    }

}
