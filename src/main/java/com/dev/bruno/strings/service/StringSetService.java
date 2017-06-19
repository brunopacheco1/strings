package com.dev.bruno.strings.service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import javax.ejb.Singleton;

import org.apache.commons.lang3.StringUtils;

import com.dev.bruno.strings.exception.AppException;
import com.dev.bruno.strings.model.Chains;
import com.dev.bruno.strings.model.Statistics;
import com.dev.bruno.strings.model.StringChain;
import com.dev.bruno.strings.model.StringSet;
import com.dev.bruno.strings.model.StringSetChain;
import com.dev.bruno.strings.model.Words;

//DELEGATING THE CONCURRENCY CONTROL TO THE EJB CONTAINER
@Singleton
public class StringSetService {

	//THE DATABASE!
	private Map<Long, StringSet> sets = new HashMap<>();
	
	//AN INDEX OF WORDS TO HELP THE QUERIES
	private Map<String, Set<Long>> wordsIndex = new HashMap<>();
	
	//THE TREE OF STRING CHAIN
	private Map<String, StringChain> stringChains = new HashMap<>();
	
	//THE TREE OF STRING SET CHAIN
	private Map<Long, StringSetChain> stringSetChains = new HashMap<>();
	
	//THE ID CONTROL
	private Long idCounter = 1l;
	
	public void upload(StringSet set) throws Exception {
		validate(set);
		
		set.setId(idCounter);
		
		List<String> wordList = Arrays.asList(set.getSet().replaceAll("\\s+", " ").split("\\s"));
		
		Integer jumps = 0;
		
		List<String> chainList = new ArrayList<>();
		String previousWord = null;
		
		for(int index = 0; index < wordList.size(); index++) {
			String word = wordList.get(index);
			
			//PREPARING THE STRING CHAIN
			if(!stringChains.containsKey(word)) {
				stringChains.values().stream().forEach(chain -> chain.addStringToChain(false, new StringChain(word)));
				
				StringChain chain = new StringChain(word);
				
				wordsIndex.keySet().stream().forEach(indexWord -> chain.addStringToChain(false, new StringChain(indexWord)));
				
				stringChains.put(word, chain);
			}
			
			//CREATING THE WORD INDEX
			if(!wordsIndex.containsKey(word)) {
				wordsIndex.put(word, new HashSet<Long>());
			}
			
			wordsIndex.get(word).add(set.getId());
			
			//PREPARING THE CHAIN
			if(jumps > 1) {
				continue;
			}
			
			if(index == 0) {
				chainList.add(word);
				previousWord = word;
			} else if (index + 1 == wordList.size()) {
				Character first = previousWord.charAt(previousWord.length() - 1);
				
				if(first.equals(word.charAt(0))) {
					chainList.add(word);
				}
			} else {
				String nextWord = wordList.get(index + 1);
				
				Character first = previousWord.charAt(previousWord.length() - 1);
				
				Character last = nextWord.charAt(0);
				
				if(first.equals(word.charAt(0)) && last.equals(word.charAt(word.length() - 1))) {
					chainList.add(word);
					previousWord = word;
				} else {
					jumps++;
				}
			}
		}
		
		StringSetChain newChain = new StringSetChain(set.getId(), chainList.stream().collect(Collectors.joining(" - ")));
		
		//POPULATING THE CHAIN TREE
		stringSetChains.values().forEach(chain -> {
			chain.addToChain(new StringSetChain(newChain.getId(), newChain.getString()));
			
			newChain.addToChain(new StringSetChain(chain.getId(), chain.getString()));
		});
		
		stringSetChains.put(newChain.getId(), newChain);
		
		sets.put(set.getId(), set);
		
		idCounter++;
	}
	
	private void validate(StringSet set) throws AppException {
	    if(set == null || StringUtils.isBlank(set.getSet())) {
	        throw new AppException("String Set is null.");
	    }
	    
	    if(sets.containsValue(set)) {
	    	throw new AppException("String Set found.");
	    }
	    
	    List<String> wordList = Arrays.asList(set.getSet().split("\\s"));

	    //MAP BETWEEN WORDS AND COUNTER
    	Map<String, Integer> wordCounters = wordList.stream().collect(Collectors.toMap(word -> word, count -> 1, Integer::sum));
    	
    	List<Integer> counters = wordCounters.values().stream().filter(counter -> counter > 1).collect(Collectors.toList());
    	
    	if(!counters.isEmpty()) {
	        throw new AppException("Duplicated string found.");
	    }
	}
	
	public List<StringSet> search(String query) {
		List<StringSet> result = new ArrayList<>();
		
		List<Long> ids = wordsIndex.entrySet().stream().filter(entry -> entry.getKey().equals(query)).map(entry -> entry.getValue()).flatMap(l -> l.stream()).collect(Collectors.toList());
		
		ids.forEach(id -> result.add(sets.get(id)));
		
		return result;
	}
	
