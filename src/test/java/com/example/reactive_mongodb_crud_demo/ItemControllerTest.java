package com.example.reactive_mongodb_crud_demo;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.reactive.server.EntityExchangeResult;
import org.springframework.test.web.reactive.server.WebTestClient;
import org.springframework.web.reactive.function.BodyInserters;

import java.util.Random;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class ItemControllerTest {

    final static Random random = new Random();
    @Autowired
    private WebTestClient webTestClient;

    Item makeRandomItem() {
        return new Item(Math.abs(random.nextLong()), Math.abs(random.nextInt()));
    }

    @Test
    void deleteAndGet() {
        Item item = makeRandomItem();

        webTestClient.post().uri("/item")
                .accept(MediaType.APPLICATION_JSON)
                .body(BodyInserters.fromValue(item))
                .exchange()
                .expectStatus().isOk();

        webTestClient.delete().uri("/item/" + item.getId())
                .exchange()
                .expectStatus().isOk();

        webTestClient.get().uri("/item/" + item.getId())
                .exchange()
                .expectStatus().isOk()
                .expectBody().isEmpty(); // It's empty because I deleted it
    }

    @Test
    void putOneAndGet() {
        Item item = makeRandomItem();

        webTestClient.post().uri("/item")
                .accept(MediaType.APPLICATION_JSON)
                .body(BodyInserters.fromValue(item))
                .exchange()
                .expectStatus().isOk();

        webTestClient.get().uri("/item/" + item.getId())
                .exchange()
                .expectStatus().isOk()
                .expectBody(Item.class)
                .isEqualTo(item);
    }

    @Test
    void putX2AndGetAll() {
        Item item1 = makeRandomItem();
        Item item2 = makeRandomItem();

        webTestClient.post().uri("/item")
                .accept(MediaType.APPLICATION_JSON)
                .body(BodyInserters.fromValue(item1))
                .exchange()
                .expectStatus().isOk();

        webTestClient.post().uri("/item")
                .accept(MediaType.APPLICATION_JSON)
                .body(BodyInserters.fromValue(item2))
                .exchange()
                .expectStatus().isOk();

        webTestClient.get().uri("/item/")
                .exchange()
                .expectStatus().isOk()
                .expectBodyList(Item.class).hasSize(2).contains(item1).contains(item2);
    }


    @Test
    void upsert() {
        Item item = makeRandomItem();

        webTestClient.post().uri("/item")
                .accept(MediaType.APPLICATION_JSON)
                .body(BodyInserters.fromValue(item))
                .exchange()
                .expectStatus().isOk();

        item.setPrice(Math.abs(random.nextInt()));

        webTestClient.post().uri("/item")
                .accept(MediaType.APPLICATION_JSON)
                .body(BodyInserters.fromValue(item))
                .exchange()
                .expectStatus().isOk();

        EntityExchangeResult<Item> resp = webTestClient.get().uri("/item/" + item.getId())
                .exchange()
                .expectStatus().isOk()
                .expectBody(Item.class)
                .isEqualTo(item)
                .returnResult();

        // System.out.println(resp); // If you want to know the actual value, print it out
    }

}