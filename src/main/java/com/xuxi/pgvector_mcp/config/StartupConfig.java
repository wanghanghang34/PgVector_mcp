package com.xuxi.pgvector_mcp.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.ai.vectorstore.VectorStore;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * 应用启动验证配置
 */
@Slf4j
@Configuration
public class StartupConfig {

    /**
     * 应用启动时验证VectorStore是否正确配置
     */
    @Bean
    public CommandLineRunner checkVectorStore(VectorStore vectorStore) {
        return args -> {
            log.info("✓ VectorStore 已成功初始化: {}", vectorStore.getClass().getSimpleName());
            log.info("✓ PgVector MCP Server 已准备就绪");
            log.info("✓ 可用的MCP工具:");
            log.info("  - searchSimilarDocuments: 搜索相似的向量文档");
            log.info("  - searchWithFilter: 使用过滤器搜索向量文档");
        };
    }
}
