package dev.khaodoes.khaoextractor.extractors;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import dev.khaodoes.khaoextractor.KhaoExtractor.IKhaoExtractor;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.registry.Registries;
import net.minecraft.server.MinecraftServer;

public class Items implements IKhaoExtractor {

    @Override
    public String fileName() {
        return "items.json";
    }

    @Override
    public JsonElement extract(MinecraftServer server) throws Exception {
        JsonArray itemsArray = new JsonArray();

        for (Item item : Registries.ITEM) {
            JsonObject itemJson = new JsonObject();
            ItemStack defaultStack = item.getDefaultStack();
            
            itemJson.addProperty("id", Registries.ITEM.getId(item).toString());
            itemJson.addProperty("max_stack_size", item.getMaxCount());
            itemJson.addProperty("translation_key", item.getTranslationKey());
            itemJson.addProperty("num_id", Registries.ITEM.getRawId(item));
            itemJson.addProperty("display_name", item.getName().getString());
            itemJson.addProperty("enchantable", defaultStack.isEnchantable());
            itemJson.addProperty("damageable", defaultStack.isDamageable());
            itemJson.addProperty("max_damage", defaultStack.getMaxDamage());
            itemJson.addProperty("rarity", defaultStack.getRarity().toString());

            itemsArray.add(itemJson);
        }

        return itemsArray;
    }
    
}