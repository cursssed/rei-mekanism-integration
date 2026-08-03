package com.zenith.reimekanismintegration.display;

import com.zenith.reimekanismintegration.gui.MekanismReiGuiWrapper;
import java.util.ArrayList;
import java.util.List;
import mekanism.api.recipes.ItemStackToItemStackRecipe;
import mekanism.client.gui.element.GuiUpArrow;
import mekanism.client.gui.element.bar.GuiVerticalPowerBar;
import mekanism.client.gui.element.progress.GuiProgress;
import mekanism.client.gui.element.progress.ProgressType;
import mekanism.client.gui.element.slot.GuiSlot;
import mekanism.client.gui.element.slot.SlotType;
import mekanism.client.recipe_viewer.RecipeViewerUtils;
import mekanism.common.inventory.container.slot.SlotOverlay;
import mekanism.common.tile.prefab.TileEntityElectricMachine;
import me.shedaniel.rei.api.client.gui.widgets.Widget;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.crafting.RecipeHolder;

public class ItemStackToItemStackDisplay extends MekanismReiDisplay<ItemStackToItemStackRecipe> {

    public ItemStackToItemStackDisplay(ResourceLocation categoryId, RecipeHolder<ItemStackToItemStackRecipe> holder) {
        super(categoryId, holder.value());
        addInput(itemIngredient(recipe.getInput().getRepresentations()));
        addOutput(itemIngredient(recipe.getOutputDefinition()));
    }

    @Override
    public List<Widget> createWidgets(MekanismReiGuiWrapper wrapper) {
        List<Widget> widgets = new ArrayList<>();
        widgets.add(guiElement(new GuiUpArrow(wrapper, 68, 38)));
        widgets.add(guiElement(new GuiSlot(SlotType.INPUT, wrapper, 63, 16)));
        widgets.add(inputSlot(wrapper, 64, 17, getInputEntries().getFirst()));
        widgets.add(guiElement(new GuiSlot(SlotType.OUTPUT, wrapper, 115, 34)));
        widgets.add(outputSlot(wrapper, 116, 35, getOutputEntries().getFirst()));
        widgets.add(guiElement(new GuiSlot(SlotType.POWER, wrapper, 63, 52).with(SlotOverlay.POWER)));
        widgets.add(guiElement(new GuiVerticalPowerBar(wrapper, RecipeViewerUtils.FULL_BAR, 164, 16)));
        widgets.add(guiElement(new GuiProgress(RecipeViewerUtils.progressHandler(TileEntityElectricMachine.BASE_TICKS_REQUIRED), ProgressType.BAR, wrapper, 86, 38)));
        return widgets;
    }
}
