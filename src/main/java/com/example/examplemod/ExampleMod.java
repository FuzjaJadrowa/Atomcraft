package com.example.examplemod;

import net.neoforged.fml.common.Mod;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Mod(ExampleMod.MODID)
public class ExampleMod {
    public static final String MODID = "examplemod";
    private static final Logger LOGGER = LoggerFactory.getLogger(ExampleMod.class);

    public ExampleMod() {
        LOGGER.info("Hello NeoForge world!");
    }
}
