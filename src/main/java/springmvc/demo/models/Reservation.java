package springmvc.demo.models;


import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;


@Document(collection = "reservations")
@JsonAutoDetect(getterVisibility = JsonAutoDetect.Visibility.NONE)
public class Reservation {

    @Id
    @Field("_id")
    private String _id;

    @Field("code")
    private String code;

    @Field("bookingFrom")
    private Date bookingFrom;

    @Field("bookingTo")
    private Date bookingTo;

    @Field("customerEmail")
    private String customerEmail;

    @Field("customerName")
    private String customerName;

    @Field("roomNo")
    private int roomNo;

    @Field("checkin")
    private Date checkin;

    @Field("checkout")
    private Date checkout;

    @Field("status")
    private String status;

    @Field("total")
    private int total;

    public Reservation(Date bookingFrom, Date bookingTo, String customerEmail, String customerName , int roomNo, Date checkin, Date checkout, String status) {
        this.bookingFrom = bookingFrom;
        this.bookingTo = bookingTo;
        this.customerEmail = customerEmail;
        this.customerName = customerName;
        this.roomNo = roomNo;
        this.checkin = checkin;
        this.checkout = checkout;
        this.status = status;
    }

    public Reservation(Date bookingFrom, Date bookingTo, String customerEmail, String customerName,int roomNo, String status) {
        this.bookingFrom = bookingFrom;
        this.bookingTo = bookingTo;
        this.customerEmail = customerEmail;
        this.customerName = customerName;
        this.roomNo = roomNo;
        this.status = status;
    }

    public Reservation(Date bookingFrom, Date bookingTo, String customerName, int roomNo, String status, int total) {
        this.bookingFrom = bookingFrom;
        this.bookingTo = bookingTo;
        this.customerName = customerName;
        this.roomNo = roomNo;
        this.status = status;
        this.total = total;
    }

    public Reservation(Date bookingFrom, Date bookingTo, String name, int roomNo) {
        this.bookingFrom = bookingFrom;
        this.bookingTo = bookingTo;
        this.customerName = name;
        this.roomNo = roomNo;
    }

    public Reservation() {
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public Date getBookingFrom() {
        return bookingFrom;
    }

    public void setBookingFrom(Date bookingFrom) {
        this.bookingFrom = bookingFrom;
    }

    public Date getBookingTo() {
        return bookingTo;
    }

    public void setBookingTo(Date bookingTo) {
        this.bookingTo = bookingTo;
    }

    public String getCustomerEmail() {
        return customerEmail;
    }

    public void setCustomerEmail(String customerEmail) {
        this.customerEmail = customerEmail;
    }

    public String getCustomerName() {
        return customerName;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    public int getRoomNo() {
        return roomNo;
    }

    public void setRoomNo(int roomNo) {
        this.roomNo = roomNo;
    }

    public Date getCheckin() {
        return checkin;
    }

    public void setCheckin(Date checkin) {
        this.checkin = checkin;
    }

    public Date getCheckout() {
        return checkout;
    }

    public void setCheckout(Date checkout) {
        this.checkout = checkout;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public int getTotal() {
        return total;
    }

    public void setTotal(int total) {
        this.total = total;
    }
}
