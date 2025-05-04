package com.trungduc.drinkshop.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.trungduc.drinkshop.enums.Status;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "users")
public class User extends AbstractBase implements UserDetails {

    @Column(unique = true, nullable = false)
    private String email;

    @JsonIgnore
    @Column(nullable = false)
    private String password;

    @Column(name = "full_name")
    private String fullName;

    @Column(name = "phone_number")
    private String phoneNumber;

    @Column(name = "address")
    private String address;

    @Column(name = "status")
    private Status status;

    @JsonIgnore
    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(name = "favorite_items",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "drink_id"))
    private Set<Drink> favoriteDrinks;

    @JsonIgnore
    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(name = "users_roles",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "role_id"))
    private Set<Role> roles = new HashSet<>();

    @JsonIgnore
    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    private Set<Comment> comments;

    @JsonIgnore
    @OneToMany(mappedBy = "user")
    private Set<Blog> blogs;

    public void addFavoriteDrink(Drink drink) {
        Set<Drink> favoriteDrinks = this.getFavoriteDrinks();
        if (favoriteDrinks == null) {
            favoriteDrinks = new HashSet<>();
        }
        favoriteDrinks.add(drink);
        this.setFavoriteDrinks(favoriteDrinks);
    }

    public void removeFavoriteDrink(Drink drink) {
        if (this.favoriteDrinks != null) {
            this.favoriteDrinks.remove(drink);
        }
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return this.roles;
    }

    @Override
    public String getUsername() {
        return this.email;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}

