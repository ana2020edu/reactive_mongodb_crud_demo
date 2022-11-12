package com.example.reactive_mongodb_crud_demo;

import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/item")
public class ItemController {

    private final ItemRepository itemRepository;

    public ItemController(ItemRepository itemRepository) {
        this.itemRepository = itemRepository;
    }

    // Create & Update
    @PostMapping()
    public Mono<Item> upsert(@RequestBody Item item) {
        return itemRepository.save(item);
    }

    // Read One
    @GetMapping("/{id}")
    public Mono<Item> get(@PathVariable("id") Long id) {
        return itemRepository.findById(id);
    }

    // Read All
    @GetMapping()
    public Flux<Item> getAll() {
        return itemRepository.findAll();
    }

    // Delete
    @DeleteMapping("/{id}")
    public Mono<Void> del(@PathVariable("id") Long id) {
        return itemRepository.deleteById(id);
    }
}
