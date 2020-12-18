

import java.util.Properties;
import java.util.concurrent.CountDownLatch;
import org.apache.kafka.common.serialization.Serdes;
import org.apache.kafka.streams.KafkaStreams;
import org.apache.kafka.streams.StreamsBuilder;
import org.apache.kafka.streams.StreamsConfig;
import org.apache.kafka.streams.Topology;
import org.apache.kafka.streams.kstream.Grouped;
import org.apache.kafka.streams.kstream.KStream;
import org.apache.kafka.streams.kstream.KTable;
import org.apache.kafka.streams.kstream.Produced;
public class Main {
    public static void main(String[] args) {

            final Properties props = new Properties();
            props.put(StreamsConfig.APPLICATION_ID_CONFIG, "inventory-data");
            props.put(StreamsConfig.BOOTSTRAP_SERVERS_CONFIG, "localhost:9092");
            props.put(StreamsConfig.CACHE_MAX_BYTES_BUFFERING_CONFIG, 0);
            //SINCE THE INPUT TOPIC USES STRINGS FOR BOTH KEY AND VALUE, SET THE DEFAULT SERDES TO STRING

            props.put(StreamsConfig.DEFAULT_KEY_SERDE_CLASS_CONFIG, Serdes.String().getClass().getName());
            props.put(StreamsConfig.DEFAULT_VALUE_SERDE_CLASS_CONFIG, Serdes.String().getClass().getName());

                //This gets the source stream
            final StreamsBuilder builder = new StreamsBuilder();
            final KStream<String, String> source = builder.stream("inventory_purchases");

            //Convert the value from stream to Integer
            //If value is not formatted as an int, then set to 0
            final KStream<String, Integer> integerValuesSource = source.mapValues( value -> {
                                try{
                                        return Integer.valueOf(value);
                                }catch(NumberFormatException e){
                                        return 0;
                                }
                                 }
                            );


                //Group by the key and reduce to provide a total quantity for each key
           final KTable<String, Integer> productCounts = integerValuesSource.groupByKey(Grouped.with(Serdes.String(), Serdes.Integer()))
                   .reduce((total, newQuantity) -> total + newQuantity)
                   ;

           //output to the output topic
           productCounts.toStream()
                   .to("total_purchases", Produced.with(Serdes.String(), Serdes.Integer()));

           final Topology topology = builder.build();
           final KafkaStreams streams = new KafkaStreams(topology, props);
           //Print the topology to the console
           System.out.println(topology.describe());
           final CountDownLatch latch = new CountDownLatch(1);


   Runtime.getRuntime().addShutdownHook(new Thread("streams-wordcount-shutdown-hook") {
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
