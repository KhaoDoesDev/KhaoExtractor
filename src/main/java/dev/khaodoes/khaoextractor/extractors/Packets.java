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

import java.util.List;

public class Packets implements IKhaoExtractor {

    @Override
    public String fileName() {
        return "packets.json";
    }

    @Override
    public JsonElement extract(MinecraftServer server) throws Exception {
        JsonObject packetsJson = new JsonObject();
        packetsJson.addProperty("protocol_version", SharedConstants.getProtocolVersion());
        packetsJson.addProperty("version", SharedConstants.getGameVersion().getName());
        packetsJson.add("serverbound", serializeBound(List.of(
                HandshakeStates.C2S_FACTORY,
                QueryStates.C2S_FACTORY,
                LoginStates.C2S_FACTORY,
                ConfigurationStates.C2S_FACTORY,
                PlayStateFactories.C2S), true));
        packetsJson.add("clientbound", serializeBound(List.of(
                QueryStates.S2C_FACTORY,
                LoginStates.S2C_FACTORY,
                ConfigurationStates.S2C_FACTORY,
                PlayStateFactories.S2C), false));
        return packetsJson;
    }

    private JsonObject serializeBound(List<NetworkState.Factory<?, ?>> factories, boolean isServerbound) {
        JsonObject result = new JsonObject();
        JsonArray handshaking = new JsonArray(), status = new JsonArray(),
                login = new JsonArray(), configuration = new JsonArray(), play = new JsonArray();

        for (NetworkState.Factory<?, ?> factory : factories) {
            NetworkPhase phase = factory.phase();
            factory.forEachPacketType((PacketType<?> packetType, int id) -> {
                String path = packetType.id().getPath();
                switch (phase) {
                    case HANDSHAKING:
                        if (isServerbound)
                            handshaking.add(path);
                        else
                            throw new IllegalStateException("Clientbound packets shouldn't have handshaking packets.");
                        break;
                    case STATUS:
                        status.add(path);
                        break;
                    case LOGIN:
                        login.add(path);
                        break;
                    case CONFIGURATION:
                        configuration.add(path);
                        break;
                    case PLAY:
                        play.add(path);
                        break;
                }
            });
        }

        if (isServerbound)
            result.add("handshaking", handshaking);
        result.add("status", status);
        result.add("login", login);
        result.add("configuration", configuration);
        result.add("play", play);
        return result;
    }
}