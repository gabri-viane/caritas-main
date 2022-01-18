/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package theopenhand.runtime.connection.runtime.engine;

import java.util.HashMap;
import theopenhand.commons.connection.runtime.interfaces.BindableResult;
import theopenhand.commons.connection.runtime.interfaces.ResultHolder;

/**
 *
 * @author gabri
 */
public final class EngineBuilder {

    private final HashMap<Class<? extends BindableResult>, ConnectionEngine<?, ?>> engines = new HashMap<>();
    private static EngineBuilder instance;

    private EngineBuilder() {

    }

    public static EngineBuilder getInstance() {
        if (instance == null) {
            instance = new EngineBuilder();
        }
        return instance;
    }

    public ConnectionEngine registerClass(Class<? extends ResultHolder<?>> clazz_holder, Class<? extends BindableResult> clazz_result) {
        ConnectionEngine ce = new ConnectionEngine(clazz_holder, clazz_result);
        engines.put(clazz_result, ce);
        return ce;
    }

    public ConnectionEngine findEngine(Class<? extends BindableResult> bindable_result) {
        return engines.get(bindable_result);
    }

    public void removeConnectionEngine(Class<? extends BindableResult> clz) {
        ConnectionEngine<?, ?> remove = engines.remove(clz);
        remove.getHolderInstance().clearResults();
        remove.close();
    }

}
