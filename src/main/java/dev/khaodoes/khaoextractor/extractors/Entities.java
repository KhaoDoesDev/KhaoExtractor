package dev.khaodoes.khaoextractor.extractors;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import dev.khaodoes.khaoextractor.KhaoExtractor.IKhaoExtractor;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.registry.Registries;
import net.minecraft.server.MinecraftServer;

public class Entities implements IKhaoExtractor {

    @Override
    public String fileName() {
        return "entities.json";
    }

    @Override
    public JsonArray extract(MinecraftServer server) throws Exception {
        JsonArray entitiesArray = new JsonArray();

        for (EntityType<?> entityType : Registries.ENTITY_TYPE) {
            JsonObject entityJson = new JsonObject();

            entityJson.addProperty("id", Registries.ENTITY_TYPE.getId(entityType).toString());
            entityJson.addProperty("name", Registries.ENTITY_TYPE.getId(entityType).getPath());
            entityJson.addProperty("display_name", entityType.getName().getString());
            entityJson.addProperty("height", entityType.getHeight());
            entityJson.addProperty("width", entityType.getWidth());
            entityJson.addProperty("type", entityType.getSpawnGroup().getName());
            entityJson.addProperty("translation_key", entityType.getTranslationKey());
            entityJson.addProperty("num_id", Registries.ENTITY_TYPE.getRawId(entityType));
            entityJson.addProperty("eye_height", entityType.getDimensions().eyeHeight());
            entityJson.addProperty("file_immune", entityType.isFireImmune());
            entityJson.addProperty("summonable", entityType.isSummonable());

            Entity entity = entityType.create(server.getOverworld(), null);
            if (entity != null) {
                if (entity instanceof LivingEntity) {
                    entityJson.addProperty("max_health", ((LivingEntity) entity).getMaxHealth());
                };

                entityJson.addProperty("attackable", entity.isAttackable());
            }

            entitiesArray.add(entityJson);
        }

        return entitiesArray;
    }
}
