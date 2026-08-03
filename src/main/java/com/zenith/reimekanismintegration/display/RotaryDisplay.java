package com.zenith.reimekanismintegration.display;

import com.zenith.reimekanismintegration.gui.MekanismReiGuiWrapper;
import java.util.ArrayList;
import java.util.List;
import mekanism.api.recipes.RotaryRecipe;
import mekanism.client.gui.element.GuiDownArrow;
import mekanism.client.gui.element.gauge.GaugeType;
import mekanism.client.gui.element.gauge.GuiChemicalGauge;
import mekanism.client.gui.element.gauge.GuiFluidGauge;
import mekanism.client.gui.element.progress.GuiProgress;
import mekanism.client.gui.element.progress.ProgressType;
import mekanism.client.gui.element.slot.GuiSlot;
import mekanism.client.gui.element.slot.SlotType;
import mekanism.client.recipe_viewer.RecipeViewerUtils;
import mekanism.common.inventory.container.slot.SlotOverlay;
import me.shedaniel.rei.api.client.gui.widgets.Widget;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.crafting.RecipeHolder;

public class RotaryDisplay extends MekanismReiDisplay<RotaryRecipe> {

    private final boolean condensentrating;

    public RotaryDisplay(ResourceLocation categoryId, RecipeHolder<RotaryRecipe> holder, boolean condensentrating) {
        super(categoryId, holder.value());
        this.condensentrating = condensentrating;
        if (condensentrating) {
            addInput(chemicalIngredient(recipe.getChemicalInput()));
            addOutput(fluidIngredient(recipe.getFluidOutputDefinition()));
        } else {
            addInput(fluidIngredient(recipe.getFluidInput()));
            addOutput(chemicalIngredient(recipe.getChemicalOutputDefinition()));
        }
    }

    @Override
    public List<Widget> createWidgets(MekanismReiGuiWrapper wrapper) {
        List<Widget> widgets = new ArrayList<>();
        widgets.add(guiElement(new GuiDownArrow(wrapper, 159, 44)));
        widgets.add(tankWidget(GuiChemicalGauge.getDummy(GaugeType.STANDARD, wrapper, 25, 13), condensentrating ? getInputEntries().getFirst() : getOutputEntries().getFirst()));
        widgets.add(tankWidget(GuiFluidGauge.getDummy(GaugeType.STANDARD, wrapper, 133, 13), condensentrating ? getOutputEntries().getFirst() : getInputEntries().getFirst()));
        widgets.add(guiElement(new GuiSlot(SlotType.INPUT, wrapper, 5, 25).with(SlotOverlay.PLUS)));
        widgets.add(guiElement(new GuiSlot(SlotType.OUTPUT, wrapper, 5, 56).with(SlotOverlay.MINUS)));
        widgets.add(guiElement(new GuiSlot(SlotType.INPUT, wrapper, 155, 25)));
        widgets.add(guiElement(new GuiSlot(SlotType.OUTPUT, wrapper, 155, 56)));
        widgets.add(guiElement(new GuiProgress(RecipeViewerUtils.CONSTANT_PROGRESS, condensentrating ? ProgressType.LARGE_RIGHT : ProgressType.LARGE_LEFT, wrapper, 64, 39)));
        return widgets;
    }
}
