package com.xbrain.pedidos.entity;


import jakarta.persistence.Embeddable;
import jakarta.validation.constraints.NotBlank;
import lombok.*;

@Embeddable
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class EnderecoEntity{

@NotBlank (message = "Rua é obrigatório!")
private String rua;

@NotBlank (message = "Bairro é obrigatório!")
private String bairro;

@NotBlank (message = "Cidade é obrigatório!")
private String cidade;

@NotBlank (message = "CEP é obrigatório!")
private String cep;




}
