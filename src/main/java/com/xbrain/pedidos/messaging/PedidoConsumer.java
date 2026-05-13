package com.xbrain.pedidos.messaging;

import com.xbrain.pedidos.config.RabbitMQConfig;
import com.xbrain.pedidos.entity.EntregaEntity;
import com.xbrain.pedidos.entity.PedidoEntity;
import com.xbrain.pedidos.repository.EntregaRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@Component
@Slf4j
@RequiredArgsConstructor
public class PedidoConsumer {

    private final EntregaRepository entregaRepository;

    @RabbitListener(queues = RabbitMQConfig.PEDIDOS_QUEUE)
    public void consumir(PedidoEntity pedidoEntity){

        try {
            //Recebe id na fila
            log.info("Pedido recebido da fila: {}", pedidoEntity.getId());
            EntregaEntity entrega = new EntregaEntity();

            //set id e endereco na entrega
            entrega.setIdPedido(pedidoEntity.getId());
            entrega.setEndereco(pedidoEntity.getEndereco());

            entregaRepository.save(entrega);

            log.info("Entrega criada para o pedido: {}", pedidoEntity.getId());
        }
        catch (Exception e){
            log.info("Erro ao processar pedido! "+e);
        }

    }

}
