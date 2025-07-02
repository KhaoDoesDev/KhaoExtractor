package dev.khaodoes.khaoextractor.extractors;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import dev.khaodoes.khaoextractor.KhaoExtractor.IKhaoExtractor;
import net.minecraft.registry.Registries;
import net.minecraft.server.MinecraftServer;
import net.minecraft.sound.SoundEvent;

public class Sounds implements IKhaoExtractor {

    @Override
    public String fileName() {
        return "sounds.json";
    }

    @Override
    public JsonElement extract(MinecraftServer server) throws Exception {
        JsonArray soundsArray = new JsonArray();


        for (SoundEvent soundEvent : Registries.SOUND_EVENT) {
            JsonObject soundJson = new JsonObject();

            soundJson.addProperty("id", Registries.SOUND_EVENT.getRawId(soundEvent));
            soundJson.addProperty("name", soundEvent.id().getPath());

            soundsArray.add(soundJson);
        };

        return soundsArray;
    }
    
}
