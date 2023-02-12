package net.jsharren.dreamt_dinner;

import org.slf4j.Logger;

import net.fabricmc.api.ModInitializer;
import net.jsharren.dreamt_dinner.resources.DtDResource;
import net.jsharren.dreamt_dinner.utils.NameUitl;

public class DreamtDinner implements ModInitializer {
    public static final Logger LOGGER = NameUitl.getRootLogger();
    public static final DtDResource RESOURCE = DtDResource.createResource();

    @Override
    public void onInitialize() {
        LOGGER.info("DreamtDinner Initialized.");
        RESOURCE.itemPool.register();
        RESOURCE.blockPool.register();
        RESOURCE.blockEntityPool.register();
    }
    
}
