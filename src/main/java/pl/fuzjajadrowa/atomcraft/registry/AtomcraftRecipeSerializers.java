package pl.fuzjajadrowa.atomcraft.registry;

import pl.fuzjajadrowa.atomcraft.recipe.MachineRecipe;
import pl.fuzjajadrowa.atomcraft.Atomcraft;

import java.util.function.Supplier;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredRegister;

public final class AtomcraftRecipeSerializers {
    public static final DeferredRegister<RecipeSerializer<?>> RECIPE_SERIALIZERS = DeferredRegister.create(Registries.RECIPE_SERIALIZER, Atomcraft.MODID);

    public static final Supplier<RecipeSerializer<MachineRecipe>> MACHINE = RECIPE_SERIALIZERS.register("machine", MachineRecipe.Serializer::new);

    public static void register(IEventBus bus) {
        RECIPE_SERIALIZERS.register(bus);
    }

    private AtomcraftRecipeSerializers() {}
}