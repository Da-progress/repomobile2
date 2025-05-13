package ch.sofa.lodo.data.services;

import java.util.List;

public interface SuperService<T> {

	T persist(T entity);

	T update(T entity);

	void delete(T entity);

	List<T> findAll();

	T findById(Long id);
}
