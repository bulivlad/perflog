package io.dotinc.perflog.logging;

import org.slf4j.Logger;

/**
 * @author vladbulimac on 11.04.2022.
 */

public class PerfLogger {
    private final LogConsumer logConsumer;

    public PerfLogger(LogConsumer logConsumer) {
        this.logConsumer = logConsumer;
    }

    public void log(Logger logger, String fmt, Object... arguments) {
        logConsumer.apply(logger, fmt, arguments);
    }
}
