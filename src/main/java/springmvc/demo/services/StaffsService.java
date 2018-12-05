package springmvc.demo.services;

import com.mongodb.util.JSON;
import org.json.JSONObject;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import springmvc.demo.repositories.staffRepository.StaffsRepository;
import springmvc.demo.models.ResponseModel;
import springmvc.demo.models.Staff;
import springmvc.demo.utils.Message;

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

            return new ResponseModel(staffList, Message.SUCCESS, HttpStatus.OK);
        } catch (Exception e) {

            return new ResponseModel(JSONObject.NULL, Message.INTERNAL_ERROR, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    public static ResponseModel registerNewStaff(Staff user) {

        if(staffsRepository.findStaffByEmail(user.getEmail()) == null) {

            try {

                staffsRepository.insert(user);

                return new ResponseModel(user, Message.SUCCESS, HttpStatus.OK);
            } catch (Exception e) {

                return new ResponseModel(JSONObject.NULL, Message.INTERNAL_ERROR, HttpStatus.INTERNAL_SERVER_ERROR);
            }
        } else {

            return new ResponseModel(JSONObject.NULL,"User is already existed!", HttpStatus.CONFLICT);
        }
    }

    public static ResponseModel getStaffById(String id) {

        try {
            Staff user = staffsRepository.findStaffBy_id(id);

            if(user == null) {

                return new ResponseModel(JSONObject.NULL,Message.NOT_FOUND, HttpStatus.BAD_REQUEST);
            } else {

                return new ResponseModel(user, Message.SUCCESS, HttpStatus.OK);
            }
        } catch (Exception e) {

            return new ResponseModel(null,Message.INTERNAL_ERROR, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    public static ResponseModel updateStaffById(String id, Map<String, String> params) {

        Staff user;

        try {
            user = staffsRepository.findStaffBy_id(id);
        } catch (Exception e) {

            return new ResponseModel(JSONObject.NULL,Message.INTERNAL_ERROR, HttpStatus.INTERNAL_SERVER_ERROR);
        }

        if(user == null) {

            return new ResponseModel(JSONObject.NULL,Message.NOT_FOUND, HttpStatus.NOT_FOUND);
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
            return new ResponseModel(user, Message.SUCCESS, HttpStatus.OK);
        } catch (Exception e) {

            return new ResponseModel(JSONObject.NULL,Message.INTERNAL_ERROR, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    public static ResponseModel deleteStaffById(String id) {

        try {

            Long x = staffsRepository.deleteStaffBy_id(id);

            return new ResponseModel(x,Message.SUCCESS, HttpStatus.OK);
        } catch (Exception e) {

            return new ResponseModel(JSONObject.NULL,Message.INTERNAL_ERROR, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
