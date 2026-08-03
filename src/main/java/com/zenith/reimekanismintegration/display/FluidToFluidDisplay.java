package com.zenith.reimekanismintegration.display;

import com.zenith.reimekanismintegration.gui.MekanismReiGuiWrapper;
import java.util.ArrayList;
import java.util.List;
import mekanism.api.heat.HeatAPI;
import mekanism.api.recipes.FluidToFluidRecipe;
import mekanism.client.gui.element.GuiDownArrow;
import mekanism.client.gui.element.GuiInnerScreen;
import mekanism.client.gui.element.bar.GuiHorizontalRateBar;
import mekanism.client.gui.element.gauge.GaugeType;
import mekanism.client.gui.element.gauge.GuiFluidGauge;
import mekanism.client.gui.element.slot.GuiSlot;
import mekanism.client.gui.element.slot.SlotType;
import mekanism.client.recipe_viewer.RecipeViewerUtils;
import mekanism.common.MekanismLang;
import mekanism.common.content.evaporation.EvaporationMultiblockData;
import mekanism.common.util.MekanismUtils;
import mekanism.common.util.UnitDisplayUtils.TemperatureUnit;
import me.shedaniel.rei.api.client.gui.widgets.Widget;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.crafting.RecipeHolder;

public class FluidToFluidDisplay extends MekanismReiDisplay<FluidToFluidRecipe> {

    public FluidToFluidDisplay(ResourceLocation categoryId, RecipeHolder<FluidToFluidRecipe> holder) {
        super(categoryId, holder.value());
        addInput(fluidIngredient(recipe.getInput()));
        addOutput(fluidIngredient(recipe.getOutputDefinition()));
    }

    @Override
    public List<Widget> createWidgets(MekanismReiGuiWrapper wrapper) {
        List<Widget> widgets = new ArrayList<>();
        widgets.add(guiElement(new GuiInnerScreen(wrapper, 48, 19, 86, 40, () -> List.of(
              MekanismLang.MULTIBLOCK_FORMED.translate(), MekanismLang.EVAPORATION_HEIGHT.translate(EvaporationMultiblockData.MAX_HEIGHT),
              MekanismLang.TEMPERATURE.translate(MekanismUtils.getTemperatureDisplay(HeatAPI.AMBIENT_TEMP, TemperatureUnit.KELVIN, true)),
              MekanismLang.FLUID_PRODUCTION.translate(0.0)
        )).padding(3).clearSpacing()));
        widgets.add(guiElement(new GuiDownArrow(wrapper, 32, 39)));
        widgets.add(guiElement(new GuiDownArrow(wrapper, 142, 39)));
        widgets.add(guiElement(new GuiHorizontalRateBar(wrapper, RecipeViewerUtils.FULL_BAR, 51, 63)));
        widgets.add(guiElement(new GuiSlot(SlotType.INPUT, wrapper, 28, 20)));
        widgets.add(guiElement(new GuiSlot(SlotType.OUTPUT, wrapper, 28, 51)));
        widgets.add(guiElement(new GuiSlot(SlotType.INPUT, wrapper, 138, 20)));
        widgets.add(guiElement(new GuiSlot(SlotType.OUTPUT, wrapper, 138, 51)));
        widgets.add(tankWidget(GuiFluidGauge.getDummy(GaugeType.STANDARD, wrapper, 6, 13), getInputEntries().getFirst()));
        widgets.add(tankWidget(GuiFluidGauge.getDummy(GaugeType.STANDARD, wrapper, 158, 13), getOutputEntries().getFirst()));
        return widgets;
    }
}
