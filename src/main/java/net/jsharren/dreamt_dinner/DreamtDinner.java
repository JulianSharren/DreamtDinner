package net.jsharren.dreamt_dinner;

import java.util.HashSet;
import java.util.Set;
import java.util.stream.Stream;

import org.slf4j.Logger;

import net.fabricmc.api.ModInitializer;
import net.fabricmc.loader.api.FabricLoader;
import net.jsharren.dreamt_dinner.resources.DtDResource;
import net.jsharren.dreamt_dinner.utils.NameUtil;

public class DreamtDinner implements ModInitializer {
    public static final Logger LOGGER = NameUtil.getRootLogger();
    public static final DtDResource RESOURCE = DtDResource.createResource();
    private static final Set<String> LOADED_DEPENDENCIES = new HashSet<String>();

    @Override
    public void onInitialize() {
        LOGGER.info("DreamtDinner Initialized.");
        initDependencies();
        RESOURCE.itemPool.register();
        RESOURCE.blockPool.register();
        RESOURCE.blockEntityPool.register();
    }

    public void initDependencies() {
        Stream<String> dependencies = Stream.of("patchouli");
        FabricLoader loader = FabricLoader.getInstance();
        dependencies.filter(loader::isModLoaded).forEach(LOADED_DEPENDENCIES::add);
        if (LOGGER.isInfoEnabled()) {
            String deps = "(NONE)";
            if (LOADED_DEPENDENCIES.size() > 0) {
                deps = String.join(", ", LOADED_DEPENDENCIES);
            }
            LOGGER.info(String.format("Loaded dependencies: %s", deps));
        }
    }

    public static boolean isLoaded(String dep) {
        return LOADED_DEPENDENCIES.contains(dep);
    }
}
