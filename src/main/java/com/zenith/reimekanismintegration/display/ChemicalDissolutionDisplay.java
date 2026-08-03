package com.zenith.reimekanismintegration.display;

import com.zenith.reimekanismintegration.gui.MekanismReiGuiWrapper;
import java.util.ArrayList;
import java.util.List;
import mekanism.api.recipes.ChemicalDissolutionRecipe;
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
import mekanism.common.tile.machine.TileEntityChemicalDissolutionChamber;
import me.shedaniel.rei.api.client.gui.widgets.Widget;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.crafting.RecipeHolder;

public class ChemicalDissolutionDisplay extends MekanismReiDisplay<ChemicalDissolutionRecipe> {

    public ChemicalDissolutionDisplay(ResourceLocation categoryId, RecipeHolder<ChemicalDissolutionRecipe> holder) {
        super(categoryId, holder.value());
        addInput(itemIngredient(recipe.getItemInput().getRepresentations()));
        int scalar = recipe.perTickUsage() ? TileEntityChemicalDissolutionChamber.BASE_TICKS_REQUIRED : 1;
        addInput(chemicalIngredient(recipe.getChemicalInput(), scalar));
        addOutput(chemicalIngredient(recipe.getOutputDefinition()));
    }

    @Override
    public List<Widget> createWidgets(MekanismReiGuiWrapper wrapper) {
        List<Widget> widgets = new ArrayList<>();
        widgets.add(tankWidget(GuiChemicalGauge.getDummy(GaugeType.STANDARD.with(DataType.INPUT), wrapper, 7, 4), getInputEntries().get(1)));
        widgets.add(tankWidget(GuiChemicalGauge.getDummy(GaugeType.STANDARD.with(DataType.OUTPUT), wrapper, 131, 13), getOutputEntries().getFirst()));
        widgets.add(guiElement(new GuiSlot(SlotType.INPUT, wrapper, 27, 35)));
        widgets.add(inputSlot(wrapper, 28, 36, getInputEntries().getFirst()));
        widgets.add(guiElement(new GuiSlot(SlotType.EXTRA, wrapper, 7, 64).with(SlotOverlay.MINUS)));
        widgets.add(guiElement(new GuiSlot(SlotType.OUTPUT, wrapper, 151, 54).with(SlotOverlay.PLUS)));
        widgets.add(guiElement(new GuiSlot(SlotType.POWER, wrapper, 151, 13).with(SlotOverlay.POWER)));
        widgets.add(guiElement(new GuiProgress(RecipeViewerUtils.progressHandler(TileEntityChemicalDissolutionChamber.BASE_TICKS_REQUIRED), ProgressType.LARGE_RIGHT, wrapper, 64, 40)));
        widgets.add(guiElement(new GuiHorizontalPowerBar(wrapper, RecipeViewerUtils.FULL_BAR, 115, 75)));
        return widgets;
    }
}
