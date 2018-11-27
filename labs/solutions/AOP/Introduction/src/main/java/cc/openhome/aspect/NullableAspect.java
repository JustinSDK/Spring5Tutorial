package cc.openhome.aspect;

import java.util.Arrays;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.DeclareParents;
import org.springframework.stereotype.Component;

@Component
@Aspect
public class NullableAspect {
	@DeclareParents(
	    value = "cc.openhome.model.AccountDAO+",
	    defaultImpl = cc.openhome.aspect.NullableIntroduction.class
	)
	public Nullable nullableIntroduction; // configuration only
	
	@Around("execution(* cc.openhome.model.AccountDAO.*(..)) && this(nullable)")
	public Object around(ProceedingJoinPoint proceedingJoinPoint, Nullable nullable) throws Throwable {
		Object target = proceedingJoinPoint.getTarget();
		Object[] args = proceedingJoinPoint.getArgs();
		String methodName = proceedingJoinPoint.getSignature().getName();
		
		if(!nullable.isEnabled() && Arrays.stream(args).anyMatch(arg -> arg == null)) {
            throw new IllegalArgumentException(
                String.format("argument(s) of %s.%s cannot be null", 
                    target.getClass().getName(), 
                    methodName
                )
            );
        }

		return proceedingJoinPoint.proceed();
	}
}
