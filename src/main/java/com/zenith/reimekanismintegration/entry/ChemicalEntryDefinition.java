package com.zenith.reimekanismintegration.entry;

import java.util.List;
import java.util.stream.Stream;
import mekanism.api.MekanismAPI;
import mekanism.api.chemical.Chemical;
import mekanism.api.chemical.ChemicalStack;
import mekanism.api.text.TextComponentUtil;
import mekanism.client.recipe_viewer.RecipeViewerUtils;
import me.shedaniel.rei.api.client.entry.renderer.EntryRenderer;
import me.shedaniel.rei.api.common.entry.EntrySerializer;
import me.shedaniel.rei.api.common.entry.EntryStack;
import me.shedaniel.rei.api.common.entry.comparison.ComparisonContext;
import me.shedaniel.rei.api.common.entry.type.EntryDefinition;
import me.shedaniel.rei.api.common.entry.type.EntryType;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.TooltipFlag;

public class ChemicalEntryDefinition implements EntryDefinition<ChemicalStack> {

    private static final EntryRenderer<ChemicalStack> RENDERER = new ChemicalEntryRenderer();

    @Override
    public Class<ChemicalStack> getValueType() {
        return ChemicalStack.class;
    }

    @Override
    public EntryType<ChemicalStack> getType() {
        return MekanismReiEntryTypes.CHEMICAL;
    }

    @Override
    public EntryRenderer<ChemicalStack> getRenderer() {
        return RENDERER;
    }

    @Override
    public ResourceLocation getIdentifier(EntryStack<ChemicalStack> entry, ChemicalStack value) {
        ResourceKey<Chemical> key = value.getChemicalHolder().getKey();
        return key == null ? MekanismAPI.CHEMICAL_REGISTRY.getDefaultKey() : key.location();
    }

    @Override
    public boolean isEmpty(EntryStack<ChemicalStack> entry, ChemicalStack value) {
        return value.isEmpty();
    }

    @Override
    public ChemicalStack copy(EntryStack<ChemicalStack> entry, ChemicalStack value) {
        return value.copy();
    }

    @Override
    public ChemicalStack normalize(EntryStack<ChemicalStack> entry, ChemicalStack value) {
        return value.isEmpty() ? ChemicalStack.EMPTY : new ChemicalStack(value.getChemicalHolder(), 1);
    }

    @Override
    public ChemicalStack wildcard(EntryStack<ChemicalStack> entry, ChemicalStack value) {
        return normalize(entry, value);
    }

    @Override
    public long hash(EntryStack<ChemicalStack> entry, ChemicalStack value, ComparisonContext context) {
        if (value.isEmpty()) {
            return 0;
        }
        long hash = value.getChemicalHolder().hashCode();
        if (context == ComparisonContext.EXACT) {
            hash = 31 * hash + Long.hashCode(value.getAmount());
        }
        return hash;
    }

    @Override
    public boolean equals(ChemicalStack o1, ChemicalStack o2, ComparisonContext context) {
        if (o1.isEmpty() || o2.isEmpty()) {
            return o1.isEmpty() == o2.isEmpty();
        }
        if (!o1.is(o2.getChemicalHolder())) {
            return false;
        }
        return context != ComparisonContext.EXACT || o1.getAmount() == o2.getAmount();
    }

    @Override
    public EntrySerializer<ChemicalStack> getSerializer() {
        //Not synced from the server, so no serializer is required
        return null;
    }

    @Override
    public Component asFormattedText(EntryStack<ChemicalStack> entry, ChemicalStack value) {
        return TextComponentUtil.build(value.getChemical());
    }

    @Override
    public Stream<? extends TagKey<?>> getTagsFor(EntryStack<ChemicalStack> entry, ChemicalStack value) {
        return value.getTags();
    }

    static List<Component> tooltipText(ChemicalStack stack) {
        List<Component> tooltips = new java.util.ArrayList<>();
        tooltips.add(TextComponentUtil.build(stack.getChemical()));
        stack.appendHoverText(RecipeViewerUtils.getRVTooltipContext(), tooltips, TooltipFlag.NORMAL);
        return tooltips;
    }
}
