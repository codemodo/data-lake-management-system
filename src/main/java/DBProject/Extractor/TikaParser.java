package DBProject.Extractor;

import java.io.File;
import java.io.IOException;

import org.apache.tika.Tika;
import org.apache.tika.exception.TikaException;

public class TikaParser {
	
	public static boolean parseWithTika(File file) {
		// Instantiating Tika facade class
		Tika tika = new Tika();
		try {
			String fileContent;
			try {
				fileContent = tika.parseToString(file);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				return false;
			}

		} catch (TikaException e) {
			return false;
		}
		return true;
	}

}
