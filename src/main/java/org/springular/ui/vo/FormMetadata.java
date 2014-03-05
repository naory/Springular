package org.springular.ui.vo;

import java.util.List;

/**
 * Value object for parsed UI configuration (@FormMetadata and
 */
public class FormMetadata {

    /** the entity name */
    String entity;

    /** the API URL for the form's action (submit) */
    String actionUrl;

    /** */
    List<FormFieldMetadata> fields;


    public String getEntity() {




        return entity;
    }

    public void setEntity(String entity){
        this.entity = entity;
    }

    public String getActionUrl() { return actionUrl; }

    public void setActionUrl(String actionUrl){
        this.actionUrl = actionUrl;
    }

    public List<FormFieldMetadata> getFields() {
        return fields;
    }

    public void setFields(List<FormFieldMetadata> fields){
        this.fields = fields;
    }


}
