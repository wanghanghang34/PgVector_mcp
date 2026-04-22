package com.xuxi.pgvector_mcp;

import com.xuxi.pgvector_mcp.mcp.PgVectorQueryTool;
import org.springframework.ai.tool.ToolCallback;
import org.springframework.ai.tool.ToolCallbackProvider;
import org.springframework.ai.tool.method.MethodToolCallbackProvider;
import org.springframework.ai.vectorstore.VectorStore;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class PgVectorMcpApplication {

    public static void main(String[] args) {
        SpringApplication.run(PgVectorMcpApplication.class, args);
    }
//
//    @Bean
//    public ToolCallbackProvider pgVectorQueryToolProvider() {
//        return MethodToolCallbackProvider.builder().toolObjects(new PgVectorQueryTool()).build();
//    }
}
