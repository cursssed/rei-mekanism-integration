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
import mekanism.common.tile.prefab.TileEntityAdvancedElectricMachine;
import me.shedaniel.rei.api.client.gui.widgets.Widget;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.crafting.RecipeHolder;

public class ItemStackChemicalToItemStackDisplay extends MekanismReiDisplay<ItemStackChemicalToItemStackRecipe> {

    private static final int PROCESS_TIME = TileEntityAdvancedElectricMachine.BASE_TICKS_REQUIRED;

    public ItemStackChemicalToItemStackDisplay(ResourceLocation categoryId, RecipeHolder<ItemStackChemicalToItemStackRecipe> holder) {
        super(categoryId, holder.value());
        addInput(itemIngredient(recipe.getItemInput().getRepresentations()));
        int scalar = recipe.perTickUsage() ? PROCESS_TIME : 1;
        addInput(chemicalIngredient(recipe.getChemicalInput(), scalar));
        addOutput(itemIngredient(recipe.getOutputDefinition()));
    }

    @Override
    public List<Widget> createWidgets(MekanismReiGuiWrapper wrapper) {
        List<Widget> widgets = new ArrayList<>();
        widgets.add(guiElement(new GuiSlot(SlotType.INPUT, wrapper, 63, 16)));
        widgets.add(inputSlot(wrapper, 64, 17, getInputEntries().getFirst()));
        widgets.add(guiElement(new GuiSlot(SlotType.EXTRA, wrapper, 63, 52)));
        widgets.add(itemSlot(wrapper, 64, 53, getInputEntries().get(1)));
        widgets.add(guiElement(new GuiSlot(SlotType.OUTPUT, wrapper, 115, 34)));
        widgets.add(outputSlot(wrapper, 116, 35, getOutputEntries().getFirst()));
        widgets.add(guiElement(new GuiSlot(SlotType.POWER, wrapper, 38, 34).with(SlotOverlay.POWER)));
        widgets.add(guiElement(new GuiVerticalPowerBar(wrapper, RecipeViewerUtils.FULL_BAR, 164, 16)));
        widgets.add(guiElement(new GuiProgress(RecipeViewerUtils.progressHandler(PROCESS_TIME), ProgressType.BAR, wrapper, 86, 38)));
        return widgets;
    }
}
