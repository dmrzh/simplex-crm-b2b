package ru.dev_server.client;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.transaction.annotation.Transactional;
import ru.dev_server.client.dao.EmployeeDAO;
import ru.dev_server.client.model.Employee;

import javax.annotation.Resource;
import java.util.ArrayList;

/**.*/
public class HibernateDaoImpl implements UserDetailsService {
    @Resource
    private EmployeeDAO employeeDAO;

    public static final SimpleGrantedAuthority ROLE_LEADER = new SimpleGrantedAuthority("ROLE_LEADER");
    public static final SimpleGrantedAuthority ROLE_EMPLOYEE = new SimpleGrantedAuthority("ROLE_EMPLOYEE");
    public static final SimpleGrantedAuthority ROLE_ADMIN = new SimpleGrantedAuthority("ROLE_ADMIN");

    @Override
    @Transactional
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Employee e = employeeDAO.findByEmail(username);
        if (e==null){
            throw new UsernameNotFoundException(username + " not found");
        }
        ArrayList<GrantedAuthority> grantedAuthorities = getGrantedAuthorities(e);
        User userDetails = new User(username,e.getPassword(),(!e.isDisable())&& e.getActivationCode()==null,true,true,true,grantedAuthorities);
        return userDetails;
    }

    public static ArrayList<GrantedAuthority> getGrantedAuthorities(Employee e) {
        ArrayList<GrantedAuthority> grantedAuthorities = new ArrayList<GrantedAuthority>();


        if("ROLE_EMPLOYEE".equals(e.getRole().toUpperCase())){
            grantedAuthorities.add(ROLE_EMPLOYEE);
        }
        if("ROLE_LEADER".equals(e.getRole().toUpperCase())){
            grantedAuthorities.add(ROLE_EMPLOYEE);
            grantedAuthorities.add(ROLE_LEADER);
        }

        if("ROLE_ADMIN".equals(e.getRole().toUpperCase())){
            grantedAuthorities.add(ROLE_ADMIN);
            grantedAuthorities.add(ROLE_EMPLOYEE);
            grantedAuthorities.add(ROLE_LEADER);
        }
        if("ROLE_ROOT".equals(e.getRole().toUpperCase())){
            grantedAuthorities.add(new SimpleGrantedAuthority("ROLE_ROOT"));
            grantedAuthorities.add(ROLE_ADMIN);
            grantedAuthorities.add(ROLE_EMPLOYEE);
        }
        return grantedAuthorities;
    }
}
