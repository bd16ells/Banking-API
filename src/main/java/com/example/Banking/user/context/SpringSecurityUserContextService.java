package com.example.Banking.user.context;

import com.example.Banking.user.User;
import com.example.Banking.user.UserRepository;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.util.Optional;

@Data
@AllArgsConstructor
public class SpringSecurityUserContextService implements UserContextService {

    private UserRepository userRepository;

    @Override
    public User getCurrentUser() {
        Optional<User> u = userRepository.findById(getCurrentUserId());
        if( !u.isPresent() ){
            throw new RuntimeException("Current user does not exist");
        }else{
            return u.get();
        }
    }

    @Override
    public Long getCurrentUserId() {
        Object p = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if( !(p instanceof UserDetails) ){
            throw new RuntimeException("Current user does not exist");
        }else{
            UserDetails userDetails = (UserDetails)p;
            Optional<User> user = userRepository.findByUsername(userDetails.getUsername());
            if( !user.isPresent() ){
                throw new UsernameNotFoundException(userDetails.getUsername());
            }
            return user.get().getId();
        }
    }

    @Override
    public String getCurrentUsername() {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        if( username == null ){
            throw new RuntimeException("Current user does not exist");
        }else{
            return username;
        }
    }

}