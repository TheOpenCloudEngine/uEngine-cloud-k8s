package com.example.template.user;

import lombok.AllArgsConstructor;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;

@Service
@AllArgsConstructor
public class UserService implements UserDetailsService {

    private final UserRepository userRepository;

    public UserDetails loadUserByUsername(String userId) throws UsernameNotFoundException {
        User user = userRepository.findByUsername(userId);
        if(user == null){
            throw new UsernameNotFoundException("Invalid username or password.");
        }
        // User 에 정보를 추가 하고 싶으면 UserDetails 의 구현체를 만들어서 해당 정보를 리턴하면 된다.
        return new org.springframework.security.core.userdetails.User(user.getUsername(), user.getPassword(), getAuthority());
    }

    private List<SimpleGrantedAuthority> getAuthority() {
        return Arrays.asList(new SimpleGrantedAuthority("ROLE_ADMIN"));
    }

    public User signUp(UserConnection userConnection) {
        final User user = User.signUp(userConnection);
        return userRepository.save(user);
    }

    public User findBySocial(UserConnection userConnection) {
        User user = userRepository.findByUsername(userConnection.getEmail());
        if (user == null) throw new RuntimeException();
        return user;
    }

    public boolean isExistUser(UserConnection userConnection) {
        User user = userRepository.findByUsername(userConnection.getEmail());
        return (user != null);
    }
}
