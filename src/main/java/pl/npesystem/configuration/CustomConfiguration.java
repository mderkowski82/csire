package pl.npesystem.configuration;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.vaadin.hilla.EndpointController;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class CustomConfiguration {

//    @Bean
//    @Qualifier(EndpointController.ENDPOINT_MAPPER_FACTORY_BEAN_QUALIFIER)
//    public ObjectMapper endpointMapperFactory() {
//        // return a customized ObjectMapper.
//        ObjectMapper mapper = new ObjectMapper();
//        mapper.setSerializationInclusion(JsonInclude.Include.ALWAYS);
//        // Customize the mapper.
//        return mapper;
//    }
}