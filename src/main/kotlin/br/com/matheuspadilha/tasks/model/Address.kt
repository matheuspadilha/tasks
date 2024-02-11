package br.com.matheuspadilha.tasks.model

import com.fasterxml.jackson.annotation.JsonInclude
import com.fasterxml.jackson.annotation.JsonInclude.Include
import com.fasterxml.jackson.annotation.JsonProperty
import java.io.Serializable

@JsonInclude(Include.NON_NULL)
data class Address(
    @JsonProperty("cep")
    val zipCode: String? = null,

    @JsonProperty("logradouro")
    val street: String? = null,

    @JsonProperty("complemento")
    val complement: String? = null,

    @JsonProperty("bairro")
    val neighborhood: String? = null,

    @JsonProperty("localidade")
    val city: String? = null,

    @JsonProperty("uf")
    val state: String? = null,
) : Serializable {}
