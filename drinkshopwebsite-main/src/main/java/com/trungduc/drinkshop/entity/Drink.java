package com.trungduc.drinkshop.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;

@Getter
@Setter
@Entity
@Table(name = "drinks")
public class Drink extends AbstractBase {


    @Column(name = "title")
    private String title;

    @Column(name = "brand")
    private String author;

    @Column(name = "manufacturer")
    private String publisher;

    @Column(name = "published_date")
    private Date publishedDate;

    @ManyToOne
    @JoinColumn(name = "category_id")
    private Category category;

    @Column(name = "original_price")
    private Double originalPrice;

    @Column(name = "sale_price")
    private Double salePrice;

    @Column(name = "qty")
    private Integer qty;

    @Column(name = "volume_ml")
    private Integer numberOfPages;

    @Lob
    @Column(nullable = false, length = 1000)
    private String description;

    @Column(name = "cover_image")
    private String coverImage;

    @Column(name = "buy_count")
    private Integer buyCount;

    @Column(name = "total_revenue")
    private Double totalRevenue;


    @JsonIgnore
    @ManyToMany(mappedBy = "favoriteDrinks", fetch = FetchType.EAGER)
    private Set<User> usersWhoFavorited;

    @JsonIgnore
    @OneToMany(mappedBy = "drink", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<OrderDetail> orderDetails = new HashSet<>();

    public void addUser(User user) {
        Set<User> users = this.getUsersWhoFavorited();
        if (usersWhoFavorited == null) {
            usersWhoFavorited = new HashSet<>();
        }
        usersWhoFavorited.add(user);
        this.setUsersWhoFavorited(users);
    }

    public void removeUserWhoFavorited(User user) {
        if (this.usersWhoFavorited != null) {
            this.usersWhoFavorited.remove(user);
        }
    }

    public void addOrderDetail(OrderDetail orderDetail) {
        orderDetail.setDrink(this);
        orderDetails.add(orderDetail);
    }
}