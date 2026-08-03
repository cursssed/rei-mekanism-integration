package com.zenith.reimekanismintegration.display;

import com.zenith.reimekanismintegration.gui.MekanismReiGuiWrapper;
import java.util.ArrayList;
import java.util.List;
import mekanism.api.recipes.ItemStackToEnergyRecipe;
import mekanism.client.gui.element.gauge.GaugeType;
import mekanism.client.gui.element.gauge.GuiEnergyGauge;
import mekanism.client.gui.element.gauge.GuiEnergyGauge.IEnergyInfoHandler;
import mekanism.client.gui.element.progress.GuiProgress;
import mekanism.client.gui.element.progress.ProgressType;
import mekanism.client.gui.element.slot.GuiSlot;
import mekanism.client.gui.element.slot.SlotType;
import mekanism.client.recipe_viewer.RecipeViewerUtils;
import mekanism.common.tile.component.config.DataType;
import me.shedaniel.rei.api.client.gui.widgets.Widget;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.crafting.RecipeHolder;

public class ItemStackToEnergyDisplay extends MekanismReiDisplay<ItemStackToEnergyRecipe> {

    public ItemStackToEnergyDisplay(ResourceLocation categoryId, RecipeHolder<ItemStackToEnergyRecipe> holder) {
        super(categoryId, holder.value());
        addInput(itemIngredient(recipe.getInput().getRepresentations()));
    }

    @Override
    public List<Widget> createWidgets(MekanismReiGuiWrapper wrapper) {
        List<Widget> widgets = new ArrayList<>();
        long[] outputDefinition = recipe.getOutputDefinition();
        long maxEnergy = 0;
        for (long value : outputDefinition) {
            maxEnergy = Math.max(maxEnergy, value);
        }
        long finalMaxEnergy = maxEnergy;
        widgets.add(guiElement(new GuiEnergyGauge(new IEnergyInfoHandler() {
            @Override
            public long getEnergy() {
                return finalMaxEnergy;
            }

            @Override
            public long getMaxEnergy() {
                return finalMaxEnergy;
            }
        }, GaugeType.STANDARD.with(DataType.OUTPUT), wrapper, 133, 13)));
        widgets.add(guiElement(new GuiSlot(SlotType.INPUT, wrapper, 25, 35)));
        widgets.add(inputSlot(wrapper, 26, 36, getInputEntries().getFirst()));
        widgets.add(guiElement(new GuiProgress(RecipeViewerUtils.CONSTANT_PROGRESS, ProgressType.LARGE_RIGHT, wrapper, 64, 40)));
        return widgets;
    }
}
