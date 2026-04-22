package com.xuxi.pgvector_mcp.config;

import com.xuxi.pgvector_mcp.mcp.PgVectorQueryTool;
import lombok.extern.slf4j.Slf4j;
import org.springframework.ai.tool.ToolCallbackProvider;
import org.springframework.ai.tool.method.MethodToolCallbackProvider;
import org.springframework.ai.vectorstore.VectorStore;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Slf4j
@Configuration
public class McpToolConfig {

    @Bean
    public ToolCallbackProvider pgVectorQueryToolProvider(VectorStore vectorStore) {
        log.info("========== 开始注册 MCP 工具 ==========");
        log.info("VectorStore bean 类型: {}", vectorStore.getClass().getName());

        PgVectorQueryTool queryTool = new PgVectorQueryTool(vectorStore);
        log.info("PgVectorQueryTool 实例创建成功");

        ToolCallbackProvider provider = MethodToolCallbackProvider.builder()
                .toolObjects(queryTool)
                .build();

        log.info("✓ MCP 工具注册成功");
        log.info("========================================");
        return provider;
    }

    @Bean
    public CommandLineRunner checkMcpServer() {
        return args -> {
            log.info("========== MCP Server 状态 ==========");
            log.info("SSE 端点: http://localhost:8080/api/v1/sse");
            log.info("Message 端点: http://localhost:8080/api/v1/mcp");
            log.info("=====================================");
        };
    }
}
