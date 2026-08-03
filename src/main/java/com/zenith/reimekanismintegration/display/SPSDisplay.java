package com.zenith.reimekanismintegration.display;

import com.zenith.reimekanismintegration.gui.MekanismReiGuiWrapper;
import java.util.ArrayList;
import java.util.List;
import mekanism.client.gui.element.GuiInnerScreen;
import mekanism.client.gui.element.gauge.GaugeType;
import mekanism.api.math.MathUtils;
import mekanism.client.gui.element.gauge.GuiChemicalGauge;
import mekanism.client.recipe_viewer.recipe.SPSRecipeViewerRecipe;
import mekanism.common.MekanismLang;
import mekanism.common.config.MekanismConfig;
import mekanism.common.util.text.EnergyDisplay;
import me.shedaniel.rei.api.client.gui.widgets.Widget;
import net.minecraft.resources.ResourceLocation;

public class SPSDisplay extends MekanismReiDisplay<SPSRecipeViewerRecipe> {

    public SPSDisplay(ResourceLocation categoryId, SPSRecipeViewerRecipe recipe) {
        super(categoryId, recipe);
        addInput(chemicalIngredient(recipe.input()));
        addOutput(chemicalIngredient(List.of(recipe.output())));
    }

    @Override
    public List<Widget> createWidgets(MekanismReiGuiWrapper wrapper) {
        List<Widget> widgets = new ArrayList<>();
        widgets.add(guiElement(new GuiInnerScreen(wrapper, 26, 13, 122, 60, () -> List.of(
              MekanismLang.STATUS.translate(MekanismLang.ACTIVE),
              MekanismLang.SPS_ENERGY_INPUT.translate(EnergyDisplay.of(
                    MathUtils.multiplyClamped(MekanismConfig.general.spsEnergyPerInput.get(), MekanismConfig.general.spsInputPerAntimatter.get())))
        ))));
        widgets.add(tankWidget(GuiChemicalGauge.getDummy(GaugeType.STANDARD, wrapper, 6, 13), getInputEntries().getFirst()));
        widgets.add(tankWidget(GuiChemicalGauge.getDummy(GaugeType.STANDARD, wrapper, 150, 13), getOutputEntries().getFirst()));
        return widgets;
    }
}
