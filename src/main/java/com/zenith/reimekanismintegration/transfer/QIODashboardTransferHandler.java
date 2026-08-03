package com.zenith.reimekanismintegration.transfer;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import mekanism.api.Action;
import mekanism.client.recipe_viewer.QIOCraftingTransferHandler;
import mekanism.client.recipe_viewer.QIOCraftingTransferHandler.RVRecipeInfo;
import mekanism.client.recipe_viewer.QIOCraftingTransferHandler.RVRecipeSlot;
import mekanism.common.inventory.container.QIOItemViewerContainer;
import mekanism.common.lib.inventory.HashedItem;
import me.shedaniel.rei.api.client.registry.transfer.TransferHandler;
import me.shedaniel.rei.api.common.display.Display;
import me.shedaniel.rei.api.common.entry.EntryIngredient;
import me.shedaniel.rei.api.common.entry.EntryStack;
import me.shedaniel.rei.plugin.common.BuiltinPlugin;
import net.minecraft.client.Minecraft;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.CraftingRecipe;
import net.minecraft.world.item.crafting.RecipeHolder;
import net.minecraft.world.item.crafting.RecipeManager;
import org.jetbrains.annotations.Nullable;

public class QIODashboardTransferHandler implements TransferHandler {

    @Override
    public ApplicabilityResult checkApplicable(Context context) {
        if (context.getMenu() instanceof QIOItemViewerContainer && context.getDisplay().getCategoryIdentifier().equals(BuiltinPlugin.CRAFTING)) {
            return ApplicabilityResult.createApplicable();
        }
        return ApplicabilityResult.createNotApplicable();
    }

    @Override
    public Result handle(Context context) {
        RecipeInfo recipeInfo = RecipeInfo.create(context);
        if (recipeInfo == null) {
            return Result.createNotApplicable();
        }
        Action action = context.isActuallyCrafting() ? Action.EXECUTE : Action.SIMULATE;
        TransferResult transferResult = QIOCraftingTransferHandler.transferRecipe(recipeInfo, action);
        if (transferResult == null) {
            if (context.isActuallyCrafting()) {
                Minecraft.getInstance().setScreen(context.getContainerScreen());
            }
            return Result.createSuccessful().blocksFurtherHandling();
        }
        Component tooltip = transferResult.tooltip();
        return Result.createFailed(tooltip == null ? Component.literal("Error") : tooltip);
    }

    private record RecipeInfo(QIOItemViewerContainer container, RecipeHolder<CraftingRecipe> recipeHolder, List<RecipeSlot> inputs, Player player,
                               boolean stackedCrafting) implements RVRecipeInfo<TransferResult, RecipeSlot, HashedItem> {

        @Nullable
        @SuppressWarnings("unchecked")
        private static RecipeInfo create(Context context) {
            if (!(context.getMenu() instanceof QIOItemViewerContainer container)) {
                return null;
            }
            Display display = context.getDisplay();
            Optional<ResourceLocation> location = display.getDisplayLocation();
            if (location.isEmpty()) {
                return null;
            }
            Player player = Minecraft.getInstance().player;
            if (player == null) {
                return null;
            }
            RecipeManager recipeManager = player.level().getRecipeManager();
            Optional<RecipeHolder<?>> holder = recipeManager.byKey(location.get());
            if (holder.isEmpty() || !(holder.get().value() instanceof CraftingRecipe)) {
                return null;
            }
            RecipeHolder<CraftingRecipe> recipeHolder = (RecipeHolder<CraftingRecipe>) holder.get();
            List<RecipeSlot> inputs = display.getInputEntries().stream().map(RecipeSlot::of).toList();
            return new RecipeInfo(container, recipeHolder, inputs, player, context.isStackedCrafting());
        }

        @Override
        public int transferAmount() {
            return stackedCrafting ? 64 : 1;
        }

        @Override
        public TransferResult createInternalError() {
            return TransferResult.INTERNAL_ERROR;
        }

        @Override
        public TransferResult createNoRoomError() {
            return new TransferResult(Component.literal("Not enough room in inventory"), null);
        }

        @Override
        public TransferResult createMissingSlotsError(List<RecipeSlot> missing) {
            return new TransferResult(Component.literal("Missing ingredients"), missing);
        }

        @Override
        public HashedItem itemUUID(HashedItem hashed) {
            return hashed;
        }
    }

    private record RecipeSlot(List<ItemStack> itemStacks) implements RVRecipeSlot {

        private static RecipeSlot of(EntryIngredient ingredient) {
            List<ItemStack> stacks = new ArrayList<>();
            for (EntryStack<?> stack : ingredient) {
                if (stack.getValue() instanceof ItemStack itemStack && !itemStack.isEmpty()) {
                    stacks.add(itemStack);
                }
            }
            return new RecipeSlot(stacks);
        }

        @Override
        public ItemStack displayedIngredient() {
            return itemStacks.isEmpty() ? ItemStack.EMPTY : itemStacks.getFirst();
        }
    }

    private record TransferResult(@Nullable Component tooltip, @Nullable List<RecipeSlot> missingSlots) {

        private static final TransferResult INTERNAL_ERROR = new TransferResult(null, null);
    }
}
