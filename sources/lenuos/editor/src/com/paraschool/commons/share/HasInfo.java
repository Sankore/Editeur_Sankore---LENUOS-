package com.paraschool.commons.share;

/*
 * Created at 12 nov. 2010
 * By bathily
 */
public interface HasInfo<T extends Info> {
	public void setInfo(T info);
	public T getInfo();
}
