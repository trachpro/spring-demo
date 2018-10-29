package springmvc.demo.models;


import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;


@Document(collection = "Reservations")
@JsonAutoDetect(getterVisibility = JsonAutoDetect.Visibility.NONE)
public class Reservation {

    @Id
    @Field("_id")
    private String _id;

    @Field("bookingFrom")
    private Date bookingFrom;

    @Field("bookingTo")
    private Date bookingTo;

    @Field("customerEmail")
    private String customerEmail;

    @Field("roomNo")
    private int roomNo;

    @Field("checkin")
    private DateTimeFormat checkin;

    @Field("checkout")
    private DateTimeFormat checkout;

    @Field("status")
    private String status;

    @Field("total")
    private int total;

    public Reservation(Date bookingFrom, Date bookingTo, String customerEmail, int roomNo, DateTimeFormat checkin, DateTimeFormat checkout, String status) {
        this.bookingFrom = bookingFrom;
        this.bookingTo = bookingTo;
        this.customerEmail = customerEmail;
        this.roomNo = roomNo;
        this.checkin = checkin;
        this.checkout = checkout;
        this.status = status;
    }

    public Reservation(Date bookingFrom, Date bookingTo, String customerEmail, int roomNo, String status) {
        this.bookingFrom = bookingFrom;
        this.bookingTo = bookingTo;
        this.customerEmail = customerEmail;
        this.roomNo = roomNo;
        this.status = status;
    }

    public Reservation() {
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

    public int getRoomNo() {
        return roomNo;
    }

    public void setRoomNo(int roomNo) {
        this.roomNo = roomNo;
    }

    public DateTimeFormat getCheckin() {
        return checkin;
    }

    public void setCheckin(DateTimeFormat checkin) {
        this.checkin = checkin;
    }

    public DateTimeFormat getCheckout() {
        return checkout;
    }

    public void setCheckout(DateTimeFormat checkout) {
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
