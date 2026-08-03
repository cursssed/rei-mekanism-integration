package com.zenith.reimekanismintegration.entry;

import java.util.List;
import mekanism.api.chemical.ChemicalStack;
import mekanism.client.render.MekanismRenderer;
import mekanism.common.MekanismLang;
import mekanism.common.util.text.TextUtils;
import me.shedaniel.math.Rectangle;
import me.shedaniel.rei.api.client.entry.renderer.EntryRenderer;
import me.shedaniel.rei.api.client.gui.widgets.Tooltip;
import me.shedaniel.rei.api.client.gui.widgets.TooltipContext;
import me.shedaniel.rei.api.common.entry.EntryStack;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.network.chat.Component;

public class ChemicalEntryRenderer implements EntryRenderer<ChemicalStack> {

    @Override
    public void render(EntryStack<ChemicalStack> entry, GuiGraphics graphics, Rectangle bounds, int mouseX, int mouseY, float delta) {
        ChemicalStack stack = entry.getValue();
        if (stack.isEmpty()) {
            return;
        }
        int color = MekanismRenderer.getTint(stack.getChemicalHolder());
        TextureAtlasSprite sprite = MekanismRenderer.getChemicalTexture(stack.getChemicalHolder());
        float red = MekanismRenderer.getRed(color);
        float green = MekanismRenderer.getGreen(color);
        float blue = MekanismRenderer.getBlue(color);
        graphics.blit(bounds.x, bounds.y, 0, bounds.width, bounds.height, sprite, red, green, blue, 1);
    }

    @Override
    public Tooltip getTooltip(EntryStack<ChemicalStack> entry, TooltipContext context) {
        ChemicalStack stack = entry.getValue();
        if (stack.isEmpty()) {
            return null;
        }
        List<Component> tooltips = ChemicalEntryDefinition.tooltipText(stack);
        if (stack.getAmount() > 1) {
            tooltips.add(MekanismLang.GENERIC_MB.translateColored(mekanism.api.text.EnumColor.GRAY, TextUtils.format(stack.getAmount())));
        }
        return Tooltip.create(context.getPoint(), tooltips);
    }
}
