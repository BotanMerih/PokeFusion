package io.github.landonjw.fusions;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import io.github.landonjw.fusions.commands.FusionCommand;
import io.github.landonjw.fusions.config.Config;
import io.github.landonjw.fusions.init.MenuInit;
import io.github.landonjw.fusions.network.PacketHandler;
import io.github.landonjw.fusions.ui.screen.FusionScreen;
import net.minecraft.client.gui.ScreenManager;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.RegisterCommandsEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;

@Mod(Fusions.MOD_ID)
public class Fusions {

    public static final String MOD_ID = "fusions";
    public static final Logger LOGGER = LogManager.getLogger();

    public Fusions() {
        IEventBus modEventBus = FMLJavaModLoadingContext.get().getModEventBus();

        ModLoadingContext.get().registerConfig(ModConfig.Type.COMMON, Config.SPEC, "fusions-common.toml");
        
        MenuInit.MENUS.register(modEventBus);
        
        modEventBus.addListener(this::setup);
        modEventBus.addListener(this::clientSetup);
        MinecraftForge.EVENT_BUS.addListener(this::onRegisterCommands);
    }

    private void setup(final FMLCommonSetupEvent event) {
        PacketHandler.register();
        LOGGER.info("Fusions mod has been loaded.");
    }

    private void clientSetup(final FMLClientSetupEvent event) {
        ScreenManager.register(MenuInit.FUSION_MENU.get(), FusionScreen::new);
    }

    public void onRegisterCommands(RegisterCommandsEvent event) {
        FusionCommand.register(event.getDispatcher());
    }
} 