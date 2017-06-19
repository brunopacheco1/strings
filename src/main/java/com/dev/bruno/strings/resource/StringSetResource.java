package com.dev.bruno.strings.resource;

import java.util.List;

import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import com.dev.bruno.strings.model.Chains;
import com.dev.bruno.strings.model.Response;
import com.dev.bruno.strings.model.Statistics;
import com.dev.bruno.strings.model.StringSet;
import com.dev.bruno.strings.model.Words;
import com.dev.bruno.strings.service.StringSetService;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;

@Produces(MediaType.APPLICATION_JSON)
@Stateless
@Path("/string-set")
@Api("services")
public class StringSetResource {

	@Inject
	private StringSetService service;
	
	@POST
	@Path("/upload")
	@ApiOperation(value = "Service that persist a Set of Strings.")
	@ApiResponses(value = {
	    @ApiResponse(code = 200, message = "String Set queued.", response = Response.class),
	    @ApiResponse(code = 409, message = "Validation error.", response = Response.class),
	    @ApiResponse(code = 500, message = "Not expected error.", response = Response.class)
	})
	public Response upload(@ApiParam(required = true, value = "The Set of Strings") StringSet set) throws Exception {
		service.upload(set);
		
		return new Response(true, "String Set uploaded.");
	}
	
	@GET
	@Path("/search/{query}")
	@ApiOperation(value = "Service that returns a list of Sets of Strings that contains the string passed by.")
	@ApiResponses(value = {
	    @ApiResponse(code = 200, message = "List of String Sets.", response = List.class),
	    @ApiResponse(code = 500, message = "Not expected error.", response = Response.class)
	})
	public List<StringSet> search(@ApiParam(required=true, value = "The query string") @PathParam("query") String query) throws Exception {
		return service.search(query);
	}
	
	@DELETE
	@Path("/{id}/delete")
	@ApiOperation(value = "Service that deletes a Set of Strings.")
	@ApiResponses(value = {
	    @ApiResponse(code = 200, message = "String Set deleted.", response = Response.class),
	    @ApiResponse(code = 409, message = "Validation error.", response = Response.class),
	    @ApiResponse(code = 500, message = "Not expected error.", response = Response.class)
	})
	public Response delete(@ApiParam(required=true, value = "The id of String Set to be deleted") @PathParam("id") Long id) throws Exception {
		service.delete(id);
		
		return new Response(true, "String Set deleted.");
	}
	
	@GET
	@Path("/{id}/statistics")
	@ApiOperation(value = "Service that returns statistics of a Set of Strings.")
	@ApiResponses(value = {
	    @ApiResponse(code = 200, message = "Statistics from a String Set.", response = Statistics.class),
	    @ApiResponse(code = 409, message = "Validation error.", response = Response.class),
	    @ApiResponse(code = 500, message = "Not expected error.", response = Response.class)
	})
	public Statistics statistics(@ApiParam(required=true, value = "Return statistics from this id of String Set") @PathParam("id") Long id) throws Exception {
		return service.statistics(id);
	}
	
	@GET
	@Path("/most_common")
	@ApiOperation(value = "Service that returns the common words(strings).")
	@ApiResponses(value = {
	    @ApiResponse(code = 200, message = "Most common words.", response = Words.class),
	    @ApiResponse(code = 500, message = "Not expected error.", response = Response.class)
	})
	public Words mostCommon() throws Exception {
		return service.mostCommonWords();
	}
	
	@GET
	@Path("/exactly_in/{counter}")
	@ApiOperation(value = "Service that returns words(strings) with exactly the counter passed by.")
	@ApiResponses(value = {
	    @ApiResponse(code = 200, message = "Exactly in words.", response = Words.class),
	    @ApiResponse(code = 500, message = "Not expected error.", response = Response.class)
	})
	public Words exactlyIn(@ApiParam(required=true, value = "Return words (strings) with this counter") @PathParam("counter") Integer counter) throws Exception {
		return service.exactlyIn(counter);
	}
	

	@GET
	@Path("/longest")
	@ApiOperation(value = "Service that returns longest words(strings).")
	@ApiResponses(value = {
	    @ApiResponse(code = 200, message = "Longest words.", response = Words.class),
	    @ApiResponse(code = 500, message = "Not expected error.", response = Response.class)
	})
	public Words longest() throws Exception {
		return service.longestWords();
	}
	
	@POST
	@Path("/{A}/intersection/{B}")
	@ApiOperation(value = "Service that creates a new Set of Strings from a intersection between two others.")
	@ApiResponses(value = {
	    @ApiResponse(code = 200, message = "String Sets intersection created.", response = Response.class),
	    @ApiResponse(code = 409, message = "Validation error.", response = Response.class),
	    @ApiResponse(code = 500, message = "Not expected error.", response = Response.class)
	})
	public Response intersection(@ApiParam(required=true, value = "Id of Set of Strings A") @PathParam("A") Long a, @ApiParam(required=true, value = "Id of Set of Strings B") @PathParam("B") Long b) throws Exception {
		service.intersection(a, b);
		
		return new Response(true, "String Sets intersection created.");
	}
	
	@GET
	@Path("/longest_chain")
	@ApiOperation(value = "Service that returns the longest string set chain.")
	@ApiResponses(value = {
	    @ApiResponse(code = 200, message = "The longest string chain.", response = Chains.class),
	    @ApiResponse(code = 500, message = "Not expected error.", response = Response.class)
	})
	public Chains longestChain() throws Exception {
		return service.longestStringSetChains();
	}
	
	@GET
	@Path("/longest_string_chain")
	@ApiOperation(value = "Service that returns the longest string chain of all words in the index.")
	@ApiResponses(value = {
	    @ApiResponse(code = 200, message = "The longest string chain.", response = Chains.class),
	    @ApiResponse(code = 500, message = "Not expected error.", response = Response.class)
	})
	public Chains longestStringChain() throws Exception {
		return service.longestStringChains();
	}
}