package io.dotinc.perflog.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @author vladbulimac on 11.04.2022.
 */

@Target({ElementType.METHOD, ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
public @interface LogExecutionTime {

    String loggingLevel() default "INFO";
    boolean detailedLogging() default false;
    boolean maskPackagePath() default true;

}
