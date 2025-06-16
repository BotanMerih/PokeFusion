package io.github.landonjw.fusions.config;

import net.minecraftforge.common.ForgeConfigSpec;

public class Config {

    private static final ForgeConfigSpec.Builder BUILDER = new ForgeConfigSpec.Builder();
    public static final General GENERAL = new General(BUILDER);
    public static final Rules RULES = new Rules(BUILDER);
    public static final TransferSettings TRANSFER = new TransferSettings(BUILDER);
    public static final ForgeConfigSpec SPEC = BUILDER.build();

    public static class General {
        public final ForgeConfigSpec.ConfigValue<Boolean> enableGUI;
        public final ForgeConfigSpec.ConfigValue<Integer> cooldownSeconds;
        public final ForgeConfigSpec.ConfigValue<Double> fusionCost;
        public final ForgeConfigSpec.ConfigValue<String> fusionItem;
        public final ForgeConfigSpec.ConfigValue<String> economyCommand;

        General(ForgeConfigSpec.Builder builder) {
            builder.push("General Settings");
            enableGUI = builder.comment("Enable or disable the GUI for fusions.").define("EnableGUI", true);
            cooldownSeconds = builder.comment("Cooldown in seconds for the fusion command. 0 to disable.").defineInRange("CooldownSeconds", 30, 0, Integer.MAX_VALUE);
            fusionCost = builder.comment("Cost for performing a fusion. Requires an economy mod if implemented.").defineInRange("FusionCost", 1000.0, 0.0, Double.MAX_VALUE);
            fusionItem = builder.comment("Item required for fusion. Use item ID, e.g., 'minecraft:diamond'. Leave empty to disable.").define("FusionItem", "minecraft:diamond");
            economyCommand = builder.comment("The command to execute for taking money from the player. Use {player} for player name and {cost} for the cost.").define("EconomyCommand", "eco take {player} {cost}");
            builder.pop();
        }
    }

    public static class Rules {
        public final ForgeConfigSpec.ConfigValue<String> fusionMode;
        public final ForgeConfigSpec.ConfigValue<Integer> maxFuseCount;

        Rules(ForgeConfigSpec.Builder builder) {
            builder.push("Fusion Rules");
            fusionMode = builder.comment("Defines which Pokemon can be fused. Options: SPECIES_ONLY, EGG_GROUP, ANY").define("FusionMode", "EGG_GROUP");
            maxFuseCount = builder.comment("Maximum number of times a Pokemon can be fused. -1 for unlimited.").defineInRange("MaxFuseCount", -1, -1, Integer.MAX_VALUE);
            builder.pop();
        }
    }

    public static class TransferSettings {
        public final ForgeConfigSpec.ConfigValue<Double> ivGainPercentage;
        public final ForgeConfigSpec.ConfigValue<Boolean> inheritShiny;
        public final ForgeConfigSpec.ConfigValue<Boolean> inheritHiddenAbility;
        public final ForgeConfigSpec.ConfigValue<Boolean> inheritGrowth;
        public final ForgeConfigSpec.ConfigValue<Integer> numStatsToTransfer;
        public final ForgeConfigSpec.ConfigValue<Integer> guaranteedStatGain;

        TransferSettings(ForgeConfigSpec.Builder builder) {
            builder.push("Transfer Settings");
            ivGainPercentage = builder.comment("Percentage of IVs gained from the sacrificed Pokemon.").defineInRange("IvGainPercentage", 0.5, 0.0, 1.0);
            inheritShiny = builder.comment("If the main Pokemon should become shiny if the sacrificed one is shiny.").define("InheritShiny", true);
            inheritHiddenAbility = builder.comment("If the main Pokemon should get the hidden ability if the sacrificed one has it.").define("InheritHiddenAbility", true);
            inheritGrowth = builder.comment("If the main Pokemon should inherit the growth from the sacrificed one.").define("InheritGrowth", true);
            numStatsToTransfer = builder.comment("Number of highest stats to transfer from sacrificed Pokemon. -1 for all.").defineInRange("NumStatsToTransfer", 3, -1, 6);
            guaranteedStatGain = builder.comment("Guaranteed stat points to add to each of the transferred stats.").defineInRange("GuaranteedStatGain", 3, 0, 255);
            builder.pop();
        }
    }
} 