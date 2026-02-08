package lnd.mcp.server;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@Slf4j
public class SimpleMcpServerApplication {

	public static void main(String[] args) {
		SpringApplication.run(SimpleMcpServerApplication.class, args);
	}

}
