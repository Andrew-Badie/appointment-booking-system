/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ryerson.ca.searchappointment.helper;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Objects;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;
/**
 *
 * @author student
 */
@XmlRootElement(name="appointment")
@XmlAccessorType(XmlAccessType.FIELD)
public class Appointment {
    private String id;
    private String serviceName;
    ArrayList<Service> service;


public Appointment(){

}

public Appointment(String id, String serviceName){
    this.id=id;
    this.serviceName = serviceName;
    this.service = new ArrayList<Service>();
    //this.service.add(new Service(service.getFirstName(),service.getLastName()));
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