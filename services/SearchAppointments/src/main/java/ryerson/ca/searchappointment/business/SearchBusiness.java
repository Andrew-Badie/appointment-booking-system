/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ryerson.ca.searchappointment.business;
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
import ryerson.ca.searchappointment.helper.Appointment;
import ryerson.ca.searchappointment.persisitence.Appointment_CRUD;
import ryerson.ca.searchappointment.helper.AppointmentsXML;
/**
 *
 * @author student
 */
public class SearchBusiness {
    public AppointmentsXML getAppointmentsByQuery(String query){
       Set<Appointment> appointments = Appointment_CRUD.searchForAppointments(query);
       Map<String, Appointment> allServicesAppointments = new HashMap();
       if (appointments == null || appointments.isEmpty()){
           System.out.println("No appointments found for query "+query);
           return null;
       }
       
       
        System.out.println("&&&&&&&&&&&"+appointments.size());
        for(Appointment appointment : appointments){
            if(allServicesAppointments.containsKey(appointment.getId())){
                allServicesAppointments.get(appointment.getId()).addService(appointment.getServices());
            }
            else{
                allServicesAppointments.put(appointment.getId(), appointment);
            }
        }
        System.out.println("***********************"+allServicesAppointments.size());
        AppointmentsXML as;
        as = new AppointmentsXML();
        as.setAppointment(new ArrayList(allServicesAppointments.values()));
        return as;
    }
    
    
    
    
    
    
    
}
