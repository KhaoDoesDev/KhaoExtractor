package dev.khaodoes.khaoextractor.extractors;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import dev.khaodoes.khaoextractor.KhaoExtractor.IKhaoExtractor;
import net.minecraft.registry.Registries;
import net.minecraft.server.MinecraftServer;
import net.minecraft.world.event.GameEvent;

public class GameEvents implements IKhaoExtractor {

    @Override
    public String fileName() {
        return "game_events.json";
    }

    @Override
    public JsonElement extract(MinecraftServer server) throws Exception {
        JsonArray gameEventsArray = new JsonArray();

        for (GameEvent gameEvent : Registries.GAME_EVENT) {
            JsonObject gameEventJson = new JsonObject();

            gameEventJson.addProperty("id", Registries.GAME_EVENT.getRawId(gameEvent));
            gameEventJson.addProperty("name", Registries.GAME_EVENT.getId(gameEvent).getPath());

            gameEventsArray.add(gameEventJson);
        };

        return gameEventsArray;
    }
    
}
