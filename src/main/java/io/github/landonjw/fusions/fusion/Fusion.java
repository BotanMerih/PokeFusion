package io.github.landonjw.fusions.fusion;

import com.pixelmonmod.pixelmon.api.pokemon.Pokemon;
import com.pixelmonmod.pixelmon.api.pokemon.species.gender.Gender;
import com.pixelmonmod.pixelmon.api.pokemon.stats.BattleStatsType;
import com.pixelmonmod.pixelmon.api.pokemon.stats.IVStore;
import com.pixelmonmod.pixelmon.api.storage.PlayerPartyStorage;
import com.pixelmonmod.pixelmon.api.storage.StorageProxy;
import io.github.landonjw.fusions.config.Config;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.stream.Collectors;

public class Fusion {

    private final ServerPlayerEntity player;
    private final Pokemon pokemon1;
    private final Pokemon pokemon2;
    private final int fuseCount1;
    private final int fuseCount2;

    public Fusion(ServerPlayerEntity player, Pokemon pokemon1, Pokemon pokemon2) {
        this.player = player;
        this.pokemon1 = pokemon1;
        this.pokemon2 = pokemon2;
        this.fuseCount1 = getFuseCount(pokemon1);
        this.fuseCount2 = getFuseCount(pokemon2);
    }

    public String validate() {
        if (pokemon1 == null || pokemon2 == null) {
            return "One or both Pokemon are not selected.";
        }
        if (pokemon1.isEgg() || pokemon2.isEgg()) {
            return "Cannot fuse eggs.";
        }
        int maxFuses = Config.RULES.maxFuseCount.get();
        if (maxFuses != -1 && (fuseCount1 >= maxFuses)) {
            return pokemon1.getDisplayName() + " has reached the maximum number of fusions.";
        }
        if (maxFuses != -1 && (fuseCount2 >= maxFuses)) {
            return pokemon2.getDisplayName() + " has reached the maximum number of fusions.";
        }

        String fusionItemName = Config.GENERAL.fusionItem.get();
        if (fusionItemName != null && !fusionItemName.isEmpty()) {
            Item fusionItem = ForgeRegistries.ITEMS.getValue(new ResourceLocation(fusionItemName));
            if (fusionItem != null) {
                if (!player.inventory.contains(new ItemStack(fusionItem))) {
                    return "You do not have the required item for fusion: " + fusionItem.getName(new ItemStack(fusionItem)).getString();
                }
            }
        }

        String mode = Config.RULES.fusionMode.get();
        boolean canFuse = false;
        switch (mode.toUpperCase()) {
            case "SPECIES_ONLY":
                canFuse = pokemon1.getSpecies().equals(pokemon2.getSpecies());
                if (!canFuse) return "Pokemon must be the same species to fuse.";
                break;
            case "EGG_GROUP":
                canFuse = pokemon1.getForm().getEggGroups().stream()
                        .anyMatch(eggGroup -> pokemon2.getForm().getEggGroups().contains(eggGroup));
                if (!canFuse) return "Pokemon must share an egg group to fuse.";
                break;
            case "ANY":
                canFuse = true;
                break;
        }

        if (!canFuse) {
            return "These Pokemon cannot be fused based on the current rules.";
        }
        
        return ""; // Success
    }

    public void execute() {
        if (!validate().isEmpty()) {
            return;
        }

        String fusionItemName = Config.GENERAL.fusionItem.get();
        if (fusionItemName != null && !fusionItemName.isEmpty()) {
            Item fusionItem = ForgeRegistries.ITEMS.getValue(new ResourceLocation(fusionItemName));
            if (fusionItem != null) {
                player.inventory.clearOrCountMatchingItems(itemStack -> itemStack.getItem() == fusionItem, 1, player.inventoryMenu.getCraftSlots());
            }
        }

        //TODO: Implement economy support

        IVStore ivs1 = pokemon1.getIVs();
        IVStore ivs2 = pokemon2.getIVs();
        int numStatsToTransfer = Config.TRANSFER.numStatsToTransfer.get();
        int guaranteedStatGain = Config.TRANSFER.guaranteedStatGain.get();

        if (numStatsToTransfer != 0) {
            Map<BattleStatsType, Integer> ivMap = new LinkedHashMap<>();
            for (BattleStatsType stat : BattleStatsType.values()) {
                ivMap.put(stat, ivs2.getStat(stat));
            }

            Map<BattleStatsType, Integer> sortedIvs = ivMap.entrySet().stream()
                    .sorted(Map.Entry.comparingByValue(Comparator.reverseOrder()))
                    .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue,
                            (e1, e2) -> e1, LinkedHashMap::new));

            int statsTransferred = 0;
            for (Map.Entry<BattleStatsType, Integer> entry : sortedIvs.entrySet()) {
                if (numStatsToTransfer != -1 && statsTransferred >= numStatsToTransfer) {
                    break;
                }

                BattleStatsType stat = entry.getKey();
                int newIV = ivs1.getStat(stat) + guaranteedStatGain;
                ivs1.setStat(stat, Math.min(31, newIV));
                statsTransferred++;
            }
        }

        if (Config.TRANSFER.inheritShiny.get() && pokemon2.isShiny()) {
            pokemon1.setShiny(true);
        }

        if (Config.TRANSFER.inheritHiddenAbility.get() && pokemon2.getAbilitySlot() == 2) {
             pokemon1.setAbility(pokemon2.getAbility());
        }

        if (Config.TRANSFER.inheritGrowth.get()) {
            pokemon1.setGrowth(pokemon2.getGrowth());
        }

        setFuseCount(pokemon1, fuseCount1 + 1);
        pokemon1.addFlag("unbreedable");
        
        PlayerPartyStorage party = StorageProxy.getParty(player);
        party.set(party.getPosition(pokemon2), null);
    }

    private int getFuseCount(Pokemon pokemon) {
        if (pokemon != null && pokemon.getPersistentData().contains("fuseCount")) {
            return pokemon.getPersistentData().getInt("fuseCount");
        }
        return 0;
    }

    private void setFuseCount(Pokemon pokemon, int count) {
        if (pokemon != null) {
            pokemon.getPersistentData().putInt("fuseCount", count);
        }
    }
} 