package net.jsharren.dreamt_dinner.utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import net.minecraft.util.Identifier;

public class NameUitl {
    public static final String MOD_ID = "dreamt_dinner";

    public static Identifier toID(String... names) {
        return Identifier.of(MOD_ID, String.join("/", names));
    }

    public static String toPath(String... names) {
        return String.join("/", MOD_ID, String.join("/", names));
    }

    public static String toKey(String category, String... names) {
        return String.join(".", category, MOD_ID, String.join(".", names));
    }

    public static Logger getRootLogger() {
        return LoggerFactory.getLogger(MOD_ID);
    }
}
