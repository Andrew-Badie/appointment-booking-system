/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ryerson.ca.helper;
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
public class AppointmentBook {
    private String id;
    private String bookDate;
    private String username;
    
    public AppointmentBook(String id, String bookDate, String username){
        this.id=id;
        this.bookDate = bookDate;
        this.username = username;
    }
    public AppointmentBook(){
        this.id = "";
        this.bookDate = "";
        this.username = "";
    }
    public String getId(){
        return id;
    }
    public String getBookDate(){
        return bookDate;
    }
    public String getUsername(){
        return username;
    }
    
    
}
