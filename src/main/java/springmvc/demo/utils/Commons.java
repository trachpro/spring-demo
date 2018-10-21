package springmvc.demo.utils;

import org.springframework.security.core.context.SecurityContextHolder;

public class Commons {

    private static final String[] ROLES = {"ROLE_MANAGER", "ROLE_STAFF", "ROLE_CUSTOMER"};

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
}
