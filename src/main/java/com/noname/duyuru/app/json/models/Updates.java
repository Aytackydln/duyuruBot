package com.noname.duyuru.app.json.models;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.util.ArrayIterator;

import java.util.Iterator;

public class Updates implements Iterable<Update>{
	private boolean ok;
	private Update[] result;

	@Override
	public Iterator<Update> iterator(){
		return new ArrayIterator<>(result);
	}

	@JsonProperty("ok")
	public boolean isOk() {
		return ok;
	}

	public void setOk(boolean ok) {
		this.ok = ok;
	}

	@JsonProperty("result")
	public Update[] getResult() {
		return result;
	}

	public void setResult(Update[] result) {
		this.result = result;
	}
}
