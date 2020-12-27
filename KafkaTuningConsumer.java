import java.time.Duration;
import java.util.Arrays;
import java.util.Properties;
import org.apache.kafka.clients.consumer.Consumer;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;


public class KafkaTuningConsumer {

    Consumer <String, String> consumer;
    public KafkaTuningConsumer (){
    
        Properties props = new Properties();
        props.setProperty("bootstrap.servers", "zoo1:9092");
        props.setProperty("group.id", "group1");
        props.setProperty("enable.auto.commit", "false");
        props.setProperty("key.deserializer", "org.apache.kafka.common.serialization.StringDeserializer");
        props.setProperty("value.deserializer", "org.apache.kafka.common.serialization.StringDeserializer");
        
        props.setProperty("fetch.min.bytes", "1024");
        props.setProperty("heartbeat.interval.ms", "2000");
        props.setProperty("auto.offset.reset", "earliest");
        
        consumer = new KafkaConsumer<>(props);
        consumer.subscribe(Arrays.asList("member_signups");
    
    }
    



}
