package com.zenith.reimekanismintegration.display;

import com.zenith.reimekanismintegration.gui.MekanismReiGuiWrapper;
import java.util.ArrayList;
import java.util.List;
import mekanism.api.recipes.ItemStackChemicalToItemStackRecipe;
import mekanism.client.gui.element.bar.GuiVerticalPowerBar;
import mekanism.client.gui.element.gauge.GaugeType;
import mekanism.client.gui.element.gauge.GuiChemicalGauge;
import mekanism.client.gui.element.progress.GuiProgress;
import mekanism.client.gui.element.progress.ProgressType;
import mekanism.client.gui.element.slot.GuiSlot;
import mekanism.client.gui.element.slot.SlotType;
import mekanism.client.recipe_viewer.RecipeViewerUtils;
import mekanism.common.inventory.container.slot.SlotOverlay;
import mekanism.common.tile.component.config.DataType;
import mekanism.common.tile.machine.TileEntityMetallurgicInfuser;
import mekanism.common.tile.machine.TileEntityPaintingMachine;
import me.shedaniel.rei.api.client.gui.widgets.Widget;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.crafting.RecipeHolder;

public class PaintingDisplay extends MekanismReiDisplay<ItemStackChemicalToItemStackRecipe> {

    public PaintingDisplay(ResourceLocation categoryId, RecipeHolder<ItemStackChemicalToItemStackRecipe> holder) {
        super(categoryId, holder.value());
        addInput(itemIngredient(recipe.getItemInput().getRepresentations()));
        int scalar = recipe.perTickUsage() ? TileEntityMetallurgicInfuser.BASE_TICKS_REQUIRED : 1;
        addInput(chemicalIngredient(recipe.getChemicalInput(), scalar));
        addOutput(itemIngredient(recipe.getOutputDefinition()));
    }

    @Override
    public List<Widget> createWidgets(MekanismReiGuiWrapper wrapper) {
        List<Widget> widgets = new ArrayList<>();
        widgets.add(guiElement(new GuiSlot(SlotType.INPUT, wrapper, 44, 34)));
        widgets.add(inputSlot(wrapper, 45, 35, getInputEntries().getFirst()));
        widgets.add(guiElement(new GuiSlot(SlotType.POWER, wrapper, 143, 34).with(SlotOverlay.POWER)));
        widgets.add(guiElement(new GuiSlot(SlotType.OUTPUT, wrapper, 115, 34)));
        widgets.add(outputSlot(wrapper, 116, 35, getOutputEntries().getFirst()));
        widgets.add(guiElement(new GuiVerticalPowerBar(wrapper, RecipeViewerUtils.FULL_BAR, 164, 15)));
        widgets.add(tankWidget(GuiChemicalGauge.getDummy(GaugeType.STANDARD.with(DataType.INPUT), wrapper, 25, 13), getInputEntries().get(1)));
        widgets.add(guiElement(new GuiProgress(RecipeViewerUtils.progressHandler(TileEntityPaintingMachine.BASE_TICKS_REQUIRED), ProgressType.LARGE_RIGHT, wrapper, 64, 39)));
        return widgets;
    }
}
