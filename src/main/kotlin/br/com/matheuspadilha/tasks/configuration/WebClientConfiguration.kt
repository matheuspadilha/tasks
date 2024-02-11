package br.com.matheuspadilha.tasks.configuration

import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.web.reactive.function.client.WebClient
import org.springframework.web.util.DefaultUriBuilderFactory

@Configuration
class WebClientConfiguration {

    @Bean
    fun viaCep(builder: WebClient.Builder, @Value("\${via.cep.url}") url: String): WebClient {
        return getWebClient(builder, url)
    }

    private fun getWebClient(builder: WebClient.Builder, url: String): WebClient {
        return builder
            .uriBuilderFactory(DefaultUriBuilderFactory(url))
            .build()
    }
}