package br.com.matheuspadilha.tasks.client

import br.com.matheuspadilha.tasks.exception.NotFoundException
import br.com.matheuspadilha.tasks.model.Address
import org.springframework.stereotype.Component
import org.springframework.web.reactive.function.client.WebClient
import reactor.core.publisher.Mono

@Component
class ViaCepClient(
    private val viaCep: WebClient
) {

    fun getAddress(zipCode: String?): Mono<Address?> {
        return viaCep
            .get()
            .uri(VIA_CEP_URI, zipCode)
            .retrieve()
            .bodyToMono(Address::class.java)
            .onErrorResume { _: Throwable? ->
                Mono.error(
                    NotFoundException("CEP Not Found")
                )
            }
    }

    companion object {
        private const val VIA_CEP_URI = "/{cep}/json"
    }
}