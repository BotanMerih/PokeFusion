package io.github.landonjw.fusions.network;

import io.github.landonjw.fusions.Fusions;
import io.github.landonjw.fusions.network.message.ExecuteFusionPacket;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.network.NetworkRegistry;
import net.minecraftforge.fml.network.simple.SimpleChannel;

public class PacketHandler {

    private static final String PROTOCOL_VERSION = "1";
    public static final SimpleChannel INSTANCE = NetworkRegistry.newSimpleChannel(
            new ResourceLocation(Fusions.MOD_ID, "main"),
            () -> PROTOCOL_VERSION,
            PROTOCOL_VERSION::equals,
            PROTOCOL_VERSION::equals
    );

    public static void register() {
        int id = 0;
        INSTANCE.registerMessage(id++, ExecuteFusionPacket.class, ExecuteFusionPacket::encode, ExecuteFusionPacket::decode, ExecuteFusionPacket::handle);
    }
} 