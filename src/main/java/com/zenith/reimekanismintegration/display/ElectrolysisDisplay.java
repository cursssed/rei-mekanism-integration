package com.zenith.reimekanismintegration.display;

import com.zenith.reimekanismintegration.gui.MekanismReiGuiWrapper;
import java.util.ArrayList;
import java.util.List;
import mekanism.api.chemical.ChemicalStack;
import mekanism.api.recipes.ElectrolysisRecipe;
import mekanism.api.recipes.ElectrolysisRecipe.ElectrolysisRecipeOutput;
import mekanism.client.gui.element.bar.GuiVerticalPowerBar;
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

public class ElectrolysisDisplay extends MekanismReiDisplay<ElectrolysisRecipe> {

    public ElectrolysisDisplay(ResourceLocation categoryId, RecipeHolder<ElectrolysisRecipe> holder) {
        super(categoryId, holder.value());
        addInput(fluidIngredient(recipe.getInput()));
        List<ChemicalStack> left = new ArrayList<>();
        List<ChemicalStack> right = new ArrayList<>();
        for (ElectrolysisRecipeOutput output : recipe.getOutputDefinition()) {
            left.add(output.left());
            right.add(output.right());
        }
        addOutput(chemicalIngredient(left));
        addOutput(chemicalIngredient(right));
    }

    @Override
    public List<Widget> createWidgets(MekanismReiGuiWrapper wrapper) {
        List<Widget> widgets = new ArrayList<>();
        widgets.add(tankWidget(GuiFluidGauge.getDummy(GaugeType.STANDARD.with(DataType.INPUT), wrapper, 5, 10), getInputEntries().getFirst()));
        widgets.add(tankWidget(GuiChemicalGauge.getDummy(GaugeType.SMALL.with(DataType.OUTPUT_1), wrapper, 58, 18), getOutputEntries().getFirst()));
        widgets.add(tankWidget(GuiChemicalGauge.getDummy(GaugeType.SMALL.with(DataType.OUTPUT_2), wrapper, 100, 18), getOutputEntries().get(1)));
        widgets.add(guiElement(new GuiVerticalPowerBar(wrapper, RecipeViewerUtils.FULL_BAR, 164, 15)));
        widgets.add(guiElement(new GuiSlot(SlotType.INPUT, wrapper, 25, 34)));
        widgets.add(guiElement(new GuiSlot(SlotType.OUTPUT, wrapper, 58, 51)));
        widgets.add(guiElement(new GuiSlot(SlotType.OUTPUT_2, wrapper, 100, 51)));
        widgets.add(guiElement(new GuiSlot(SlotType.POWER, wrapper, 142, 34).with(SlotOverlay.POWER)));
        widgets.add(guiElement(new GuiProgress(RecipeViewerUtils.CONSTANT_PROGRESS, ProgressType.BI, wrapper, 80, 30)));
        return widgets;
    }
}
