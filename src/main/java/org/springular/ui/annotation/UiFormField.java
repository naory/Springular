package org.springular.ui.annotation;

import org.springular.ui.InputType;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Annotation for UI Form field metadata
 */

@Target({ ElementType.FIELD })
@Retention(RetentionPolicy.RUNTIME)
public @interface UiFormField {

    /** the label for the form control */
    String label() default "label";

    /** inline label used for checkboxes */
    String checkboxText() default "";

    /** the input type for Add and Edit forms */
    InputType editType() default InputType.text;

    /** if provided, the input type for the Filter form */
    InputType filterType() default InputType.none;

    /** is this field required for successful submission of the form */
    boolean required() default false;

    /** API URL to get dynamic option names and values (for select, multi-select, radio buttons) */
    String optionsUrl() default "";

    /** alternative to *optionsUrl* - provide a static arrays of options values and option names */
    String[] optionValues() default {};
    String[] optionNames() default {};


}
