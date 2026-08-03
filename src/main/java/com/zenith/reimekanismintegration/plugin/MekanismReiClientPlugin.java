package com.zenith.reimekanismintegration.plugin;

import com.zenith.reimekanismintegration.category.MekanismReiCategory;
import com.zenith.reimekanismintegration.display.BoilerDisplay;
import com.zenith.reimekanismintegration.display.ChemicalChemicalToChemicalDisplay;
import com.zenith.reimekanismintegration.display.ChemicalCrystallizerDisplay;
import com.zenith.reimekanismintegration.display.ChemicalDissolutionDisplay;
import com.zenith.reimekanismintegration.display.ChemicalToChemicalDisplay;
import com.zenith.reimekanismintegration.display.CombinerDisplay;
import com.zenith.reimekanismintegration.display.ElectrolysisDisplay;
import com.zenith.reimekanismintegration.display.FluidChemicalToChemicalDisplay;
import com.zenith.reimekanismintegration.display.FluidToFluidDisplay;
import com.zenith.reimekanismintegration.display.ItemStackChemicalToItemStackDisplay;
import com.zenith.reimekanismintegration.display.ItemStackToChemicalDisplay;
import com.zenith.reimekanismintegration.display.ItemStackToEnergyDisplay;
import com.zenith.reimekanismintegration.display.ItemStackToItemStackDisplay;
import com.zenith.reimekanismintegration.display.MekanismReiDisplay;
import com.zenith.reimekanismintegration.display.MetallurgicInfuserDisplay;
import com.zenith.reimekanismintegration.display.NucleosynthesizingDisplay;
import com.zenith.reimekanismintegration.display.NutritionalLiquificationDisplay;
import com.zenith.reimekanismintegration.display.PaintingDisplay;
import com.zenith.reimekanismintegration.display.PressurizedReactionDisplay;
import com.zenith.reimekanismintegration.display.RotaryDisplay;
import com.zenith.reimekanismintegration.display.SPSDisplay;
import com.zenith.reimekanismintegration.display.SawmillDisplay;
import com.zenith.reimekanismintegration.entry.ChemicalEntryDefinition;
import com.zenith.reimekanismintegration.entry.MekanismReiEntryTypes;
import com.zenith.reimekanismintegration.screen.MekanismReiDragDropVisitor;
import com.zenith.reimekanismintegration.transfer.FormulaicAssemblicatorTransferHandler;
import com.zenith.reimekanismintegration.transfer.QIODashboardTransferHandler;
import java.util.List;
import java.util.function.BiFunction;
import mekanism.api.recipes.RotaryRecipe;
import mekanism.api.recipes.basic.BasicItemStackToFluidOptionalItemRecipe;
import mekanism.client.gui.GuiMekanism;
import mekanism.client.recipe_viewer.GuiElementHandler;
import mekanism.client.recipe_viewer.RecipeViewerUtils;
import mekanism.client.recipe_viewer.recipe.BoilerRecipeViewerRecipe;
import mekanism.client.recipe_viewer.recipe.SPSRecipeViewerRecipe;
import mekanism.client.recipe_viewer.type.IRecipeViewerRecipeType;
import mekanism.client.recipe_viewer.type.RecipeViewerRecipeType;
import mekanism.common.block.attribute.Attribute;
import mekanism.common.block.attribute.AttributeFactoryType;
import mekanism.common.registries.MekanismBlocks;
import mekanism.common.tier.FactoryTier;
import mekanism.common.tile.machine.TileEntityChemicalOxidizer;
import mekanism.common.tile.machine.TileEntityPigmentExtractor;
import mekanism.common.util.EnumUtils;
import me.shedaniel.math.Rectangle;
import me.shedaniel.rei.api.client.plugins.REIClientPlugin;
import me.shedaniel.rei.api.client.registry.category.CategoryRegistry;
import me.shedaniel.rei.api.client.registry.display.DisplayRegistry;
import me.shedaniel.rei.api.client.registry.screen.ExclusionZones;
import me.shedaniel.rei.api.client.registry.screen.ScreenRegistry;
import me.shedaniel.rei.api.client.registry.transfer.TransferHandlerRegistry;
import me.shedaniel.rei.api.common.category.CategoryIdentifier;
import me.shedaniel.rei.api.common.entry.EntryIngredient;
import me.shedaniel.rei.api.common.entry.type.EntryTypeRegistry;
import me.shedaniel.rei.api.common.util.EntryStacks;
import me.shedaniel.rei.forge.REIPluginClient;
import net.minecraft.client.Minecraft;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.RecipeHolder;
import net.minecraft.world.item.crafting.RecipeManager;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.Level;

