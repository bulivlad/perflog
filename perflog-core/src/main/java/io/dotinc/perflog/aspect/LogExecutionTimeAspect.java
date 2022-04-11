package io.dotinc.perflog.aspect;

import io.dotinc.perflog.annotation.LogExecutionTime;
import io.dotinc.perflog.logging.PerfLogger;
import io.dotinc.perflog.util.ExecutionStopWatch;
import io.dotinc.perflog.util.LogConsumerUtil;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.StopWatch;

import java.util.Arrays;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * @author vladbulimac on 11.04.2022.
 */

@Aspect
public class LogExecutionTimeAspect {

    @Around("@annotation(io.dotinc.perflog.annotation.LogExecutionTime)")
    public Object loggable(ProceedingJoinPoint proceedingJoinPoint) throws Throwable {
        MethodSignature methodSignature = (MethodSignature) proceedingJoinPoint.getSignature();

        String classQualifiedName = getPathAbbreviation(methodSignature.getDeclaringType().getCanonicalName(), methodSignature);
        String methodName = methodSignature.getName();
        String parameters = Arrays.stream(methodSignature.getParameterTypes())
                .map(Class::getName)
                .map(e -> getPathAbbreviation(e, methodSignature))
                .collect(Collectors.joining(", "));

        StringBuilder sb = new StringBuilder(classQualifiedName);
        sb.append(".");
        sb.append(methodName);
        sb.append("(");
        sb.append(parameters);
        sb.append(")");

        StopWatch stopWatch = new ExecutionStopWatch(sb.toString());
        stopWatch.start(sb.toString());
        Object result = proceedingJoinPoint.proceed();
        stopWatch.stop();

        log(methodSignature, stopWatch);

        return result;
    }

    private String getPathAbbreviation(String path, MethodSignature methodSignature) {
        boolean isMaskPackagePath = methodSignature.getMethod().getAnnotation(LogExecutionTime.class).maskPackagePath();

        if (isMaskPackagePath) {
            String[] pathComponents = path.split("\\.");
            if (pathComponents.length == 0) {
                return path;
            }

            return Stream.of(pathComponents)
                    .limit(pathComponents.length - 2)
                    .map(e -> e.substring(0, 1))
                    .collect(Collectors.joining("."))
                    .concat(".")
                    .concat(pathComponents[pathComponents.length - 2]) //last bit of package
                    .concat(".")
                    .concat(pathComponents[pathComponents.length - 1]); //class name
        }

        return path;
    }

    private void log(MethodSignature methodSignature, StopWatch stopWatch) {
        String classQualifiedName = methodSignature.getDeclaringType().getCanonicalName();
        String declaredLoggingLevel = methodSignature.getMethod().getAnnotation(LogExecutionTime.class).loggingLevel();
        boolean isPrettyPrint = methodSignature.getMethod().getAnnotation(LogExecutionTime.class).detailedLogging();

        PerfLogger perfLogger = new PerfLogger(LogConsumerUtil.getConsumer(declaredLoggingLevel));
        Logger logger = LoggerFactory.getLogger(classQualifiedName);

        if (isPrettyPrint) {
            perfLogger.log(logger, stopWatch.prettyPrint());
        } else {
            perfLogger.log(logger, stopWatch.shortSummary());
        }
    }
}