	public void delete(Long id) throws Exception {
		if(!sets.containsKey(id)) {
	    	throw new AppException("String Set not found.");
	    }
		
		StringSet set = sets.remove(id);
		
		List<String> wordList = Arrays.asList(set.getSet().split("\\s"));
		
		//CLEANING THE INDEX

		wordList.forEach(word -> wordsIndex.get(word).remove(set.getId()));
		
		wordsIndex = wordsIndex.entrySet().stream().filter(map -> !map.getValue().isEmpty()).collect(Collectors.toMap(p -> p.getKey(), p -> p.getValue()));
		
		//CLEANING THE CHAIN TREE
		stringSetChains.remove(id);
		stringSetChains.values().forEach(chain -> chain.deleteFromChain(id));
	}

	public Statistics statistics(Long id) throws Exception {
		if(!sets.containsKey(id)) {
	    	throw new AppException("String Set not found.");
	    }
		
		//DELEGATING THE METHOD
		return sets.get(id).toStatistics();
	}

	//SEARCHING WORDS (STRINGS) BY THE BIGGEST COUNTER
	public Words mostCommonWords() {
		Map<String, Integer> wordCounters = wordsIndex.entrySet().stream().collect(Collectors.toMap(entry -> entry.getKey(), entry -> entry.getValue().size()));

		Integer biggestCounter = wordCounters.values().stream().mapToInt(Integer::intValue).max().orElse(0);
		
		return exactlyIn(biggestCounter, wordCounters);
	}
	
	//SEARCHING WORDS (STRINGS) BY A COUNTER
	public Words exactlyIn(Integer counter) {
		Map<String, Integer> wordCounters = wordsIndex.entrySet().stream().collect(Collectors.toMap(entry -> entry.getKey(), entry -> entry.getValue().size()));
		
		return exactlyIn(counter, wordCounters);
	}
	
	//MOST_COMMON AND EXACTLY_IN ARE THE SAME METHOD, SEARCHING WORDS BY THEIR COUNTERS
	private Words exactlyIn(Integer counter, Map<String, Integer> wordCounters) {
		List<String> words = wordCounters.entrySet().stream().filter(entry -> entry.getValue().equals(counter)).map(entry -> entry.getKey()).sorted().collect(Collectors.toList());
		
		Words excactlyInWords = new Words();
		
		excactlyInWords.setWords(words);
		
		return excactlyInWords;
	}
	
	public Words longestWords() {
		Map<Integer, List<String>> groupByLength = wordsIndex.keySet().stream().collect(Collectors.groupingBy(key -> key.length()));
		
		Integer longestWordsLength = groupByLength.keySet().stream().mapToInt(Integer::intValue).max().orElse(0);

		List<String> Words = groupByLength.entrySet().stream().filter(entry -> entry.getKey().equals(longestWordsLength)).flatMap(entry -> entry.getValue().stream()).distinct().sorted().collect(Collectors.toList());
		
		Words longestWords = new Words();
		
		longestWords.setWords(Words);
		
		return longestWords;
	}

	//AN INTERSECTION IS NOTHING MORE THEN A UPLOAD
	public void intersection(Long a, Long b) throws Exception {
		if(!sets.containsKey(a)) {
	    	throw new AppException("String Set A not found.");
	    }
		
		if(!sets.containsKey(b)) {
	    	throw new AppException("String Set B not found.");
	    }
		
		StringSet setA = sets.get(a);
		
		StringSet setB = sets.get(b);
		
		List<String> wordListA = Arrays.asList(setA.getSet().split("\\s"));
		
		List<String> wordListB = Arrays.asList(setB.getSet().split("\\s"));
		
		//THE INTERSECTION HAPPENING
		String set = wordListA.stream().filter(wordListB::contains).collect(Collectors.joining(" "));
		
		StringSet newSet = new StringSet();
		newSet.setSet(set);
		
		upload(newSet);
	}

	public Chains longestStringChains() {
		List<String> chainsToCheck = new ArrayList<>();
		
		this.stringChains.values().forEach(chain -> chainsToCheck.addAll(chain.getChains()));
		
		Map<Integer, List<String>> groupByLength = chainsToCheck.stream().collect(Collectors.groupingBy(key -> key.split("-").length + 1 ));
		
		Integer longestWordsLength = groupByLength.keySet().stream().mapToInt(Integer::intValue).max().orElse(0);

		List<String> longestChains = groupByLength.entrySet().stream().filter(entry -> entry.getKey().equals(longestWordsLength)).flatMap(entry -> entry.getValue().stream()).distinct().sorted().collect(Collectors.toList());
		
		Chains chains = new Chains();
		
		chains.setChains(longestChains);
		
		return chains;
	}
	
	public Chains longestStringSetChains() {
		List<String> chainsToCheck = new ArrayList<>();
		
		this.stringSetChains.values().forEach(chain -> chainsToCheck.addAll(chain.getChains()));
		
		Map<Integer, List<String>> groupByLength = chainsToCheck.stream().collect(Collectors.groupingBy(key -> key.split("\\(").length + 1 ));
		
		Integer longestWordsLength = groupByLength.keySet().stream().mapToInt(Integer::intValue).max().orElse(0);

		List<String> longestChains = groupByLength.entrySet().stream().filter(entry -> entry.getKey().equals(longestWordsLength)).flatMap(entry -> entry.getValue().stream()).distinct().sorted().collect(Collectors.toList());
		
		Chains chains = new Chains();
		
		chains.setChains(longestChains);
		
		return chains;
	}
}