package com.backend.blooming.common;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.restdocs.mockmvc.MockMvcRestDocumentation;
import org.springframework.restdocs.mockmvc.RestDocumentationResultHandler;
import org.springframework.restdocs.operation.preprocess.Preprocessors;

import javax.management.Attribute;

@Configuration
public class RestDocsConfiguration {

    @Bean
    public RestDocumentationResultHandler write() {
        return MockMvcRestDocumentation.document(
                "{class-name}/{method-name}",
                Preprocessors.preprocessRequest(
                        Preprocessors.modifyHeaders()
                                     .remove("Content-Length")
                                     .remove("Host"),
                        Preprocessors.prettyPrint()
                ),
                Preprocessors.preprocessResponse(
                        Preprocessors.modifyHeaders()
                                     .remove("Content-Length")
                                     .remove("Transfer-Encoding")
                                     .remove("Date")
                                     .remove("Keep-Alive")
                                     .remove("Connection"),
                        Preprocessors.prettyPrint()
                )
        );
    }
}
