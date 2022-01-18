/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package theopenhand.commons.connection.runtime.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 *
 * @author gabri
 */
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
public @interface Query {

    /**
     *
     * @return
     */
    public int queryID();
    
    /**
     *
     * @return
     */
    public Class bindedClass();
    
    /**
     *
     * @return
     */
    public boolean hasBindedParams();

    /**
     *
     * @return
     */
    public boolean hasResult() default true;

    /**
     *
     * @return
     */
    public boolean isUpdate() default false;

    /**
     *
     * @return
     */
    public boolean isStoredProcedureCall() default false;
    
    /**
     *
     * @return
     */
    public int[] outPrams() default {};
}
