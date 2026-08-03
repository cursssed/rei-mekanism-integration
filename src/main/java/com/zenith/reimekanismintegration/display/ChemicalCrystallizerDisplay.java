package com.zenith.reimekanismintegration.display;

import com.zenith.reimekanismintegration.gui.MekanismReiGuiWrapper;
import java.util.ArrayList;
import java.util.List;
import mekanism.api.recipes.ChemicalCrystallizerRecipe;
import mekanism.client.gui.element.gauge.GaugeType;
import mekanism.client.gui.element.gauge.GuiChemicalGauge;
import mekanism.client.gui.element.progress.GuiProgress;
import mekanism.client.gui.element.progress.ProgressType;
import mekanism.client.gui.element.slot.GuiSlot;
import mekanism.client.gui.element.slot.SlotType;
import mekanism.client.recipe_viewer.RecipeViewerUtils;
import mekanism.common.inventory.container.slot.SlotOverlay;
import mekanism.common.tile.component.config.DataType;
import mekanism.common.tile.machine.TileEntityChemicalCrystallizer;
import me.shedaniel.rei.api.client.gui.widgets.Widget;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.crafting.RecipeHolder;

public class ChemicalCrystallizerDisplay extends MekanismReiDisplay<ChemicalCrystallizerRecipe> {

    public ChemicalCrystallizerDisplay(ResourceLocation categoryId, RecipeHolder<ChemicalCrystallizerRecipe> holder) {
        super(categoryId, holder.value());
        addInput(chemicalIngredient(recipe.getInput()));
        addOutput(itemIngredient(recipe.getOutputDefinition()));
    }

    @Override
    public List<Widget> createWidgets(MekanismReiGuiWrapper wrapper) {
        List<Widget> widgets = new ArrayList<>();
        widgets.add(tankWidget(GuiChemicalGauge.getDummy(GaugeType.STANDARD.with(DataType.INPUT), wrapper, 7, 4), getInputEntries().getFirst()));
        widgets.add(guiElement(new GuiSlot(SlotType.INPUT, wrapper, 7, 64).with(SlotOverlay.PLUS)));
        widgets.add(guiElement(new GuiSlot(SlotType.OUTPUT, wrapper, 128, 56)));
        widgets.add(outputSlot(wrapper, 129, 57, getOutputEntries().getFirst()));
        widgets.add(guiElement(new GuiProgress(RecipeViewerUtils.progressHandler(TileEntityChemicalCrystallizer.BASE_TICKS_REQUIRED), ProgressType.LARGE_RIGHT, wrapper, 53, 61)));
        return widgets;
    }
}
