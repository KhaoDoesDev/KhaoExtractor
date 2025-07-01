package dev.khaodoes.khaoextractor.extractors;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import dev.khaodoes.khaoextractor.KhaoExtractor.IKhaoExtractor;
import net.minecraft.SharedConstants;
import net.minecraft.network.NetworkPhase;
import net.minecraft.network.NetworkState;
import net.minecraft.network.packet.PacketType;
import net.minecraft.network.state.*;
import net.minecraft.server.MinecraftServer;
import net.minidev.json.JSONObject;

import java.util.Arrays;
import java.util.List;

public class Packets implements IKhaoExtractor {

    @Override
    public String fileName() {
        return "packets.json";
    }

    @Override
    public JsonElement extract(MinecraftServer server) throws Exception {
        JsonObject packetsJson = new JsonObject();

        List<NetworkState.Factory<?, ?>> clientbound = Arrays.asList(
                QueryStates.S2C_FACTORY,
                LoginStates.S2C_FACTORY,
                ConfigurationStates.S2C_FACTORY,
                PlayStateFactories.S2C
        );

        List<NetworkState.Factory<?, ?>> serverbound = Arrays.asList(
                HandshakeStates.C2S_FACTORY,
                QueryStates.C2S_FACTORY,
                LoginStates.C2S_FACTORY,
                ConfigurationStates.C2S_FACTORY,
                PlayStateFactories.C2S
        );

        packetsJson.addProperty("protocol_version", SharedConstants.getProtocolVersion());
        packetsJson.addProperty("version", SharedConstants.getGameVersion().getName());

        packetsJson.add("clientbound", extractSide(clientbound));
        packetsJson.add("serverbound", extractSide(serverbound));

        return packetsJson;
    }

    private JsonObject extractSide(List<NetworkState.Factory<?, ?>> factories) {
        JsonArray handshakingArray = new JsonArray();
        JsonArray statusArray = new JsonArray();
        JsonArray loginArray = new JsonArray();
        JsonArray configurationArray = new JsonArray();
        JsonArray playArray = new JsonArray();
        for (NetworkState.Factory<?, ?> factory : factories) {
            factory.forEachPacketType((reg, id) -> {
                switch (factory.phase()) {
                    case NetworkPhase.HANDSHAKING: handshakingArray.add(reg.id().getPath());
                    case NetworkPhase.STATUS: statusArray.add(reg.id().getPath());
                    case NetworkPhase.LOGIN: loginArray.add(reg.id().getPath());
                    case NetworkPhase.CONFIGURATION: configurationArray.add(reg.id().getPath());
                    case NetworkPhase.PLAY: playArray.add(reg.id().getPath());
                }
            });
        }

        JsonObject output = new JsonObject();
        output.add("handshaking", handshakingArray);
        output.add("status", statusArray);
        output.add("login", loginArray);
        output.add("configuration", configurationArray);
        output.add("play", playArray);
        return output;
    }
}
