import br.com.gazebo.service.ParseRep;
import org.junit.Test;

import java.util.List;
import java.util.Map;

import static org.junit.Assert.assertTrue;

public class ColetaTest {
    @Test
    public void parseRep(){
        final String rep = "0000000001111777162000238760596900200XXXXX" +
                "000090009900099990205201612052016120520161022\n" +
                new String(new char[145]).replace("\0", " ") +
                "0000120093020520160756019999999999\n" +
                "0000120103020520160811019999999999\n" +
                "0000120113020520160844019999999999\n" +
                "0000120123020520160847019999999999\n" +
                "9999999990000000000000005200000000000000000029\n";

        Map<String, Map<String, Map<String, List<String>>>> bRep = ParseRep.parse(rep);

        assertTrue(bRep.containsKey("019999999999"));
    }
}
