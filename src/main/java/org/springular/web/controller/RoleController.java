package org.springular.web.controller;

import org.springular.model.Role;
import org.springular.repository.RoleRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.inject.Inject;
import java.util.List;

/**
 * Controller for Roles
 */
@Controller
public class RoleController {

    Logger logger = LoggerFactory.getLogger(UserController.class);

    @Inject
    RoleRepository repo;

    public @ResponseBody List<Role> getRoles()
    {
        return repo.findAll();
    }
}
