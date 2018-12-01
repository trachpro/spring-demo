package springmvc.demo.utils;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.util.MultiValueMap;
import springmvc.demo.models.Reservation;
import springmvc.demo.models.Staff;
import springmvc.demo.models.User;
import springmvc.demo.services.authentication.UserCustom;

import java.security.SecureRandom;
import java.util.Date;
import java.util.Map;
import java.util.concurrent.TimeUnit;
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
                .equals("ROLE_CLIENT");
    }

    public static String getEmail() {

        UserCustom user = (UserCustom) SecurityContextHolder.getContext().getAuthentication();

        return user.getEmail();
    }

    public static String getName() {

        UserCustom user = (UserCustom) SecurityContextHolder.getContext().getAuthentication();

        return user.getuName();
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

    public static User getUserFromParams(MultiValueMap<String, String> params) {
        String name = params.getFirst("name");
        String email = params.getFirst("email");
        String password = params.getFirst("password");

        if(name == null || name == "" ||
                email == null || email == "" || !validateEmail(email) ||
                params == null || password == ""
                ) {
            return null;
        }

        return new User( name, passwordEncoder.encode(password), email );
    }

    public static Staff getStaffFromParams(MultiValueMap<String, String> params) {

        String name = params.getFirst("name");
        String email = params.getFirst("email");
        String password = "defaulpassword";
        String role = params.getFirst("role");

        if(name == null || name == "" ||
                email == null || email == "" || !validateEmail(email) ||
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

    public static Reservation getReservationFromParams(MultiValueMap<String, String> params) {

        Date from = Converts.convertStringToDate(params.getFirst("bookingFrom"));
        Date to = Converts.convertStringToDate(params.getFirst("bookingTo"));
        String name = params.getFirst("customerName");

        int roomNo = Integer.parseInt(params.getFirst("roomNo"));

        if(from == null || to == null || name == null || name.length() == 0 || roomNo <= 0) {
            return null;
        }

        return new Reservation(from, to, name, roomNo);
    }

    public static String generateRandomString() {

        String prefix = "BK";

        String validChar = "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZ";
        SecureRandom rnd = new SecureRandom();
        int len = 7;

        StringBuilder sb = new StringBuilder( len );
            for( int i = 0; i < len; i++ )
                sb.append( validChar.charAt( rnd.nextInt(validChar.length()) ) );

        String surfix = sb.toString();

        return prefix + surfix;
    }

    public static long calculateDayDifference(Date from, Date to) {
        long diff = Math.abs(to.getTime() - from.getTime());
        long daydiff = TimeUnit.DAYS.convert(diff, TimeUnit.MILLISECONDS);

        if (daydiff == 0) return 1;

        return daydiff;
    }
}
