package com.yaago.roo.addon.shiro;

import java.util.ArrayList;
import java.util.List;
import java.util.jar.Attributes;
import java.util.logging.Logger;

import org.apache.commons.lang3.Validate;
import org.apache.felix.scr.annotations.Component;
import org.apache.felix.scr.annotations.Reference;
import org.apache.felix.scr.annotations.Service;
import org.osgi.framework.BundleContext;
import org.osgi.service.component.ComponentContext;
import org.springframework.roo.addon.web.mvc.jsp.tiles.TilesOperations;
import org.springframework.roo.classpath.TypeLocationService;
import org.springframework.roo.classpath.TypeManagementService;
import org.springframework.roo.classpath.details.MemberFindingUtils;
import org.springframework.roo.classpath.details.ClassOrInterfaceTypeDetails;
import org.springframework.roo.classpath.details.ClassOrInterfaceTypeDetailsBuilder;
import org.springframework.roo.classpath.details.annotations.AnnotationMetadataBuilder;
import org.springframework.roo.metadata.MetadataService;
import org.springframework.roo.model.JavaType;
import org.springframework.roo.process.manager.FileManager;
import org.springframework.roo.project.*;
import org.springframework.roo.project.maven.Pom;
import org.springframework.roo.support.logging.HandlerUtils;
import org.springframework.roo.support.util.WebXmlUtils;
import org.springframework.roo.support.util.XmlUtils;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

import javax.annotation.*;

/**
 * Implementation of operations this add-on offers.
 *
 * @since 1.1
 */
@Component // Use these Apache Felix annotations to register your commands class in the Roo container
@Service
public class ShiroOperationsImpl implements ShiroOperations {
    private static final String WEB_XML = "WEB-INF/web.xml";
    protected final static Logger LOGGER = HandlerUtils.getLogger(ShiroOperationsImpl.class);

    // ------------ ROO platform support ----------------
    BundleContext context;

    @Reference
    FileManager fileManager;

    @Reference
    PathResolver pathResolver;

    @Reference
    ProjectOperations projectOperations;

    @Reference
    TilesOperations tilesOperations;
    // ------------ ROO platform support ----------------

    protected void activate(final ComponentContext context) {
        this.context = context.getBundleContext();
    }

    /**
     * 更新maven依赖
     * @param moduleName
     */
    private void updatePom(String moduleName) {
        final Element configuration = XmlUtils.getConfiguration(getClass());

        // Add properties
        List<Element> properties = XmlUtils.findElements(
                "/configuration/spring-shiro/properties/*", configuration);
        for (Element property : properties) {
            projectOperations.addProperty(moduleName, new Property(property));
        }

        // Add dependence
        List<Element> shiroDependencies = XmlUtils.findElements(
                "/configuration/spring-shiro/dependencies/*", configuration);
        for (Element dependencyElement : shiroDependencies) {
            projectOperations.addDependency(moduleName, new Dependency(dependencyElement));
        }
    }

    private void updateWebXml(String moduleName) {
        String webXmlPath = pathResolver.getFocusedIdentifier(Path.SRC_MAIN_WEBAPP, WEB_XML);
        Document document = XmlUtils.readXml(fileManager.getInputStream(webXmlPath));
        WebXmlUtils.addFilter(FILTER_NAME_YAAGO_SHIRO, FILTER_CLASS_YAAGO_SHIRO, "/*", document, null);
    }


    @Override
    public boolean isSetupAvailable() {
        return projectOperations.isProjectAvailable(projectOperations.getFocusedModuleName())
                && fileManager.exists(pathResolver.getFocusedIdentifier(Path.SRC_MAIN_WEBAPP, "WEB-INF/web.xml"))
                && !projectOperations.isFeatureInstalledInFocusedModule(FEATURE_NAME_YAGGO_SHIRO);
    }

    @Override
    public void setup() {
        String moduleName = projectOperations.getFocusedModuleName();
        updatePom(moduleName);
        updateWebXml(moduleName);
    }

    @Override
    public String getName() {
        return FEATURE_NAME_YAGGO_SHIRO;
    }

    @Override
    public boolean isInstalledInModule(String moduleName) {
        final Pom pom = projectOperations.getPomFromModuleName(moduleName);
        if (pom == null) {
            return false;
        }
        for(final Dependency dependency : pom.getDependencies()) {
            if("shiro-web".equals(dependency.getArtifactId())) {
                return true;
            }
        }
        return false;
    }
}