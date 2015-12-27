package cn.believeus.jms;
import javax.annotation.Resource;  
import javax.jms.Destination;  
import javax.jms.JMSException;  
import javax.jms.Message;  
import javax.jms.Session;  
import javax.jms.TextMessage;
   
import org.springframework.jms.core.JmsTemplate;  
import org.springframework.jms.core.MessageCreator;  
import org.springframework.stereotype.Component;  
 
   
@Component  
public class ProducerTopicService {  
   
	@Resource(name="topicJmsTemplate")
    private JmsTemplate topicJmsTemplate;  
      
    public void sendMessage(Destination destination, final String message) {  
        System.out.println("topic服务端发送消息:" + message);  
        topicJmsTemplate.send(destination, new MessageCreator() { 
        	TextMessage textMessage;
        	@Override
            public Message createMessage(Session session){  
                 try {
                	 textMessage = session.createTextMessage(message);
				} catch (JMSException e) {
					e.printStackTrace();
				}
                return textMessage;
            }  
        });
    }    
}  