package org.springular.ui.vo;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springular.ui.InputType;

import java.util.List;

/**
 * Object that represents the metadata for a single form input field
 * UiFormField annotation and some properties of the javax.persistence.Column annotation.
 */
@JsonSerialize(include= JsonSerialize.Inclusion.NON_NULL)
public class FormFieldMetadata {

    static Logger logger = LoggerFactory.getLogger(FormFieldMetadata.class);

    /** the name of the entity property */
    private String name;

    // the label for the form input */
    private String label;

    // the input type */
    private InputType inputType;

    /** text label for a checkbox input */
    private String checkboxText;


    /** list of static Options for inputTypes: 'select', multiSelect and radio */
    private List<Option> options;

    /** API URL to get a dynamic list of Options for inputTypes: 'select', multiSelect and radio */
    private String optionsUrl;

    /** does this field require a value to submit the form */
    private Boolean required;




    public String getName() {
        return name;
    }

    public void setName(String name){
        this.name = name;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label){
        this.label = label;
    }

    public InputType getInputType() { return inputType; }

    public void setInputType(InputType inputType) {
        this.inputType = inputType;
    }

    public String getCheckboxText() {
        return checkboxText;
    }

    public void setCheckboxText(String checkboxText) {
        this.checkboxText = checkboxText;
    }

    public List<Option> getOptions() {
        return options;
    }

    public void setOptions(List<Option> options) {
        this.options = options;
    }

    public String getOptionsUrl() {
        return optionsUrl;
    }

    public void setOptionsUrl(String optionsUrl) {
        this.optionsUrl = optionsUrl;
    }

    public Boolean getRequired() {
        return required;
    }

    public void setRequired(Boolean required) {
        this.required = required;
    }
}
