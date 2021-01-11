import java.util.Properties;
import java.util.concurrent.CountDownLatch;
import org.apache.kafka.common.serialization.Serdes;
import org.apache.kafka.streams.KafkaStreams;
import org.apache.kafka.streams.StreamsBuilder;
import org.apache.kafka.streams.StreamsConfig;
import org.apache.kafka.streams.Topology;
import org.apache.kafka.streams.kstream.KGroupedStream;
import org.apache.kafka.streams.kstream.KStream;
import org.apache.kafka.streams.kstream.KTable;
import org.apache.kafka.streams.kstream.Materialized;
import org.apache.kafka.streams.kstream.Produced;

public class KafkaAggregations {

    public static void main(String [] args){
    
    import java.util.Properties;
import java.util.concurrent.CountDownLatch;
import org.apache.kafka.common.serialization.Serdes;
import org.apache.kafka.streams.KafkaStreams;
import org.apache.kafka.streams.StreamsBuilder;
import org.apache.kafka.streams.StreamsConfig;
import org.apache.kafka.streams.Topology;
import org.apache.kafka.streams.kstream.KGroupedStream;
import org.apache.kafka.streams.kstream.KStream;
import org.apache.kafka.streams.kstream.KTable;
import org.apache.kafka.streams.kstream.Materialized;
import org.apache.kafka.streams.kstream.Produced;

      final StreamsBuilder builder = new StreamsBuilder();
      KStream<String, String> source = builder.stream("aggregations-input-topic");
      
      KGroupedStream<String, String> groupedStream = source.groupByKey();
      
      KTable<String, Integer> aggregatedTable = groupedStream.aggregate(
          () -> 0,
          (aggKey, newValue, aggValue) -> aggValue + newValue.length(),
          Materialized.with(Serdes.String(), Serdes.Integer()));
          
      aggregatedTable.toStream().to("aggregations-output-charactercount-topic", Produced.with(Serdes.String(), Serdes.Integer()));
      
      //count the number of records for each key
      KTable<String, Long> countedTable = groupedStream.count(Materialized.with(Sedes.String(), Serdes.Long()));
      countedTable.toStream().to("aggregations-output-count-topic", Produced.with(Serdes.String(), Serdes.Long()));
    
      //Combine the values of all records with the same key into a string separated by spaces
      KTable<String, String> reducedTable = groupedStream.reduce((aggValue, new Value)->aggValue + " " + newValue);
      reducedTable.toStream().to("aggregations-output-reduce-topic");
       final Topology topology = builder.build();
        final KafkaStreams streams = new KafkaStreams(topology, props);
        // Print the topology to the console.
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
