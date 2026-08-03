package com.zenith.reimekanismintegration.display;

import com.zenith.reimekanismintegration.gui.MekanismReiGuiWrapper;
import java.util.ArrayList;
import java.util.List;
import mekanism.api.recipes.ChemicalChemicalToChemicalRecipe;
import mekanism.client.gui.element.bar.GuiHorizontalPowerBar;
import mekanism.client.gui.element.gauge.GaugeType;
import mekanism.client.gui.element.gauge.GuiChemicalGauge;
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

public class ChemicalChemicalToChemicalDisplay extends MekanismReiDisplay<ChemicalChemicalToChemicalRecipe> {

    public ChemicalChemicalToChemicalDisplay(ResourceLocation categoryId, RecipeHolder<ChemicalChemicalToChemicalRecipe> holder) {
        super(categoryId, holder.value());
        addInput(chemicalIngredient(recipe.getLeftInput()));
        addInput(chemicalIngredient(recipe.getRightInput()));
        addOutput(chemicalIngredient(recipe.getOutputDefinition()));
    }

    @Override
    public List<Widget> createWidgets(MekanismReiGuiWrapper wrapper) {
        List<Widget> widgets = new ArrayList<>();
        widgets.add(tankWidget(GuiChemicalGauge.getDummy(GaugeType.STANDARD.with(DataType.INPUT_1), wrapper, 25, 13), getInputEntries().getFirst()));
        widgets.add(tankWidget(GuiChemicalGauge.getDummy(GaugeType.STANDARD.with(DataType.OUTPUT), wrapper, 79, 4), getOutputEntries().getFirst()));
        widgets.add(tankWidget(GuiChemicalGauge.getDummy(GaugeType.STANDARD.with(DataType.INPUT_2), wrapper, 133, 13), getInputEntries().get(1)));
        widgets.add(guiElement(new GuiSlot(SlotType.INPUT, wrapper, 5, 55).with(SlotOverlay.MINUS)));
        widgets.add(guiElement(new GuiSlot(SlotType.INPUT_2, wrapper, 153, 55).with(SlotOverlay.MINUS)));
        widgets.add(guiElement(new GuiSlot(SlotType.OUTPUT, wrapper, 79, 64).with(SlotOverlay.PLUS)));
        widgets.add(guiElement(new GuiProgress(RecipeViewerUtils.CONSTANT_PROGRESS, ProgressType.SMALL_RIGHT, wrapper, 47, 39)));
        widgets.add(guiElement(new GuiProgress(RecipeViewerUtils.CONSTANT_PROGRESS, ProgressType.SMALL_LEFT, wrapper, 101, 39)));
        widgets.add(guiElement(new GuiHorizontalPowerBar(wrapper, RecipeViewerUtils.FULL_BAR, 115, 75)));
        return widgets;
    }
}
