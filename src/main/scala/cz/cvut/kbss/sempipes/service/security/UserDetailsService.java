package cz.cvut.kbss.sempipes.service.security;

import cz.cvut.kbss.sempipes.model.view.Person;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.HashSet;

@Service
public class UserDetailsService implements org.springframework.security.core.userdetails.UserDetailsService {

//    @Autowired
//    private UserDao userDao;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        final Person user = new Person();
        user.setPassword("pass");
        user.setUsername("user");
        user.setTypes(new HashSet<>());
        if (user == null) {
            throw new UsernameNotFoundException("User with username " + username + " not found.");
        }
        return new cz.cvut.kbss.sempipes.security.model.UserDetails(user);
    }
}