@REIPluginClient
public class MekanismReiClientPlugin implements REIClientPlugin {

    @Override
    public void registerEntryTypes(EntryTypeRegistry registry) {
        registry.register(MekanismReiEntryTypes.CHEMICAL, new ChemicalEntryDefinition());
    }

    @Override
    @SuppressWarnings({"rawtypes"})
    public void registerExclusionZones(ExclusionZones zones) {
        zones.register(GuiMekanism.class, (GuiMekanism gui) ->
              GuiElementHandler.getGuiExtraAreas(gui).stream()
                    .map(rect -> new Rectangle(rect.getX(), rect.getY(), rect.getWidth(), rect.getHeight()))
                    .toList());
    }

    @Override
    public void registerScreens(ScreenRegistry registry) {
        registry.registerDraggableStackVisitor(new MekanismReiDragDropVisitor());
    }

    @Override
    public void registerTransferHandlers(TransferHandlerRegistry registry) {
        registry.register(new FormulaicAssemblicatorTransferHandler());
        registry.register(new QIODashboardTransferHandler());
    }

    @Override
    public void registerCategories(CategoryRegistry registry) {
        for (IRecipeViewerRecipeType<?> type : allTypes()) {
            registry.add(new MekanismReiCategory(type));
            registry.addWorkstations(CategoryIdentifier.of(type.id()), workstations(type.workstations()));
        }
    }

