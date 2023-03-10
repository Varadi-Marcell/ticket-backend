package com.example.JPA.service.serviceImpl;

import com.example.JPA.dto.CartDto;
import com.example.JPA.dto.CreateItemRequest;
import com.example.JPA.dto.ItemDto;
import com.example.JPA.dto.UpdateItemDto;
import com.example.JPA.model.Cart;
import com.example.JPA.model.Item;
import com.example.JPA.repository.CartRepository;
import com.example.JPA.repository.ItemRepository;
import com.example.JPA.repository.TicketRepository;
import com.example.JPA.repository.UserRepository;
import com.example.JPA.service.CartService;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class CartServiceImpl implements CartService {

    private final CartRepository cartRepository;
    private final UserRepository userRepository;
    private final ItemRepository itemRepository;
    private final TicketRepository ticketRepository;

    public CartServiceImpl(CartRepository cartRepository, UserRepository userRepository, ItemRepository itemRepository, TicketRepository ticketRepository) {
        this.cartRepository = cartRepository;
        this.userRepository = userRepository;
        this.itemRepository = itemRepository;
        this.ticketRepository = ticketRepository;
    }

    @Override
    public Optional<CartDto> getShoppingCartById(Long id) {
        Cart cart = cartRepository.findById(id).get();

        List<ItemDto> itemDtos = cart.getItemList().stream()
                .map(item -> new ItemDto(item, ticketRepository.findById(item.getTicketId()).get()))
                .toList();

        return Optional.of(new CartDto(cart.getId(), cart.getAmount(), itemDtos));
    }

    @Override
    public void updateItemQuantity(UpdateItemDto itemDto) {
        Item item = itemRepository.findById(itemDto.getItemId()).get();
        item.setQuantity(itemDto.getQuantity());
        itemRepository.save(item);
    }

    @Override
    public void addItemToCart(CreateItemRequest req) {
        String userEmail = SecurityContextHolder.getContext().getAuthentication().getName();
        Cart cart = userRepository.findByEmail(userEmail).get().getCardPass().getCart();

        Item item = req.toEntity(req);
        cart.addCartItem(item);
        cartRepository.save(cart);
    }

    @Override
    public void removeItemFromCartByItemId(Long itemId) {
        String userEmail = SecurityContextHolder.getContext().getAuthentication().getName();
        Cart cart = userRepository.findByEmail(userEmail).get().getCardPass().getCart();

        cart.removeItemFromCart(itemRepository.findById(itemId).get());
        cartRepository.save(cart);
    }


    @Override
    public void clearCart() {
        String userEmail = SecurityContextHolder.getContext().getAuthentication().getName();
        Cart cart = userRepository.findByEmail(userEmail).get().getCardPass().getCart();

        cart.clearCart();

        cartRepository.save(cart);
    }

    @Override
    public Optional<CartDto> viewCart() {
        String userEmail = SecurityContextHolder.getContext().getAuthentication().getName();
        Cart cart = userRepository.findByEmail(userEmail).get().getCardPass().getCart();

        List<ItemDto> itemDtos = cart.getItemList().stream()
                .map(item -> new ItemDto(item, ticketRepository.findById(item.getTicketId()).get()))
                .toList();

        return Optional.of(new CartDto(cart.getId(), cart.getAmount(), itemDtos));

    }


}
