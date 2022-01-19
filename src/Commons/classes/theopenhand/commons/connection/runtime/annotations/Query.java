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
import theopenhand.commons.connection.runtime.interfaces.BindableResult;
import theopenhand.commons.connection.runtime.interfaces.ResultHolder;

/**
 * Questa annotazione dovrebbe essere usata per annotare delle stringhe che
 * corrispondono a query o stored procedure da chiamare.
 * <p/>
 * Esempio:<br>  {@code
 *  @Query(queryID=0, bindedClass=BindableResultImpl.class, hasBindedParams=false )}
 * <br>
 * {@code private static final String QUERY_EXAMPLE = "select %N1 as Name, %N3 as Surname FROM Students WHERE %N0  = %V0";}
 * <p/>
 * Questo esempio suppone che la classe associata {@code BindableResultImpl}
 * implementi {@link BindableResult} e dichiari dei campi annotati con
 * {@link QueryField} e esistano almeno 3 campi con id:0,1,3 (quelli usati nella
 * stringa della query).
 *
 * @author gabri
 */
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
public @interface Query {

    /**
     * Serve per indicare in modo univoco una query in una stessa classe.
     *
     * @return ID della query
     */
    public int queryID();

    /**
     * Classe associata a questa query, questo valore viene usato per
     * immigazzinare i risultati della query.
     *
     * @return Classe associata ({@link BindableResult})
     */
    public Class bindedClass();

    /**
     * Indica se la query è un CallableStatement e ha dei parametri che devono
     * essere registrati in output.
     *
     * @return {@code true} se ha parametri da sovrascrivere dal risultato della
     * chiamata.
     */
    public boolean hasBindedParams();

    /**
     * Indica se la query ha un'output. Per ora non è utilizzato.
     *
     * @return {@code true} se bisogna registrare nel {@link ResultHolder}
     * associato classe della query il risultato della chiamata.
     */
    public boolean hasResult() default true;

    /**
     * Indica se la query è di tipo di aggiornamento dati.
     *
     * @return {@code true} se la libreria deve chiamare
     * {@code PreparedStatement.executeUpdate}.
     */
    public boolean isUpdate() default false;

    /**
     * Indica se la query è di tipo Stored Procedure.
     *
     * @return {@code true} se la libreria deve usare
     * {@link java.sql.CallableStatement}, altrimenti verrà usato
     * {@link java.sql.PreparedStatement}
     */
    public boolean isStoredProcedureCall() default false;

    /**
     * [Opzionale] Indica quali campi la stored procedure (a patto che
     * {@link #hasBindedParams() } sia {@code true} ) dovrà
     * aggiornare/sovrascrivere.
     *
     * @return un'array di ID di {@link QueryField#fieldID() }
     */
    public int[] outPrams() default {};
}
