package org.springular.web.controller;

import org.springular.ui.FormType;
import org.springular.ui.parser.UiMetadataAnnotationParser;
import org.springular.ui.vo.FormMetadata;
import org.springular.model.QUser;
import org.springular.model.User;
import org.springular.repository.UserRepository;
import com.mysema.query.BooleanBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.propertyeditors.CustomDateEditor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;

import javax.inject.Inject;
import javax.servlet.ServletException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Controller for the User entity
 */

@Controller
public class UserController {

	Logger logger = LoggerFactory.getLogger(UserController.class);

	@Inject
	UserRepository repo;

    public @ResponseBody Page<User> findUsers(@RequestBody User u,
                                              @RequestParam(required = false) String sort, // field name
                                              @RequestParam(required = false) String direction,// ASC or
                                              @RequestParam(required = true) int limit,
                                              @RequestParam(required = true) int page){


        QUser user = QUser.user;
		BooleanBuilder p = new BooleanBuilder();

        String firstName = u.getFirstName();
		if(firstName != null){
			p.and(user.firstName.startsWithIgnoreCase(firstName));
		}

        String lastName = u.getLastName();
        if(lastName != null){
			p.and(user.lastName.startsWithIgnoreCase(lastName));
		}

        String email = u.getEmail();
        if(email != null){
            p.and(user.email.startsWithIgnoreCase(email));
        }

        Boolean active = u.getActive();
        if(active != null){
            p.and(user.active.eq(active));
        }

        if(u.getRoles() != null){
            p.and(user.roles.any().in(u.getRoles()));
            logger.debug(u.getRoles().toString());
        }

        Date updateDate_from = u.getUpdateDate_from();
        Date updateDate_to = u.getUpdateDate_to();
        if(updateDate_from != null && updateDate_to != null){
            p.and(user.updateDate.between(updateDate_from, updateDate_to));
        }

        //ORDER BY:
        Sort s = null;
        if (sort != null && direction != null){
            s = new Sort(Sort.Direction.fromString(direction), sort);
        }

		logger.info("Executing Predicate: "+p.toString()+" sort="+ s +" page="+page+"; pageSize="+limit);

		// app will be sending page numbers starting at 1 but spring data is using zero based indexing
		return repo.findAll(p, new PageRequest(page-1, limit, s));
	}



	public @ResponseBody User createUser(@RequestBody User user){
		try{
            repo.save(user);
        }
        catch(Exception e){
            user.getErrors().put("firstName", e.getMessage());
        }

		return user;
	}


	public @ResponseBody User updateUser(@RequestBody User user){
		
		logger.info("Updating an user with information "+user.toString());
		
		User existingUserInfo = repo.findOne(user.getId());
		
		if(existingUserInfo==null)
		{
			throw new RuntimeException("Update failed for User [id="+user.getId()+"]: User Not Found");
		}

		return repo.save(user);
	}


    public @ResponseBody
    FormMetadata getFormConfig(FormType action){
        return UiMetadataAnnotationParser.parseClassAnnotationsForFormType(User.class, action);
    }

    // TODO make binders common to all controllers
    // Parse Date fields in the default Angular format [ISO-8601] ('yyyy-MM-ddTHH:mm:ss.SSSZ')
    @InitBinder
    protected void initBinder( WebDataBinder binder) throws ServletException {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSZ");
        dateFormat.setLenient(false);
        binder.registerCustomEditor(Date.class, new CustomDateEditor(dateFormat, false));
    }
}
