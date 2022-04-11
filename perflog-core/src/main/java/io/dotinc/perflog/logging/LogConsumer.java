package io.dotinc.perflog.logging;

import org.slf4j.Logger;

/**
 * @author vladbulimac on 11.04.2022.
 */

@FunctionalInterface
public interface LogConsumer {
    void apply(Logger logger, String fmt, Object... args);
}
