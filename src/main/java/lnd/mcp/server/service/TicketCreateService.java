package lnd.mcp.server.service;

import lnd.mcp.server.model.Status;
import lnd.mcp.server.model.Ticket;
import org.springframework.ai.tool.annotation.Tool;
import org.springframework.ai.tool.annotation.ToolParam;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class TicketCreateService {

    private  static final Map<String, Ticket> tickets =new HashMap<>();

    @Tool(description = """
            Create a ticket with description
            """)
    public Ticket createTicket(
            @ToolParam(description = "description of the ticket ")
            String description) {
        Ticket ticket = Ticket.builder().description(description).id(UUID.randomUUID().toString())
                .status(Status.CREATED).build();
        return tickets.put(ticket.getId(), ticket);
    }

    @Tool(description = """
            Get all the tickets
            """)
    public Collection<Ticket> getAllTickets(){
        return  tickets.values();
    }

    @Tool(description = """
            Find ticket by its ticket id
            """)
    public Ticket findById(
            @ToolParam(description = "ticket id")
            String id){
        return tickets.get(id);
    }

    @Tool(description = """
            update status of a ticket.
            """)

    public Ticket updateStatus(
            @ToolParam(description = """
                    ticket id
                    """)
            String id,
            @ToolParam(description = "new status of the ticket.")
            Status status){
        var ticket=findById(id);
        if(null!=ticket){
            ticket.setStatus(status);

        }
        return  ticket;
    }
}
