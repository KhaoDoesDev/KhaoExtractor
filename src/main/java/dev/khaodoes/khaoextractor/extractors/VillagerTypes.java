package dev.khaodoes.khaoextractor.extractors;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import dev.khaodoes.khaoextractor.KhaoExtractor.IKhaoExtractor;
import net.minecraft.registry.Registries;
import net.minecraft.server.MinecraftServer;
import net.minecraft.village.VillagerType;

public class VillagerTypes implements IKhaoExtractor {

    @Override
    public String fileName() {
        return "villager_types.json";
    }

    @Override
    public JsonElement extract(MinecraftServer server) throws Exception {
        JsonArray villagerTypesArray = new JsonArray();


        for (VillagerType villagerType : Registries.VILLAGER_TYPE) {
            JsonObject villagerTypeJson = new JsonObject();

            villagerTypeJson.addProperty("id", Registries.VILLAGER_TYPE.getRawId(villagerType));
            villagerTypeJson.addProperty("name", Registries.VILLAGER_TYPE.getId(villagerType).getPath());

            villagerTypesArray.add(villagerTypeJson);
        };

        return villagerTypesArray;
    }
    
}
