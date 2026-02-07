package lnd.mcp.server;

import lnd.mcp.server.service.TicketCreateService;
import org.springframework.ai.support.ToolCallbacks;
import org.springframework.ai.tool.ToolCallback;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
public class AiConfig {

    @Bean
    public List<ToolCallback> toolCallbacks(TicketCreateService ticketCreateService){
        return List.of(ToolCallbacks.from(ticketCreateService));
    }

}
