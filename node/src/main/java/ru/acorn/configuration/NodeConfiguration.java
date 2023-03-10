package ru.acorn.configuration;


import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import ru.acorn.utils.CryptoTool;

@Configuration
public class NodeConfiguration {
    //@Value("${salt}")
    private String salt = "qwertyui";

    @Bean
    CryptoTool getCryptoTool (){
        return new CryptoTool(salt);
    }
}
