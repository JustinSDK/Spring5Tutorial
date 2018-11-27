package cc.openhome.aspect;

public class NullableIntroduction implements Nullable {
	private boolean enabled; 
	
    @Override
    public void enable() {
        enabled = true; 
    }

    @Override
    public void disable() {
        enabled = false;        
    }

    public boolean isEnabled() {
        return enabled;
    }
}
