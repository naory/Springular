package org.springular.model;

import org.springular.ui.annotation.UiForm;
import org.springular.ui.annotation.UiFormField;
import org.springular.ui.InputType;
import org.springular.ui.vo.Option;

import javax.persistence.Transient;
import java.util.Date;
import java.util.List;
import java.util.Set;

/**
 * Description:  Sample TestForm object (not an @Entity) to simulate all supported form controls
 */
@UiForm(filterApiUrl = "/api/test", addApiUrl = "/api/test", editApiUrl = "/api/test")
public class TestForm {

     @UiFormField(label = "Text Field")
    private String textField;

    @UiFormField(label = "Password Field", editType = InputType.password, filterType = InputType.none)
    private String passwordField;

    @UiFormField(label = "Email", editType = InputType.email)
    private String email;

    @UiFormField(label = "Integer Field", editType = InputType.number)
    private Integer integerField;

    @UiFormField(label = "Active", editType = InputType.checkbox, checkboxText = "This is a checkbox", filterType = InputType.select, optionNames = {"YES", "NO"}, optionValues = {"true", "false"})
    private Boolean checkbox;

    @UiFormField(label = "Single Select", editType = InputType.select, optionsUrl = "/api/test/options1")
    private Set<Option> options1;

    @UiFormField(label = "Multi Select", editType = InputType.multiSelect, filterType = InputType.select, optionsUrl = "/api/test/options2")
    private Set<Option> options2;

    @UiFormField(label = "Multi Select 2", editType = InputType.multiSelect, filterType = InputType.select, optionsUrl = "/api/test/options3")
    private Set<Option> options3;

    @UiFormField(label = "Date Field", editType = InputType.date, filterType = InputType.dateRange)
    private Date date;
    // to filter as a date range provide two transient fields:
    @Transient private Date date_from;
    @Transient private Date date_to;

    @UiFormField(label = "Date Field 2", editType = InputType.date, filterType = InputType.dateRange)
    private Date date2;
    // to filter as a date range provide two transient fields:
    @Transient private Date date2_from;
    @Transient private Date date2_to;



    @UiFormField(label = "Text Area", editType = InputType.textarea, filterType = InputType.text)
    private String textAreaField;

    // list of errors for form validation
    private List<String> errors;

}