    @Override
    public void registerDisplays(DisplayRegistry registry) {
        Level level = Minecraft.getInstance().level;
        if (level == null) {
            return;
        }
        RecipeManager recipeManager = level.getRecipeManager();

        addDisplays(registry, RecipeViewerRecipeType.CRUSHING.id(), RecipeViewerRecipeType.CRUSHING.getRecipes(recipeManager), ItemStackToItemStackDisplay::new);
        addDisplays(registry, RecipeViewerRecipeType.ENRICHING.id(), RecipeViewerRecipeType.ENRICHING.getRecipes(recipeManager), ItemStackToItemStackDisplay::new);
        addDisplays(registry, RecipeViewerRecipeType.SMELTING.id(), RecipeViewerRecipeType.SMELTING.getRecipes(recipeManager), ItemStackToItemStackDisplay::new);
        addDisplays(registry, RecipeViewerRecipeType.CHEMICAL_INFUSING.id(), RecipeViewerRecipeType.CHEMICAL_INFUSING.getRecipes(recipeManager), ChemicalChemicalToChemicalDisplay::new);
        addDisplays(registry, RecipeViewerRecipeType.PIGMENT_MIXING.id(), RecipeViewerRecipeType.PIGMENT_MIXING.getRecipes(recipeManager), ChemicalChemicalToChemicalDisplay::new);
        addDisplays(registry, RecipeViewerRecipeType.COMBINING.id(), RecipeViewerRecipeType.COMBINING.getRecipes(recipeManager), CombinerDisplay::new);
        addDisplays(registry, RecipeViewerRecipeType.SEPARATING.id(), RecipeViewerRecipeType.SEPARATING.getRecipes(recipeManager), ElectrolysisDisplay::new);
        addDisplays(registry, RecipeViewerRecipeType.WASHING.id(), RecipeViewerRecipeType.WASHING.getRecipes(recipeManager), FluidChemicalToChemicalDisplay::new);
        addDisplays(registry, RecipeViewerRecipeType.EVAPORATING.id(), RecipeViewerRecipeType.EVAPORATING.getRecipes(recipeManager), FluidToFluidDisplay::new);
        addDisplays(registry, RecipeViewerRecipeType.ACTIVATING.id(), RecipeViewerRecipeType.ACTIVATING.getRecipes(recipeManager), ChemicalToChemicalDisplay::new);
        addDisplays(registry, RecipeViewerRecipeType.CENTRIFUGING.id(), RecipeViewerRecipeType.CENTRIFUGING.getRecipes(recipeManager), ChemicalToChemicalDisplay::new);
        addDisplays(registry, RecipeViewerRecipeType.CRYSTALLIZING.id(), RecipeViewerRecipeType.CRYSTALLIZING.getRecipes(recipeManager), ChemicalCrystallizerDisplay::new);
        addDisplays(registry, RecipeViewerRecipeType.DISSOLUTION.id(), RecipeViewerRecipeType.DISSOLUTION.getRecipes(recipeManager), ChemicalDissolutionDisplay::new);
        addDisplays(registry, RecipeViewerRecipeType.COMPRESSING.id(), RecipeViewerRecipeType.COMPRESSING.getRecipes(recipeManager), ItemStackChemicalToItemStackDisplay::new);
        addDisplays(registry, RecipeViewerRecipeType.PURIFYING.id(), RecipeViewerRecipeType.PURIFYING.getRecipes(recipeManager), ItemStackChemicalToItemStackDisplay::new);
        addDisplays(registry, RecipeViewerRecipeType.INJECTING.id(), RecipeViewerRecipeType.INJECTING.getRecipes(recipeManager), ItemStackChemicalToItemStackDisplay::new);
        addDisplays(registry, RecipeViewerRecipeType.METALLURGIC_INFUSING.id(), RecipeViewerRecipeType.METALLURGIC_INFUSING.getRecipes(recipeManager), MetallurgicInfuserDisplay::new);
        addDisplays(registry, RecipeViewerRecipeType.PAINTING.id(), RecipeViewerRecipeType.PAINTING.getRecipes(recipeManager), PaintingDisplay::new);
        addDisplays(registry, RecipeViewerRecipeType.NUCLEOSYNTHESIZING.id(), RecipeViewerRecipeType.NUCLEOSYNTHESIZING.getRecipes(recipeManager), NucleosynthesizingDisplay::new);
        addDisplays(registry, RecipeViewerRecipeType.ENERGY_CONVERSION.id(), RecipeViewerRecipeType.ENERGY_CONVERSION.getRecipes(recipeManager), ItemStackToEnergyDisplay::new);
        addDisplays(registry, RecipeViewerRecipeType.CHEMICAL_CONVERSION.id(), RecipeViewerRecipeType.CHEMICAL_CONVERSION.getRecipes(recipeManager),
              (id, holder) -> new ItemStackToChemicalDisplay(id, holder, 0));
        addDisplays(registry, RecipeViewerRecipeType.OXIDIZING.id(), RecipeViewerRecipeType.OXIDIZING.getRecipes(recipeManager),
              (id, holder) -> new ItemStackToChemicalDisplay(id, holder, TileEntityChemicalOxidizer.BASE_TICKS_REQUIRED));
        addDisplays(registry, RecipeViewerRecipeType.PIGMENT_EXTRACTING.id(), RecipeViewerRecipeType.PIGMENT_EXTRACTING.getRecipes(recipeManager),
              (id, holder) -> new ItemStackToChemicalDisplay(id, holder, TileEntityPigmentExtractor.BASE_TICKS_REQUIRED));
        addDisplays(registry, RecipeViewerRecipeType.REACTION.id(), RecipeViewerRecipeType.REACTION.getRecipes(recipeManager), PressurizedReactionDisplay::new);
        addDisplays(registry, RecipeViewerRecipeType.SAWING.id(), RecipeViewerRecipeType.SAWING.getRecipes(recipeManager), SawmillDisplay::new);

        addRotaryDisplays(registry, recipeManager);

        for (BoilerRecipeViewerRecipe recipe : BoilerRecipeViewerRecipe.getBoilerRecipes()) {
            registry.add(new BoilerDisplay(RecipeViewerRecipeType.BOILER.id(), recipe));
        }
        for (SPSRecipeViewerRecipe recipe : SPSRecipeViewerRecipe.getSPSRecipes()) {
            registry.add(new SPSDisplay(RecipeViewerRecipeType.SPS.id(), recipe));
        }
        for (BasicItemStackToFluidOptionalItemRecipe recipe : RecipeViewerUtils.getLiquificationRecipes().values()) {
            registry.add(new NutritionalLiquificationDisplay(RecipeViewerRecipeType.NUTRITIONAL_LIQUIFICATION.id(), recipe));
        }
    }

