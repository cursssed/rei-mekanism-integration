package com.zenith.reimekanismintegration.display;

import com.zenith.reimekanismintegration.gui.MekanismReiGuiWrapper;
import java.util.ArrayList;
import java.util.List;
import mekanism.api.recipes.NucleosynthesizingRecipe;
import mekanism.client.gui.element.gauge.GaugeType;
import mekanism.client.gui.element.gauge.GuiChemicalGauge;
import mekanism.client.gui.element.gauge.GuiEnergyGauge;
import mekanism.client.gui.element.gauge.GuiEnergyGauge.IEnergyInfoHandler;
import mekanism.client.gui.element.slot.GuiSlot;
import mekanism.client.gui.element.slot.SlotType;
import mekanism.common.inventory.container.slot.SlotOverlay;
import mekanism.common.tile.component.config.DataType;
import mekanism.common.tile.machine.TileEntityAntiprotonicNucleosynthesizer;
import me.shedaniel.rei.api.client.gui.widgets.Widget;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.crafting.RecipeHolder;

public class NucleosynthesizingDisplay extends MekanismReiDisplay<NucleosynthesizingRecipe> {

    public NucleosynthesizingDisplay(ResourceLocation categoryId, RecipeHolder<NucleosynthesizingRecipe> holder) {
        super(categoryId, holder.value());
        addInput(itemIngredient(recipe.getItemInput().getRepresentations()));
        int scalar = recipe.perTickUsage() ? TileEntityAntiprotonicNucleosynthesizer.BASE_TICKS_REQUIRED : 1;
        addInput(chemicalIngredient(recipe.getChemicalInput(), scalar));
        addOutput(itemIngredient(recipe.getOutputDefinition()));
    }

    @Override
    public List<Widget> createWidgets(MekanismReiGuiWrapper wrapper) {
        List<Widget> widgets = new ArrayList<>();
        widgets.add(guiElement(new GuiSlot(SlotType.INPUT, wrapper, 25, 39)));
        widgets.add(inputSlot(wrapper, 26, 40, getInputEntries().getFirst()));
        widgets.add(guiElement(new GuiSlot(SlotType.EXTRA, wrapper, 5, 68)));
        widgets.add(itemSlot(wrapper, 6, 69, getInputEntries().get(1)));
        widgets.add(guiElement(new GuiSlot(SlotType.OUTPUT, wrapper, 151, 39)));
        widgets.add(outputSlot(wrapper, 152, 40, getOutputEntries().getFirst()));
        widgets.add(guiElement(new GuiSlot(SlotType.POWER, wrapper, 172, 68).with(SlotOverlay.POWER)));
        widgets.add(tankWidget(GuiChemicalGauge.getDummy(GaugeType.SMALL_MED.with(DataType.INPUT), wrapper, 5, 18), getInputEntries().get(1)));
        widgets.add(guiElement(new GuiEnergyGauge(new IEnergyInfoHandler() {
            @Override
            public long getEnergy() {
                return 1L;
            }

            @Override
            public long getMaxEnergy() {
                return 1L;
            }
        }, GaugeType.SMALL_MED, wrapper, 172, 18)));
        return widgets;
    }
}
