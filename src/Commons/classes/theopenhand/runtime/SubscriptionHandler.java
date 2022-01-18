/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package theopenhand.runtime;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import theopenhand.commons.Pair;
import theopenhand.commons.connection.runtime.interfaces.BindableResult;
import theopenhand.commons.connection.runtime.interfaces.ResultHolder;
import theopenhand.commons.events.programm.utils.ListEventHandler;
import theopenhand.commons.events.programm.utils.ListEventListener;
import theopenhand.runtime.templates.RuntimeReference;

/**
 *
 * @author gabri
 */
public class SubscriptionHandler {

    private SubscriptionHandler() {
    }

    private static final ListEventHandler<RuntimeReference> references = new ListEventHandler<>(new ArrayList<>());
    private static final ArrayList<Pair<RuntimeReference, Pair<Class<? extends ResultHolder<?>>, Class<? extends BindableResult>>>> db_results = new ArrayList<>();

    /**
     *
     * @param rf
     */
    public static void subscribeToLoading(RuntimeReference rf) {
        references.elementAdded(rf);
    }

    /**
     *
     * @param rf
     */
    public static void unsubscribeToLoading(RuntimeReference rf) {
        references.elementRemoved(rf);
    }

    /**
     *
     * @param subscription_listener
     */
    public static void addListener(ListEventListener<RuntimeReference> subscription_listener) {
        references.addListener(subscription_listener);
    }

    /**
     *
     * @param <T>
     * @param <X>
     * @param rr
     * @param rh
     * @param br
     */
    public static <T extends BindableResult, X extends ResultHolder<T>> void subscribeToDBObjects(RuntimeReference rr, Class<X> rh, Class<T> br) {
        db_results.add(new Pair<>(rr, new Pair<>(rh, br)));
    }

    /**
     *
     * @return
     */
    public static List<RuntimeReference> getPluginReferences() {
        return Collections.unmodifiableList(references.getList());
    }

    /**
     *
     * @return
     */
    public static List<Pair<RuntimeReference, Pair<Class<? extends ResultHolder<?>>, Class<? extends BindableResult>>>> getDBObjects() {
        return Collections.unmodifiableList(db_results);
    }

}
