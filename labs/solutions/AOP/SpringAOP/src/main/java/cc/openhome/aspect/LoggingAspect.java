package cc.openhome.aspect;

import java.util.Arrays;
import java.util.logging.Logger;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.stereotype.Component;

@Component
@Aspect
public class LoggingAspect {
	@Before("execution(* cc.openhome.model.AccountDAO.*(..))")
	public void before(JoinPoint joinPoint) {	
		Object target = joinPoint.getTarget();
		String methodName = joinPoint.getSignature().getName();
		Object[] args = joinPoint.getArgs();
		Logger.getLogger(target.getClass().getName())
		      .info(String.format("%s.%s(%s)",
                target.getClass().getName(), methodName, Arrays.toString(args)));
	}
}
