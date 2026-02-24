package bet.astral.multiversion;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@Retention(RetentionPolicy.RUNTIME)
public @interface Version {
	String internalVersion();
	String legacyVersion() default "";
	String miniumVersion();
	String maximumVersion();
	boolean legacy() default false;
}
