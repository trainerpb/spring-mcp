package lnd.mcp.server.service;

import lnd.mcp.server.model.Status;
import lnd.mcp.server.model.Ticket;
import org.springaicommunity.mcp.annotation.McpTool;
import org.springaicommunity.mcp.annotation.McpToolParam;
import org.springframework.ai.tool.annotation.Tool;
import org.springframework.ai.tool.annotation.ToolParam;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class TicketCreateService {

    private  static final Map<String, Ticket> tickets =new HashMap<>();

    @McpTool(description = """
            Create a ticket with description
            """)
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
    public Collection<Ticket> getAllTickets(){
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
            """)

    public Ticket updateStatus(
            @McpToolParam(description = """
                    ticket id
                    """)
            String id,
            @McpToolParam(description = "new status of the ticket.")
            Status status){
        var ticket=findById(id);
        if(null!=ticket){
            ticket.setStatus(status);

        }
        return  ticket;
    }
}
