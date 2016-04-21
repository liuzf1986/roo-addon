package com.yaago.roo.addon.shiro;

import org.springframework.roo.model.JavaType;
import org.springframework.roo.project.Feature;
import org.springframework.roo.project.maven.Pom;

/**
 * Interface of operations this add-on offers. Typically used by a command type or an external add-on.
 *
 * @since 1.1
 */
public interface ShiroOperations extends Feature  {

    String FEATURE_NAME_YAGGO_SHIRO = "yaago-shiro";
    /**
     * Indicate setup should be available
     *
     * @return true if it should be available, otherwise false
     */
    boolean isSetupAvailable();

    /**
     * Setup all add-on artifacts (dependencies in this case)
     */
    void setup(Pom module);
}