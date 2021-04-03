package com.noname.duyuru.app.json.models;

import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import com.fasterxml.jackson.databind.util.ArrayIterator;
import lombok.Data;

import java.util.Iterator;

@JsonNaming(PropertyNamingStrategy.SnakeCaseStrategy.class)
@Data
public class Updates implements Iterable<Update>{
	private boolean ok;
	private Update[] result;

	@Override
	public Iterator<Update> iterator(){
		return new ArrayIterator<>(result);
	}
}
