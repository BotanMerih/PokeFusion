package io.github.landonjw.fusions.data;

import com.pixelmonmod.pixelmon.api.pokemon.Pokemon;
import com.pixelmonmod.pixelmon.api.storage.PlayerPartyStorage;
import com.pixelmonmod.pixelmon.api.storage.StorageProxy;
import io.github.landonjw.fusions.config.Config;
import io.github.landonjw.fusions.fusion.Fusion;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.util.text.TextFormatting;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class FusionManager {

    private static final FusionManager INSTANCE = new FusionManager();
    private final Map<UUID, FusionData> fusionDataMap = new HashMap<>();

    private FusionManager() {}

    public static FusionManager getInstance() {
        return INSTANCE;
    }

    public FusionData getFusionData(UUID playerUuid) {
        return fusionDataMap.computeIfAbsent(playerUuid, k -> new FusionData());
    }

    public void setPokemon(ServerPlayerEntity player, int fusionSlot, int partySlot) {
        PlayerPartyStorage party = StorageProxy.getParty(player.getUUID());
        if (partySlot < 1 || partySlot > party.getAll().length) {
            player.sendMessage(new StringTextComponent("Invalid party slot.").withStyle(TextFormatting.RED), player.getUUID());
            return;
        }

        Pokemon pokemon = party.get(partySlot - 1);
        if (pokemon == null) {
            player.sendMessage(new StringTextComponent("There is no Pokemon in that slot.").withStyle(TextFormatting.RED), player.getUUID());
            return;
        }

        FusionData data = getFusionData(player.getUUID());
        if (fusionSlot == 1) {
            data.setBase(pokemon);
            player.sendMessage(new StringTextComponent("Set " + pokemon.getDisplayName() + " as the base Pokemon.").withStyle(TextFormatting.GREEN), player.getUUID());
        } else {
            data.setSacrifice(pokemon);
            player.sendMessage(new StringTextComponent("Set " + pokemon.getDisplayName() + " as the sacrifice Pokemon.").withStyle(TextFormatting.GREEN), player.getUUID());
        }
    }

    public void confirmFusion(ServerPlayerEntity player) {
        FusionData data = getFusionData(player.getUUID());
        Pokemon base = data.getBase();
        Pokemon sacrifice = data.getSacrifice();

        if (base == null || sacrifice == null) {
            player.sendMessage(new StringTextComponent("You must select two Pokemon to fuse.").withStyle(TextFormatting.RED), player.getUUID());
            return;
        }

        if (base.getUUID().equals(sacrifice.getUUID())) {
            player.sendMessage(new StringTextComponent("You cannot fuse a Pokemon with itself.").withStyle(TextFormatting.RED), player.getUUID());
            return;
        }

        // --- Economy Integration Start ---
        double cost = Config.GENERAL.fusionCost.get();
        if (cost > 0) {
            String command = Config.GENERAL.economyCommand.get()
                    .replace("{player}", player.getName().getString())
                    .replace("{cost}", String.valueOf(cost));

            MinecraftServer server = player.getServer();
            if (server != null) {
                server.getCommands().performCommand(server.createCommandSourceStack(), command);
            }
        }
        // --- Economy Integration End ---

        Fusion fusion = new Fusion(player, base, sacrifice);
        String validationMessage = fusion.validate();
        if (validationMessage.isEmpty()) {
            fusion.execute();
            player.sendMessage(new StringTextComponent("Successfully fused " + base.getDisplayName() + " and " + sacrifice.getDisplayName() + "!").withStyle(TextFormatting.GOLD), player.getUUID());
            // Clear data after successful fusion
            fusionDataMap.remove(player.getUUID());
        } else {
            player.sendMessage(new StringTextComponent(validationMessage).withStyle(TextFormatting.RED), player.getUUID());
        }
    }

    public void declineFusion(ServerPlayerEntity player) {
        fusionDataMap.remove(player.getUUID());
        player.sendMessage(new StringTextComponent("Fusion cancelled.").withStyle(TextFormatting.YELLOW), player.getUUID());
    }
} 