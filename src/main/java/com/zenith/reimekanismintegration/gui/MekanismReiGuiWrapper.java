package com.zenith.reimekanismintegration.gui;

import java.util.Collections;
import java.util.List;
import mekanism.client.gui.IGuiWrapper;
import mekanism.client.recipe_viewer.type.IRecipeViewerRecipeType;
import me.shedaniel.math.Rectangle;
import net.minecraft.client.gui.components.events.AbstractContainerEventHandler;
import net.minecraft.client.gui.components.events.GuiEventListener;

public class MekanismReiGuiWrapper extends AbstractContainerEventHandler implements IGuiWrapper {

    private final Rectangle bounds;
    private final IRecipeViewerRecipeType<?> recipeType;

    public MekanismReiGuiWrapper(Rectangle bounds, IRecipeViewerRecipeType<?> recipeType) {
        this.bounds = bounds;
        this.recipeType = recipeType;
    }

    @Override
    public int getGuiLeft() {
        return bounds.x + recipeType.xOffset();
    }

    @Override
    public int getGuiTop() {
        return bounds.y + recipeType.yOffset();
    }

    @Override
    public int getXSize() {
        return recipeType.width();
    }

    @Override
    public int getYSize() {
        return recipeType.height();
    }

    @Override
    public List<? extends GuiEventListener> children() {
        return Collections.emptyList();
    }
}
