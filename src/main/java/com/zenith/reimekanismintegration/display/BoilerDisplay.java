package com.zenith.reimekanismintegration.display;

import com.zenith.reimekanismintegration.gui.MekanismReiGuiWrapper;
import java.util.ArrayList;
import java.util.List;
import mekanism.client.gui.element.GuiInnerScreen;
import mekanism.client.gui.element.gauge.GaugeType;
import mekanism.client.gui.element.gauge.GuiChemicalGauge;
import mekanism.client.gui.element.gauge.GuiFluidGauge;
import mekanism.client.recipe_viewer.recipe.BoilerRecipeViewerRecipe;
import mekanism.common.MekanismLang;
import mekanism.common.util.MekanismUtils;
import mekanism.common.util.UnitDisplayUtils.TemperatureUnit;
import mekanism.common.util.text.TextUtils;
import me.shedaniel.rei.api.client.gui.widgets.Widget;
import me.shedaniel.rei.api.common.entry.EntryIngredient;
import net.minecraft.resources.ResourceLocation;

public class BoilerDisplay extends MekanismReiDisplay<BoilerRecipeViewerRecipe> {

    public BoilerDisplay(ResourceLocation categoryId, BoilerRecipeViewerRecipe recipe) {
        super(categoryId, recipe);
        addInput(fluidIngredient(recipe.water()));
        addOutput(chemicalIngredient(List.of(recipe.steam())));
        if (recipe.superHeatedCoolant() == null) {
            addInput(EntryIngredient.empty());
            addOutput(EntryIngredient.empty());
        } else {
            addInput(chemicalIngredient(recipe.superHeatedCoolant()));
            addOutput(chemicalIngredient(List.of(recipe.cooledCoolant())));
        }
    }

    @Override
    public List<Widget> createWidgets(MekanismReiGuiWrapper wrapper) {
        List<Widget> widgets = new ArrayList<>();
        widgets.add(guiElement(new GuiInnerScreen(wrapper, 48, 23, 96, 40, () -> List.of(
              MekanismLang.TEMPERATURE.translate(MekanismUtils.getTemperatureDisplay(recipe.temperature(), TemperatureUnit.KELVIN, true)),
              MekanismLang.BOIL_RATE.translate(TextUtils.format((int) Math.min(Integer.MAX_VALUE, recipe.steam().getAmount())))
        ))));
        if (recipe.superHeatedCoolant() != null) {
            widgets.add(tankWidget(GuiChemicalGauge.getDummy(GaugeType.STANDARD, wrapper, 6, 13), getInputEntries().get(1)));
            widgets.add(tankWidget(GuiChemicalGauge.getDummy(GaugeType.STANDARD, wrapper, 168, 13), getOutputEntries().get(1)));
        }
        widgets.add(tankWidget(GuiFluidGauge.getDummy(GaugeType.STANDARD, wrapper, 26, 13), getInputEntries().getFirst()));
        widgets.add(tankWidget(GuiChemicalGauge.getDummy(GaugeType.STANDARD, wrapper, 148, 13), getOutputEntries().getFirst()));
        return widgets;
    }
}
