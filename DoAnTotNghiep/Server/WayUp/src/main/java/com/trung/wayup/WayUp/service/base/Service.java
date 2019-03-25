package com.trung.wayup.WayUp.service.base;

import com.trung.wayup.WayUp.model.AbstractModel;

import java.util.Collection;

public interface Service<E extends AbstractModel> {

    E insert(E e);

    E update(E e);

    void delete(int id);

    Collection<E> gettAll();

}
