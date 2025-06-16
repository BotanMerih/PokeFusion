package io.github.landonjw.fusions.init;

import io.github.landonjw.fusions.Fusions;
import io.github.landonjw.fusions.ui.menu.FusionMenu;
import net.minecraft.inventory.container.ContainerType;
import net.minecraftforge.common.extensions.IForgeContainerType;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

public class MenuInit {

    public static final DeferredRegister<ContainerType<?>> MENUS = DeferredRegister.create(ForgeRegistries.CONTAINERS, Fusions.MOD_ID);

    public static final RegistryObject<ContainerType<FusionMenu>> FUSION_MENU = MENUS.register("fusion_menu",
            () -> IForgeContainerType.create((windowId, inv, data) -> new FusionMenu(windowId, inv)));

} 