import java.util.LinkedList;
import java.util.List;
import java.util.Properties;
import java.util.concurrent.CountDownLatch;
import org.apache.kafka.common.serialization.Serdes;
import org.apache.kafka.streams.KafkaStreams;
import org.apache.kafka.streams.KeyValue;
import org.apache.kafka.streams.StreamsBuilder;
import org.apache.kafka.streams.StreamsConfig;
import org.apache.kafka.streams.Topology;
import org.apache.kafka.streams.kstream.KStream;

public class StatelessTransformationsKafka {

    public static void main(String [] args){
    
          final Properties props = new Properties();
          props.put(StreamsConfig.APPLICATION_ID_CONFIG, "stateless-transformations-example");
          props.put(StreamsConfig.BOOTSTRAP_SERVERS_CONFIG, "localhost:9092");
          props.put(StreamsConfig.CACHE_MAX_BYTES_BUFFERING_CONFIG, 0)'
          
          props.put(StreamsConfig.DEFAULT_KEY_SERDE_CLASS_CONFIG, Serdes.String().getClass().getName());
          props.put(StreamsConfig.DEFAULT_KEY_SERDE_CLASS_CONFIG, Serdes.String().getClass().getName());
          
          final StreamsBuilder builder = new StreamsBuilder();
          final KStream<String, String> source = builder.stream("stateless-transformations-input-topic");
          
          //Branch: splits streams into 1 or more streams based on a predicate
          KStream<String, String> [] branches = source.branch((key, value)-> key.startsWith("a"), (key, value)->true);
          KStream<String, String> aKeysStream = branches[0];
          KStream<String, String> othersStream = branches[1];
          
          //filter: remove records based on a condition
          aKeysStream = aKeysStream.filter((key,value)->value.startsWith("a"));
          
          //flatmap: map a record to 0, 1 or more records
          aKeysStream = aKeysStream.flatMap((key, value)->{
              List<KeyValue<String, String>> result = new LinkedList<>();
              result.add(KeyValue.pair(key, value.toUpperCase()));
              result.add(KeyValue.pair(key, value.toLowerCase()));
              return result;
          
          });
          
          // map: map 1 record to 1 record
          aKeysStream = aKeysStream.map((key, value)-> KeyValue.pair(key.toUpperCase(), value));
          
          //merge: Merge the 2 streams back together
          KStream<String, String> mergedStream = aKeysStream.merge(othersStream);
          
          //output the transformed data to a topic
          mergedStream.to("stateless-transformation-output-topic");
          
          final Topology topology = builder.build();
          final KafkaStreams streams = new KafkaStreams(topology, props);
          
          System.out.println(topology.describe());
          
          final CountDownLatch latch = new CountDownLatch(1);

        // Attach a shutdown handler to catch control-c and terminate the application gracefully.
        Runtime.getRuntime().addShutdownHook(new Thread("streams-shutdown-hook") {
            @Override
            public void run() {
                streams.close();
                latch.countDown();
            }
        });

        try {
            streams.start();
            latch.await();
        } catch (final Throwable e) {
            System.out.println(e.getMessage());
            System.exit(1);
        }
        System.exit(0);
          
          
    
    }

      

}
