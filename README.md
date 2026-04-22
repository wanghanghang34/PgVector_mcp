# PgVector MCP Server

这是一个基于Spring AI和MCP协议的PgVector向量数据库查询服务。它提供了通过自然语言查询PgVector中存储的向量数据的功能。

## 功能特性

- ✅ 向量相似度搜索：根据查询文本搜索相似的向量文档
- ✅ 带过滤器的搜索：使用元数据过滤器进行精确查询
- ✅ MCP协议支持：可与Ollama等AI模型集成
- ✅ 自动向量化：使用Ollama嵌入模型自动生成查询向量

## 技术栈

- Spring Boot 3.5.13
- Spring AI 1.1.4
- PostgreSQL + PgVector
- Ollama (嵌入模型)
- MCP Server

## 前置要求

1. **PostgreSQL with PgVector**
   - 安装PostgreSQL并启用pgvector扩展
   - 创建数据库 `pgvector_db`

2. **Ollama**
   - 安装Ollama: https://ollama.ai
   - 拉取嵌入模型: `ollama pull nomic-embed-text`
   - (可选) 拉取聊天模型: `ollama pull qwen2.5:7b`

3. **Java 17+**

## 配置说明

编辑 `src/main/resources/application.yaml`:

```yaml
spring:
  datasource:
    url: jdbc:postgresql://localhost:5432/pgvector_db
    username: postgres
    password: postgres
  
  ai:
    ollama:
      base-url: http://localhost:11434
      embedding:
        model: nomic-embed-text
    
    vectorstore:
      pgvector:
        dimensions: 768  # 根据嵌入模型调整
        index-type: hnsw
        distance-type: cosine-distance

spring.ai.mcp.server:
  name: pgvector-mcp-server
  version: 1.0.0
  type: SYNC
  enabled: true
```

## 可用MCP工具

### 1. searchSimilarDocuments

在PgVector中搜索与查询文本相似的向量文档。

**参数:**
- `query` (必需): 查询文本，用于搜索相似文档
- `topK` (可选): 返回的最相似文档数量，默认5
- `similarityThreshold` (可选): 相似度阈值（0-1之间），默认0.5

**示例:**
```
查询: "人工智能的应用"
返回: 最相似的5个文档
```

### 2. searchWithFilter

使用元数据过滤器在PgVector中搜索向量文档。

**参数:**
- `query` (必需): 查询文本
- `filterKey` (必需): 元数据过滤键
- `filterValue` (必需): 元数据过滤值
- `topK` (可选): 返回的文档数量，默认5

**示例:**
```
查询: "机器学习算法"
过滤器: category = "AI"
返回: 类别为AI的相关文档
```

## 运行项目

### 1. 编译项目
```bash
mvn clean package -DskipTests
```

### 2. 运行应用
```bash
java -jar target/PgVector_mcp-0.0.1-SNAPSHOT.jar
```

或使用Maven:
```bash
mvn spring-boot:run
```

### 3. 验证MCP Server

应用启动后，会显示以下日志：
```
✓ VectorStore 已成功初始化
✓ PgVector MCP Server 已准备就绪
✓ 可用的MCP工具:
  - searchSimilarDocuments: 搜索相似的向量文档
  - searchWithFilter: 使用过滤器搜索向量文档
```

## 与Ollama集成

在Ollama中配置MCP服务器，使其能够调用PgVector查询工具。

## 数据库准备

确保PostgreSQL中已创建包含向量数据的表。Spring AI会自动创建必要的表结构。

如果需要手动插入测试数据，可以使用Spring AI的VectorStore API。

## 项目结构

```
src/main/java/com/xuxi/pgvector_mcp/
├── PgVectorMcpApplication.java    # 主应用类
├── config/
│   ├── McpToolConfig.java         # MCP工具配置
│   ├── StartupConfig.java         # 启动验证配置
│   └── VectorStoreConfig.java     # VectorStore配置
└── mcp/
    └── PgVectorQueryTool.java     # PgVector查询工具实现
```

## 注意事项

1. 确保PostgreSQL和Ollama服务正在运行
2. 向量维度必须与嵌入模型输出维度一致（nomic-embed-text为768维）
3. MCP Server默认使用STDIO协议，可根据需要修改配置
4. 首次运行时，Spring AI会自动创建PgVector所需的表结构

## 许可证

MIT License
