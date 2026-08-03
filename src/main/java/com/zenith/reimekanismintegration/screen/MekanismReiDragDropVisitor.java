package com.zenith.reimekanismintegration.screen;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;
import mekanism.api.chemical.ChemicalStack;
import mekanism.client.gui.GuiMekanism;
import mekanism.client.recipe_viewer.GhostIngredientHandler;
import mekanism.client.recipe_viewer.interfaces.IRecipeViewerGhostTarget.IGhostIngredientConsumer;
import me.shedaniel.math.Point;
import me.shedaniel.math.Rectangle;
import me.shedaniel.rei.api.client.gui.drag.DraggableStack;
import me.shedaniel.rei.api.client.gui.drag.DraggedAcceptorResult;
import me.shedaniel.rei.api.client.gui.drag.DraggableStackVisitor;
import me.shedaniel.rei.api.client.gui.drag.DraggingContext;
import me.shedaniel.rei.api.common.entry.EntryStack;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.renderer.Rect2i;
import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.fluids.FluidStack;
import net.neoforged.neoforge.fluids.FluidType;
import org.jetbrains.annotations.Nullable;

public class MekanismReiDragDropVisitor implements DraggableStackVisitor<Screen> {

    @Override
    public <R extends Screen> boolean isHandingScreen(R screen) {
        return screen instanceof GuiMekanism<?>;
    }

    @Override
    public DraggedAcceptorResult acceptDraggedStack(DraggingContext<Screen> context, DraggableStack stack) {
        if (!(context.getScreen() instanceof GuiMekanism<?> gui)) {
            return DraggedAcceptorResult.PASS;
        }
        Object raw = toRaw(stack.getStack());
        if (raw == null) {
            return DraggedAcceptorResult.PASS;
        }
        Point pos = context.getCurrentPosition();
        if (pos == null) {
            return DraggedAcceptorResult.PASS;
        }
        for (Target target : getTargets(gui, raw)) {
            if (target.area().contains(pos.x, pos.y)) {
                target.handler().accept(target.ingredient());
                return DraggedAcceptorResult.CONSUMED;
            }
        }
        return DraggedAcceptorResult.PASS;
    }

    @Override
    public Stream<BoundsProvider> getDraggableAcceptingBounds(DraggingContext<Screen> context, DraggableStack stack) {
        if (!(context.getScreen() instanceof GuiMekanism<?> gui)) {
            return Stream.empty();
        }
        Object raw = toRaw(stack.getStack());
        if (raw == null) {
            return Stream.empty();
        }
        List<Rectangle> rectangles = new ArrayList<>();
        for (Target target : getTargets(gui, raw)) {
            Rect2i area = target.area();
            rectangles.add(new Rectangle(area.getX(), area.getY(), area.getWidth(), area.getHeight()));
        }
        if (rectangles.isEmpty()) {
            return Stream.empty();
        }
        return Stream.of(BoundsProvider.ofRectangles(rectangles));
    }

    private static List<Target> getTargets(GuiMekanism<?> gui, Object ingredient) {
        return GhostIngredientHandler.getTargetsTyped(gui, ingredient, MekanismReiDragDropVisitor::supported, Target::new);
    }

    @Nullable
    private static Object supported(IGhostIngredientConsumer handler, Object ingredient) {
        return handler.supportedTarget(ingredient);
    }

    @Nullable
    private static Object toRaw(EntryStack<?> stack) {
        Object value = stack.getValue();
        if (value instanceof ItemStack itemStack) {
            return itemStack;
        } else if (value instanceof ChemicalStack chemicalStack) {
            return chemicalStack;
        } else if (value instanceof dev.architectury.fluid.FluidStack archFluid) {
            return new FluidStack(archFluid.getFluid(), FluidType.BUCKET_VOLUME);
        }
        return null;
    }

    private record Target(IGhostIngredientConsumer handler, Object ingredient, Rect2i area) {
    }
}
