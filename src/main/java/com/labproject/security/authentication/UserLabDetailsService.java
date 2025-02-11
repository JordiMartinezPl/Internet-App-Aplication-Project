package com.labproject.security.authentication;


import com.labproject.domain.CollectionUser;
import com.labproject.persistence.UserRepository;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class UserLabDetailsService implements UserDetailsService {
    private UserRepository userLabRepository;

    public UserLabDetailsService(UserRepository userLabRepository) {
        this.userLabRepository = userLabRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        CollectionUser user = userLabRepository.findByEmail(username)
                .orElseThrow(() -> new UsernameNotFoundException("User Not Found with username: " + username));

        return new UserLabDetails(user);
    }

}