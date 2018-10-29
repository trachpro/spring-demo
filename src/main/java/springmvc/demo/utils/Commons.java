package springmvc.demo.utils;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import springmvc.demo.models.Staff;
import springmvc.demo.models.User;

import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Commons {

    private static final String[] ROLES = {"ROLE_MANAGER", "ROLE_STAFF", "ROLE_CUSTOMER"};

    private static final Pattern VALID_EMAIL_ADDRESS_REGEX =
            Pattern.compile("^[A-Z0-9._%+-]+@[A-Z0-9.-]+\\.[A-Z]{2,6}$", Pattern.CASE_INSENSITIVE);

    @Autowired
    private static PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    public static boolean isManager() {

        return SecurityContextHolder
                .getContext()
                .getAuthentication()
                .getAuthorities()
                .toArray()[0]
                .toString()
                .equals("ROLE_MANAGER");
    }

    public static boolean isStaff() {

        return SecurityContextHolder
                .getContext()
                .getAuthentication()
                .getAuthorities()
                .toArray()[0]
                .toString()
                .equals("ROLE_STAFF");
    }

    public static boolean isCustomer() {

        return SecurityContextHolder
                .getContext()
                .getAuthentication()
                .getAuthorities()
                .toArray()[0]
                .toString()
                .equals("ROLE_CUSTOMER");
    }

    public static boolean isOwner(String id) {

        return SecurityContextHolder
                .getContext()
                .getAuthentication()
                .getName()
                .equals(id);
    }

    public static boolean isValidRole(String role) {

        for (String s : ROLES) {

            if(s.equals(role)) {
                return true;
            }
        }

        return false;
    }

    public static User getUserFromParams(Map<String, String> params) {

        String name = params.get("name");
        String email = params.get("email");
        String password = params.get("password");

        if(name == null || name == "" ||
                email == null || email == "" || !validateEmail(email) ||
                params == null || password == ""
                ) {
            return null;
        }

        return new User( name, passwordEncoder.encode(password), email );
    }

    public static Staff getStaffFromParams(Map<String, String> params) {

        String name = params.get("name");
        String email = params.get("email");
        String password = params.get("password");
        String role = params.get("role");

        if(name == null || name == "" ||
                email == null || email == "" || !validateEmail(email) ||
                params == null || password == "" ||
                role == null || role == ""
                ) {
            return null;
        }

        return new Staff( name, passwordEncoder.encode(password), email, role );
    }

    public static boolean validateEmail(String emailStr) {
        Matcher matcher = VALID_EMAIL_ADDRESS_REGEX .matcher(emailStr);
        return matcher.find();
    }
}
