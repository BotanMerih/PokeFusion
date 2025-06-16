package io.github.landonjw.fusions.commands;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.IntegerArgumentType;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import io.github.landonjw.fusions.data.FusionData;
import io.github.landonjw.fusions.data.FusionManager;
import net.minecraft.command.CommandSource;
import net.minecraft.command.Commands;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.util.text.TextFormatting;

public class FusionCommand {

    public static void register(CommandDispatcher<CommandSource> dispatcher) {
        LiteralArgumentBuilder<CommandSource> command = Commands.literal("pokefusion")
                .requires(source -> source.hasPermission(0)) // Allow all players
                .then(Commands.literal("1")
                        .then(Commands.argument("slot", IntegerArgumentType.integer(1, 6))
                                .executes(context -> setPokemon(context.getSource().getPlayerOrException(), 1, IntegerArgumentType.getInteger(context, "slot")))))
                .then(Commands.literal("2")
                        .then(Commands.argument("slot", IntegerArgumentType.integer(1, 6))
                                .executes(context -> setPokemon(context.getSource().getPlayerOrException(), 2, IntegerArgumentType.getInteger(context, "slot")))))
                .then(Commands.literal("confirm")
                        .executes(context -> confirm(context.getSource().getPlayerOrException())))
                .then(Commands.literal("decline")
                        .executes(context -> decline(context.getSource().getPlayerOrException())))
                .then(Commands.literal("help")
                        .executes(context -> help(context.getSource().getPlayerOrException())))
                .executes(context -> status(context.getSource().getPlayerOrException()));

        dispatcher.register(command);
    }

    private static int setPokemon(ServerPlayerEntity player, int fusionSlot, int partySlot) {
        FusionManager.getInstance().setPokemon(player, fusionSlot, partySlot);
        return 1;
    }

    private static int confirm(ServerPlayerEntity player) {
        FusionManager.getInstance().confirmFusion(player);
        return 1;
    }

    private static int decline(ServerPlayerEntity player) {
        FusionManager.getInstance().declineFusion(player);
        return 1;
    }

    private static int status(ServerPlayerEntity player) {
        FusionData data = FusionManager.getInstance().getFusionData(player.getUUID());
        String baseName = (data.getBase() != null) ? data.getBase().getDisplayName() : "Empty";
        String sacrificeName = (data.getSacrifice() != null) ? data.getSacrifice().getDisplayName() : "Empty";

        player.sendMessage(new StringTextComponent("--- Fusion Status ---").withStyle(TextFormatting.YELLOW), player.getUUID());
        player.sendMessage(new StringTextComponent("Slot 1 (Base): " + baseName).withStyle(TextFormatting.WHITE), player.getUUID());
        player.sendMessage(new StringTextComponent("Slot 2 (Sacrifice): " + sacrificeName).withStyle(TextFormatting.WHITE), player.getUUID());
        player.sendMessage(new StringTextComponent("Use '/pokefusion help' for commands.").withStyle(TextFormatting.GRAY), player.getUUID());
        return 1;
    }

    private static int help(ServerPlayerEntity player) {
        player.sendMessage(new StringTextComponent("--- PokeFusion Help ---").withStyle(TextFormatting.GOLD), player.getUUID());
        player.sendMessage(new StringTextComponent("/pokefusion - Shows current selection.").withStyle(TextFormatting.WHITE), player.getUUID());
        player.sendMessage(new StringTextComponent("/pokefusion 1 <slot> - Select base Pokemon (1-6).").withStyle(TextFormatting.WHITE), player.getUUID());
        player.sendMessage(new StringTextComponent("/pokefusion 2 <slot> - Select sacrifice Pokemon (1-6).").withStyle(TextFormatting.WHITE), player.getUUID());
        player.sendMessage(new StringTextComponent("/pokefusion confirm - Execute the fusion.").withStyle(TextFormatting.WHITE), player.getUUID());
        player.sendMessage(new StringTextComponent("/pokefusion decline - Cancel the fusion.").withStyle(TextFormatting.WHITE), player.getUUID());
        return 1;
    }
} 