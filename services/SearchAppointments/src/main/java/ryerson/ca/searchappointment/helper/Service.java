/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ryerson.ca.searchappointment.helper;

/**
 *
 * @author student
 */
public class Service {
    String firstName;
    String lastName;
    
    public Service(String firstName, String lastName){
        this.firstName = firstName;
        this.lastName = lastName;
        
    }
    public String getFirstName(){
        return firstName;
    }
    public String getLastName(){
        return lastName;
    }
}
