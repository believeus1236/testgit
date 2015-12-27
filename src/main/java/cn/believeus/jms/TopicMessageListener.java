package cn.believeus.jms;
import javax.jms.JMSException;  
import javax.jms.Message;  
import javax.jms.MessageListener;  
import javax.jms.TextMessage;  
   
public class TopicMessageListener implements MessageListener {  
   
    public void onMessage(Message message) {  
        //这里我们知道生产者发送的就是一个纯文本消息，所以这里可以直接进行强制转换  
        TextMessage textMsg = (TextMessage) message;  
        try {  
            System.out.println("Topic客户端获取消息：" + textMsg.getText());  
        } catch (JMSException e) {  
            e.printStackTrace();  
        }
    }  
   
}  