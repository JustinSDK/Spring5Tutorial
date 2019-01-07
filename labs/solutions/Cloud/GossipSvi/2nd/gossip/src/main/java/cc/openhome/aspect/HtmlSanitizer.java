package cc.openhome.aspect;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.owasp.html.PolicyFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
@Aspect
public class HtmlSanitizer {
	@Autowired
	private PolicyFactory policy;
	
    @Around("execution(* cc.openhome.controller.MemberController.newMessage(..))")
    public Object around(ProceedingJoinPoint proceedingJoinPoint) throws Throwable {
        Object[] args = proceedingJoinPoint.getArgs();
        args[0] = policy.sanitize(args[0].toString());
        return proceedingJoinPoint.proceed(args);
    }
}