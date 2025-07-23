    package com.Brew_Track.Cafe.Brew_Track.JWT;

    import java.util.List;
    import java.util.Objects;

    import org.springframework.beans.factory.annotation.Autowired;
    import org.springframework.security.core.GrantedAuthority;
    import org.springframework.security.core.authority.SimpleGrantedAuthority;
    import org.springframework.security.core.userdetails.UserDetails;
    import org.springframework.security.core.userdetails.UserDetailsService;
    import org.springframework.security.core.userdetails.UsernameNotFoundException;
    import org.springframework.stereotype.Service;

    import com.Brew_Track.Cafe.Brew_Track.POJO.User;
    import com.Brew_Track.Cafe.Brew_Track.dao.UserDao;

    import lombok.extern.slf4j.Slf4j;

    @Slf4j
    @Service
    public class CustomerUsersDetailsService implements UserDetailsService {

        @Autowired
        private UserDao userDao;

        private User userDetail;

        @Override
        public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
            log.info("Loading user by username: {}", username);

            userDetail = userDao.findByEmail(username);

            if (!Objects.isNull(userDetail)) {
                List<GrantedAuthority> authorities = List.of(
                    new SimpleGrantedAuthority("ROLE_" + userDetail.getRole().toUpperCase())
                );

                return new org.springframework.security.core.userdetails.User(
                    userDetail.getEmail(),
                    userDetail.getPassword(),
                    authorities
                );
            } else {
                throw new UsernameNotFoundException("User not found with username: " + username);
            }
        }

        public User getUserDetail() {
            return userDetail;
        }
    }
