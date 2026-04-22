package com.xuxi.pgvector_mcp.mcp;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.ai.document.Document;
import org.springframework.ai.vectorstore.SearchRequest;
import org.springframework.ai.vectorstore.VectorStore;
import org.springframework.ai.tool.annotation.Tool;
import org.springframework.ai.tool.annotation.ToolParam;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

/**
 * PgVector向量查询MCP工具
 * 提供通过自然语言查询PgVector中存储的向量数据的功能
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class PgVectorQueryTool {

    private final VectorStore vectorStore;

    /**
     * 根据查询文本搜索相似的向量文档
     * 
     * @param query 查询文本，用于生成向量并搜索相似文档
     * @param topK 返回的最相似文档数量，默认为5
     * @param similarityThreshold 相似度阈值（0-1之间），默认为0.5
     * @return 匹配的文档列表，包含内容和元数据
     */
    @Tool(description = "在PgVector中搜索与查询文本相似的向量文档")
    public String searchSimilarDocuments(
            @ToolParam(description = "查询文本，用于搜索相似文档", required = true) String query,
            @ToolParam(description = "返回的最相似文档数量，默认5", required = false) Integer topK,
            @ToolParam(description = "相似度阈值（0-1之间），默认0.5", required = false) Double similarityThreshold) {
        
        try {
            // 设置默认值
            int k = (topK != null && topK > 0) ? topK : 5;
            double threshold = (similarityThreshold != null) ? similarityThreshold : 0.5;

            log.info("执行向量搜索 - 查询: {}, TopK: {}, 阈值: {}", query, k, threshold);

            // 构建搜索请求
            SearchRequest searchRequest = SearchRequest.builder()
                    .query(query)
                    .topK(k)
                    .similarityThreshold(threshold)
                    .build();

            // 执行搜索
            List<Document> results = vectorStore.similaritySearch(searchRequest);

            log.info("搜索完成，找到 {} 个结果", results.size());

            // 格式化结果
            if (results.isEmpty()) {
                return "未找到与查询 \"" + query + "\" 相似的文档。";
            }

            StringBuilder resultBuilder = new StringBuilder();
            resultBuilder.append("找到 ").append(results.size()).append(" 个相似文档:\n\n");

            for (int i = 0; i < results.size(); i++) {
                Document doc = results.get(i);
                resultBuilder.append("【文档 ").append(i + 1).append("】\n");
                resultBuilder.append("内容: ").append(doc.getText()).append("\n");
                
                // 添加元数据信息
                Map<String, Object> metadata = doc.getMetadata();
                if (metadata != null && !metadata.isEmpty()) {
                    resultBuilder.append("元数据: ").append(metadata).append("\n");
                }
                
                resultBuilder.append("\n");
            }

            return resultBuilder.toString();

        } catch (Exception e) {
            log.error("向量搜索失败", e);
            return "搜索失败: " + e.getMessage();
        }
    }

    /**
     * 根据过滤器条件查询向量文档
     * 
     * @param query 查询文本
     * @param filterKey 元数据过滤键
     * @param filterValue 元数据过滤值
     * @param topK 返回的文档数量
     * @return 匹配的文档列表
     */
    @Tool(description = "使用元数据过滤器在PgVector中搜索向量文档")
    public String searchWithFilter(
            @ToolParam(description = "查询文本", required = true) String query,
            @ToolParam(description = "元数据过滤键", required = true) String filterKey,
            @ToolParam(description = "元数据过滤值", required = true) String filterValue,
            @ToolParam(description = "返回的文档数量，默认5", required = false) Integer topK) {
        
        try {
            int k = (topK != null && topK > 0) ? topK : 5;

            log.info("执行带过滤的向量搜索 - 查询: {}, 过滤器: {}={}, TopK: {}", 
                    query, filterKey, filterValue, k);

            // 构建搜索请求，添加过滤器
            SearchRequest searchRequest = SearchRequest.builder()
                    .query(query)
                    .topK(k)
                    .filterExpression(filterKey + " = '" + filterValue + "'")
                    .build();

            // 执行搜索
            List<Document> results = vectorStore.similaritySearch(searchRequest);

            log.info("过滤搜索完成，找到 {} 个结果", results.size());

            // 格式化结果
            if (results.isEmpty()) {
                return "未找到符合条件的文档（过滤器: " + filterKey + "=" + filterValue + "）。";
            }

            StringBuilder resultBuilder = new StringBuilder();
            resultBuilder.append("找到 ").append(results.size()).append(" 个符合条件的文档:\n\n");

            for (int i = 0; i < results.size(); i++) {
                Document doc = results.get(i);
                resultBuilder.append("【文档 ").append(i + 1).append("】\n");
                resultBuilder.append("内容: ").append(doc.getText()).append("\n");
                
                Map<String, Object> metadata = doc.getMetadata();
                if (metadata != null && !metadata.isEmpty()) {
                    resultBuilder.append("元数据: ").append(metadata).append("\n");
                }
                
                resultBuilder.append("\n");
            }

            return resultBuilder.toString();

        } catch (Exception e) {
            log.error("带过滤的向量搜索失败", e);
            return "搜索失败: " + e.getMessage();
        }
    }
}
