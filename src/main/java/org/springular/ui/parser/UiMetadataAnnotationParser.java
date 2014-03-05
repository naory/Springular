package org.springular.ui.parser;

import org.springular.ui.FormType;
import org.springular.ui.InputType;
import org.springular.ui.annotation.UiForm;
import org.springular.ui.annotation.UiFormField;
import org.springular.ui.vo.FormFieldMetadata;
import org.springular.ui.vo.FormMetadata;
import org.springular.ui.vo.Option;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

/**
 * Parser for classes annotated with @UiForm and @UiFormField annotations
 * returns a FormMetadata object that represents the form metadata for the class and form type
 */
public class UiMetadataAnnotationParser {

    public static FormMetadata parseClassAnnotationsForFormType(Class<?> clazz, FormType formType){
        FormMetadata formMetadata = new FormMetadata();
        UiForm formAnnotation = clazz.getAnnotation(UiForm.class);
        formMetadata.setEntity(clazz.getSimpleName());
        formMetadata.setActionUrl(getFormActionUrl(formAnnotation, formType));
        formMetadata.setFields(getFieldsMetadata(clazz, formType));
        return formMetadata;
    }


    private static List<FormFieldMetadata> getFieldsMetadata(Class<?> clazz, FormType formType) {
        List<FormFieldMetadata> fieldMetadataList = new ArrayList<FormFieldMetadata>();
        Field[] fields = clazz.getDeclaredFields();
        for (Field field : fields) {
            if (field.isAnnotationPresent(UiFormField.class)) {
               fieldMetadataList.add(parseFieldMetadata(field, formType));
            }
        }
        return fieldMetadataList;
    }


    private static FormFieldMetadata parseFieldMetadata(Field field, FormType formType) {
        FormFieldMetadata fieldMetadata = new FormFieldMetadata();
        UiFormField fieldAnnotation = field.getAnnotation(UiFormField.class);
        fieldMetadata.setName(field.getName());
        fieldMetadata.setLabel(fieldAnnotation.label());
        fieldMetadata.setRequired(fieldAnnotation.required());
        fieldMetadata.setInputType(getInputType(fieldAnnotation, formType));
        fieldMetadata.setCheckboxText(fieldAnnotation.checkboxText());
        fieldMetadata.setOptionsUrl(fieldAnnotation.optionsUrl());
        fieldMetadata.setOptions(getFieldOptions(fieldAnnotation));
        return fieldMetadata;
    }


    private static InputType getInputType(UiFormField fieldAnnotation, FormType formType) {
        if(formType == FormType.FILTER && fieldAnnotation.filterType() != InputType.none) {
            return fieldAnnotation.filterType();
        }
        else {
            return fieldAnnotation.editType();
        }
    }


    private static List<Option> getFieldOptions(UiFormField fieldAnnotation) {
        ArrayList<Option> options = null;
        if(fieldAnnotation.optionNames().length > 0){
            String[] optionNames = fieldAnnotation.optionNames();
            String[] optionsIds = fieldAnnotation.optionValues();
            options = new ArrayList<>(optionNames.length);
            for (int i = 0; i < optionNames.length; i++) {
                String txt = optionNames[i];
                String val = optionsIds[i];
                options.add(new Option(val, txt));
            }
        }
        return options;
    }


    private static String getFormActionUrl(UiForm uiForm, FormType formType){
        if(formType == FormType.FILTER){
            return uiForm.filterApiUrl();
        }
        else if(formType == FormType.ADD){
            return uiForm.addApiUrl();
        }
        else if (formType == FormType.EDIT){
            return uiForm.editApiUrl();
        }
        else return null;
    }
}
