package com.example.JPA.service.serviceImpl;

import com.example.JPA.dto.CartDto;
import com.example.JPA.dto.item.CreateItemRequest;
import com.example.JPA.dto.item.ItemDto;
import com.example.JPA.dto.item.UpdateItemDto;
import com.example.JPA.exceptions.ResourceNotFoundException;
import com.example.JPA.model.Cart;
import com.example.JPA.model.Item;
import com.example.JPA.model.Ticket;
import com.example.JPA.repository.CartRepository;
import com.example.JPA.repository.ItemRepository;
import com.example.JPA.repository.TicketRepository;
import com.example.JPA.repository.UserRepository;
import com.example.JPA.service.CartService;
import com.example.JPA.service.UserService;
import lombok.AllArgsConstructor;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.context.WebApplicationContext;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Service
@AllArgsConstructor
@Scope(value = WebApplicationContext.SCOPE_SESSION, proxyMode = ScopedProxyMode.TARGET_CLASS)
public class CartServiceImpl implements CartService {
    private final CartRepository cartRepository;
    private final UserService userService;
    private final ItemRepository itemRepository;
    private final TicketRepository ticketRepository;
    @Override
    public Optional<CartDto> getShoppingCartById(Long id) {
        Cart cart = cartRepository.findById(id)
                .orElseThrow(()-> new ResourceNotFoundException("Cart with id:"+id+"not found!"));

        List<ItemDto> itemDtos = cart.getItemList().stream()
                .map(item -> new ItemDto(item, ticketRepository.findById(item.getTicketId()).get()))
                .toList();

        return Optional.of(new CartDto(cart.getId(), cart.getAmount(), itemDtos));
    }

    @Override
    @Transactional
    public Optional<CartDto> updateItemQuantity(UpdateItemDto itemDto) {
        Cart cart = userService.getUser().getCardPass().getCart();

        Item item = itemRepository.findById(itemDto.getItemId())
                .orElseThrow(()->new ResourceNotFoundException("Item with id:"+itemDto.getItemId()+"not found!"));

        item.setQuantity(itemDto.getQuantity());
        cart.calculateAmount();

        itemRepository.save(item);
        cartRepository.save(cart);


        List<ItemDto> itemDtos = cart.getItemList().stream()
                .map(item1 -> new ItemDto(item1, ticketRepository.findById(item1.getTicketId()).get()))
                .toList();


        return Optional.of(new CartDto(cart.getId(), cart.getAmount(), itemDtos));

    }

    @Transactional
    @Override
    public void addItemToCart(CreateItemRequest req) {
        Cart cart = userService.getUser().getCardPass().getCart();

        Item item = req.toEntity(req);

        cart.addCartItem(item);
        cartRepository.save(cart);
    }

    @Transactional
    @Override
    public Optional<CartDto> removeItemFromCartByItemId(Long itemId) {
        Cart cart = userService.getUser().getCardPass().getCart();

        Item item = itemRepository.findById(itemId).orElseThrow();


        cart.setAmount(cart.getAmount() - (item.getAmount() * item.getQuantity()));
        cart.removeItemFromCart(item);
        cartRepository.save(cart);
        List<ItemDto> itemDtos = cart.getItemList().stream()
                .map(item1 -> new ItemDto(item1, ticketRepository.findById(item1.getTicketId()).get()))
                .toList();

        return Optional.of(new CartDto(cart.getId(), cart.getAmount(), itemDtos));

    }


    @Override
    public void clearCart() {
        Cart cart = userService.getUser().getCardPass().getCart();

        cart.clearCart();

        cartRepository.save(cart);
    }

    @Override
    public Optional<CartDto> viewCart() {
        Cart cart = userService.getUser().getCardPass().getCart();

        List<ItemDto> itemDtos = cart.getItemList().stream()
                .map(item -> new ItemDto(item, ticketRepository.findById(item.getTicketId()).get()))
                .toList();


        return Optional.of(new CartDto(cart.getId(), cart.getAmount(), itemDtos));

    }


}
