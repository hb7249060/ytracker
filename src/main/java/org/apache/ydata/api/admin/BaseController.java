package org.apache.ydata.api.admin;

import java.io.UnsupportedEncodingException;

public abstract class BaseController<T> {

    public abstract Object create(T t) throws UnsupportedEncodingException;

    public abstract Object getListByPage(int pageNum,  int pageSize);

    public abstract Object getOne(Object primaryKey);

    public abstract Object update(T t);

    public abstract Object deleteOne(Object primaryKey);

}
