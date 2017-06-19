package com.dev.bruno.strings.model;

import java.util.Arrays;
import java.util.IntSummaryStatistics;
import java.util.List;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

@ApiModel
public class StringSet {

	@ApiModelProperty(value="Id of this Set of Strings")
	private Long id;
	
	@ApiModelProperty(value="The Set of Strings")
	private String set;
	
	public StringSet() {}
	
	public StringSet(String set) {
		this.set = set;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getSet() {
		return set;
	}

	public void setSet(String set) {
		this.set = set;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((set == null) ? 0 : set.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		StringSet other = (StringSet) obj;
		if (set == null) {
			if (other.set != null)
				return false;
		} else if (!set.equals(other.set))
			return false;
		return true;
	}

	public Statistics toStatistics() {
		List<String> wordList = Arrays.asList(set.split("\\s"));
		
		IntSummaryStatistics summary = wordList.stream().mapToInt(String::length).summaryStatistics();
		
		Long count = summary.getCount();
		
		Integer longest = summary.getMax();
		
		Integer shortest = summary.getMin();
		
		Double average = summary.getAverage();
		
		Statistics statistics = new Statistics();
		statistics.setNumberOfStrings(count.intValue());
		statistics.setLongestStringLength(longest);
		statistics.setShortestStringLength(shortest);
		statistics.setAverageLength(average.intValue());
		
		return statistics;
	}
}