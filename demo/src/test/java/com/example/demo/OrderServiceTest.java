package com.example.demo;

import com.example.demo.domain.Address;
import com.example.demo.domain.Member;
import com.example.demo.domain.Order;
import com.example.demo.domain.OrderStatus;
import com.example.demo.domain.item.Book;
import com.example.demo.domain.item.Item;
import com.example.demo.exception.NotEnoughStockException;
import com.example.demo.repository.OrderRepository;
import com.example.demo.service.OrderService;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest
@Transactional
public class OrderServiceTest {

    @PersistenceContext
    private EntityManager em;
    @Autowired
    private OrderService orderService;
    @Autowired
    private OrderRepository orderRepository;

    @Test
    public void 상품주문() {
        //given
        Member member = createMember("회원1", "서울", "테헤란로", "12345");
        Item book = createBook("시골 JPA", 10000, 10);
        int orderCount = 2;

        //when
        Long orderId = orderService.order(member.getId(), book.getId(), orderCount);

        //then
        Order getOrder = orderRepository.findOne(orderId);
        assertThat(getOrder.getOrderItems().size()).isEqualTo(1);
        assertThat(getOrder.getStatus()).isEqualTo(OrderStatus.ORDER);
        assertThat(getOrder.getTotalPrice()).isEqualTo(10000 * orderCount);
        assertThat(book.getStockQuantity()).isEqualTo(8);
    }

    private Item createBook(String name, int price, int stockQuantity) {
        Item book = new Book();
        book.setName(name);
        book.setPrice(price);
        book.setStockQuantity(stockQuantity);
        em.persist(book);
        return book;
    }

    private Member createMember(String name, String city, String street, String zipcode) {
        Member member = new Member();
        member.setName(name);
        member.setAddress(new Address(city, street, zipcode));
        em.persist(member);
        return member;
    }
}
