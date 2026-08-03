package com.zenith.reimekanismintegration.transfer;

import mekanism.client.recipe_viewer.RVTransferUtils;
import mekanism.common.inventory.container.tile.FormulaicAssemblicatorContainer;
import me.shedaniel.rei.api.client.registry.transfer.TransferHandler;
import me.shedaniel.rei.api.client.registry.transfer.simple.SimpleTransferHandler;
import me.shedaniel.rei.api.common.transfer.info.stack.SlotAccessor;
import me.shedaniel.rei.plugin.common.BuiltinPlugin;

public class FormulaicAssemblicatorTransferHandler implements SimpleTransferHandler {

    @Override
    public TransferHandler.ApplicabilityResult checkApplicable(TransferHandler.Context context) {
        if (context.getMenu() instanceof FormulaicAssemblicatorContainer && context.getDisplay().getCategoryIdentifier().equals(BuiltinPlugin.CRAFTING)) {
            return TransferHandler.ApplicabilityResult.createApplicable();
        }
        return TransferHandler.ApplicabilityResult.createNotApplicable();
    }

    @Override
    public Iterable<SlotAccessor> getInputSlots(TransferHandler.Context context) {
        FormulaicAssemblicatorContainer container = (FormulaicAssemblicatorContainer) context.getMenu();
        return RVTransferUtils.getFormulaicCraftingSlots(container).stream().map(SlotAccessor::fromSlot).toList();
    }

    @Override
    public Iterable<SlotAccessor> getInventorySlots(TransferHandler.Context context) {
        FormulaicAssemblicatorContainer container = (FormulaicAssemblicatorContainer) context.getMenu();
        return RVTransferUtils.getFormulaicInputSlots(container).stream().map(SlotAccessor::fromSlot).toList();
    }
}
