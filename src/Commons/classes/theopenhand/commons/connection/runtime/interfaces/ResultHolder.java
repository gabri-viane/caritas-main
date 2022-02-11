/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package theopenhand.commons.connection.runtime.interfaces;

import java.util.List;

/**
 *
 * @author gabri
 * @param <T>
 */
public interface ResultHolder<T extends BindableResult> {

    /**
     *
     * @param single_row
     */
    public void addResult(T single_row);

    /**
     *
     * @param rows
     */
    public void addResults(List<T> rows);

    /**
     *
     */
    public void clearResults();

    /**
     *
     * @return
     */
    public T getLastInsert();

    /**
     *
     * @return
     */
    public List<T> getList();

    public Exception getExecutionException();

    public void setLastException(Exception e);

}
