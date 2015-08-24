package com.vinniccius.dao;

import java.io.Serializable;
import java.util.Collection;
import java.util.List;

public interface Dao<T, I extends Serializable> {

	List<T> list();

	List<T> list(Integer start, Integer limit);
	
	List<T> list(T model);
	
	List<T> list(T model, Integer start, Integer limit);
	
	void save(T model);

	void saveAll(Collection<T> models);

	void delete(I id);	
	
	void delete(T model);

	void deleteAll(Collection<T> models);

	boolean contains(T model);
	
	List<T> find(T model);
	
    T find(I id);
	
	T get(I id);
	
	Long total();
	
}
