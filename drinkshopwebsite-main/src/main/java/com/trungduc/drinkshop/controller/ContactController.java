package com.trungduc.drinkshop.controller;

import com.trungduc.drinkshop.controller.common.BaseController;
import com.trungduc.drinkshop.entity.Contact;
import com.trungduc.drinkshop.service.ContactService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@AllArgsConstructor
@Controller
@RequestMapping("/contact")
public class ContactController extends BaseController {
    private ContactService contactService;

    @GetMapping
    public String getContactPage(Model model) {
        model.addAttribute("contact", new Contact());
        return "user/contact";
    }

    @PostMapping("/submit")
    public String submitContactForm(@ModelAttribute Contact contact,
                                    RedirectAttributes redirectAttributes) {
        Contact savedContact = contactService.saveContact(contact);
        redirectAttributes.addFlashAttribute("thankForContacting", "Cảm ơn bạn đã liên hệ");
        return "redirect:/contact?success=true";
    }
}
