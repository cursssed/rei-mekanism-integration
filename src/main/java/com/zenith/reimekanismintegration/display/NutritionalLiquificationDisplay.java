package com.zenith.reimekanismintegration.display;

import com.zenith.reimekanismintegration.gui.MekanismReiGuiWrapper;
import java.util.ArrayList;
import java.util.List;
import mekanism.api.recipes.ItemStackToFluidOptionalItemRecipe.FluidOptionalItemOutput;
import mekanism.api.recipes.basic.BasicItemStackToFluidOptionalItemRecipe;
import mekanism.client.gui.element.gauge.GaugeType;
import mekanism.client.gui.element.gauge.GuiFluidGauge;
import mekanism.client.gui.element.progress.GuiProgress;
import mekanism.client.gui.element.progress.ProgressType;
import mekanism.client.gui.element.slot.GuiSlot;
import mekanism.client.gui.element.slot.SlotType;
import mekanism.client.recipe_viewer.RecipeViewerUtils;
import mekanism.common.tile.component.config.DataType;
import me.shedaniel.rei.api.client.gui.widgets.Widget;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.fluids.FluidStack;

public class NutritionalLiquificationDisplay extends MekanismReiDisplay<BasicItemStackToFluidOptionalItemRecipe> {

    public NutritionalLiquificationDisplay(ResourceLocation categoryId, BasicItemStackToFluidOptionalItemRecipe recipe) {
        super(categoryId, recipe);
        addInput(itemIngredient(recipe.getInput().getRepresentations()));
        List<FluidStack> fluidOutputs = new ArrayList<>();
        List<ItemStack> itemOutputs = new ArrayList<>();
        for (FluidOptionalItemOutput output : recipe.getOutputDefinition()) {
            fluidOutputs.add(output.fluid());
            itemOutputs.add(output.optionalItem());
        }
        addOutput(fluidIngredient(fluidOutputs));
        addOutput(itemIngredient(itemOutputs));
    }

    @Override
    public List<Widget> createWidgets(MekanismReiGuiWrapper wrapper) {
        List<Widget> widgets = new ArrayList<>();
        widgets.add(tankWidget(GuiFluidGauge.getDummy(GaugeType.STANDARD.with(DataType.OUTPUT), wrapper, 131, 13), getOutputEntries().getFirst()));
        widgets.add(guiElement(new GuiSlot(SlotType.INPUT, wrapper, 25, 35)));
        widgets.add(inputSlot(wrapper, 26, 36, getInputEntries().getFirst()));
        widgets.add(guiElement(new GuiSlot(SlotType.INPUT, wrapper, 109, 35)));
        widgets.add(outputSlot(wrapper, 110, 36, getOutputEntries().get(1)));
        widgets.add(guiElement(new GuiProgress(RecipeViewerUtils.CONSTANT_PROGRESS, ProgressType.LARGE_RIGHT, wrapper, 54, 40)));
        return widgets;
    }
}
