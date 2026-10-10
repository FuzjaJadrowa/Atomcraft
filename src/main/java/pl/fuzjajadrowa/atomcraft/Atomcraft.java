package pl.fuzjajadrowa.atomcraft;

import pl.fuzjajadrowa.atomcraft.registry.AtomcraftBlocks;
import pl.fuzjajadrowa.atomcraft.registry.AtomcraftItems;
import pl.fuzjajadrowa.atomcraft.registry.AtomcraftBlockEntities;
import pl.fuzjajadrowa.atomcraft.registry.AtomcraftMenus;
import pl.fuzjajadrowa.atomcraft.registry.AtomcraftRecipeTypes;
import pl.fuzjajadrowa.atomcraft.registry.AtomcraftRecipeSerializers;
import pl.fuzjajadrowa.atomcraft.registry.AtomcraftCreativeTabs;
import pl.fuzjajadrowa.atomcraft.config.AtomcraftConfig;

import net.neoforged.fml.common.Mod;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.config.ModConfig;
import net.neoforged.neoforge.capabilities.RegisterCapabilitiesEvent;
import net.neoforged.neoforge.capabilities.Capabilities;

@Mod(Atomcraft.MODID)
public class Atomcraft {
    public static final String MODID = "atomcraft";

    public Atomcraft(IEventBus bus, ModContainer container) {
        AtomcraftBlocks.register(bus);
        AtomcraftItems.register(bus);
        AtomcraftBlockEntities.register(bus);
        AtomcraftMenus.register(bus);
        AtomcraftRecipeTypes.register(bus);
        AtomcraftRecipeSerializers.register(bus);
        AtomcraftCreativeTabs.register(bus);
        container.registerConfig(ModConfig.Type.SERVER, AtomcraftConfig.SPEC);
        bus.addListener(this::capabilities);
    }

    private void capabilities(RegisterCapabilitiesEvent event) {
        event.registerBlockEntity(Capabilities.EnergyStorage.BLOCK, AtomcraftBlockEntities.MACHINE.get(),
                (machine, side) -> machine.energy());
    }
}