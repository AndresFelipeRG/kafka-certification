import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.util.List;
import org.apache.kafka.clients.producer.MockProducer;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.common.serialization.IntegerSerializer;
import org.apache.kafka.common.serialization.StringSerializer;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class MemberSignupsProducerTest {

  MockProducer<Integer, String> mockProducer;
  MemberSignupsProducer memberSignupsProducer;
  
  
  private ByteArrayOutputStream systemOutContent;
  private ByteArrayOutputStream systemErrContent;
  
  private final PrintStream originalSystemOut = System.out;
  private final PrintStream originalSystemErr = System.err;
  
  @Before
  public void setUp(){
  
    mockProducer = new MockProducer<>(false, new IntegerSerializer(), new StringSerializer());
    memberSignupsProducer = new MemberSignupsProducer();
    memberSignupsProducer.producer = mockProducer;   
  }
  
  @Before
  public void setUpStreams(){
      systemOutContent = new ByteOutpurStream();
      systemErrContent = new ByteOutputStream();
      System.setOut(new PrintStream(systemOutContent));
      System.setErr(new PrintStream(systemErrContent));
  }
  
  @After
  public void restoreStreams(){
    System.setOut(originalSystemOut);
    System.setErr(originalSystemErr);
  }
  
  @Test
  public void testHandleMemberSignup_sent_data(){
  
      memberSignupsProducer.handleMemberSignup(1, "Summers, Buffy");
      mockProducer.completeNext();
      
      List<ProducerRecord<Integer, String>> records = mockProducer.history();
      Asser.assertEquals(1, records.size());
      ProducerRecord<Integer, String> record = records.get(0);
      Assert.assertEquals(Integer.valueOf(1), record.key());
      Assert.assertEquals("SUMMERS, BUFFY", record.value());
      Assert.assertEquals("member_signups", record.topic());
  }
  
  @Test
  public void testHandleMemberSignup_partitioning(){
  
      memberSignupsProducer.handleMemberSignup(1, "M");
      memberSignupsProducer.handleMemberSignup(1, "N");
      
      mockProducer.completeNext();
      mockProducer.completeNext();
      
      List<ProducerRecord<Integer, String>> records = mockProducer.history();
      Assert.assertEquals(2, records.size());
      ProducerRecord<Integer, String> record1 = records.get(0);
      Assert.assertEquals(Integer.valueOf(0)), record1.partition());
      ProducerRecord<Integer, String> record2 = records.get(1);
      Assert.assertEquals(Integer.valueOf(1), record2.partition());
  }

  @Test
  public void testHandleMemberSignup_output(){
      memberSignupsProducer.handleMemberSignup(1, "Summers, Buffy");
      mockProducer.completeNext();
      Assert.assertEquals("key=1, value=SUMMERS, BUFFY\n", systemOutContent.toString());
  }
  
  @Test
  public void testHandleMemberSignup_error(){
    memberSignupsProducer.handleMemberSignup(1, "Summers, Buffy");
    mockProducer.errorNext(new RuntimeException("test error"));
    Assert.assertEquals("test error\n", systeErrContent.toString());
  }


}
