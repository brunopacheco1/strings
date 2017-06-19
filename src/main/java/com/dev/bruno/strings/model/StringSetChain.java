package com.dev.bruno.strings.model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

@ApiModel
public class StringSetChain {

	@ApiModelProperty(value = "The id of the String Chain")
	private Long id;

	@ApiModelProperty(value = "The string")
	private String string;

	@ApiModelProperty(value = "Nodes of the chain")
	private Map<Long, StringSetChain> chain = new HashMap<>();

	public StringSetChain(String string) {
		this.string = string;
	}

	public StringSetChain(Long id, String string) {
		this.string = string;
	}

	public String getString() {
		return string;
	}

	public void setString(String string) {
		this.string = string;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Character firstCharacter() {
		return string.charAt(0);
	}

	public Character lastCharacter() {
		return string.charAt(string.length() - 1);
	}

	public void addToChain(StringSetChain stringChain) {
		if (stringChain.firstCharacter().equals(this.lastCharacter())) {
			chain.put(stringChain.getId(), stringChain);
		}

		chain.values().forEach(node -> node.addToChain(new StringSetChain(stringChain.getId(), stringChain.getString())));
	}

	public void deleteFromChain(Long id) {
		chain.remove(id);

		chain.values().forEach(node -> node.deleteFromChain(id));
	}

	public List<String> getChains() {
		return getChains(null);
	}

	private List<String> getChains(String beforeString) {
		List<String> result = new ArrayList<>();

		String newString = beforeString != null ? beforeString + " - (set changed here) - " + this.string : this.string;

		if (this.chain.isEmpty()) {
			result.add(newString);
		} else {
			this.chain.values().forEach(node -> result.addAll(node.getChains(newString)));
		}

		return result;
	}
}