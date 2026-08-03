package com.zenith.reimekanismintegration.display;

import com.zenith.reimekanismintegration.gui.MekanismReiGuiWrapper;
import java.util.ArrayList;
import java.util.List;
import mekanism.api.recipes.ItemStackToChemicalRecipe;
import mekanism.client.gui.element.gauge.GaugeType;
import mekanism.client.gui.element.gauge.GuiChemicalGauge;
import mekanism.client.gui.element.progress.GuiProgress;
import mekanism.client.gui.element.progress.ProgressType;
import mekanism.client.gui.element.slot.GuiSlot;
import mekanism.client.gui.element.slot.SlotType;
import mekanism.client.recipe_viewer.RecipeViewerUtils;
import mekanism.common.tile.component.config.DataType;
import me.shedaniel.rei.api.client.gui.widgets.Widget;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.crafting.RecipeHolder;

public class ItemStackToChemicalDisplay extends MekanismReiDisplay<ItemStackToChemicalRecipe> {

    private final int processTime;

    public ItemStackToChemicalDisplay(ResourceLocation categoryId, RecipeHolder<ItemStackToChemicalRecipe> holder, int processTime) {
        super(categoryId, holder.value());
        this.processTime = processTime;
        addInput(itemIngredient(recipe.getInput().getRepresentations()));
        addOutput(chemicalIngredient(recipe.getOutputDefinition()));
    }

    @Override
    public List<Widget> createWidgets(MekanismReiGuiWrapper wrapper) {
        List<Widget> widgets = new ArrayList<>();
        widgets.add(tankWidget(GuiChemicalGauge.getDummy(GaugeType.STANDARD.with(DataType.OUTPUT), wrapper, 131, 13), getOutputEntries().getFirst()));
        widgets.add(guiElement(new GuiSlot(SlotType.INPUT, wrapper, 25, 35)));
        widgets.add(inputSlot(wrapper, 26, 36, getInputEntries().getFirst()));
        GuiProgress progress = processTime == 0
              ? new GuiProgress(RecipeViewerUtils.CONSTANT_PROGRESS, ProgressType.LARGE_RIGHT, wrapper, 64, 40)
              : new GuiProgress(RecipeViewerUtils.progressHandler(processTime), ProgressType.LARGE_RIGHT, wrapper, 64, 40);
        widgets.add(guiElement(progress));
        return widgets;
    }
}
