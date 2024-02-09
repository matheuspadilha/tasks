package br.com.matheuspadilha.tasks.service;

import br.com.matheuspadilha.tasks.client.ViaCepClient;
import br.com.matheuspadilha.tasks.model.Address;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Service
@RequiredArgsConstructor
public class AddressService {

    private static final Logger LOGGER = LoggerFactory.getLogger(AddressService.class);

    private final ViaCepClient viaCepClient;

    public Mono<Address> getAddress(String zipCode) {
        return Mono.just(zipCode)
                .doOnNext(it -> LOGGER.info("Getting address to zipcode {}", zipCode))
                .flatMap(viaCepClient::getAddress)
                .doOnError(it -> LOGGER.error("Error during consult address"));

    }
}
