package dev.khaodoes.khaoextractor.extractors;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import dev.khaodoes.khaoextractor.KhaoExtractor.IKhaoExtractor;
import net.minecraft.registry.Registries;
import net.minecraft.server.MinecraftServer;
import net.minecraft.village.VillagerProfession;

public class VillagerProfessions implements IKhaoExtractor {

    @Override
    public String fileName() {
        return "villager_professions.json";
    }

    @Override
    public JsonElement extract(MinecraftServer server) throws Exception {
        JsonArray villagerProfessionsArray = new JsonArray();

        for (VillagerProfession villagerProfession : Registries.VILLAGER_PROFESSION) {
            JsonObject villagerProfessionJson = new JsonObject();

            villagerProfessionJson.addProperty("id", Registries.VILLAGER_PROFESSION.getRawId(villagerProfession));
            villagerProfessionJson.addProperty("name", Registries.VILLAGER_PROFESSION.getId(villagerProfession).getPath());
            
            // todo: add more details

            villagerProfessionsArray.add(villagerProfessionJson);
        }

        return villagerProfessionsArray;
    }
}