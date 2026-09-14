/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ryerson.ca.searchappointment.helper;
import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlRootElement;
import ryerson.ca.searchappointment.helper.Appointment;
/**
 *
 * @author student
 */
@XmlRootElement(name= "appointments")
@XmlAccessorType (XmlAccessType.FIELD)

public class AppointmentsXML {
    @XmlElement(name="appointment")
        private ArrayList<Appointment> appointments = new ArrayList<>();
    
    
    public List<Appointment>getAppointments(){
        return appointments;
    }
    public AppointmentsXML(){
        
    }
    
    public void setAppointment(ArrayList<Appointment> as){
        appointments = as;
    }
    
}
