/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ryerson.ca.business;
import io.kubemq.sdk.basic.ServerAddressNotSuppliedException;
import java.io.IOException;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import ryerson.ca.helper.AppointmentBook;
import ryerson.ca.peristence.APPOINTMENT_Book_CRUD;
/**
 *
 * @author student
 */
public class Business {
    
    
    
    public AppointmentBook getAppointments(String id){
        AppointmentBook bs = APPOINTMENT_Book_CRUD.getBookAppointment(id);
        return bs;
    }
    public boolean book(String id, String userid)throws ClassNotFoundException, SQLException, 
            ServerAddressNotSuppliedException, IOException, InterruptedException{
        boolean success = false;
        success = APPOINTMENT_Book_CRUD.addBook(id, userid);
        if(success){
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
            LocalDate date = LocalDate.now();
            LocalDate exDate = date.plusDays(14);
            
            Messaging.sendmessage("BOOK:"+id+":"+userid+":"+exDate.format(formatter));
        }
        return success;
    }
}
