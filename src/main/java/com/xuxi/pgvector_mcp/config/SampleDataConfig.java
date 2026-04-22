package com.xuxi.pgvector_mcp.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.ai.document.Document;
import org.springframework.ai.vectorstore.VectorStore;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

import java.util.List;
import java.util.Map;

/**
 * 示例数据初始化配置
 * 仅在 'dev' profile 激活时运行
 */
@Slf4j
@Configuration
@Profile("dev")
public class SampleDataConfig {

    /**
     * 应用启动时插入示例数据（仅用于测试）
     */
    @Bean
    public CommandLineRunner initSampleData(VectorStore vectorStore) {
        return args -> {
            log.info("开始插入示例向量数据...");

            List<Document> documents = List.of(
                new Document(
                    "人工智能是计算机科学的一个分支，致力于创建能够执行通常需要人类智能的任务的系统。",
                    Map.of("category", "AI", "source", "wikipedia")
                ),
                new Document(
                    "机器学习是人工智能的一个子集，专注于开发能够从数据中学习的算法。",
                    Map.of("category", "AI", "source", "tech_blog")
                ),
                new Document(
                    "深度学习是机器学习的一种方法，使用多层神经网络来建模和理解复杂模式。",
                    Map.of("category", "Deep Learning", "source", "research_paper")
                ),
                new Document(
                    "自然语言处理使计算机能够理解、解释和生成人类语言。",
                    Map.of("category", "NLP", "source", "tutorial")
                ),
                new Document(
                    "计算机视觉是人工智能领域，使计算机能够从图像和视频中提取有意义的信息。",
                    Map.of("category", "Computer Vision", "source", "course_material")
                )
            );

            try {
                vectorStore.add(documents);
                log.info("✓ 成功插入 {} 个示例文档", documents.size());
            } catch (Exception e) {
                log.error("插入示例数据失败", e);
            }
        };
    }
}
