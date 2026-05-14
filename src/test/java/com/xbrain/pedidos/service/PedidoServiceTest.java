package com.xbrain.pedidos.service;

import com.xbrain.pedidos.entity.EnderecoEntity;
import com.xbrain.pedidos.entity.PedidoEntity;
import com.xbrain.pedidos.entity.ProdutoEntity;
import com.xbrain.pedidos.messaging.PedidoProducer;
import com.xbrain.pedidos.repository.PedidoRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class PedidoServiceTest {

    @Mock
    private PedidoRepository pedidoRepository;

    @Mock
    private PedidoProducer pedidoProducer;

    @InjectMocks
    private PedidoService pedidoService;

    private PedidoEntity pedido;

    @BeforeEach
    void setUp() {
        ProdutoEntity produto1 = new ProdutoEntity();
        produto1.setPreco(new BigDecimal("50.00"));

        ProdutoEntity produto2 = new ProdutoEntity();
        produto2.setPreco(new BigDecimal("30.00"));

        EnderecoEntity endereco = new EnderecoEntity();
        endereco.setRua("Rua Nova");
        endereco.setBairro("Zona Nova");
        endereco.setCidade("Capao da Canoa");
        endereco.setCep("95900000");

        pedido = new PedidoEntity();
        pedido.setCodigoCliente(1);
        pedido.setEndereco(endereco);
        pedido.setListaProdutos(Arrays.asList(produto1, produto2));
    }

    @Test
    @DisplayName("Deve calcular o valor total somando os preços dos produtos")
    void criarPedido_deveCalcularValorTotal() {
        when(pedidoRepository.save(any(PedidoEntity.class))).thenReturn(pedido);

        pedidoService.criarPedido(pedido);

        assertEquals(new BigDecimal("80.00"), pedido.getValorTotal());
    }

    @Test
    @DisplayName("Deve persistir o pedido no banco de dados")
    void criarPedido_deveSalvarNoBanco() {
        when(pedidoRepository.save(any(PedidoEntity.class))).thenReturn(pedido);

        pedidoService.criarPedido(pedido);

        verify(pedidoRepository, times(1)).save(pedido);
    }

    @Test
    @DisplayName("Deve publicar o pedido na fila após salvar")
    void criarPedido_devePublicarNaFila() {
        PedidoEntity salvo = new PedidoEntity();
        salvo.setId(1L);

        when(pedidoRepository.save(any(PedidoEntity.class))).thenReturn(salvo);

        pedidoService.criarPedido(pedido);

        verify(pedidoProducer, times(1)).publicar(salvo);
    }

    @Test
    @DisplayName("Deve lançar exceção quando pedido não for encontrado pelo ID")
    void buscarPorId_idInexistente_deveLancarExcecao() {
        when(pedidoRepository.findById(99L)).thenReturn(Optional.empty());

        RuntimeException ex = assertThrows(RuntimeException.class,
                () -> pedidoService.buscarPorId(99L));

        assertEquals("Pedido não encontrado!", ex.getMessage());
    }
}