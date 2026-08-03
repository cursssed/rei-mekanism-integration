package com.zenith.reimekanismintegration.display;

import com.zenith.reimekanismintegration.gui.MekanismReiGuiWrapper;
import java.util.ArrayList;
import java.util.List;
import mekanism.api.recipes.SawmillRecipe;
import mekanism.client.gui.element.GuiUpArrow;
import mekanism.client.gui.element.bar.GuiVerticalPowerBar;
import mekanism.client.gui.element.progress.GuiProgress;
import mekanism.client.gui.element.progress.ProgressType;
import mekanism.client.gui.element.slot.GuiSlot;
import mekanism.client.gui.element.slot.SlotType;
import mekanism.client.recipe_viewer.RecipeViewerUtils;
import mekanism.common.inventory.container.slot.SlotOverlay;
import mekanism.common.tile.machine.TileEntityPrecisionSawmill;
import me.shedaniel.rei.api.client.gui.widgets.Widget;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.crafting.RecipeHolder;

public class SawmillDisplay extends MekanismReiDisplay<SawmillRecipe> {

    public SawmillDisplay(ResourceLocation categoryId, RecipeHolder<SawmillRecipe> holder) {
        super(categoryId, holder.value());
        addInput(itemIngredient(recipe.getInput().getRepresentations()));
        addOutput(itemIngredient(recipe.getMainOutputDefinition()));
        addOutput(itemIngredient(recipe.getSecondaryOutputDefinition()));
    }

    @Override
    public List<Widget> createWidgets(MekanismReiGuiWrapper wrapper) {
        List<Widget> widgets = new ArrayList<>();
        widgets.add(guiElement(new GuiUpArrow(wrapper, 60, 38)));
        widgets.add(guiElement(new GuiSlot(SlotType.INPUT, wrapper, 55, 16)));
        widgets.add(inputSlot(wrapper, 56, 17, getInputEntries().getFirst()));
        widgets.add(guiElement(new GuiSlot(SlotType.POWER, wrapper, 55, 52).with(SlotOverlay.POWER)));
        widgets.add(guiElement(new GuiSlot(SlotType.OUTPUT_WIDE, wrapper, 111, 30)));
        widgets.add(outputSlot(wrapper, 115, 34, getOutputEntries().getFirst()));
        widgets.add(outputSlot(wrapper, 131, 34, getOutputEntries().get(1)));
        widgets.add(guiElement(new GuiVerticalPowerBar(wrapper, RecipeViewerUtils.FULL_BAR, 164, 15)));
        widgets.add(guiElement(new GuiProgress(RecipeViewerUtils.progressHandler(TileEntityPrecisionSawmill.BASE_TICKS_REQUIRED), ProgressType.BAR, wrapper, 78, 38)));
        return widgets;
    }
}
