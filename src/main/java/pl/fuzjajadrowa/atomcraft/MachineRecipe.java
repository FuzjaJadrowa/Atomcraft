package pl.fuzjajadrowa.atomcraft;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.HolderLookup;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.*;
import net.minecraft.world.level.Level;

public record MachineRecipe(String machine, Ingredient ingredient, ItemStack result, int time) implements Recipe<SingleRecipeInput> {
    @Override public boolean matches(SingleRecipeInput input, Level level) { return ingredient.test(input.item()); }
    @Override public ItemStack assemble(SingleRecipeInput input, HolderLookup.Provider registries) { return result.copy(); }
    @Override public boolean canCraftInDimensions(int width, int height) { return width * height >= 1; }
    @Override public ItemStack getResultItem(HolderLookup.Provider registries) { return result; }
    @Override public RecipeSerializer<?> getSerializer() { return ModContent.MACHINE_SERIALIZER.get(); }
    @Override public RecipeType<?> getType() { return ModContent.MACHINE_RECIPE.get(); }
    @Override public boolean isSpecial() { return true; }

    public static final class Serializer implements RecipeSerializer<MachineRecipe> {
        private static final MapCodec<MachineRecipe> CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(
                com.mojang.serialization.Codec.STRING.validate(value -> value.equals("crusher") || value.equals("electric_furnace")
                        ? com.mojang.serialization.DataResult.success(value) : com.mojang.serialization.DataResult.error(() -> "Unknown machine: " + value))
                        .fieldOf("machine").forGetter(MachineRecipe::machine),
                Ingredient.CODEC_NONEMPTY.fieldOf("ingredient").forGetter(MachineRecipe::ingredient),
                ItemStack.CODEC.fieldOf("result").forGetter(MachineRecipe::result),
                com.mojang.serialization.Codec.intRange(0, 10000000).optionalFieldOf("time", 0).forGetter(MachineRecipe::time)
        ).apply(instance, MachineRecipe::new));
        private static final StreamCodec<RegistryFriendlyByteBuf, MachineRecipe> STREAM = new StreamCodec<>() {
            @Override public MachineRecipe decode(RegistryFriendlyByteBuf buffer) {
                return new MachineRecipe(buffer.readUtf(), Ingredient.CONTENTS_STREAM_CODEC.decode(buffer), ItemStack.STREAM_CODEC.decode(buffer), buffer.readVarInt());
            }
            @Override public void encode(RegistryFriendlyByteBuf buffer, MachineRecipe recipe) {
                buffer.writeUtf(recipe.machine); Ingredient.CONTENTS_STREAM_CODEC.encode(buffer, recipe.ingredient);
                ItemStack.STREAM_CODEC.encode(buffer, recipe.result); buffer.writeVarInt(recipe.time);
            }
        };
        @Override public MapCodec<MachineRecipe> codec() { return CODEC; }
        @Override public StreamCodec<RegistryFriendlyByteBuf, MachineRecipe> streamCodec() { return STREAM; }
    }
}