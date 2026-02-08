package lnd.mcp.server.service;

import io.modelcontextprotocol.server.McpSyncServerExchange;
import io.modelcontextprotocol.spec.McpSchema;
import lnd.mcp.server.model.Status;
import lnd.mcp.server.model.Ticket;
import lombok.extern.slf4j.Slf4j;
import org.springaicommunity.mcp.annotation.McpProgressToken;
import org.springaicommunity.mcp.annotation.McpTool;
import org.springaicommunity.mcp.annotation.McpToolParam;
import org.springaicommunity.mcp.context.McpSyncRequestContext;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.concurrent.TimeUnit;

@Service
@Slf4j
public class TicketCreateService {

    private  static final Map<String, Ticket> tickets =new HashMap<>();

    @McpTool(description = """
            Create a ticket with description
            """,
            name = "JIRATicketCreationTool",
            title = "Create a JIRA issue",
            annotations = @McpTool.McpAnnotations(destructiveHint = false,
            idempotentHint = false
            )

    )
    public Ticket createTicket(
            @McpToolParam(description = "description of the ticket ")
            String description) {
        Ticket ticket = Ticket.builder().description(description).id(UUID.randomUUID().toString())
                .status(Status.CREATED).build();
        return tickets.put(ticket.getId(), ticket);
    }

    @McpTool(description = """
            Get all the tickets
            """)
    public Collection<Ticket> getAllTickets(McpSyncServerExchange exchange,
                                            @McpProgressToken String progressToken){
        exchange.loggingNotification(McpSchema.LoggingMessageNotification.builder().data("Tool1 Started!").build());

        exchange.progressNotification(
                new McpSchema.ProgressNotification(progressToken, 0.0, 1.0, "tool call start"));

        exchange.ping(); // call client ping
        return  tickets.values();
    }

    @McpTool(description = """
            Find ticket by its ticket id
            """)
    public Ticket findById(
            @McpToolParam(description = "ticket id")
            String id){
        return tickets.get(id);
    }

    @McpTool(description = """
            update status of a ticket.
            """,

            annotations = @McpTool.McpAnnotations(destructiveHint = true,
                    idempotentHint = false)
    )

    public Ticket updateStatus(
            @McpToolParam(description = """
                    ticket id
                    """)
            String id,
            @McpToolParam(description = "new status of the ticket.")
            Status status,
            McpSyncRequestContext context
            ){
        // Access progress token from context
        Object progressToken = context.request().progressToken();
        log.info("Progress token : {}",progressToken);
       log.info("Elicit enabled : {}",context.elicitEnabled());
       context.progress(ps->{
           ps.message("Handing it to proper route ").progress(0.0)
                   .percentage(0);

       });
       context.ping();
        var ticket=findById(id);
        if(null!=ticket){
            ticket.setStatus(status);

        }

        for(int i=1;i<=5;i++){
            try {
                TimeUnit.SECONDS.sleep(2);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
            final int j=i;
            context.progress(ps->{

                ps.message("Working on part: "+j).progress((double) j /5)
                        .percentage((j/5)*100);

            });
        }
        return  ticket;
    }


}
