package dev.khaodoes.khaoextractor.extractors;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import dev.khaodoes.khaoextractor.KhaoExtractor.IKhaoExtractor;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.registry.Registries;
import net.minecraft.server.MinecraftServer;
import net.minecraft.state.property.Property;
import java.util.HashMap;
import java.util.Map;

public class Blocks implements IKhaoExtractor {

    @Override
    public String fileName() {
        return "blocks.json";
    }

    @Override
    public JsonArray extract(MinecraftServer server) throws Exception {
        JsonArray blocksArray = new JsonArray();

        for (Block block : Registries.BLOCK) {
            JsonObject blockJson = new JsonObject();

            blockJson.addProperty("id", Registries.BLOCK.getId(block).toString());
            blockJson.addProperty("name", Registries.BLOCK.getId(block).getPath());
            blockJson.addProperty("display_name", block.getName().getString());
            blockJson.addProperty("hardness", block.getHardness());
            blockJson.addProperty("resistance", block.getBlastResistance());
            blockJson.addProperty("stack_size", block.asItem().getMaxCount());
            blockJson.addProperty("transparent", block.getDefaultState().isTransparent());
            blockJson.addProperty("velocity_multiplier", block.getVelocityMultiplier());
            blockJson.addProperty("jump_velocity_multiplier", block.getJumpVelocityMultiplier());
            blockJson.addProperty("slipperiness", block.getSlipperiness());
            blockJson.addProperty("translation_key", block.getTranslationKey());
            blockJson.addProperty("item_id", Registries.ITEM.getId(block.asItem()).toString());
            blockJson.addProperty("num_id", Registries.BLOCK.getRawId(block));

            // todo: add the loot table

            blockJson.add("states", getStatesJsonArray(block));

            blocksArray.add(blockJson);
        }

        return blocksArray;
    }

    private JsonArray getStatesJsonArray(Block block) {
        JsonArray statesArray = new JsonArray();

        Map<String, JsonObject> propertyMap = new HashMap<>();

        for (BlockState state : block.getStateManager().getStates()) {
            if (state.getProperties().isEmpty()) {
                continue;
            }

            for (Property<?> property : state.getProperties()) {
                String propName = property.getName();

                if (propertyMap.containsKey(propName)) {
                    JsonObject existing = propertyMap.get(propName);
                    int currentAmount = existing.get("amount").getAsInt();
                    existing.addProperty("amount", currentAmount + 1);
                } else {
                    JsonObject propertyJson = new JsonObject();
                    propertyJson.addProperty("name", propName);
                    propertyJson.addProperty("type", property.getType().getSimpleName());
                    propertyJson.addProperty("amount", 1);
                    propertyMap.put(propName, propertyJson);
                }
            }
        }

        for (JsonObject obj : propertyMap.values()) {
            statesArray.add(obj);
        }

        return statesArray;
    }
}
