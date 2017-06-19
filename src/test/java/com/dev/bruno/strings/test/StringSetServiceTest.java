package com.dev.bruno.strings.test;

import java.util.List;

import javax.ejb.embeddable.EJBContainer;

import com.dev.bruno.strings.exception.AppException;
import com.dev.bruno.strings.model.Chains;
import com.dev.bruno.strings.model.Statistics;
import com.dev.bruno.strings.model.StringSet;
import com.dev.bruno.strings.model.Words;
import com.dev.bruno.strings.service.StringSetService;

import junit.framework.TestCase;

public class StringSetServiceTest extends TestCase {

	private StringSetService service;
	
    private EJBContainer container;
    
	@Override
	protected void setUp() throws Exception {
		container = EJBContainer.createEJBContainer();
		Object object = container.getContext().lookup("java:global/strings/StringSetService");
		
		assertTrue(object instanceof StringSetService);

		service = (StringSetService) object;
		
		service.upload(new StringSet("foo oomph hgh"));
		
		service.upload(new StringSet("hij jkl jkm lmn"));
		
		service.upload(new StringSet("abc cde cdf fuf fgh hij"));
    }

	@Override
	protected void tearDown() throws Exception {
		container.close();
	}

	public void testLongest() {
		Words words = service.longestWords();
		assertTrue(words.getWords().size() == 1 && words.getWords().get(0).equals("oomph"));
	}
	
	public void testExactlyIn() {
		Words words = service.exactlyIn(2);
		assertTrue(words.getWords().size() == 1 && words.getWords().get(0).equals("hij"));
	}
	
	public void testMostCommon() {
		Words words = service.mostCommonWords();
		assertTrue(words.getWords().size() == 1 && words.getWords().get(0).equals("hij"));
	}
	
	public void testSearch() {
		List<StringSet> sets = service.search("hij");
		assertTrue(sets.size() == 2);
	}
	
	public void testStatistics() throws Exception {
		Statistics statistics = service.statistics(1l);
		assertTrue(statistics.getNumberOfStrings() == 3 && statistics.getLongestStringLength() == 5 && statistics.getShortestStringLength() == 3 && statistics.getAverageLength() == 3);
	}
	
	public void testIntersection() throws Exception {
		service.intersection(2l, 3l);
		
		Statistics statistics = service.statistics(4l);
		
		assertTrue(statistics.getNumberOfStrings() == 1 && statistics.getLongestStringLength() == 3 && statistics.getShortestStringLength() == 3 && statistics.getAverageLength() == 3);
	}
	
	public void testLongestStringChain() throws Exception {
		Chains chains = service.longestStringChains();
		
		System.out.println(chains.getChains());
		
		assertTrue(chains.getChains().size() == 1 && chains.getChains().get(0).split("-").length == 9);
	}
	
	public void testExceptions() {
		try {
			service.upload(null);
		} catch (Exception e) {
			assertTrue(e instanceof AppException);
		}
		
		try {
			service.upload(new StringSet());
		} catch (Exception e) {
			assertTrue(e instanceof AppException);
		}
		
		try {
			service.upload(new StringSet("foo oomph hgh"));
		} catch (Exception e) {
			assertTrue(e instanceof AppException);
		}
		
		try {
			service.delete(1000l);
		} catch (Exception e) {
			assertTrue(e instanceof AppException);
		}
		
		try {
			service.statistics(1000l);
		} catch (Exception e) {
			assertTrue(e instanceof AppException);
		}
		
		try {
			service.intersection(1l, 1000l);
		} catch (Exception e) {
			assertTrue(e instanceof AppException);
		}
		
		try {
			service.intersection(1000l, 1l);
		} catch (Exception e) {
			assertTrue(e instanceof AppException);
		}
		
		try {
			service.intersection(1l, 2l);
		} catch (Exception e) {
			assertTrue(e instanceof AppException);
		}
	}
}