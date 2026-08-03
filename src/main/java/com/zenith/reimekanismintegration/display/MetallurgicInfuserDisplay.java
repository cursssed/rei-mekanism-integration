package com.zenith.reimekanismintegration.display;

import com.zenith.reimekanismintegration.gui.MekanismReiGuiWrapper;
import java.util.ArrayList;
import java.util.List;
import mekanism.api.recipes.ItemStackChemicalToItemStackRecipe;
import mekanism.client.gui.element.bar.GuiVerticalPowerBar;
import mekanism.client.gui.element.progress.GuiProgress;
import mekanism.client.gui.element.progress.ProgressType;
import mekanism.client.gui.element.slot.GuiSlot;
import mekanism.client.gui.element.slot.SlotType;
import mekanism.client.recipe_viewer.RecipeViewerUtils;
import mekanism.common.inventory.container.slot.SlotOverlay;
import mekanism.common.tile.machine.TileEntityMetallurgicInfuser;
import me.shedaniel.rei.api.client.gui.widgets.Widget;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.crafting.RecipeHolder;

public class MetallurgicInfuserDisplay extends MekanismReiDisplay<ItemStackChemicalToItemStackRecipe> {

    public MetallurgicInfuserDisplay(ResourceLocation categoryId, RecipeHolder<ItemStackChemicalToItemStackRecipe> holder) {
        super(categoryId, holder.value());
        addInput(itemIngredient(recipe.getItemInput().getRepresentations()));
        int scalar = recipe.perTickUsage() ? TileEntityMetallurgicInfuser.BASE_TICKS_REQUIRED : 1;
        addInput(chemicalIngredient(recipe.getChemicalInput(), scalar));
        addOutput(itemIngredient(recipe.getOutputDefinition()));
    }

    @Override
    public List<Widget> createWidgets(MekanismReiGuiWrapper wrapper) {
        List<Widget> widgets = new ArrayList<>();
        widgets.add(guiElement(new GuiSlot(SlotType.EXTRA, wrapper, 16, 34)));
        widgets.add(itemSlot(wrapper, 17, 35, getInputEntries().get(1)));
        widgets.add(guiElement(new GuiSlot(SlotType.INPUT, wrapper, 50, 42)));
        widgets.add(inputSlot(wrapper, 51, 43, getInputEntries().getFirst()));
        widgets.add(guiElement(new GuiSlot(SlotType.OUTPUT, wrapper, 108, 42)));
        widgets.add(outputSlot(wrapper, 109, 43, getOutputEntries().getFirst()));
        widgets.add(guiElement(new GuiSlot(SlotType.POWER, wrapper, 142, 34).with(SlotOverlay.POWER)));
        widgets.add(guiElement(new GuiVerticalPowerBar(wrapper, RecipeViewerUtils.FULL_BAR, 164, 15)));
        widgets.add(guiElement(new GuiProgress(RecipeViewerUtils.progressHandler(TileEntityMetallurgicInfuser.BASE_TICKS_REQUIRED), ProgressType.RIGHT, wrapper, 72, 47)));
        return widgets;
    }
}
