/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Business;

/**
 *
 * @author student
 */
import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlRootElement;
import Helper.AppointmentConfirm;


 @XmlRootElement
       public class ConfirmXML{
           private ArrayList<AppointmentConfirm> appointments;
           @XmlElementWrapper
           @XmlElement(name="appointmentConfirmed")
           public List<AppointmentConfirm>getAppointments(){
               return appointments;
               
           }
           ConfirmXML(){
               
               
           }
           public void setAppointment(ArrayList<AppointmentConfirm> bs){
               appointments=bs;
               
           }
           
       }