/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package contacts.manager;

/**
 *
 * @author admin
 */
public class ContactBuilder {
    private Integer Id;
    private String Name;
    private String Phone;
    private String Email;
    
    private String Street;
    private String City;
    private String State;
    private String ZipCode;
    
    public Integer getId(){
        return Id;
    }
    
    public void setId(int Id) {
        this.Id = Id;
    }


    public String getName() {
        return Name;
    }

    public void setName(String Name) {
        this.Name = Name;
    }

    public String getPhone() {
        return Phone;
    }

    public void setPhone(String Number) {
        this.Phone = Number;
    }

    public String getEmail() {
        return Email;
    }

    public void setEmail(String Email) {
        this.Email = Email;
    }

    public String getStreet() {
        return Street;
    }

    public void setStreet(String Street) {
        this.Street = Street;
    }

    //Adreeses Getters and Setters
    public String getCity() {
        return City;
    }

    public void setCity(String City) {
        this.City = City;
    }

    public String getState() {
        return State;
    }

    public void setState(String State) {
        this.State = State;
    }

    public String getZipCode() {
        return ZipCode;
    }

    public void setZipCode(String ZipCode) {
        this.ZipCode = ZipCode;
    }
    
    public Contact getContact() throws NullPointerException { 
        return (Id == null)? 
                new Contact(0, Name, Phone, Email, Street, City, State, ZipCode) 
                : 
                new Contact(Id, Name, Phone, Email, Street, City, State, ZipCode);
    }
}
