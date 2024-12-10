/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package contacts.manager;

/**
 *
 * @author admin
 */
public class Contact {
    public int Id;
    //Contact Information
    public String Name;
    public String Phone;
    public String Email;
    
    //Adresses
    public String Street;
    public String City;
    public String State;
    public String ZipCode;

    public Contact(int Id, String Name, String Phone, String Email, String Street, String City, String State, String ZipCode) {
        this.Id = Id;
        this.Name = Name;
        this.Phone = Phone;
        this.Email = Email;
        this.Street = Street;
        this.City = City;
        this.State = State;
        this.ZipCode = ZipCode;
    }
}
