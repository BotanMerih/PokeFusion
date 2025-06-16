package io.github.landonjw.fusions.network.message;

import java.util.function.Supplier;

import com.pixelmonmod.pixelmon.api.pokemon.Pokemon;
import com.pixelmonmod.pixelmon.api.pokemon.PokemonFactory;

import io.github.landonjw.fusions.fusion.Fusion;
import io.github.landonjw.fusions.ui.menu.FusionMenu;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.inventory.container.Container;
import net.minecraft.item.ItemStack;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraftforge.fml.network.NetworkEvent;

public class ExecuteFusionPacket {

    public ExecuteFusionPacket() {
    }

    public static void encode(ExecuteFusionPacket pkt, PacketBuffer buf) {
    }

    public static ExecuteFusionPacket decode(PacketBuffer buf) {
        return new ExecuteFusionPacket();
    }

    public static void handle(ExecuteFusionPacket pkt, Supplier<NetworkEvent.Context> ctx) {
        ctx.get().enqueueWork(() -> {
            ServerPlayerEntity player = ctx.get().getSender();
            if (player == null) return;

            Container openContainer = player.containerMenu;
            if (openContainer instanceof FusionMenu) {
                FusionMenu menu = (FusionMenu) openContainer;
                ItemStack stack1 = menu.getFusionInventory().getItem(0);
                ItemStack stack2 = menu.getFusionInventory().getItem(1);

                if (!stack1.isEmpty() && !stack2.isEmpty()) {
                    Pokemon pokemon1 = PokemonFactory.create(stack1.getOrCreateTag());
                    Pokemon pokemon2 = PokemonFactory.create(stack2.getOrCreateTag());
                    
                    if (pokemon1 != null && pokemon2 != null) {
                        Fusion fusion = new Fusion(player, pokemon1, pokemon2);
                        String validationMessage = fusion.validate();
                        if (validationMessage.isEmpty()) {
                            fusion.execute();
                            player.sendMessage(new TranslationTextComponent("commands.fusions.success"), player.getUUID());
                        } else {
                            // Return items to player if fusion is invalid
                            player.inventory.add(stack1);
                            player.inventory.add(stack2);
                            player.sendMessage(new StringTextComponent(validationMessage), player.getUUID());
                        }
                        menu.getFusionInventory().setItem(0, ItemStack.EMPTY);
                        menu.getFusionInventory().setItem(1, ItemStack.EMPTY);
                        openContainer.broadcastChanges();
                    }
                }
            }
        });
        ctx.get().setPacketHandled(true);
    }
} 