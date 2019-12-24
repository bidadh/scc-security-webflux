package com.ideabaker.samples.scc.security.consumerwebflux

import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.springframework.boot.test.autoconfigure.json.AutoConfigureJsonTesters
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.cloud.contract.stubrunner.spring.AutoConfigureStubRunner
import org.springframework.cloud.contract.stubrunner.spring.StubRunnerProperties
import org.springframework.http.HttpHeaders
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import org.springframework.test.context.junit4.SpringRunner
import org.springframework.test.web.reactive.server.WebTestClient

/**
 *
 * @author Arthur Kazemi<bidadh@gmail.com>
 * @since 24/12/19 23:21
 */
@RunWith(SpringRunner::class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.NONE)
@AutoConfigureStubRunner(
    ids = ["com.ideabaker.samples.scc.security:secured-producer-webflux:+:stubs:6565"],
    stubsMode = StubRunnerProperties.StubsMode.LOCAL
)
@AutoConfigureJsonTesters
class ContactConsumerTest {
  lateinit var webTestClient: WebTestClient

  @Before
  fun before() {
    webTestClient = WebTestClient
        .bindToServer()
        .baseUrl("http://localhost:6565")
        .build()
  }

  @Test
  fun givenUnauthorizedUser_whenSearchContacts_shouldRespond401() {
    webTestClient
        .get()
        .uri("/contact/search")
        .accept(MediaType.APPLICATION_JSON)
        .exchange()
        .expectStatus()
        .isEqualTo(HttpStatus.UNAUTHORIZED)
  }

  @Test
  fun givenAuthorizedUser_whenSearchContacts_shouldRespondOK() {
    webTestClient
        .get()
        .uri("/contact/search?query=existing")
        .accept(MediaType.APPLICATION_JSON)
        .header(HttpHeaders.AUTHORIZATION, "FAKE_AUTH")
        .exchange()
        .expectStatus()
        .isEqualTo(HttpStatus.OK)
    //TODO: validate body here!
  }
}