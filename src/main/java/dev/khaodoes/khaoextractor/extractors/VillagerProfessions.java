package dev.khaodoes.khaoextractor.extractors;

import java.util.HashSet;
import java.util.Set;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import dev.khaodoes.khaoextractor.KhaoExtractor.IKhaoExtractor;
import net.minecraft.item.Item;
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
            Set<Item> gatherableItems = new HashSet<>(villagerProfession.gatherableItems());
            JsonArray gatherableItemsArray = new JsonArray();

            for (Item item : gatherableItems) {
                JsonObject gatherableItemJson = new JsonObject();

                gatherableItemJson.addProperty("id", Registries.ITEM.getRawId(item));
                gatherableItemJson.addProperty("name", Registries.ITEM.getId(item).getPath());

                gatherableItemsArray.add(gatherableItemJson);
            }

            villagerProfessionJson.add("gatherable_items", gatherableItemsArray);

            villagerProfessionsArray.add(villagerProfessionJson);
        }

        return villagerProfessionsArray;
    }
}