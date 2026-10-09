package pl.fuzjajadrowa.atomcraft;

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
        ModContent.register(bus);
        container.registerConfig(ModConfig.Type.SERVER, AtomcraftConfig.SPEC);
        bus.addListener(this::capabilities);
    }

    private void capabilities(RegisterCapabilitiesEvent event) {
        event.registerBlockEntity(Capabilities.EnergyStorage.BLOCK, ModContent.MACHINE_ENTITY.get(),
                (machine, side) -> machine.energy());
    }
}