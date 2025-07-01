package dev.khaodoes.khaoextractor.extractors;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.mojang.serialization.JsonOps;

import dev.khaodoes.khaoextractor.KhaoExtractor.IKhaoExtractor;
import net.minecraft.recipe.Recipe;
import net.minecraft.recipe.RecipeEntry;
import net.minecraft.registry.RegistryOps;
import net.minecraft.server.MinecraftServer;

public class Recipes implements IKhaoExtractor {

    @Override
    public String fileName() {
        return "recipes.json";
    }

    @Override
    public JsonElement extract(MinecraftServer server) throws Exception {
        JsonArray recipesJson = new JsonArray();
        RegistryOps<JsonElement> registryOps = RegistryOps.of(JsonOps.INSTANCE, server.getRegistryManager());

        for (RecipeEntry<?> rawRecipe : server.getRecipeManager().values()) {
            Recipe<?> recipe = rawRecipe.value();

            JsonObject json = Recipe.CODEC
                .encodeStart(registryOps, recipe)
                .getOrThrow()
                .getAsJsonObject();

            // todo: add numeric ids to the namespace ids.

            recipesJson.add(json);
        }

        return recipesJson;
    }
}