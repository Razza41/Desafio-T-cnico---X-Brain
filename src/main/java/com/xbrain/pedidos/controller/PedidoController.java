package com.xbrain.pedidos.controller;


import com.xbrain.pedidos.PedidosApplication;
import com.xbrain.pedidos.entity.PedidoEntity;
import com.xbrain.pedidos.repository.PedidoRepository;
import com.xbrain.pedidos.service.PedidoService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/pedidos")
@RequiredArgsConstructor
public class PedidoController {

    private final PedidoService pedidoService;

//POST
    @PostMapping
    public ResponseEntity<PedidoEntity> criarPedido(@Valid @RequestBody PedidoEntity pedidoEntity){
        PedidoEntity salvo = pedidoService.criarPedido(pedidoEntity);
        return ResponseEntity.status(HttpStatus.CREATED).body(salvo);
    }

    //GET
    @GetMapping
    public ResponseEntity<List<PedidoEntity>> listarTodos(){
        return ResponseEntity.ok(pedidoService.listarTodos());
    }

    //GET POR ID
    @GetMapping(value = "/{id}")
    public ResponseEntity<PedidoEntity> listarPorID(@PathVariable("id") Long id){
        return ResponseEntity.ok( (pedidoService.buscarPorId(id)));
    }
}
