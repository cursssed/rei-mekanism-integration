package com.zenith.reimekanismintegration.entry;

import com.zenith.reimekanismintegration.ReiMekanismIntegration;
import mekanism.api.chemical.ChemicalStack;
import me.shedaniel.rei.api.common.entry.type.EntryType;
import net.minecraft.resources.ResourceLocation;

public final class MekanismReiEntryTypes {

    private MekanismReiEntryTypes() {
    }

    public static final EntryType<ChemicalStack> CHEMICAL = EntryType.deferred(ResourceLocation.fromNamespaceAndPath(ReiMekanismIntegration.MOD_ID, "chemical"));
}
