package com.dev.bruno.strings.model;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

@ApiModel
public class Statistics {

	@ApiModelProperty(value="Number of strings", example="3")
	private Integer numberOfStrings;
	
	@ApiModelProperty(value="Length of the shortest string", example="5")
	private Integer shortestStringLength;
	
	@ApiModelProperty(value="Length of the longest string", example="10")
	private Integer longestStringLength;
	
	@ApiModelProperty(value="Average Length", example="7")
	private Integer averageLength;
	
	public Integer getNumberOfStrings() {
		return numberOfStrings;
	}

	public void setNumberOfStrings(Integer numberOfStrings) {
		this.numberOfStrings = numberOfStrings;
	}

	public Integer getShortestStringLength() {
		return shortestStringLength;
	}

	public void setShortestStringLength(Integer shortestStringLength) {
		this.shortestStringLength = shortestStringLength;
	}

	public Integer getLongestStringLength() {
		return longestStringLength;
	}

	public void setLongestStringLength(Integer longestStringLength) {
		this.longestStringLength = longestStringLength;
	}

	public Integer getAverageLength() {
		return averageLength;
	}

	public void setAverageLength(Integer averageLength) {
		this.averageLength = averageLength;
	}
}