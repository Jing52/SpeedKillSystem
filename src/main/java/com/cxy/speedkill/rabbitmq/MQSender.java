package com.cxy.speedkill.rabbitmq;

import com.cxy.speedkill.redis.RedisService;
import com.cxy.speedkill.vo.SpeedKillMessageVo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class MQSender {

	private static Logger log = LoggerFactory.getLogger(MQSender.class);
	
	@Autowired
	AmqpTemplate amqpTemplate ;

	@Autowired
	private RabbitTemplate rabbitTemplate;

	public void sendMiaoshaMessage(MiaoshaMessage mm) {
		String msg = RedisService.beanToString(mm);
		log.info("send message:"+msg);
		amqpTemplate.convertAndSend(MQConfig.MIAOSHA_QUEUE, msg);
	}

	/**
	 * 站内信
	 * @param mm
	 */
	public void sendMessage(MiaoshaMessage mm) {
//		String msg = RedisService.beanToString(mm);
		log.info("send message:"+"11111");
		rabbitTemplate.convertAndSend(MQConfig.EXCHANGE_TOPIC,"miaosha_*", "111111111");
	}

    /**
     * 站内信
     * @param
     */
    public void sendRegisterMessage(SpeedKillMessageVo speedKillMessageVo) {
		String msg = RedisService.beanToString(speedKillMessageVo);
        log.info("send message:{}" , msg);
		rabbitTemplate.convertAndSend(MQConfig.MIAOSHATEST,msg);
//        rabbitTemplate.convertAndSend(MQConfig.EXCHANGE_TOPIC,"miaosha_*", msg);
    }
}
