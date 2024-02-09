package br.com.matheuspadilha.tasks.client;

import br.com.matheuspadilha.tasks.exception.NotFoundException;
import br.com.matheuspadilha.tasks.model.Address;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

@Component
@RequiredArgsConstructor
public class ViaCepClient {

    private final WebClient viaCep;
    private static final String VIA_CEP_URI = "/{cep}/json";

    public Mono<Address> getAddress(String zipCode) {
        return viaCep
                .get()
                .uri(VIA_CEP_URI, zipCode)
                .retrieve()
                .bodyToMono(Address.class)
                .onErrorResume(it -> Mono.error(new NotFoundException("CEP Not Found")));
    }
}
