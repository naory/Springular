package org.springular;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springular.model.Role;
import org.springular.model.User;
import org.springular.repository.RoleRepository;
import org.springular.repository.UserRepository;

import javax.annotation.PostConstruct;
import javax.inject.Inject;
import javax.inject.Named;
import java.util.HashSet;
import java.util.Set;

/**
 * Created with IntelliJ IDEA.
 * User: Naor Yuval
 * Date: 11/14/13
 * Loads some values to the DB
 */
//@Configuration
public class DatabaseInitializer {

    Logger logger = LoggerFactory.getLogger(DatabaseInitializer.class);

    @Inject
    @Named("userRepository")
    private UserRepository userRepository;

    @Inject
    @Named("roleRepository")
    private RoleRepository roleRepository;

    @PostConstruct
    public void init() throws Exception {

        // load the available roles:");
        Role userRole = new Role(1L, "USER", "Application User");
        Role adminRole = new Role(3L, "ADMIN", "Administrator");

        roleRepository.save(userRole);
        roleRepository.save(adminRole);


        // a set with the USER role:
        Set<Role> userSet = new HashSet<Role>();
        userSet.add(userRole);

        // a set with the ADMIN role:
        Set<Role> adminSet = new HashSet<Role>();
        adminSet.add(adminRole);

        // a set with USER and ADMIN roles:
        Set<Role> userAdminSet = new HashSet<Role>();
        userAdminSet.add(userRole);
        userAdminSet.add(adminRole);

        // a super-user set (all roles):
        Set<Role> allSet = new HashSet<Role>();
        allSet.add(userRole);
        allSet.add(adminRole);


        // load up some app users (role = USER)
        userRepository.save(new User("Alice", "Cooper",     "USER01",true,userSet));
        userRepository.save(new User("Bob", "Dylan",        "USER02",true,userSet));
        userRepository.save(new User("Bob", "Marley",       "USER03",false,userSet));
        userRepository.save(new User("Chuck", "Berry",      "USER04",true,userSet));
        userRepository.save(new User("David", "Bowie",      "USER05",false,userSet));
        userRepository.save(new User("Elvis", "Costello",   "USER06",true,userSet));
        userRepository.save(new User("Frank", "Zappa",      "USER07",false,userSet));
        userRepository.save(new User("George", "Harrison",  "USER08",true,userSet));
        userRepository.save(new User("Ian", "Gillan",       "USER09",true,userSet));
        userRepository.save(new User("Jimi", "Hendrix",     "USER10",true,userSet));
        userRepository.save(new User("Jimmy", "Page",       "USER11",true,userSet));
        userRepository.save(new User("John", "Lennon",      "USER12",false,userSet));

        // load up some admin users (roles = [USER, ADMIN])
        userRepository.save(new User("Lou", "Reed",         "ADMIN01",true,allSet));
        userRepository.save(new User("Paul", "Simon",       "ADMIN02",true,allSet));
        userRepository.save(new User("Ray", "Charles",      "ADMIN03",true,allSet));
        userRepository.save(new User("Steve", "Miller",     "ADMIN04",true,allSet));
        userRepository.save(new User("Stevie", "Wonder",    "ADMIN05",true,allSet));
        userRepository.save(new User("Tim", "Curry",        "ADMIN06",true,allSet));
        userRepository.save(new User("Tom", "Petty",        "ADMIN07",true,allSet));
        userRepository.save(new User("Tom", "Waits",        "ADMIN08",false,allSet));



        logger.info("Loaded " + roleRepository.count() + " Roles to local DB");
        logger.info("Loaded " + userRepository.count() + " Users to local DB");

    }
}
