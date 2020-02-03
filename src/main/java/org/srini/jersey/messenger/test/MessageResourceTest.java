package org.srini.jersey.messenger.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThat;

import java.util.List;

import javax.ws.rs.core.Application;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import org.glassfish.jersey.server.ResourceConfig;
import org.glassfish.jersey.test.JerseyTest;
import org.hamcrest.CoreMatchers;
import org.junit.Test;
import org.srini.jersey.messenger.model.Link;
import org.srini.jersey.messenger.model.Message;
import org.srini.jersey.messenger.resources.MessageResource;

import com.google.gson.JsonArray;

public class MessageResourceTest extends JerseyTest {
	
	@Override
    protected Application configure() {
        return new ResourceConfig(MessageResource.class);
    }
	
	@Test
	public void returnDefaultXMLMessages() {
		Response xmlResponse = target("/messages").request(MediaType.APPLICATION_XML).get();
		assertEquals("HTTP Status code of response should be 200: ", Status.OK.getStatusCode(),xmlResponse.getStatus());
		assertEquals("HTTP Content-Type of response should be: ", MediaType.APPLICATION_XML, xmlResponse.getHeaderString(HttpHeaders.CONTENT_TYPE));	
	}
	
	@Test
	public void returnDefaultJSONresponse() {
		Response jsonResponse = target("/messages").request(MediaType.APPLICATION_JSON).get();
		assertEquals("HTTP Status code of response should be 200: ", Status.OK.getStatusCode(),jsonResponse.getStatus());
		assertEquals("HTTP Content-Type of response should be: ", MediaType.APPLICATION_JSON, jsonResponse.getHeaderString(HttpHeaders.CONTENT_TYPE));
		
		String jsonStr = target("/messages").request(MediaType.APPLICATION_JSON).get(String.class);
		assertThat(jsonStr, CoreMatchers.containsString("Hello Jersey"));
	}
	
	@Test
	public void testJSONresponse() {
		final Message messageEntity = target("/messages/1").request(MediaType.APPLICATION_JSON).get(Message.class);
		
		assertEquals("Srini", messageEntity.getAuthor());
		assertEquals("Hello World!", messageEntity.getMessage());
		
		List<Link> links = messageEntity.getLinks();
		for(Link link: links) {
			if("self".equals(link.getRel())) {assertThat(link.getLink(),CoreMatchers.endsWith("/messages/1"));}
			if("profile".equals(link.getRel())) {assertThat(link.getLink(),CoreMatchers.endsWith("/profiles/Srini"));}
			if("comments".equals(link.getRel())) {assertThat(link.getLink(),CoreMatchers.endsWith("/messages/1/comments/"));}
		}
	}
	
}
