package com.myproject.common;

import java.util.logging.Level;
import java.util.logging.Logger;

public class LoggerUtil {
    private static final Logger logger = Logger.getLogger("MyAppLogger");

    static {
        logger.setLevel(Level.ALL);
    }

    public static void info(String msg) {
        logger.log(Level.INFO, msg);
    }

    public static void warn(String msg) {
        logger.log(Level.WARNING, msg);
    }

    public static void error(String msg) {
        logger.log(Level.SEVERE, msg);
    }
}
