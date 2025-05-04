package com.trungduc.drinkshop.entity.composite_key;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class UserRoleId implements Serializable {

    private Long user;
    private Long role;

    // Constructors, equals, hashcode methods, and other necessary logic
}
