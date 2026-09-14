/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Business;


import static java.lang.System.in;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlRootElement;
//import org.apache.commons.codec.binary.Base64;
import Persistence.User_Appointment_Confirm_CRUD;
import Persistence.APPOINTMENT_Booking_CRUD;
import Helper.AppointmentConfirm;

/**
 *
 * @author student
 */
public class ConfrimBusiness {

    public  ConfirmXML getAppointmentsByQuery(String username){
       Set<AppointmentConfirm> appointments = User_Appointment_Confirm_CRUD.getConfirmedAppointments(username);
       
       
        ConfirmXML bs;
        bs = new ConfirmXML();
        bs.setAppointment(new ArrayList(appointments));
        return (bs);
    }
    
    
    
    public ConfirmXML getBookings(){
        Set<AppointmentConfirm> bookings = APPOINTMENT_Booking_CRUD.getBookings();
       
       ConfirmXML bs;
        bs = new ConfirmXML();
        bs.setAppointment(new ArrayList(bookings));
        return (bs);
    }
  
}
