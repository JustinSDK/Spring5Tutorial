package cc.openhome.aspect;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.owasp.html.PolicyFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.MultiValueMap;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.server.ServerWebExchangeDecorator;

import reactor.core.publisher.Mono;

@Component
@Aspect
public class HtmlSanitizer {
	@Autowired
	private PolicyFactory policy;
	
    @Around("execution(* cc.openhome.controller.MemberController.newMessage(..))")
    public Object around(ProceedingJoinPoint proceedingJoinPoint) throws Throwable {
        Object[] args = proceedingJoinPoint.getArgs();
        ServerWebExchange decorated = new SanitizedServerWebExchange((ServerWebExchange) args[0]);
        args[0] = decorated;
        return proceedingJoinPoint.proceed(args);
    }
    
    class SanitizedServerWebExchange extends ServerWebExchangeDecorator {
    	public SanitizedServerWebExchange(ServerWebExchange origin) {
    		super(origin);
    	}

    	@Override
    	public Mono<MultiValueMap<String, String>> getFormData() {
    		return super.getFormData()
    				     .map(multiValueMap -> {
    							multiValueMap.computeIfPresent("blabla", (param, values) -> {
    			   		    		 values.set(0, policy.sanitize(values.get(0)));
    			   		    		 return values;
    		   		    	    });
    							return multiValueMap;
    				     });
    	}	
    }
}

