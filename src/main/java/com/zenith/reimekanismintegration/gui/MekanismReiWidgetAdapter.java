package com.zenith.reimekanismintegration.gui;

import com.mojang.blaze3d.vertex.PoseStack;
import java.util.Collections;
import java.util.List;
import mekanism.client.gui.element.GuiElement;
import me.shedaniel.math.Rectangle;
import me.shedaniel.rei.api.client.gui.widgets.WidgetWithBounds;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.events.GuiEventListener;

public class MekanismReiWidgetAdapter extends WidgetWithBounds {

    private final GuiElement element;
    private final Rectangle bounds;

    public MekanismReiWidgetAdapter(GuiElement element) {
        this.element = element;
        this.bounds = new Rectangle(element.getX(), element.getY(), element.getWidth(), element.getHeight());
    }

    @Override
    public Rectangle getBounds() {
        return bounds;
    }

    @Override
    public void render(GuiGraphics graphics, int mouseX, int mouseY, float delta) {
        PoseStack pose = graphics.pose();
        pose.pushPose();
        pose.translate(element.getGuiLeft(), element.getGuiTop(), 0);
        element.renderShifted(graphics, mouseX, mouseY, delta);
        element.onDrawBackground(graphics, mouseX, mouseY, delta);
        int zOffset = 200;
        pose.pushPose();
        element.onRenderForeground(graphics, mouseX, mouseY, zOffset, zOffset);
        pose.popPose();
        pose.popPose();
    }

    @Override
    public List<? extends GuiEventListener> children() {
        return Collections.emptyList();
    }
}
