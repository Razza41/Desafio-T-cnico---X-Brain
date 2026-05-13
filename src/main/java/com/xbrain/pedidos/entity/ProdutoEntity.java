package com.xbrain.pedidos.entity;

import jakarta.persistence.Embeddable;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;

@Embeddable
@Data
public class ProdutoEntity{

    @NotNull
    private Integer codigoProduto;

    @NotBlank
    private String nome;

    @NotNull
    private BigDecimal preco;

}
