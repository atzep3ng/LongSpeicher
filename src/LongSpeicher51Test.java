import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;


public class LongSpeicher51Test {

   @Before
   public void setUp() throws Exception {}

   @Test
   public void shouldReturnEmptyArray() {
      
      LongSpeicher51 speicher = new LongSpeicher51();
      String result = speicher.toString();
      assertEquals("[]", result);
   }

}
