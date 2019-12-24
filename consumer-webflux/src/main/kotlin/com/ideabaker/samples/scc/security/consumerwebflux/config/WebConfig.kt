package com.ideabaker.samples.scc.security.consumerwebflux.config

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.databind.SerializationFeature
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.http.HttpHeaders
import org.springframework.http.codec.ServerCodecConfigurer
import org.springframework.http.codec.json.Jackson2JsonDecoder
import org.springframework.http.codec.json.Jackson2JsonEncoder
import org.springframework.web.reactive.config.EnableWebFlux
import org.springframework.web.reactive.config.WebFluxConfigurer
import org.springframework.web.reactive.function.client.ExchangeFilterFunction
import org.springframework.web.reactive.function.client.WebClient

/**
 *
 * @author Arthur Kazemi<bidadh@gmail.com>
 * @since 2019-01-13 18:47
 */
@EnableWebFlux
@Configuration
class WebConfig : WebFluxConfigurer {
  private val logger = LoggerFactory.getLogger(WebConfig::class.java)

  @Autowired
  lateinit var objectMapper: ObjectMapper

  override fun configureHttpMessageCodecs(configurer: ServerCodecConfigurer) {
    objectMapper.configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false)
    configurer.defaultCodecs().jackson2JsonDecoder(Jackson2JsonDecoder(objectMapper))
    configurer.defaultCodecs().jackson2JsonEncoder(Jackson2JsonEncoder(objectMapper))
    configurer.defaultCodecs().enableLoggingRequestDetails(true)
  }

  @Bean
  fun webClientBuilder(): WebClient.Builder {
    return WebClient.builder()
        .filter(logRequest())
  }

  private fun logRequest(): ExchangeFilterFunction {
    return ExchangeFilterFunction { request, next ->
      if (logger.isDebugEnabled) {
        logger.debug("Request: {} {}", request.method(), request.url())
        request.headers()
            .filter { entry -> entry.key != HttpHeaders.AUTHORIZATION }
            .forEach { (name, values) -> logger.debug("{}={}", name, values.reduce { s1, s2 -> "$s1,$s2" }) }
      }
      next.exchange(request)
    }
  }
}