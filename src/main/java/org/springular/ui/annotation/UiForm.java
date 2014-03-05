package org.springular.ui.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Class-level annotation for entity classes that map to UI forms.
 * Require values for the API URLs to filter, add, and edit entities
 */
@Target({ ElementType.TYPE })
@Retention(RetentionPolicy.RUNTIME)
public @interface UiForm {

    /** API URL to search (filter) entities */
    String filterApiUrl();

    /** API URL to Add an entity */
    String addApiUrl();

    /** API URL to Edit (update) an entity */
    String editApiUrl();
}
