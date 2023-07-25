package com.example.demo;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.reactive.server.WebTestClient;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class DemoApplicationTests {

	@Autowired
	private WebTestClient webClient;

	@Test
	void testCreate() throws Exception {
		this.webClient.post().uri("/").bodyValue("title=Foo")
				.header("Content-Type", "application/x-www-form-urlencoded; charset=UTF-8")
				.exchange()
				.expectStatus()
				.isOk().expectBody(String.class).value(value -> {
					assertThat(value).contains("<form id=\"new-todo\"");
				});
	}

}
