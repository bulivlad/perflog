package io.dotinc.perflog.util;

import io.dotinc.perflog.aspect.LogExecutionTimeAspect;
import io.dotinc.perflog.logging.LogConsumer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.event.Level;

/**
 * @author vladbulimac on 11.04.2022.
 */

public final class LogConsumerUtil {
    private static final Logger logger = LoggerFactory.getLogger(LogExecutionTimeAspect.class);
    private LogConsumerUtil() {}

    public static LogConsumer getConsumer(String level) {
        try {
            Level loggingLevel = Level.valueOf(level);
            if (loggingLevel == Level.ERROR) {
                return Logger::error;
            }
            if (loggingLevel == Level.WARN) {
                return Logger::warn;
            }
            if (loggingLevel == Level.INFO) {
                return Logger::info;
            }
            if (loggingLevel == Level.DEBUG) {
                return Logger::debug;
            }
            if (loggingLevel == Level.TRACE) {
                return Logger::trace;
            }
        } catch (Exception ex) {
            logger.error("Level {} is not a valid org.slf4j.event.Level element. Continue logging on DEBUG level", level);
        }
        return Logger::debug;
    }
}
