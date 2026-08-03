package com.zenith.reimekanismintegration.display;

import com.zenith.reimekanismintegration.gui.MekanismReiGuiWrapper;
import java.util.ArrayList;
import java.util.List;
import mekanism.api.recipes.FluidChemicalToChemicalRecipe;
import mekanism.client.gui.element.gauge.GaugeType;
import mekanism.client.gui.element.gauge.GuiChemicalGauge;
import mekanism.client.gui.element.gauge.GuiFluidGauge;
import mekanism.client.gui.element.progress.GuiProgress;
import mekanism.client.gui.element.progress.ProgressType;
import mekanism.client.gui.element.slot.GuiSlot;
import mekanism.client.gui.element.slot.SlotType;
import mekanism.client.recipe_viewer.RecipeViewerUtils;
import mekanism.common.inventory.container.slot.SlotOverlay;
import mekanism.common.tile.component.config.DataType;
import me.shedaniel.rei.api.client.gui.widgets.Widget;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.crafting.RecipeHolder;

public class FluidChemicalToChemicalDisplay extends MekanismReiDisplay<FluidChemicalToChemicalRecipe> {

    public FluidChemicalToChemicalDisplay(ResourceLocation categoryId, RecipeHolder<FluidChemicalToChemicalRecipe> holder) {
        super(categoryId, holder.value());
        addInput(fluidIngredient(recipe.getFluidInput()));
        addInput(chemicalIngredient(recipe.getChemicalInput()));
        addOutput(chemicalIngredient(recipe.getOutputDefinition()));
    }

    @Override
    public List<Widget> createWidgets(MekanismReiGuiWrapper wrapper) {
        List<Widget> widgets = new ArrayList<>();
        widgets.add(tankWidget(GuiFluidGauge.getDummy(GaugeType.STANDARD.with(DataType.INPUT), wrapper, 7, 13), getInputEntries().getFirst()));
        widgets.add(tankWidget(GuiChemicalGauge.getDummy(GaugeType.STANDARD.with(DataType.INPUT), wrapper, 28, 13), getInputEntries().get(1)));
        widgets.add(tankWidget(GuiChemicalGauge.getDummy(GaugeType.STANDARD.with(DataType.OUTPUT), wrapper, 131, 13), getOutputEntries().getFirst()));
        widgets.add(guiElement(new GuiSlot(SlotType.POWER, wrapper, 151, 13).with(SlotOverlay.POWER)));
        widgets.add(guiElement(new GuiSlot(SlotType.OUTPUT, wrapper, 151, 55).with(SlotOverlay.MINUS)));
        widgets.add(guiElement(new GuiProgress(RecipeViewerUtils.CONSTANT_PROGRESS, ProgressType.LARGE_RIGHT, wrapper, 64, 39)));
        return widgets;
    }
}
