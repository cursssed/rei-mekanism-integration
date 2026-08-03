package com.zenith.reimekanismintegration.gui;

import com.mojang.blaze3d.vertex.PoseStack;
import java.util.Collections;
import java.util.List;
import mekanism.api.chemical.ChemicalStack;
import mekanism.client.gui.GuiUtils;
import mekanism.client.gui.GuiUtils.TilingDirection;
import mekanism.client.gui.element.GuiElement;
import mekanism.client.gui.element.gauge.GuiGauge;
import mekanism.client.recipe_viewer.RecipeViewerUtils;
import mekanism.client.render.MekanismRenderer;
import mekanism.client.render.MekanismRenderer.FluidTextureType;
import com.mojang.blaze3d.platform.InputConstants;
import me.shedaniel.math.Rectangle;
import me.shedaniel.rei.api.client.config.ConfigObject;
import me.shedaniel.rei.api.client.gui.widgets.Tooltip;
import me.shedaniel.rei.api.client.gui.widgets.TooltipContext;
import me.shedaniel.rei.api.client.gui.widgets.WidgetWithBounds;
import me.shedaniel.rei.api.client.view.ViewSearchBuilder;
import me.shedaniel.rei.api.common.entry.EntryIngredient;
import me.shedaniel.rei.api.common.entry.EntryStack;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.events.GuiEventListener;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.world.item.Item;
import net.neoforged.neoforge.fluids.FluidStack;

public class MekanismReiTankWidget extends WidgetWithBounds {

    private final GuiElement backgroundElement;
    private final GuiGauge<?> gauge;
    private final EntryIngredient ingredient;
    private final Rectangle bounds;

    public MekanismReiTankWidget(GuiElement backgroundElement, EntryIngredient ingredient) {
        this.backgroundElement = backgroundElement;
        this.gauge = backgroundElement instanceof GuiGauge<?> g ? g : null;
        this.ingredient = ingredient;
        this.bounds = new Rectangle(backgroundElement.getX(), backgroundElement.getY(), backgroundElement.getWidth(), backgroundElement.getHeight());
    }

    @Override
    public Rectangle getBounds() {
        return bounds;
    }

    private EntryStack<?> currentStack() {
        if (ingredient.isEmpty()) {
            return null;
        }
        return RecipeViewerUtils.getCurrent(ingredient);
    }

    @Override
    public void render(GuiGraphics graphics, int mouseX, int mouseY, float delta) {
        PoseStack pose = graphics.pose();
        pose.pushPose();
        pose.translate(backgroundElement.getGuiLeft(), backgroundElement.getGuiTop(), 0);
        backgroundElement.renderShifted(graphics, mouseX, mouseY, delta);
        backgroundElement.onDrawBackground(graphics, mouseX, mouseY, delta);
        pose.popPose();

        EntryStack<?> current = currentStack();
        if (current != null && !current.isEmpty()) {
            TextureAtlasSprite sprite;
            Object value = current.getValue();
            if (value instanceof ChemicalStack chemicalStack) {
                MekanismRenderer.color(graphics, chemicalStack);
                sprite = MekanismRenderer.getChemicalTexture(chemicalStack);
            } else if (value instanceof dev.architectury.fluid.FluidStack archFluid) {
                FluidStack fluidStack = new FluidStack(archFluid.getFluid(), (int) Math.min(Integer.MAX_VALUE, archFluid.getAmount()));
                MekanismRenderer.color(graphics, fluidStack);
                sprite = MekanismRenderer.getFluidTexture(fluidStack, FluidTextureType.STILL);
            } else {
                sprite = null;
            }
            if (sprite != null) {
                int x = bounds.x + 1;
                int y = bounds.y + 1;
                int width = bounds.width - 2;
                int height = bounds.height - 2;
                GuiUtils.drawTiledSprite(graphics, x, y, height, width, height, sprite, 16, 16, 0, TilingDirection.UP_RIGHT);
                MekanismRenderer.resetColor(graphics);
            }
        }
        if (gauge != null) {
            PoseStack pose2 = graphics.pose();
            pose2.pushPose();
            pose2.translate(gauge.getGuiLeft(), gauge.getGuiTop(), 0);
            gauge.drawBarOverlay(graphics);
            pose2.popPose();
        }

        if (current != null && !current.isEmpty() && containsMouse(mouseX, mouseY)) {
            Tooltip tooltip = current.getTooltip(TooltipContext.ofMouse(Item.TooltipContext.of(Minecraft.getInstance().level)));
            if (tooltip != null) {
                tooltip.queue();
            }
        }
    }

    @Override
    public Tooltip getTooltip(TooltipContext context) {
        EntryStack<?> current = currentStack();
        if (current == null || current.isEmpty()) {
            return null;
        }
        return current.getTooltip(context);
    }

    @Override
    public List<? extends GuiEventListener> children() {
        return Collections.emptyList();
    }

    @Override
    public boolean mouseClicked(double mouseX, double mouseY, int button) {
        if (containsMouse(mouseX, mouseY)) {
            EntryStack<?> current = currentStack();
            if (current != null && !current.isEmpty()) {
                ConfigObject config = ConfigObject.getInstance();
                if ((config.getRecipeKeybind().getType() != InputConstants.Type.MOUSE && button == 0) || config.getRecipeKeybind().matchesMouse(button)) {
                    return ViewSearchBuilder.builder().addRecipesFor(current).open();
                } else if ((config.getUsageKeybind().getType() != InputConstants.Type.MOUSE && button == 1) || config.getUsageKeybind().matchesMouse(button)) {
                    return ViewSearchBuilder.builder().addUsagesFor(current).open();
                }
            }
        }
        return super.mouseClicked(mouseX, mouseY, button);
    }

    @Override
    public boolean keyPressed(int keyCode, int scanCode, int modifiers) {
        if (containsMouse(mouse())) {
            EntryStack<?> current = currentStack();
            if (current != null && !current.isEmpty()) {
                ConfigObject config = ConfigObject.getInstance();
                if (config.getRecipeKeybind().matchesKey(keyCode, scanCode)) {
                    return ViewSearchBuilder.builder().addRecipesFor(current).open();
                } else if (config.getUsageKeybind().matchesKey(keyCode, scanCode)) {
                    return ViewSearchBuilder.builder().addUsagesFor(current).open();
                }
            }
        }
        return super.keyPressed(keyCode, scanCode, modifiers);
    }
}
