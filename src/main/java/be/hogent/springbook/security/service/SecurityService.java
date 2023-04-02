package be.hogent.springbook.security.service;

import be.hogent.springbook.security.entity.SecurityUser;
import be.hogent.springbook.user.entity.ApplicationUser;
import be.hogent.springbook.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Log4j2
public class SecurityService implements UserDetailsService {
    private final UserRepository userRepository;
    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        ApplicationUser potentialUser = userRepository.findByEmail(email).orElseThrow( ()-> {
            log.error("User with email {} not found", email);
            throw new UsernameNotFoundException("User Not Found");
        });

        return new SecurityUser(potentialUser);
    }


}
