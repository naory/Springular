package org.springular.web.controller;

import org.springular.ui.FormType;
import org.springular.ui.parser.UiMetadataAnnotationParser;
import org.springular.ui.vo.FormMetadata;
import org.springular.model.TestForm;
import org.springular.ui.vo.Option;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.ArrayList;
import java.util.List;

/**
 * Description:  Sample Test Controller for the TestForm model
 */
@Controller
public class TestFormController{

    public @ResponseBody
    FormMetadata getFormConfig(FormType action){
        return UiMetadataAnnotationParser.parseClassAnnotationsForFormType(TestForm.class, action);
    }


    public @ResponseBody
    List<Option> getOptions1(){
        List<Option> list = new ArrayList<Option>();
        list.add(new Option(1, "ONE"));
        list.add(new Option(2, "TWO"));
        list.add(new Option(3, "THREE"));
        list.add(new Option(4, "FOUR"));
        list.add(new Option(5, "FIVE"));
        list.add(new Option(6, "SIX"));
        list.add(new Option(7, "SEVEN"));
        list.add(new Option(8, "EIGHT"));
        list.add(new Option(9, "NINE"));
        return list;
    }

    public @ResponseBody
    List<Option> getOptions2(){
        List<Option> list = new ArrayList<Option>();
        list.add(new Option(1, "ONE"));
        list.add(new Option(2, "TWO"));
        list.add(new Option(3, "THREE"));
        list.add(new Option(4, "FOUR"));
        list.add(new Option(5, "FIVE"));
        list.add(new Option(6, "SIX"));
        list.add(new Option(7, "SEVEN"));
        list.add(new Option(8, "EIGHT"));
        list.add(new Option(9, "NINE"));
        list.add(new Option(10, "TEN"));
        list.add(new Option(11, "ELEVEN"));
        list.add(new Option(12, "TWELVE"));
        return list;
    }

    public @ResponseBody
    List<Option> getOptions3(){
        List<Option> list = new ArrayList<Option>();
        list.add(new Option(1, "Option A"));
        list.add(new Option(2, "Option B"));
        list.add(new Option(3, "Option C"));
        list.add(new Option(4, "Option D"));
        list.add(new Option(5, "Option E"));
        return list;
    }
}
