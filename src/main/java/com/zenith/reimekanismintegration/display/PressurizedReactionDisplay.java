package com.zenith.reimekanismintegration.display;

import com.zenith.reimekanismintegration.gui.MekanismReiGuiWrapper;
import java.util.ArrayList;
import java.util.List;
import mekanism.api.chemical.ChemicalStack;
import mekanism.api.recipes.PressurizedReactionRecipe;
import mekanism.api.recipes.PressurizedReactionRecipe.PressurizedReactionRecipeOutput;
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
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.RecipeHolder;

public class PressurizedReactionDisplay extends MekanismReiDisplay<PressurizedReactionRecipe> {

    public PressurizedReactionDisplay(ResourceLocation categoryId, RecipeHolder<PressurizedReactionRecipe> holder) {
        super(categoryId, holder.value());
        addInput(itemIngredient(recipe.getInputSolid().getRepresentations()));
        addInput(fluidIngredient(recipe.getInputFluid()));
        addInput(chemicalIngredient(recipe.getInputChemical()));
        List<ItemStack> itemOutputs = new ArrayList<>();
        List<ChemicalStack> chemicalOutputs = new ArrayList<>();
        for (PressurizedReactionRecipeOutput output : recipe.getOutputDefinition()) {
            itemOutputs.add(output.item());
            chemicalOutputs.add(output.chemical());
        }
        addOutput(itemIngredient(itemOutputs));
        addOutput(chemicalIngredient(chemicalOutputs));
    }

    @Override
    public List<Widget> createWidgets(MekanismReiGuiWrapper wrapper) {
        List<Widget> widgets = new ArrayList<>();
        widgets.add(guiElement(new GuiSlot(SlotType.INPUT, wrapper, 53, 39)));
        widgets.add(inputSlot(wrapper, 54, 40, getInputEntries().getFirst()));
        widgets.add(guiElement(new GuiSlot(SlotType.OUTPUT, wrapper, 115, 39)));
        widgets.add(outputSlot(wrapper, 116, 40, getOutputEntries().getFirst()));
        widgets.add(guiElement(new GuiSlot(SlotType.POWER, wrapper, 140, 21).with(SlotOverlay.POWER)));
        widgets.add(tankWidget(GuiFluidGauge.getDummy(GaugeType.STANDARD.with(DataType.INPUT), wrapper, 5, 15), getInputEntries().get(1)));
        widgets.add(tankWidget(GuiChemicalGauge.getDummy(GaugeType.STANDARD.with(DataType.INPUT), wrapper, 28, 15), getInputEntries().get(2)));
        widgets.add(tankWidget(GuiChemicalGauge.getDummy(GaugeType.SMALL.with(DataType.OUTPUT), wrapper, 140, 45), getOutputEntries().get(1)));
        widgets.add(guiElement(new GuiVerticalPowerBar(wrapper, RecipeViewerUtils.FULL_BAR, 164, 21)));
        widgets.add(guiElement(new GuiProgress(RecipeViewerUtils.progressHandler(recipe.getDuration()), ProgressType.RIGHT, wrapper, 77, 43)));
        return widgets;
    }
}
