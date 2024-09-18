package com.example.demo.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;


@Controller
public class General {

    @RequestMapping("/")
    public String welcome() {
        return "index";
    }



    @Autowired
    private JavaMailSender mailSender;

    @PostMapping("/contact")
    public String handleContactForm(@RequestParam("name") String name,
                                    @RequestParam("email") String email,
                                    @RequestParam("message") String message,
                                    Model model) {
        try {
            // Create a Simple MailMessage.
            SimpleMailMessage mailMessage = new SimpleMailMessage();
            mailMessage.setTo("recipient-email@example.com"); // Replace with your recipient email
            mailMessage.setSubject("New Contact Form Submission from " + name);
            mailMessage.setText("Name: " + name + "\nEmail: " + email + "\n\nMessage:\n" + message);

            // Send the email.
            mailSender.send(mailMessage);

            model.addAttribute("successMessage", "Thank you for reaching out, " + name + "! I'll get back to you soon.");
        } catch (Exception e) {
            model.addAttribute("errorMessage", "Sorry, there was an error sending your message. Please try again later.");
            e.printStackTrace(); // Log the exception for debugging
        }
        return "index"; // Ensure you have a contact.html template
    }



}
