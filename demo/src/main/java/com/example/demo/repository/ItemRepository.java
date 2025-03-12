package com.example.demo.repository;

import com.example.demo.domain.item.Item;
import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@RequiredArgsConstructor // generate a constructor with a parameter
// public ItemRepository(EntityManager em) { this.em = em; }
public class ItemRepository {
    private final EntityManager em;

    public void save(Item item) {
        if (item.getId() == null) { // it is a new item
            em.persist(item); // save
        } else {
            em.merge(item); // updating existing entities
        }
    }

    public Item findOne(Long id) {
        return em.find(Item.class, id);
    }

    public List<Item> findAll() {
        return em.createQuery("select i from Item i", Item.class)
                .getResultList();
    }
}
