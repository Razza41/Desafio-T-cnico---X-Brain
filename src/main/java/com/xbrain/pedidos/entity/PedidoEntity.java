package com.xbrain.pedidos.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;

@Entity
@Data
@Table(name = "pedidos")
public class PedidoEntity{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull (message = "Código do cliente é obrigatório!")
    @Column(nullable = false)
    private Integer codigoCliente;

    @NotEmpty(message = "A lista de produtos não pode ser vazia!")
    @ElementCollection
    @CollectionTable(name = "produtos_pedido",
            joinColumns  = @JoinColumn(name= "pedido_id")) //Cria um tabela auxiliar para lidar com a lista
    private List<ProdutoEntity> listaProdutos;


    @Column (nullable = false)
    private BigDecimal valorTotal;

    @Embedded
    private EnderecoEntity endereco;




}
