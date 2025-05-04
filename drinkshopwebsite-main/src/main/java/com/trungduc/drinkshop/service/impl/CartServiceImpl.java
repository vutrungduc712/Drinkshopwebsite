package com.trungduc.drinkshop.service.impl;

import com.trungduc.drinkshop.dto.CartDTO;
import com.trungduc.drinkshop.dto.CartItemDTO;
import com.trungduc.drinkshop.service.CartService;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class CartServiceImpl implements CartService {

    @Override
    public void addToCart(HttpSession session, CartItemDTO cartItem) {
        CartDTO cart = getCart(session);
        List<CartItemDTO> cartItems = cart.getCartItems();

        // Kiểm tra xem mục đã tồn tại trong giỏ hàng chưa
        Optional<CartItemDTO> existingCartItem = cartItems.stream()
                .filter(item -> item.getDrinkId().equals(cartItem.getDrinkId()))
                .findFirst();

        if (existingCartItem.isPresent()) {
            // Cập nhật số lượng nếu mục đã tồn tại
            CartItemDTO existingItem = existingCartItem.get();
            existingItem.setQuantity(existingItem.getQuantity() + cartItem.getQuantity());
        } else {
            // Thêm mới mục vào giỏ hàng
            cartItems.add(cartItem);
        }

        session.setAttribute("cart", cart);
    }


    @Override
    public void updateCartItemQuantity(HttpSession session, Long productId, int quantity) {
        CartDTO cart = getCart(session);
        List<CartItemDTO> cartItems = cart.getCartItems();

        // Tìm mục cần cập nhật
        Optional<CartItemDTO> cartItemToUpdate = cartItems.stream()
                .filter(item -> item.getDrinkId().equals(productId))
                .findFirst();

        cartItemToUpdate.ifPresent(item -> item.setQuantity(quantity));

        session.setAttribute("cart", cart);
    }


    @Override
    public void removeCartItem(HttpSession session, Long productId) {
        CartDTO cart = getCart(session);
        List<CartItemDTO> cartItems = cart.getCartItems();

        cartItems.removeIf(item -> item.getDrinkId().equals(productId));

        session.setAttribute("cart", cart);
    }


    @Override
    public void clearCart(HttpSession session) {
        CartDTO cart = new CartDTO();
        session.setAttribute("cart", cart);
    }

    @Override
    public CartDTO getCart(HttpSession session) {
        CartDTO cart = (CartDTO) session.getAttribute("cart");
        if (cart == null) {
            cart = new CartDTO();
            session.setAttribute("cart", cart);
        }
        return cart;
    }


}
