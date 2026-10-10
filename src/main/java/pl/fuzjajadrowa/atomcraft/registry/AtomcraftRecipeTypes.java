package pl.fuzjajadrowa.atomcraft.registry;

import pl.fuzjajadrowa.atomcraft.recipe.MachineRecipe;
import pl.fuzjajadrowa.atomcraft.Atomcraft;

import java.util.function.Supplier;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.item.crafting.RecipeType;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredRegister;

public final class AtomcraftRecipeTypes {
    public static final DeferredRegister<RecipeType<?>> RECIPE_TYPES = DeferredRegister.create(Registries.RECIPE_TYPE, Atomcraft.MODID);

    public static final Supplier<RecipeType<MachineRecipe>> MACHINE = RECIPE_TYPES.register("machine", () -> new RecipeType<>() {
        @Override public String toString() { return "atomcraft:machine"; }
    });

    public static void register(IEventBus bus) {
        RECIPE_TYPES.register(bus);
    }

    private AtomcraftRecipeTypes() {}
}