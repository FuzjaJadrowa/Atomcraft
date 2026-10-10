package pl.fuzjajadrowa.atomcraft.client.screen;

import pl.fuzjajadrowa.atomcraft.block.MachineKind;
import pl.fuzjajadrowa.atomcraft.menu.MachineMenu;
import pl.fuzjajadrowa.atomcraft.Atomcraft;

import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;

public final class MachineScreen extends AbstractContainerScreen<MachineMenu> {
    public MachineScreen(MachineMenu menu, Inventory inventory, Component title) {
        super(menu, inventory, title); imageWidth = 176; imageHeight = 166; inventoryLabelY = 72;
    }
    private static ResourceLocation texture(String name) { return ResourceLocation.fromNamespaceAndPath(Atomcraft.MODID, "textures/gui/" + name + ".png"); }
    @Override protected void renderBg(GuiGraphics graphics, float partialTick, int mouseX, int mouseY) {
        graphics.blit(texture(menu.kind.id), leftPos, topPos, 0, 0, imageWidth, imageHeight, imageWidth, imageHeight);
        int height = scaled(menu.value(0), menu.value(1), 50);
        if (height > 0) graphics.blit(texture("energy_fill"), leftPos + 152, topPos + 68 - height, 0, 50 - height, 12, height, 12, 50);
        if (menu.kind == MachineKind.GENERATOR) {
            int flame = scaled(menu.value(4), menu.value(5), 14);
            if (flame > 0) graphics.blit(texture("burn_fill"), leftPos + 80, topPos + 49 - flame, 0, 14 - flame, 14, flame, 14, 14);
        } else {
            int width = scaled(menu.value(2), menu.value(3), 24);
            if (width > 0) graphics.blit(texture("progress_fill"), leftPos + 79, topPos + 34, 0, 0, width, 17, 24, 17);
        }
    }
    private static int scaled(int value, int total, int size) { return total <= 0 ? 0 : (int) Math.clamp((long) value * size / total, 0, size); }
    @Override public void render(GuiGraphics graphics, int mouseX, int mouseY, float partialTick) {
        super.render(graphics, mouseX, mouseY, partialTick);
        renderTooltip(graphics, mouseX, mouseY);
        if (isHovering(152, 18, 12, 50, mouseX, mouseY)) graphics.renderTooltip(font,
                Component.literal(menu.value(0) + " / " + menu.value(1) + " FE"), mouseX, mouseY);
    }
}