    private static <RECIPE extends net.minecraft.world.item.crafting.Recipe<?>> void addDisplays(
          DisplayRegistry registry, ResourceLocation id, List<RecipeHolder<RECIPE>> recipes,
          BiFunction<ResourceLocation, RecipeHolder<RECIPE>, ? extends MekanismReiDisplay<RECIPE>> displayFactory) {
        for (RecipeHolder<RECIPE> holder : recipes) {
            registry.add(displayFactory.apply(id, holder));
        }
    }

    private static void addRotaryDisplays(DisplayRegistry registry, RecipeManager recipeManager) {
        for (RecipeHolder<RotaryRecipe> holder : RecipeViewerRecipeType.CONDENSENTRATING.getRecipes(recipeManager)) {
            if (holder.value().hasChemicalToFluid()) {
                registry.add(new RotaryDisplay(RecipeViewerRecipeType.CONDENSENTRATING.id(), holder, true));
            }
        }
        for (RecipeHolder<RotaryRecipe> holder : RecipeViewerRecipeType.DECONDENSENTRATING.getRecipes(recipeManager)) {
            if (holder.value().hasFluidToChemical()) {
                registry.add(new RotaryDisplay(RecipeViewerRecipeType.DECONDENSENTRATING.id(), holder, false));
            }
        }
    }

    private static List<IRecipeViewerRecipeType<?>> allTypes() {
        return List.of(
              RecipeViewerRecipeType.CRUSHING, RecipeViewerRecipeType.ENRICHING, RecipeViewerRecipeType.SMELTING,
              RecipeViewerRecipeType.CHEMICAL_INFUSING, RecipeViewerRecipeType.COMBINING, RecipeViewerRecipeType.SEPARATING,
              RecipeViewerRecipeType.WASHING, RecipeViewerRecipeType.EVAPORATING, RecipeViewerRecipeType.ACTIVATING,
              RecipeViewerRecipeType.CENTRIFUGING, RecipeViewerRecipeType.CRYSTALLIZING, RecipeViewerRecipeType.DISSOLUTION,
              RecipeViewerRecipeType.COMPRESSING, RecipeViewerRecipeType.PURIFYING, RecipeViewerRecipeType.INJECTING,
              RecipeViewerRecipeType.NUCLEOSYNTHESIZING, RecipeViewerRecipeType.ENERGY_CONVERSION, RecipeViewerRecipeType.CHEMICAL_CONVERSION,
              RecipeViewerRecipeType.OXIDIZING, RecipeViewerRecipeType.PIGMENT_EXTRACTING, RecipeViewerRecipeType.PIGMENT_MIXING,
              RecipeViewerRecipeType.METALLURGIC_INFUSING, RecipeViewerRecipeType.PAINTING, RecipeViewerRecipeType.REACTION,
              RecipeViewerRecipeType.CONDENSENTRATING, RecipeViewerRecipeType.DECONDENSENTRATING, RecipeViewerRecipeType.SAWING,
              RecipeViewerRecipeType.BOILER, RecipeViewerRecipeType.SPS, RecipeViewerRecipeType.NUTRITIONAL_LIQUIFICATION
        );
    }

    private static EntryIngredient[] workstations(List<ItemLike> items) {
        List<EntryIngredient> ingredients = new java.util.ArrayList<>();
        for (ItemLike workstation : items) {
            net.minecraft.world.item.Item item = workstation.asItem();
            ingredients.add(EntryIngredient.of(EntryStacks.of(new ItemStack(item))));
            if (item instanceof net.minecraft.world.item.BlockItem blockItem) {
                AttributeFactoryType factoryType = Attribute.get(blockItem.getBlock(), AttributeFactoryType.class);
                if (factoryType != null) {
                    for (FactoryTier tier : EnumUtils.FACTORY_TIERS) {
                        ingredients.add(EntryIngredient.of(EntryStacks.of(MekanismBlocks.getFactory(tier, factoryType.getFactoryType()))));
                    }
                }
            }
        }
        return ingredients.toArray(new EntryIngredient[0]);
    }
}
