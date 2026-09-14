package ryerson.ca.helper;



import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlRootElement;
import ryerson.ca.helper.Appointment;
import ryerson.ca.helper.Appointment;
import ryerson.ca.helper.Appointment;

/**
 *
 * @author student
 */
@XmlRootElement(name= "appointments")
@XmlAccessorType (XmlAccessType.FIELD)

public class AppointmentsXML {
    @XmlElement(name="appointment")
        private ArrayList<Appointment> appointments;
    
    
    public List<Appointment>getAppointments(){
        return appointments;
    }
    public AppointmentsXML(){
        
    }
    
    public void setAppointment(ArrayList<Appointment> as){
        appointments = as;
    }
    
}
