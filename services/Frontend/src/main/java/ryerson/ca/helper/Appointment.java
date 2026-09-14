/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ryerson.ca.helper;
import java.util.ArrayList;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

/**
 *
 * @author student
 */
@XmlRootElement(name = "appointment")
@XmlAccessorType(XmlAccessType.FIELD)
public class Appointment {
    @XmlTransient
    private boolean booking;
    
    public boolean booking(){
        return booking;
    }
    public void setBooking(boolean booking){
        this.booking = booking;
    }
    private String id;
    private String serviceName;
    ArrayList<Service> service;
    
    public Appointment(){
        
    }
    public Appointment(String id, String serviceName, Service service){
        this.id = id;
        this.serviceName = serviceName;
        this.service = new ArrayList<Service>();
        this.service.add(new Service(service.getFirstName(), service.getLastName()));
    }
    
    public String getId(){
        return id;
    }
    public String getServiceName(){
        return serviceName;
    }
    
    public ArrayList<Service> getServices(){
        return service;
    }
    public void addService(ArrayList<Service> services){
        for(Service s:services){
            this.service.add(new Service(s.getFirstName(),s.getLastName()));
        }
    }
}
