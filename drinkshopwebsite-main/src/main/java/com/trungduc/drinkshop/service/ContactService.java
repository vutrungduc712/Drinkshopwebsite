package com.trungduc.drinkshop.service;

import com.trungduc.drinkshop.entity.Contact;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface ContactService {
    Contact saveContact(Contact contact);

    Page<Contact> getContactsPage(String sortBy, Pageable pageable);

    void deleteById(Long id);

    Contact getContactById(Long id);
}
