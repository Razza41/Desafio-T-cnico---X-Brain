package com.xbrain.pedidos.messaging;

import com.xbrain.pedidos.config.RabbitMQConfig;
import com.xbrain.pedidos.entity.PedidoEntity;
import lombok.RequiredArgsConstructor;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class PedidoProducer {

    private final RabbitTemplate rabbitTemplate;

    public void publicar(PedidoEntity pedido) {
        rabbitTemplate.convertAndSend(RabbitMQConfig.PEDIDOS_QUEUE, pedido);
    }
}
