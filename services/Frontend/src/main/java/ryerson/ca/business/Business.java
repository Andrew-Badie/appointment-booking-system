/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ryerson.ca.business;
import java.io.IOException;
import java.io.InputStream;
import java.io.StringReader;
import java.io.StringWriter;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Invocation;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
import org.apache.commons.io.IOUtils;
import ryerson.ca.helper.Appointment;
import ryerson.ca.helper.AppointmentsXML;
/**
 *
 * @author student
 */
public class Business {
    public static boolean isAuthenticated(String username, String password){
        if ("AndrewBadie".equals(username) && "1234".equals(password)) {
            return true;
        }
        return false;
    }
    
    public static AppointmentsXML getServices(String query, String token) throws IOException {
        
        Client searchclient  = ClientBuilder.newClient();
        String searchService = System.getenv("searchService");
        String bookService = System.getenv("bookService");
        WebTarget searchwebTarget = searchclient.target("http://"+searchService+"/SearchAppointments/webresources/search");
        System.out.println("Calling: http://" + searchService + "/SearchAppointments/webresources/search/" + query);
        System.out.println("searchService: " + searchService);
        
        InputStream is = searchwebTarget.path(query).request(MediaType.APPLICATION_XML).get(InputStream.class);

        String xml = IOUtils.toString(is, "utf-8");
        AppointmentsXML appointments= appointmentxmltoObjects(xml);
        if (token != null)  {
            Client bookclient = ClientBuilder.newClient();
            System.out.println("http://"+bookService+"/BookAppointment/webresources/book/isBooked");
            WebTarget bookwebtarget = bookclient.target("http://"+bookService+"/BookAppointment/webresources/book/isBooked");
            for(Appointment appointment : appointments.getAppointments()) {
                InputStream holddata = bookwebtarget.path(appointment.getId()).queryParam("token", token).request(MediaType.APPLICATION_XML).get(InputStream.class);
                try{
                    Appointment a = appointmentbookxmltoObjects(IOUtils.toString(holddata, "utf-8"));
                    if(a!=null){
                        appointment.setBooking(true);
                    }
                    else{
                        appointment.setBooking(false);
                        
                    }
                }catch(Exception e){
                            appointment.setBooking(false);
                            }
            }
        }return (appointments);
    }
    
    private static AppointmentsXML appointmentxmltoObjects(String xml){
        JAXBContext jaxbContext;
        try{
            jaxbContext = JAXBContext.newInstance(AppointmentsXML.class);
            
            Unmarshaller jaxbUnmarshaller = jaxbContext.createUnmarshaller();
            
            AppointmentsXML appointments = (AppointmentsXML) jaxbUnmarshaller.unmarshal(new StringReader(xml));
            return appointments;
        }catch(JAXBException e){
            e.printStackTrace();
        }
        return null;
    }
    private static Appointment appointmentbookxmltoObjects(String xml){
        if(xml.isEmpty()){
            return null;
            
        }
        JAXBContext jaxbContext;
        try{
            jaxbContext = JAXBContext.newInstance(Appointment.class);
            
            Unmarshaller jaxbUnmarshaller = jaxbContext.createUnmarshaller();
            
            Appointment appointment = (Appointment) jaxbUnmarshaller.unmarshal(new StringReader(xml));
            return appointment;
        }catch(JAXBException e){
            e.printStackTrace();
        }
        return null;
    }
}
