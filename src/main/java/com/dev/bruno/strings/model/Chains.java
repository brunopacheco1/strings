package com.dev.bruno.strings.model;

import java.util.ArrayList;
import java.util.List;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

@ApiModel
public class Chains {

	@ApiModelProperty(value="Result chains")
	private List<String> chains = new ArrayList<>();

	public List<String> getChains() {
		return chains;
	}

	public void setChains(List<String> chains) {
		this.chains = chains;
	}
}