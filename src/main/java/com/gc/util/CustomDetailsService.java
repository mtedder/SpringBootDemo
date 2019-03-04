package com.gc.util;

/**
 * Created by maurice on 8/20/17.
 * Ref: http://www.baeldung.com/spring-security-authentication-with-a-database
 */
public class CustomDetailsService {

   /* public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        Dao dao = DaoFactory.getInstance(DaoFactory.JDBC);
        User user = dao.findByUserName(username);

        if (user == null) {
            throw new UsernameNotFoundException(username);
        }
        return new CustomUserDetails(user);
    }*/
}
