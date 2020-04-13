package com.kantilever.bffwebwinkel.advice;

import java.util.Arrays;
import lombok.extern.log4j.Log4j2;
import org.apache.commons.lang3.builder.ReflectionToStringBuilder;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.*;

/**
 * Aspect that handles logging all Controller Endpoint requests.
 */
@Aspect
@Log4j2
public class RestLogger {

    /**
     * Cuts into all classes with @RestController annotation.
     */
    @Pointcut("within(@org.springframework.web.bind.annotation.RestController *)")
    public void controller() {
        // No body required for pointcut
    }

    /**
     * Cuts into all public methods
     */
    @Pointcut("execution(public * *(..))")
    protected void loggingPublicOperation() {
        // No body required for pointcut
    }

    /**
     * Before: All methods within resources annotated with @RestController
     *
     * @param joinPoint point of entry
     */
    @Before("controller() && loggingPublicOperation() && args(..)")
    public void logBefore(JoinPoint joinPoint) {
        log.info("Entering in Method :  " + joinPoint.getSignature().getName());
        log.info("Arguments :  " + Arrays.toString(joinPoint.getArgs()));
    }

    /**
     *  After: All method within resource annotated with @Controller and return a value.
     *
     * @param joinPoint point of entry
     * @param result object returned by  method
     */
    @AfterReturning(value = "controller() && loggingPublicOperation()", returning = "result")
    public void logAfter(JoinPoint joinPoint, Object result) {
        String returnValue = this.getValue(result);
        log.debug("Method Return value : " + returnValue);
    }

    /**
     * After: Any method within resource annotated with @Controller annotation throws an exception
     *
     * @param joinPoint point of entry
     * @param exception exception thrown
     */
    @AfterThrowing(value = "controller() && loggingPublicOperation()", throwing = "exception")
    public void logAfterThrowing(JoinPoint joinPoint, Throwable exception) {
        log.error("An exception has been thrown in " + joinPoint.getSignature().getName() + " ()");
        log.error("Cause : " + exception.getMessage());
    }

    private String getValue(Object result) {
        String returnValue = null;
        if (null != result) {
            if (result.toString().endsWith("@" + Integer.toHexString(result.hashCode()))) {
                returnValue = ReflectionToStringBuilder.toString(result);
            } else {
                returnValue = result.toString();
            }
        }
        return returnValue;
    }
}