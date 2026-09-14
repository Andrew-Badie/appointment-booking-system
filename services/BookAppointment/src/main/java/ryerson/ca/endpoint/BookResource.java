/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ryerson.ca.endpoint;

import io.kubemq.sdk.basic.ServerAddressNotSuppliedException;
import java.io.IOException;
import java.io.StringWriter;
import java.sql.SQLException;

import java.util.logging.Level;
import java.util.logging.Logger;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.UriInfo;
import javax.ws.rs.Produces;
import javax.ws.rs.Consumes;
import javax.ws.rs.FormParam;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PUT;
import javax.ws.rs.PathParam;
import javax.ws.rs.core.MediaType;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;

import ryerson.ca.business.Business;
import ryerson.ca.helper.AppointmentBook;


/**
 * REST Web Service
 *
 * @author student
 */
@Path("book")
public class BookResource {

    @Context
    private UriInfo context;

    /**
     * Creates a new instance of BookResource
     */
    public BookResource() {
    }

    /**
     * Retrieves representation of an instance of ryerson.ca.endpoint.BookResource
     * @return an instance of java.lang.String
     */
    @GET
    @Produces(MediaType.APPLICATION_XML)
    @Path("isBooked/{id}")
    public String getXml(@PathParam("id") String id) {
        System.out.println(id);
        Business book = new Business();
        AppointmentBook appointment = book.getAppointments(id);
        if(appointment == null){
            return ("");
        }
        JAXBContext jaxbContext;
        try{
            jaxbContext = JAXBContext.newInstance(AppointmentBook.class);
            
            Marshaller jaxbMarshaller = jaxbContext.createMarshaller();
            
            jaxbMarshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
            StringWriter sw = new StringWriter();
            jaxbMarshaller.marshal(appointment, sw);
            
            return (sw.toString());
        }catch (JAXBException ex){
            Logger.getLogger(BookResource.class.getName()).log(Level.SEVERE, null, ex);
            return("ERROR");
        }
        //TODO return proper representation object
       
    }
    @POST
    @Produces(MediaType.TEXT_HTML)
    @Path("update")
    public String updateBookHold(@FormParam("code") String code, @FormParam("userid") String userid) throws InterruptedException {
        Business book = new Business();
        boolean bs;
        try {
            if (code == null || !code.matches("[A-Za-z0-9_-]{1,20}") ||
                    userid == null || !userid.matches("[A-Za-z0-9_-]{1,30}")) {
                throw new javax.ws.rs.WebApplicationException(400);
            }
            bs = book.book(code, userid);
            if (!bs) throw new javax.ws.rs.WebApplicationException(409);
            return ("Inserted");
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(BookResource.class.getName()).log(Level.SEVERE, null, ex);
            throw new javax.ws.rs.WebApplicationException(ex, 503);
        } catch (SQLException ex) {
            Logger.getLogger(BookResource.class.getName()).log(Level.SEVERE, null, ex);
               throw new javax.ws.rs.WebApplicationException(ex, 503);
        } catch (ServerAddressNotSuppliedException ex) {
            Logger.getLogger(BookResource.class.getName()).log(Level.SEVERE, null, ex);
               throw new javax.ws.rs.WebApplicationException(ex, 503);
        } catch (IOException ex) {
            Logger.getLogger(BookResource.class.getName()).log(Level.SEVERE, null, ex);
               throw new javax.ws.rs.WebApplicationException(ex, 503);
        }
        

    }
}
