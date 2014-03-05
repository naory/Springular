package org.springular.service;

import net.sf.corn.cps.CPScanner;
import net.sf.corn.cps.ClassFilter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springular.ui.FormType;
import org.springular.ui.parser.UiMetadataAnnotationParser;
import org.springular.ui.annotation.UiForm;
import org.springular.ui.vo.FormMetadata;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Scans all classes in the provided paths for @FormFieldMetadata annotations and creates a cache
 * of the UI configuration FormMetadata objects keyed on class names.
 */
public class UiMetadataRepository {

    Logger logger = LoggerFactory.getLogger(UiMetadataRepository.class);

    private List<String> scanPackages;

    public void setScanPackages(List<String> scanPackages) {
        this.scanPackages = scanPackages;
    }

    private Map<String, Map<FormType, FormMetadata>> configMap = new HashMap<String, Map<FormType, FormMetadata>>();


    public void init(){
        logger.info("Scanning for classes annotated with @UiForm...");
        for (String pkg: scanPackages) {
            List<Class<?>> classes = CPScanner.scanClasses(new ClassFilter().packageName(pkg).annotation(UiForm.class));
            for(Class c: classes){
                logger.info("Adding class: " + c.getCanonicalName());
                configMap.put(c.getCanonicalName(), getUiConfigMap(c));

            }
        }
    }

    public FormMetadata getConfig(String className, FormType type){
        Map<FormType, FormMetadata> m = configMap.get(className);
        if(m != null){
            return m.get(type);
        }
        else throw new RuntimeException("Ui Config not found for Class: "+ className);
    }


    private Map<FormType, FormMetadata> getUiConfigMap(Class c) {
        Map config = new HashMap();
        for(FormType val: FormType.values()){
            config.put(val, UiMetadataAnnotationParser.parseClassAnnotationsForFormType(c, val));
        }
        return config;
    }


}
