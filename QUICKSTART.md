# 快速开始指南

## 1. 环境准备

### 安装 PostgreSQL + PgVector

```bash
# Windows (使用 Chocolatey)
choco install postgresql

# 启用 pgvector 扩展
psql -U postgres -d pgvector_db -c "CREATE EXTENSION IF NOT EXISTS vector;"
```

### 安装 Ollama

1. 访问 https://ollama.ai 下载并安装
2. 拉取所需的模型：

```bash
# 嵌入模型（必需）
ollama pull nomic-embed-text

# 聊天模型（可选）
ollama pull qwen2.5:7b
```

## 2. 配置数据库

```sql
-- 创建数据库
CREATE DATABASE pgvector_db;

-- 连接到数据库并启用 pgvector 扩展
\c pgvector_db
CREATE EXTENSION IF NOT EXISTS vector;
```

## 3. 修改配置文件

编辑 `src/main/resources/application.yaml`，根据你的实际环境修改：

```yaml
spring:
  datasource:
    url: jdbc:postgresql://localhost:5432/pgvector_db
    username: postgres  # 修改为你的用户名
    password: postgres  # 修改为你的密码
```

## 4. 运行项目

### 方式一：使用 Maven

```bash
mvn spring-boot:run
```

### 方式二：打包后运行

```bash
# 编译打包
mvn clean package -DskipTests

# 运行
java -jar target/PgVector_mcp-0.0.1-SNAPSHOT.jar
```

### 方式三：使用 dev profile（插入示例数据）

```bash
mvn spring-boot:run -Dspring-boot.run.profiles=dev
```

## 5. 验证运行

应用成功启动后，你会看到类似以下的日志：

```
✓ VectorStore 已成功初始化: PgVectorStore
✓ PgVector MCP Server 已准备就绪
✓ 可用的MCP工具:
  - searchSimilarDocuments: 搜索相似的向量文档
  - searchWithFilter: 使用过滤器搜索向量文档
```

## 6. 测试 MCP 工具

### 方法一：使用 MCP Inspector

```bash
# 全局安装 MCP Inspector
npm install -g @modelcontextprotocol/inspector

# 在项目 target 目录下运行
cd target
npx @modelcontextprotocol/inspector java -jar PgVector_mcp-0.0.1-SNAPSHOT.jar
```

### 方法二：在代码中调用

```java
@Autowired
private VectorStore vectorStore;

// 搜索相似文档
SearchRequest request = SearchRequest.builder()
    .query("人工智能")
    .topK(3)
    .similarityThreshold(0.5)
    .build();

List<Document> results = vectorStore.similaritySearch(request);
```

## 7. 常见问题

### Q1: 连接数据库失败

**A:** 检查以下几点：
- PostgreSQL 服务是否启动
- 数据库 `pgvector_db` 是否已创建
- 用户名和密码是否正确
- pgvector 扩展是否已启用

### Q2: Ollama 连接失败

**A:** 
- 确保 Ollama 服务正在运行
- 检查 `application.yaml` 中的 `base-url` 是否正确
- 确认已拉取 `nomic-embed-text` 模型

### Q3: VectorStore 自动装配失败

**A:** 
- 这是 IDE 的误报，Spring AI 会在运行时自动配置
- 只要依赖正确，运行时不会有问题

### Q4: 如何查看已存储的向量数据？

**A:** 使用以下 SQL 查询：

```sql
-- 查看表结构
\d vector_store

-- 查询所有文档
SELECT id, content, metadata FROM vector_store;

-- 查询数量
SELECT COUNT(*) FROM vector_store;
```

## 8. 下一步

- 阅读 [README.md](README.md) 了解更多功能
- 查看 [PgVectorQueryTool.java](src/main/java/com/xuxi/pgvector_mcp/mcp/PgVectorQueryTool.java) 了解工具实现
- 集成到你的 AI 应用中

## 9. 获取帮助

如有问题，请检查：
1. 日志输出
2. 数据库连接
3. Ollama 服务状态
4. 配置文件是否正确
