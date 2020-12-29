import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.MockConsumer;
import org.apache.kafka.clients.consumer.OffsetResetStrategy;
import org.apache.kafka.common.TopicPartition;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class MemberSignupsConsumerTest { 

  MockConsumer<Integer, String> mockConsumer;
  MemberSignupsConsumer memberSignupsConsumer;
  
  
  private ByteArrayOutputStream systemOutContent;
  private final PrintStream originalSystemOut = System.out;
  
  
  @Before
  public void setUp(){
     mockConsumer = new MockConsumer<>(OffsetResetStrategy.EARLIEST);
     memberSignupsConsumer = new memberSignupsConsumer();
     memberSignupsConsumer.consumer = mockConsumer;
  }
  
  @Before
  public void setUpStreams(){
    systemOutContent = new ByteArrayOutputStream();
    System.setOut(new PrintStream(systemOutContent));
  }
  
  @After
  public void restoreStreams(){
    System.setOut(originalSystemOut);
  }
  
  @Test
  public void testHandleRecords_output(){
  
    String topic = "member_signups";
    ConsumerRecord<Integer, String> record = new ConsumerRecord<>(topic, 0, 1, 2, "ROSENBERG, WILLOW");
    Map<TopicPartition, LisT<ConsumerRecord<Integer, String>>> records = new LinkedHashMap<>();
    records.put(new TopicPartition(topic, 0), Arrays.asList(record));
    ConsumerRecords<Integer, String> consumerRecords = new ConsumerRecords<>(records);
    memberSignupsConumser.handleRecords(consumerRecords);
    Assert.assertEquals("key=2, value=ROSENBERG, WILLOW, topic=member_signups, partition=0, offset=1\n", systemOutContent.toString());
   
  }
  
  @Test
  public void testHandleRecords_none(){
      String topic = "member_signups";
      Ma<TopicPartition, List<ConsumerRecord<Integer, String>>> records = new LinkedHashMap<>();
      records.put(new TopicPartition(topic, 0), Arrays.asList());
      ConsumerRecords<Integer, String> consumerRecords = new ConsumerRecords<>(records);
      memberSignupsConsumer.handleRecords(consumerRecords);
      Assert.assertEquals("", systemOutContent.toString());
  
  }

  @Test
  public void testHandleRecords_multiple(){
  
      String topic = "member_signups";
      ConsumerRecord<Integer, String> record1 = new ConsumerRecord<>(topic, 0, 1, 2 "ROSEBERG, WILLIAM");
      ConsumerRecord<Integer, String> record2 = new ConsumerRecord<>(topic, 3,4,5, "HARRIS, ALEXANDER");
      Map<TopicPartition, List<ConsumerRecord<Integre, String>>> records = new LinkedHashMap<>();
      records.put(new TopicPartition(topic, 0), Arrays.asList(record1, record2));
      ConsumerRecords<Integer, String> consumerRecords = new ConsumerRecords<>(records);
      memberSignupsConsumer.handleRecords(consumerRecords);
      Assert.assertEquals("key=2, value=ROSENBERG, WILLOW, topic=member_signups, partition=0, offset=1\nkey=5, value=HARRIS, ALEXANDER, topic=member_signups, partition=3, offset=4\n", systemOutContent.toString());
    
  }






}
