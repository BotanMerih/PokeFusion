package io.github.landonjw.fusions.ui.menu;

import io.github.landonjw.fusions.init.MenuInit;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Inventory;
import net.minecraft.inventory.container.Container;
import net.minecraft.inventory.container.Slot;
import net.minecraft.item.ItemStack;

public class FusionMenu extends Container {

    private final IInventory fusionInventory = new Inventory(2);

    public FusionMenu(int windowId, PlayerInventory playerInventory) {
        this(windowId, playerInventory, new Inventory(2));
    }

    public FusionMenu(int windowId, PlayerInventory playerInventory, IInventory fusionInventory) {
        super(MenuInit.FUSION_MENU.get(), windowId);

        // Add the two fusion slots
        this.addSlot(new Slot(fusionInventory, 0, 44, 35)); // Slot for the base Pokemon
        this.addSlot(new Slot(fusionInventory, 1, 116, 35)); // Slot for the sacrifice Pokemon

        // Add player inventory
        for (int i = 0; i < 3; ++i) {
            for (int j = 0; j < 9; ++j) {
                this.addSlot(new Slot(playerInventory, j + i * 9 + 9, 8 + j * 18, 84 + i * 18));
            }
        }

        // Add player hotbar
        for (int k = 0; k < 9; ++k) {
            this.addSlot(new Slot(playerInventory, k, 8 + k * 18, 142));
        }
    }

    @Override
    public boolean stillValid(PlayerEntity playerIn) {
        return true;
    }

    @Override
    public ItemStack quickMoveStack(PlayerEntity playerIn, int index) {
        ItemStack itemstack = ItemStack.EMPTY;
        Slot slot = this.slots.get(index);
        if (slot != null && slot.hasItem()) {
            ItemStack itemstack1 = slot.getItem();
            itemstack = itemstack1.copy();
            if (index < 2) { // Fusion slots
                if (!this.moveItemStackTo(itemstack1, 2, this.slots.size(), true)) {
                    return ItemStack.EMPTY;
                }
            } else { // Player inventory
                if (!this.moveItemStackTo(itemstack1, 0, 2, false)) {
                    return ItemStack.EMPTY;
                }
            }

            if (itemstack1.isEmpty()) {
                slot.set(ItemStack.EMPTY);
            } else {
                slot.setChanged();
            }
        }
        return itemstack;
    }

    public IInventory getFusionInventory() {
        return this.fusionInventory;
    }
} 