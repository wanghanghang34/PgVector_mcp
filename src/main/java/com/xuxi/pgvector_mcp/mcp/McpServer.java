//package com.xuxi.pgvector_mcp.mcp;
//
//import dev.langchain4j.mcp.server.DefaultMcpServer;
//import dev.langchain4j.mcp.server.McpServer;
//
//import java.util.Map;
//
//public class McpServer {
//
//    public static void main(String[] args) {
//        // ✅ 正确路径
//        McpServer mcpServer = new DefaultMcpServer();
//
//        // 注册一个加法工具（给AI调用）
//        mcpServer.addTool(
//                dev.langchain4j.mcp.protocol.client.Tool.builder()
//                        .name("add")
//                        .description("计算两个数字的和")
//                        .inputSchema(Map.of(
//                                "type", "object",
//                                "properties", Map.of(
//                                        "a", Map.of("type", "number"),
//                                        "b", Map.of("type", "number")
//                                ),
//                                "required", java.util.List.of("a", "b")
//                        ))
//                        .build(),
//                args -> {
//                    double a = (double) args.get("a");
//                    double b = (double) args.get("b");
//                    return "结果：" + (a + b);
//                }
//        );
//
//        // 启动在 8080 端口，和你客户端一致！
//        HttpMcpServerTransport transport = new HttpMcpServerTransport(mcpServer, 8080, "/sse");
//        System.out.println("✅ MCP 服务端已启动：http://localhost:8080/sse");
//        transport.start();
//}
