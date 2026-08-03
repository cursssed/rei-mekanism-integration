package com.zenith.reimekanismintegration.display;

import com.zenith.reimekanismintegration.entry.MekanismReiEntryTypes;
import com.zenith.reimekanismintegration.gui.MekanismReiGuiWrapper;
import com.zenith.reimekanismintegration.gui.MekanismReiTankWidget;
import com.zenith.reimekanismintegration.gui.MekanismReiWidgetAdapter;
import dev.architectury.fluid.FluidStack;
import java.util.ArrayList;
import java.util.List;
import mekanism.api.chemical.ChemicalStack;
import mekanism.api.recipes.ingredients.ChemicalStackIngredient;
import mekanism.api.recipes.ingredients.FluidStackIngredient;
import mekanism.client.gui.element.GuiElement;
import me.shedaniel.math.Point;
import me.shedaniel.rei.api.client.gui.widgets.Slot;
import me.shedaniel.rei.api.client.gui.widgets.Widget;
import me.shedaniel.rei.api.client.gui.widgets.Widgets;
import me.shedaniel.rei.api.common.category.CategoryIdentifier;
import me.shedaniel.rei.api.common.display.Display;
import me.shedaniel.rei.api.common.entry.EntryIngredient;
import me.shedaniel.rei.api.common.entry.EntryStack;
import me.shedaniel.rei.api.common.util.EntryStacks;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;

public abstract class MekanismReiDisplay<RECIPE> implements Display {

    protected final RECIPE recipe;
    private final ResourceLocation categoryId;
    private final List<EntryIngredient> inputs = new ArrayList<>();
    private final List<EntryIngredient> outputs = new ArrayList<>();

    protected MekanismReiDisplay(ResourceLocation categoryId, RECIPE recipe) {
        this.categoryId = categoryId;
        this.recipe = recipe;
    }

    protected final void addInput(EntryIngredient ingredient) {
        inputs.add(ingredient);
    }

    protected final void addOutput(EntryIngredient ingredient) {
        outputs.add(ingredient);
    }

    protected static EntryIngredient itemIngredient(List<ItemStack> stacks) {
        return EntryIngredient.of(stacks.stream().map(EntryStacks::of).toList());
    }

    protected static EntryIngredient chemicalIngredient(List<ChemicalStack> stacks) {
        return EntryIngredient.of(stacks.stream().map(s -> EntryStack.of(MekanismReiEntryTypes.CHEMICAL, s)).toList());
    }

    protected static EntryIngredient chemicalIngredient(ChemicalStackIngredient ingredient) {
        return chemicalIngredient(ingredient.getRepresentations());
    }

    protected static EntryIngredient chemicalIngredient(ChemicalStackIngredient ingredient, int scalar) {
        List<ChemicalStack> representations = ingredient.getRepresentations();
        if (representations.isEmpty()) {
            return EntryIngredient.empty();
        }
        return chemicalIngredient(representations.stream().map(s -> s.copyWithAmount(s.getAmount() * scalar)).toList());
    }

    protected static EntryIngredient fluidIngredient(List<net.neoforged.neoforge.fluids.FluidStack> stacks) {
        return EntryIngredient.of(stacks.stream().map(MekanismReiDisplay::toArchitectury).map(EntryStacks::of).toList());
    }

    protected static EntryIngredient fluidIngredient(FluidStackIngredient ingredient) {
        return fluidIngredient(ingredient.getRepresentations());
    }

    private static FluidStack toArchitectury(net.neoforged.neoforge.fluids.FluidStack stack) {
        return FluidStack.create(stack.getFluid(), stack.getAmount(), stack.getComponentsPatch());
    }

    protected static Widget guiElement(GuiElement element) {
        return new MekanismReiWidgetAdapter(element);
    }

    protected static Widget tankWidget(GuiElement backgroundElement, EntryIngredient ingredient) {
        return new MekanismReiTankWidget(backgroundElement, ingredient);
    }

    protected static Slot inputSlot(MekanismReiGuiWrapper wrapper, int x, int y, EntryIngredient ingredient) {
        return itemSlot(wrapper, x, y, ingredient).markInput();
    }

    protected static Slot outputSlot(MekanismReiGuiWrapper wrapper, int x, int y, EntryIngredient ingredient) {
        return itemSlot(wrapper, x, y, ingredient).markOutput();
    }

    protected static Slot itemSlot(MekanismReiGuiWrapper wrapper, int x, int y, EntryIngredient ingredient) {
        Point point = new Point(wrapper.getGuiLeft() + x, wrapper.getGuiTop() + y);
        return Widgets.createSlot(point).entries(ingredient).disableBackground();
    }

    @Override
    public List<EntryIngredient> getInputEntries() {
        return inputs;
    }

    @Override
    public List<EntryIngredient> getOutputEntries() {
        return outputs;
    }

    @Override
    public CategoryIdentifier<?> getCategoryIdentifier() {
        return CategoryIdentifier.of(categoryId);
    }

    public abstract List<Widget> createWidgets(MekanismReiGuiWrapper wrapper);
}
