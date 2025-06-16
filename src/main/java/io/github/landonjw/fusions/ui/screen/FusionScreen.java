package io.github.landonjw.fusions.ui.screen;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.systems.RenderSystem;

import io.github.landonjw.fusions.Fusions;
import io.github.landonjw.fusions.network.PacketHandler;
import io.github.landonjw.fusions.network.message.ExecuteFusionPacket;
import io.github.landonjw.fusions.ui.menu.FusionMenu;
import net.minecraft.client.gui.screen.inventory.ContainerScreen;
import net.minecraft.client.gui.widget.button.Button;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TranslationTextComponent;

public class FusionScreen extends ContainerScreen<FusionMenu> {

    private static final ResourceLocation GUI_TEXTURE = new ResourceLocation(Fusions.MOD_ID, "textures/gui/fusion_gui.png");

    public FusionScreen(FusionMenu screenContainer, PlayerInventory inv, ITextComponent titleIn) {
        super(screenContainer, inv, titleIn);
        this.imageWidth = 176;
        this.imageHeight = 166;
    }

    @Override
    protected void init() {
        super.init();
        // This button is just a placeholder for the original functionality
        this.addButton(new Button(this.leftPos + 78, this.topPos + 58, 20, 20, new TranslationTextComponent("."), (button) -> {
            PacketHandler.INSTANCE.sendToServer(new ExecuteFusionPacket());
            this.minecraft.player.closeContainer();
        }));
    }

    @Override
    public void render(MatrixStack matrixStack, int mouseX, int mouseY, float partialTicks) {
        this.renderBackground(matrixStack);
        super.render(matrixStack, mouseX, mouseY, partialTicks);
        this.renderTooltip(matrixStack, mouseX, mouseY);
    }

    @Override
    protected void renderBg(MatrixStack matrixStack, float partialTicks, int x, int y) {
        RenderSystem.color4f(1.0F, 1.0F, 1.0F, 1.0F);
        this.minecraft.getTextureManager().bind(GUI_TEXTURE);
        int i = this.leftPos;
        int j = this.topPos;
        this.blit(matrixStack, i, j, 0, 0, this.imageWidth, this.imageHeight);
    }

    @Override
    protected void renderLabels(MatrixStack matrixStack, int x, int y) {
        this.font.draw(matrixStack, this.title, (float)this.titleLabelX, (float)this.titleLabelY, 4210752);
        this.font.draw(matrixStack, this.inventory.getDisplayName(), (float)this.inventoryLabelX, (float)this.inventoryLabelY, 4210752);
    }
} 