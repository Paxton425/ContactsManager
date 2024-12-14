/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package contacts.manager;

import org.w3c.dom.*;
import javax.xml.parsers.*;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.StringWriter;
import java.net.URL;
import java.util.Locale;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

/**
 *
 * @author Sphelele
 */
public class ContactsManager {
    private Contact[] contacts;
    private int contactsCount;
    private int limit;
    
    public ContactsManager(){
    }
    
    public ContactsManager(int Limit){
        this.contacts = new Contact[this.limit = Limit];
        this.contactsCount = 0;
        loadContactsArray();
    }
    
    String path = ("C:\\Users\\admin\\Documents\\NetBeansProjects\\Contacts Manager\\src\\contacts\\manager\\SavedContactsList.xml");
    File contactsFile = new File(path);
    DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
    
    private void loadContactsArray(){
        try{
            DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
            Document document = dBuilder.parse(contactsFile);
            document.getDocumentElement().normalize();
            
            NodeList contactsNodeList = document.getElementsByTagName("contact");
            
            for(int i=0; i<contactsNodeList.getLength(); i++){
                Node contactNode = contactsNodeList.item(i);
                
                if(contactNode.getNodeType() == Node.ELEMENT_NODE){
                    //Cast Contact Node to Element
                    Element contactElement = (Element) contactNode;
                    
                    Integer Id = Integer.parseInt(contactElement.getElementsByTagName("id").item(0).getTextContent());
                    //Contact Infomation
                    String name = contactElement.getElementsByTagName("name").item(0).getTextContent();
                    String contact = contactElement.getElementsByTagName("phone").item(0).getTextContent();
                    String email = contactElement.getElementsByTagName("email").item(0).getTextContent();      
                    
                    //Cast Adreess Node to Element
                    Element adressElement = (Element) contactElement.getElementsByTagName("address").item(0);
                    
                    String street = adressElement.getElementsByTagName("street").item(0).getTextContent();
                    String city = adressElement.getElementsByTagName("city").item(0).getTextContent();
                    String state = adressElement.getElementsByTagName("state").item(0).getTextContent();
                    String zipcode = adressElement.getElementsByTagName("zipCode").item(0).getTextContent();
                    
                    ContactBuilder contactBuilder = new ContactBuilder();
                    contactBuilder.setId(Id);
                    contactBuilder.setName(name);
                    contactBuilder.setPhone(contact);
                    contactBuilder.setEmail(email);
                    contactBuilder.setStreet(street);
                    contactBuilder.setCity(city);
                    contactBuilder.setState(state);
                    contactBuilder.setZipCode(zipcode);
                    
                    Contact newContact = contactBuilder.getContact();
        
                    contacts[contactsCount] = newContact;
                    contactsCount++;
                }
            }
        } catch(Exception e){
            e.printStackTrace();
        }
    }
    
    public void UpdateContact(Contact contact) 
            throws org.xml.sax.SAXException, IOException, NullPointerException, ParserConfigurationException{
        try{
            DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
            Document document = dBuilder.parse(contactsFile);
            document.getDocumentElement().normalize();
            
            NodeList contactsNodeList = document.getElementsByTagName("contact");
            //Search for matching Contact Item
            for(int i=0; i<contactsNodeList.getLength(); i++){
                Node contactNode = contactsNodeList.item(i);
                if(contactNode.getNodeType() == Node.ELEMENT_NODE){
                    //Cast Contact Node to Element
                    Element contactElement = (Element) contactNode;
                    String contactId = contactElement.getElementsByTagName("id").item(0).getTextContent();
                    
                    if(Integer.parseInt(contactId) == contact.Id){
                        //Value Arrays, hashmaps in future
                        String[] contactELNames = {"name", "phone", "email", "street", "city", "state", "zipCode"};
                        String[] updatedValues = {contact.Name, contact.Phone, contact.Email, contact.Street, contact.City, contact.State, contact.ZipCode};
                        
                        //check for changes on each element
                        for(int j=0; j< contactELNames.length-4; j++){
                            Element element = (Element)contactElement.getElementsByTagName(contactELNames[j]).item(0);
                            if (!element.getTextContent().matches(updatedValues[j]))
                                contactElement.getElementsByTagName(contactELNames[j])
                                        .item(0)
                                        .replaceChild(
                                                document.createTextNode(updatedValues[j]), 
                                                element.getFirstChild());//Replace Existing Child With Updated Value
                            if(j == 2){
                                Element addressElement = (Element)contactElement.getElementsByTagName("address").item(0);
                                for(int z = j+1; z<updatedValues.length; z++){
                                    Element addressCElement = (Element)contactElement.getElementsByTagName(contactELNames[z]).item(0);
                                    if (!addressCElement.getTextContent().matches(updatedValues[z]))
                                        contactElement.getElementsByTagName(contactELNames[z])
                                                .item(0)
                                                .replaceChild(
                                                    document.createTextNode(updatedValues[z]), 
                                                    addressCElement.getFirstChild());//Replace Existing Child With Updated Value
                                }
                            } 
                        }
                        
                        
                        
                        
                        //Save Changes to XML Doc
                        FileWriter fileWriter = new FileWriter(path);
                        fileWriter.write(formatXml(document));
                        fileWriter.close();
                        break; //Stop Further Interations when done.
                    }
                }
            }
        } catch(Exception e){
            e.printStackTrace();
        } 
    }
    
