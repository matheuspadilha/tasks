package br.com.matheuspadilha.tasks.service

import br.com.matheuspadilha.tasks.client.ViaCepClient
import br.com.matheuspadilha.tasks.model.Address
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Service
import reactor.core.publisher.Mono

@Service
class AddressService(
    private val viaCepClient: ViaCepClient
) {

    fun getAddress(zipCode: String): Mono<Address?> {
        return Mono.just(zipCode)
            .doOnNext { _: String -> LOGGER.info("Getting address to zipcode {}", zipCode) }
            .flatMap { _: String -> viaCepClient.getAddress(zipCode) }
            .doOnError { _: Throwable -> LOGGER.error("Error during consult address") }
    }

    companion object {
        private val LOGGER: Logger = LoggerFactory.getLogger(this::class.java)
    }
}