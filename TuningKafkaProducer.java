import java.util.Properties;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.Producer;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.clients.producer.RecordMetadata;

public class TuningKafkaProducer {


    Producer <Integer, String> producer;
    
    public TuningKafkaProducer(){
    
        Properties props = new Properties();
        props.put("bootstrap.server", "zoo1:9092");
        props.put("key.serializer", "org.apache.kafka.common.serialization.IntegerSerializer");
        props.put("value.serializer", "org.apache.kafka.common.serialization.StringSerializer");
        props.put("acks", "all");
        props.put("max.in.flight.requests.per.connection", "1");
        props.put("batch.size", "65536");
        
        producer = new KafkaProducer<>(props);
    
    
    }













}
