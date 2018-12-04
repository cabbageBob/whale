package net.htwater.whale;

import net.htwater.sesame.htweb.swagger2.EnableSwagger2Doc;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication(scanBasePackages = {"net.htwater"})
@EnableSwagger2Doc
/**
 * @author shitianlong
 * @since 2018-08-28
 */
public class WhaleApplication {
    public static void main(String[] args) {
        SpringApplication.run(WhaleApplication.class, args);
    }
}
