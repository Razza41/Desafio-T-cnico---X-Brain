package com.xbrain.pedidos.entity;

import jakarta.persistence.Embeddable;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Embeddable
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProdutoEntity {

    @NotNull
    private Integer codigoProduto;

    @NotBlank
    private String nome;

    @NotNull
    private BigDecimal preco;
}