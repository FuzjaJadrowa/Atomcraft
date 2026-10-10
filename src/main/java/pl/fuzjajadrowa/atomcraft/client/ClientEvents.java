package pl.fuzjajadrowa.atomcraft.client;

import pl.fuzjajadrowa.atomcraft.registry.AtomcraftMenus;
import pl.fuzjajadrowa.atomcraft.client.screen.MachineScreen;
import pl.fuzjajadrowa.atomcraft.Atomcraft;

import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.RegisterMenuScreensEvent;

@EventBusSubscriber(modid = Atomcraft.MODID, value = Dist.CLIENT)
public final class ClientEvents {
    @SubscribeEvent public static void screens(RegisterMenuScreensEvent event) { event.register(AtomcraftMenus.MACHINE.get(), MachineScreen::new); }
}