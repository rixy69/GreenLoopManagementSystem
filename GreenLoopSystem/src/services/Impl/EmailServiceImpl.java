package services.Impl;

import models.Property;
import services.EmailService;
import services.PropertyService;

import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.util.List;
import java.util.Properties;
import java.util.stream.Collectors;

public class EmailServiceImpl implements EmailService {

    public PropertyService propertyService;
    private String username = "kamshika236@gmail.com";
    private String password = "uhwkzenjrjiyjcdo";
    private Properties properties;

    public EmailServiceImpl(){

        //this.username = "kamshika236@gmail.com";
        //this.password = "uhwkzenjrjiyjcdo";

        //this.properties = new Properties();
        //this.properties.put("mail.smtp.auth", "true");
        //this.properties.put("mail.smtp.starttls.enable", "true");
        //this.properties.put("mail.smtp.host", "smtp.gmail.com");
        //this.properties.put("mail.smtp.port", "587");
        //this.properties.put("mail.transport.protocol", "smtp");
        //this.properties.put("mail.debug", "true");

    }

    public EmailServiceImpl(PropertyService propertyService){
        this.propertyService = propertyService;

        Property usernameProperty = propertyService.getPropertyByKeyAndType("username", "email");
        Property passwordProperty = propertyService.getPropertyByKeyAndType("password", "email");

        if (usernameProperty != null && usernameProperty.getValue() != null) {
            this.username = usernameProperty.getValue();
        }
        if (passwordProperty != null && passwordProperty.getValue() != null) {
            this.password = passwordProperty.getValue();
        }

        this.properties = new Properties();
        this.properties.put("mail.smtp.auth", "true");
        this.properties.put("mail.smtp.starttls.enable", "true");
        this.properties.put("mail.smtp.host", "smtp.gmail.com");
        this.properties.put("mail.smtp.port", "587");
        this.properties.put("mail.transport.protocol", "smtp");
        this.properties.put("mail.debug", "true");

        propertyService.getAllPropertiesByType("email").stream()
                .filter(property -> property != null)
                .filter(property -> !"username".equals(property.getPropertyKey()))
                .filter(property -> !"password".equals(property.getPropertyKey()))
                .forEach(property -> this.properties.put(property.getPropertyKey(), property.getValue()));
    }

    @Override
    public boolean sendEmail(String to, String subject, String body) {

        Session session = Session.getInstance(this.properties, new Authenticator() {
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(username, password);
            }
        });

        try {
            Message message = new MimeMessage(session);
            message.setFrom(new InternetAddress(username));
            message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(to));
            message.setSubject(subject);
            message.setText(body);

            Transport.send(message);

            System.out.println("Email sent successfully");
            return true;
        } catch (Exception e) {
            System.out.println(e.getCause());
            e.printStackTrace();
            return false;
        }
    }
}
