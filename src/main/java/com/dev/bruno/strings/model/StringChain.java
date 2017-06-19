package com.dev.bruno.strings.model;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

@ApiModel
public class StringChain {
	
	@ApiModelProperty(value="The string")
	private String string;
	
	@ApiModelProperty(value="If the chain has a jump")
	private Boolean isAJump = false;
	
	@ApiModelProperty(value="Nodes of the chain")
	private List<StringChain> chain = new ArrayList<>();

	public StringChain(String string) {
		this.string = string;
	}
	
	public StringChain(String string, Boolean isAJump) {
		this.string = string;
		this.isAJump = isAJump;
	}
	
	public String getString() {
		return string;
	}

	public void setString(String string) {
		this.string = string;
	}

	public Boolean getIsAJump() {
		return isAJump;
	}

	public void setIsAJump(Boolean isAJump) {
		this.isAJump = isAJump;
	}

	public List<StringChain> getChain() {
		return chain;
	}

	public void setChain(List<StringChain> chain) {
		this.chain = chain;
	}
	
	public Character firstCharacter() {
		return string.charAt(0);
	}
	
	public Character lastCharacter() {
		return string.charAt(string.length() - 1);
	}
	
	public void addStringToChain(Boolean previousJump, StringChain stringChain) {
		if(!stringChain.firstCharacter().equals(this.lastCharacter())) {
			stringChain.setIsAJump(true);
		}
		
		if((this.isAJump || previousJump) && stringChain.getIsAJump()) {
			return;
		}

		chain.forEach(node -> node.addStringToChain(previousJump || this.isAJump, new StringChain(stringChain.getString())));
		
		chain.add(stringChain);
	}
	
	public Integer getLongestDepth(int depth) {
		int newDepth = depth + 1;
		
		if(chain.isEmpty()) {
			return newDepth;
		}
		
		List<Integer> depths = chain.parallelStream().map(node -> node.getLongestDepth(newDepth)).distinct().collect(Collectors.toList());
		
		return depths.parallelStream().mapToInt(Integer::intValue).max().orElse(0);
	}
	
	public List<String> getChains() {
		return getChains(null);
	}
	
	private List<String> getChains(String beforeString) {
		List<String> result = new ArrayList<>();
		
		String newString = beforeString != null ? beforeString + " - " + (this.isAJump ? "[" : "") + this.string : (this.isAJump ? "[" : "") + this.string;
		
		if(this.chain.isEmpty()) {
			result.add(newString);
		} else {
			this.chain.forEach(node -> {
				result.addAll(node.getChains(newString));
			});
		}
		
		return result;
	}
}