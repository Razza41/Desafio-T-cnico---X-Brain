package com.xbrain.pedidos.service;

import com.xbrain.pedidos.entity.PedidoEntity;
import com.xbrain.pedidos.repository.PedidoRepository;
import lombok.RequiredArgsConstructor;

import org.springframework.stereotype.Service;


import java.util.List;

@Service
@RequiredArgsConstructor
public class PedidoService {

    private final PedidoRepository pedidoRepository;

    public PedidoEntity criarPedido(PedidoEntity novoPedido){
       return pedidoRepository.save(novoPedido);
    }

    public List<PedidoEntity> listarTodos(){
        return pedidoRepository.findAll();
    }

    public PedidoEntity buscarPorId(Long id) {
        return pedidoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Pedido não encontrado!"));
    }
}
