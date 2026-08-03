package com.zenith.reimekanismintegration.category;

import com.zenith.reimekanismintegration.display.MekanismReiDisplay;
import com.zenith.reimekanismintegration.gui.MekanismReiGuiWrapper;
import java.util.ArrayList;
import java.util.List;
import mekanism.client.recipe_viewer.type.IRecipeViewerRecipeType;
import me.shedaniel.math.Rectangle;
import me.shedaniel.rei.api.client.gui.Renderer;
import me.shedaniel.rei.api.client.gui.widgets.Widget;
import me.shedaniel.rei.api.client.gui.widgets.Widgets;
import me.shedaniel.rei.api.client.registry.display.DisplayCategory;
import me.shedaniel.rei.api.common.category.CategoryIdentifier;
import me.shedaniel.rei.api.common.util.EntryStacks;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;

public class MekanismReiCategory implements DisplayCategory<MekanismReiDisplay<?>> {

    private final IRecipeViewerRecipeType<?> recipeType;

    public MekanismReiCategory(IRecipeViewerRecipeType<?> recipeType) {
        this.recipeType = recipeType;
    }

    @Override
    public CategoryIdentifier<? extends MekanismReiDisplay<?>> getCategoryIdentifier() {
        return CategoryIdentifier.of(recipeType.id());
    }

    @Override
    public Component getTitle() {
        return recipeType.getTextComponent();
    }

    @Override
    public Renderer getIcon() {
        ItemStack stack = recipeType.iconStack();
        if (!stack.isEmpty()) {
            return EntryStacks.of(stack);
        }
        ResourceLocation icon = recipeType.icon();
        return (graphics, bounds, mouseX, mouseY, delta) -> graphics.blit(icon, bounds.x, bounds.y, 0, 0, 18, 18, 18, 18);
    }

    @Override
    public List<Widget> setupDisplay(MekanismReiDisplay<?> display, Rectangle bounds) {
        MekanismReiGuiWrapper wrapper = new MekanismReiGuiWrapper(bounds, recipeType);
        List<Widget> widgets = new ArrayList<>();
        widgets.add(Widgets.createRecipeBase(bounds));
        widgets.addAll(display.createWidgets(wrapper));
        return widgets;
    }

    @Override
    public int getDisplayHeight() {
        return recipeType.height();
    }

    @Override
    public int getDisplayWidth(MekanismReiDisplay<?> display) {
        return recipeType.width();
    }
}
