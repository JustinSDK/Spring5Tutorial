package cc.openhome.proxy;


import java.lang.reflect.InvocationHandler;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.logging.Logger;

public class LoggingProxy implements InvocationHandler {    
    private Object target;
	private Logger logger;

	public LoggingProxy(Object target) {
        this.target = target;
        logger = Logger.getLogger(target.getClass().getName());
    }

    public Object invoke(Object proxy, Method method, 
                         Object[] args) throws Throwable { 
        Object result = null; 
        try { 
        	logger.info(String.format("%s.%s(%s)",
    				target.getClass().getName(), method.getName(), Arrays.toString(args)));
            result = method.invoke(target, args);
        } catch (IllegalAccessException | IllegalArgumentException | 
                InvocationTargetException e){ 
            throw new RuntimeException(e);
        }
        return result; 
    } 
}

