package cz.cvut.kbss.sempipes.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.EnableMBeanExport;
import org.springframework.context.annotation.Import;

@Configuration
@EnableMBeanExport
@Import({RestConfig2.class})
public class AppConfig {

    public AppConfig() {
        System.out.println("heelo");
    }
}