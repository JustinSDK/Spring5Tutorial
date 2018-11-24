package cc.openhome.proxy;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.util.Arrays;

public class NullableProxy implements Nullable, InvocationHandler {
	private boolean enable; 
    private Object target;
    
    public NullableProxy(Object target) {
    	this.target = target;
    }
    
	@Override
	public void enable() {
		enable = true; 
	}

	@Override
	public void disable() {
		enable = false;		
	}
	
	public boolean isEnable() {
		return enable;
	}

	@Override
	public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
		String methodName = method.getName();
		if(methodName.equals("enable") || methodName.equals("disable")) {
			return method.invoke(this, args);
		}
		
		if(!isEnable() && Arrays.stream(args).anyMatch(arg -> arg == null)) {
			throw new IllegalArgumentException(
				String.format("argument(s) of %s.%s cannot be null", 
					target.getClass().getName(), 
					method.getName()
				)
			);
		}

		return method.invoke(target, args);
	}

}
