package co.avui.sailsio;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.lang.reflect.Type;
import java.util.LinkedHashSet;
import java.util.Set;

import co.avui.sailsio.adapters.CallAdapter;
import co.avui.sailsio.adapters.DefaultCallAdapterFactory;
import co.avui.sailsio.socketio.ConnectionParam;
import io.socket.client.Socket;

public class SailsIO {

    private String baseUrl;
    private Socket socket;
    private Set<ConnectionParam> connectionParams;
    private CallAdapter.Factory callAdapterFactory;

    /**
     *
     * @param baseUrl
     * @param connectionParams
     */
    private SailsIO(
            String baseUrl,
            Set<ConnectionParam> connectionParams,
            boolean autoConnect,
            CallAdapter.Factory callAdapterFactory) {

        this.baseUrl = baseUrl;
        this.connectionParams = connectionParams;
        this.callAdapterFactory = callAdapterFactory;

        if (autoConnect) {
            connect();
        }
    }

    /**
     *
     * @param service
     * @param <T>
     * @return
     */
    @SuppressWarnings("unchecked")
    public <T> T cretate(Class<T> service) {
        return (T) Proxy.newProxyInstance(service.getClassLoader(), new Class<?>[] {service},
        new InvocationHandler() {
            @Override
            public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
                return null;
            }
        });
    }

    public CallAdapter callAdapter(Type returnType) {
        Utils.checkNotNull(returnType, "The returnType not should be null");
        return callAdapterFactory.get(returnType);
    }

    public void connect() {

    }

    public void disconnect() {

    }

    // ---------------------------------------------------------------------------------------------
    // ---------------------------------------------------------------------------------------------
    // Builder class
    // ---------------------------------------------------------------------------------------------
    // ---------------------------------------------------------------------------------------------
    /**
     *
     *
     */
    public static final class Builder {
        private String baseUrl;
        private Set<ConnectionParam> connectionParams;
        private boolean autoConnect;
        private CallAdapter.Factory callAdapterFactory;

        public Builder() {
            this.callAdapterFactory = null;
            this.baseUrl =  null;
            this.connectionParams = new LinkedHashSet<ConnectionParam>();
            this.autoConnect = true;
        }

        public Builder baseUrl(String baseUrl) {
            this.baseUrl = baseUrl;
            return this;
        }

        public  Builder autoConnect(boolean autoConnect) {
            this.autoConnect = autoConnect;
            return this;
        }

        public Builder connectionParam(ConnectionParam param) {
            this.connectionParams.add(param);
            return this;
        }

        public Builder connectionParams(Set<ConnectionParam> params) {
            this.connectionParams.addAll(params);
            return this;

        }

        public Builder callAdapterFactory(CallAdapter.Factory callAdapterFactory) {
            this.callAdapterFactory = callAdapterFactory;
            return this;
        }

        public SailsIO build() {
            if (this.baseUrl == null) {
                throw new NullPointerException("The base url not should be null");
            }

            if (this.callAdapterFactory == null) {
                this.callAdapterFactory = new DefaultCallAdapterFactory();
            }

            return  new SailsIO(
                    this.baseUrl,
                    this.connectionParams,
                    this.autoConnect,
                    this.callAdapterFactory);
        }
    }
}
