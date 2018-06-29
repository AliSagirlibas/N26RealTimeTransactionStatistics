package com.n26.realtimeStats.controller;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.io.IOException;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import com.n26.realtimeStats.model.Transaction;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
public class TransactionControllerTest {

	@Autowired
	private MockMvc mockMvc;
	 
	@Value("${n26.settings.MAX_DELAY_IN_SECONDS}")
	private long maxDelayInSeconds;	
	
        
    @Test
    public void shouldReturnCreatedStatus() throws Exception {
    	
    	Transaction transaction=new Transaction(11.1, System.currentTimeMillis());
    	    	    	    	    	    
        this.mockMvc.perform(  post("/transactions")
        						.contentType(MediaType.APPLICATION_JSON)
        						.content(createJson(transaction))        		
        ).andExpect(status().isCreated());
    }
    
    @Test
    public void shouldReturn_NoContentStatus() throws Exception {
    	
    	Transaction transaction=new Transaction(6.1, System.currentTimeMillis()- (1000L*maxDelayInSeconds) );
    	    	    	    	    	    
        this.mockMvc.perform(  post("/transactions")
        						.contentType(MediaType.APPLICATION_JSON)
        						.content(createJson(transaction))        		
        ).andExpect(status().isNoContent());
    }
    
    
    protected String createJson(Transaction transaction) throws IOException 
    {
    	return "{\"timestamp\":"+transaction.getTimestamp()+",\"amount\":"+transaction.getAmount()+"}";
    }        
}

