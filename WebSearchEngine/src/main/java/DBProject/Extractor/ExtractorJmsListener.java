package DBProject.Extractor;

import org.springframework.jms.annotation.JmsListener;

public class ExtractorJmsListener {
    @JmsListener(containerFactory = "myJmsListenerContainerFactory", destination="testingQueue")
    public void process(String msg) {
        System.out.println("\n\n" + msg + "\n\n");
    }
}