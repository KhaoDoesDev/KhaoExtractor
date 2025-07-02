package dev.khaodoes.khaoextractor;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;

import dev.khaodoes.khaoextractor.extractors.Biomes;
import dev.khaodoes.khaoextractor.extractors.Blocks;
import dev.khaodoes.khaoextractor.extractors.Enchantments;
import dev.khaodoes.khaoextractor.extractors.Entities;
import dev.khaodoes.khaoextractor.extractors.Items;
import dev.khaodoes.khaoextractor.extractors.Packets;
import dev.khaodoes.khaoextractor.extractors.Particles;
import dev.khaodoes.khaoextractor.extractors.Recipes;
import dev.khaodoes.khaoextractor.extractors.Sounds;
import dev.khaodoes.khaoextractor.extractors.VillagerProfessions;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerLifecycleEvents;
import net.minecraft.server.MinecraftServer;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.logging.Logger;

public class KhaoExtractor implements ModInitializer {
    private final String modId = "khaoextractor";
    private final Logger logger = Logger.getLogger(modId);

    public interface IKhaoExtractor {
        String fileName();

        JsonElement extract(MinecraftServer server) throws Exception;
    }

    @Override
    public void onInitialize() {
        logger.info("KhaoExtractor is initializing...");

        IKhaoExtractor[] extractors = {
                new Blocks(),
                new Entities(),
                new Items(),
                new Packets(),
                new Recipes(),
                new Enchantments(),
                new Particles(),
                new Biomes(),
                new VillagerProfessions(),
                new Sounds()
        };

        Path outputDir;
        try {
            outputDir = Files.createDirectories(Paths.get("output"));
        } catch (IOException ignored) {
            logger.info("Failed to create the output dir.");
            return;
        }

        Gson gson = new GsonBuilder().disableHtmlEscaping().setPrettyPrinting().create();

        ServerLifecycleEvents.SERVER_STARTED.register((server) -> {
            long startTime = System.currentTimeMillis();
            for (IKhaoExtractor extractor : extractors) {
                try {
                    Path out = outputDir.resolve(extractor.fileName());
                    FileWriter fileWriter = new FileWriter(out.toFile(), StandardCharsets.UTF_8);
                    gson.toJson(extractor.extract(server), fileWriter);
                    fileWriter.close();
                    logger.info("Wrote " + extractor.fileName());
                } catch (Exception e) {
                    logger.severe(e.getMessage());
                    logger.severe("Extractor \"" + extractor.fileName() + "\" failed while extracting.");
                }
            }
            logger.info("Extraction complete, took " + (System.currentTimeMillis() - startTime) + "ms to extract data. Shutting down...");
            server.stop(false);
        });
    }
}
