package org.springular.web.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springular.ui.FormType;
import org.springular.ui.vo.FormMetadata;
import org.springular.service.UiMetadataRepository;

import javax.inject.Inject;

/**
 * Controller for the UiMetadataRepository
 */

@Controller
public class UiConfigController {

    @Inject
    UiMetadataRepository uiMetadataRepository;

    public @ResponseBody
    FormMetadata getFormConfig(String clazz, FormType action){
        return uiMetadataRepository.getConfig(clazz, action);
    }
}