    public void DeleteContactNode(Contact contact){
        try {
            DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
            Document document = dBuilder.parse(contactsFile);
            document.getDocumentElement().normalize();
            
            NodeList contactsNodeList = document.getElementsByTagName("contact");
            
            for(int i=0; i<contactsNodeList.getLength(); i++){
                Node contactNode = contactsNodeList.item(i);
                
                if(contactNode.getNodeType() == Node.ELEMENT_NODE){
                    //Cast Contact Node to Element
                    Element contactElement = (Element) contactNode;
                    String contactNo = contactElement.getElementsByTagName("phone").item(0).getTextContent();
                   
                    if(contactNo.trim().matches(contact.Phone.trim()))
                        document.getChildNodes().item(0).removeChild(contactNode);
                }
            }
            
            //Save Changes to XML Doc
            FileWriter fileWriter = new FileWriter(path);
            fileWriter.write(formatXml(document));
            fileWriter.close();
            
        } catch(Exception e){
            e.printStackTrace();
        }
    }
    
    void AddContact(String Name, String Phone, String Email, String Street, String City, String State, String ZipCode) 
            throws org.xml.sax.SAXException{
        try{
            DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
            Document document = dBuilder.parse(contactsFile);
            
            //Get number of contacts
            NodeList contactsNodeList = document.getElementsByTagName("contact");
            int contactId = contactsNodeList.getLength()+1;
            
            Element rootElement = document.getDocumentElement();
            
            //Create new contact node
            Element newContactElement = document.createElement("contact");
            rootElement.appendChild(newContactElement);
            
            //Create id for new element
            Element Id = document.createElement("id");
            Id.appendChild(document.createTextNode(contactId+""));
            newContactElement.appendChild(Id);
            
            //Create Other Contact details
            Element name = document.createElement("name");
            name.appendChild(document.createTextNode(Name));
            newContactElement.appendChild(name);
            
            Element phone = document.createElement("phone");
            phone.appendChild(document.createTextNode(Phone));
            newContactElement.appendChild(phone);
            
            Element email = document.createElement("email");
            email.appendChild(document.createTextNode(Email));
            newContactElement.appendChild(email);
            
            //Addresses
            Element address = document.createElement("address");
            newContactElement.appendChild(address);
            
            Element street = document.createElement("street");
            street.appendChild(document.createTextNode(Street));
            address.appendChild(street);
            
            Element city = document.createElement("city");
            city.appendChild(document.createTextNode(City));
            address.appendChild(city);
            
            Element state = document.createElement("state");
            state.appendChild(document.createTextNode(State));
            address.appendChild(state);
            
            Element zipCode = document.createElement("zipCode");
            zipCode.appendChild(document.createTextNode(ZipCode));
            address.appendChild(zipCode);
            
            // Save the updated XML file
            FileWriter fileWriter = new FileWriter(path);
            fileWriter.write(formatXml(document));
            fileWriter.close();
            
            //Load tracking array
            ContactBuilder contactBuilder = new ContactBuilder();
            contactBuilder.setName(Name);
            contactBuilder.setPhone(Phone);
            contactBuilder.setEmail(Email);
            contactBuilder.setStreet(Street);
            contactBuilder.setState(State);
            contactBuilder.setCity(City);
            contactBuilder.setZipCode(ZipCode);
                    
            Contact newContact = contactBuilder.getContact();
        
            contacts[contactsCount] = newContact;
            contactsCount++;
        } catch(NullPointerException e){
            System.out.println("Null Check");
        }catch (ParserConfigurationException e){
            e.printStackTrace();
        } catch (IOException ex) {
            Logger.getLogger(ContactsManager.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    // Helper method to format XML for better readability
    private static String formatXml(Document document) {
        try {
            // Set up the transformer
            Transformer transformer = TransformerFactory.newInstance().newTransformer();
            transformer.setOutputProperty(OutputKeys.INDENT, "yes");
            transformer.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", "4"); // Adjust indentation size here to 4

            // Convert the Document to a String
            StringWriter stringWriter = new java.io.StringWriter();
            StreamResult streamResult = new javax.xml.transform.stream.StreamResult(stringWriter);
            transformer.transform(new javax.xml.transform.dom.DOMSource(document), streamResult);

            // Return the formatted XML
            return stringWriter.toString();

        } catch (TransformerException e) {
            e.printStackTrace();
            return null;
        }
    }
    
    protected Contact[] getContacts()
    {
        if(contacts[0].Name != null)
            return contacts;
        return null;
    }
    
    protected int getContactsCount(){
        return contactsCount;
    }
    
    Contact searchContact(String Name){
        for(Contact c : contacts)
        {
            if(c.Name.toLowerCase().equals(Name.toLowerCase())) return c;
        }
        return null;
    }
}
