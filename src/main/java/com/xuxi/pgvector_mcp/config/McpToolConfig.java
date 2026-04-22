package com.xuxi.pgvector_mcp.config;

import org.springframework.ai.tool.ToolCallbackProvider;
import org.springframework.ai.tool.method.MethodToolCallbackProvider;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * MCP工具配置类
 * 将@Tool注解的方法暴露为MCP工具
 */
@Configuration
public class McpToolConfig {

    /**
     * 注册PgVector查询工具
     */
    @Bean
    public ToolCallbackProvider pgVectorQueryToolProvider(com.xuxi.pgvector_mcp.mcp.PgVectorQueryTool pgVectorQueryTool) {
        return MethodToolCallbackProvider.builder()
                .toolObjects(pgVectorQueryTool)
                .build();
    }
}
