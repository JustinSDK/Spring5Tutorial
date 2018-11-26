package cc.openhome.aspect;

import java.util.Arrays;
import java.util.logging.Logger;

public aspect LoggingAspect {
	before() : execution(* cc.openhome.model.Hello.*(..)) {
		Object target = thisJoinPoint.getTarget();
		String methodName = thisJoinPoint.getSignature().getName();
		Object[] args = thisJoinPoint.getArgs();
		Logger.getLogger(target.getClass().getName()).info(String.format("%s.%s(%s)",
                target.getClass().getName(), methodName, Arrays.toString(args)));
    }
}
