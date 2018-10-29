package springmvc.demo.services;

import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import springmvc.demo.Repositories.staffRepository.StaffsRepository;
import springmvc.demo.models.ResponseModel;
import springmvc.demo.models.Staff;

import java.util.List;
import java.util.Map;

@Service
public class StaffsService {

    private static StaffsRepository staffsRepository;

    private static PasswordEncoder passwordEncoder;

    public StaffsService(StaffsRepository repo, PasswordEncoder encoder) {

        staffsRepository = repo;
        passwordEncoder = encoder;
    }

    public static ResponseModel getAllStaffs() {

        try {
            List<Staff> staffList = staffsRepository.findAll();

            return new ResponseModel(staffList, "successfully!", HttpStatus.OK);
        } catch (Exception e) {

            return new ResponseModel(null, "internal error!", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    public static ResponseModel registerNewStaff(Staff user) {

        if(staffsRepository.findStaffByEmail(user.getEmail()) == null) {

            try {

                staffsRepository.insert(user);

                return new ResponseModel(user, "register successfully!", HttpStatus.OK);
            } catch (Exception e) {

                return new ResponseModel(null, "internal error!", HttpStatus.INTERNAL_SERVER_ERROR);
            }
        } else {

            return new ResponseModel(null,"User is already existed!", HttpStatus.CONFLICT);
        }
    }

    public static ResponseModel getStaffById(String id) {

        try {
            Staff user = staffsRepository.findStaffBy_id(id);

            if(user == null) {

                return new ResponseModel(null,"User not found!", HttpStatus.BAD_REQUEST);
            } else {

                return new ResponseModel(user, "success", HttpStatus.OK);
            }
        } catch (Exception e) {

            return new ResponseModel(null,"Internal Error", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    public static ResponseModel updateStaffById(String id, Map<String, String> params) {

        Staff user;

        try {
            user = staffsRepository.findStaffBy_id(id);
        } catch (Exception e) {

            return new ResponseModel(null,"Internal Error", HttpStatus.INTERNAL_SERVER_ERROR);
        }

        if(user == null) {

            return new ResponseModel(null,"User not found!", HttpStatus.NOT_FOUND);
        }

        for(Map.Entry<String, String> entry: params.entrySet()) {

            switch (entry.getKey()) {

                case "name":
                    user.setName(entry.getValue());
                    break;

                case "password":
                    user.setPassword(passwordEncoder.encode(entry.getValue()));
                    break;

                    default: break;
            }
        }

        try {
            staffsRepository.save(user);
            return new ResponseModel(user, "update successful!", HttpStatus.OK);
        } catch (Exception e) {

            return new ResponseModel(null,"Internal Error", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    public static ResponseModel deleteStaffById(String id) {

        try {

            Long x = staffsRepository.deleteStaffBy_id(id);

            return new ResponseModel(x,"delete successful", HttpStatus.OK);
        } catch (Exception e) {

            return new ResponseModel(null,"Internal error!", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
