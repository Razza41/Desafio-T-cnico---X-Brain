package com.xbrain.pedidos.service;

import com.xbrain.pedidos.entity.PedidoEntity;
import com.xbrain.pedidos.entity.ProdutoEntity;
import com.xbrain.pedidos.messaging.PedidoProducer;
import com.xbrain.pedidos.repository.PedidoRepository;
import lombok.RequiredArgsConstructor;

import org.springframework.stereotype.Service;


import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;

@Service
@RequiredArgsConstructor
public class PedidoService {

    private final PedidoRepository pedidoRepository;
    private final PedidoProducer  pedidoProducer;

    public PedidoEntity criarPedido(PedidoEntity novoPedido){

        //Pega o valor de cada produto e soma
        BigDecimal valorTotal = novoPedido.getListaProdutos()
                .stream()
                .map(ProdutoEntity::getPreco)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        novoPedido.setValorTotal(valorTotal);

       PedidoEntity salvo = pedidoRepository.save(novoPedido);
       pedidoProducer.publicar(salvo);

       return salvo;
    }

    public List<PedidoEntity> listarTodos(){
        return pedidoRepository.findAll();
    }

    public PedidoEntity buscarPorId(Long id) {
        return pedidoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Pedido não encontrado!"));
    }
}
