import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class Batch {
		Map<String,Object> batchAttribute = new HashMap<String,Object>();
		BatchObject batchHeader;
		ArrayList<Transaction> transList = new ArrayList<Transaction>();
		BatchObject batchFooter;
		Batch(){
			
		}
}
