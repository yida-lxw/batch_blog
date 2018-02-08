package com.yida.framework.blog.utils.httpclient;

/**
 * @Author Lanxiaowei
 * @Date 2018-02-08 11:14
 * @Description HttpClient相关配置
 */
public class HttpClientConfig {


    //Keys
    private static final String HTTP_USER_AGENT = "http.user_agent";
    private static final String HTTP_ACCEPT_CHARSET = "http.accept.charset";
    private static final String HTTP_ACCEPT_LANGUAGE = "http.accept.language";
    private static final String HTTP_ACCEPT_ENCODING = "http.accept.encoding";
    private static final String HTTP_ACCEPT_RANGES = "http.accept.ranges";
    private static final String HTTP_ACCEPT = "http.accept";
    private static final String HTTP_CACHE_CONTROL = "http.cache.control";
    private static final String HTTP_CONNECTION = "http.connection";
    private static final String HTTP_CONNECTION_REQUEST_TIMEOUT = "http.connection.request.timeout";
    private static final String HTTP_CONNECT_TIMEOUT = "http.connect.timeout";
    private static final String HTTP_SOCKET_TIMEOUT = "http.socket.timeout";
    private static final String HTTP_REDIRECTS_ENABLED = "http.redirects.enabled";
    private static final String HTTP_SSL_PROTOCOL_VERSION = "http.ssl.protocol.version";
    private static final String HTTP_CONNECTION_BUFFER_SIZE = "http.connection.buffer.size";
    private static final String HTTP_CONNECTION_POOL_MAX_TOTAL = "http.connection.pool.max.total";
    private static final String HTTP_CONNECTION_POOL_MAX_PER_ROUTE = "http.connection.pool.max.per.route";
    private static final String HTTP_CONNECTION_TIME_TO_LIVE = "http.connection.time.to.live";
    private static final String HTTP_CONNECTION_POOL_VALIDATE_AFTER_INACTIVITY = "http.connection.pool.validate.after.inactivity";
    private static final String HTTP_STATE_CONNECTION_CHECK_ENABLED = "http.stale.connection.check.enabled";
    private static final String SOCKET_ADDRESS_REUSE = "socket.address.reuse";
    private static final String SOCKET_LINGER = "socket.linger";
    private static final String SOCKET_KEEP_ALIVE = "socket.keep.alive";
    private static final String SOCKET_TCP_NODELAY = "socket.tcp.nodelay";
    private static final String SOCKET_BACKLOG_SIZE = "socket.backlog.size";
    private static final String SOCKET_TIMEOUT = "socket.timeout";
    private static final String SOCKET_RECEIVE_BUFFER_SIZE = "socket.receive.buffer.size";
    private static final String SOCKET_SEND_BUFFER_SIZE = "socket.send.buffer.size";
    //Default values
    private static final String DEFAULT_HTTP_USER_AGENT = "Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/60.0.3112.113 Safari/537.36";
    private static final String DEFAULT_HTTP_ACCEPT_CHARSET = "utf8,gbk,gb2312,iso-8859-1";
    private static final String DEFAULT_HTTP_ACCEPT_LANGUAGE = "zh-CN,zh;q=0.6, en-US;q=0.3, *;q=0.1";
    private static final String DEFAULT_HTTP_ACCEPT_ENCODING = "gzip, deflate, br";
    private static final String DEFAULT_HTTP_ACCEPT = "text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,image/apng,*/*;q=0.8";
    private static final String DEFAULT_HTTP_CACHE_CONTROL = "max-age=0";
    private static final String DEFAULT_HTTP_CONNECTION = "keep-alive";
    private static final int DEFAULT_HTTP_CONNECTION_REQUEST_TIMEOUT = 3000;
    private static final int DEFAULT_HTTP_CONNECT_TIMEOUT = 5000;
    private static final int DEFAULT_HTTP_SOCKET_TIMEOUT = 10000;
    private static final boolean DEFAULT_HTTP_REDIRECTS_ENABLED = true;
    private static final String DEFAULT_HTTP_SSL_PROTOCOL_VERSION = "SSLv3";
    private static final int DEFAULT_HTTP_CONNECTION_BUFFER_SIZE = 16384;
    private static final int DEFAULT_HTTP_CONNECTION_POOL_MAX_TOTAL = 100;
    private static final int DEFAULT_HTTP_CONNECTION_POOL_MAX_PER_ROUTE = 10;
    private static final int DEFAULT_HTTP_CONNECTION_TIME_TO_LIVE = 1200000;
    private static final int DEFAULT_HTTP_CONNECTION_POOL_VALIDATE_AFTER_INACTIVITY = 200;
    private static final boolean DEFAULT_HTTP_STATE_CONNECTION_CHECK_ENABLED = true;
    private static final boolean DEFAULT_SOCKET_ADDRESS_REUSE = false;
    private static final int DEFAULT_SOCKET_LINGER = -1;
    private static final boolean DEFAULT_SOCKET_KEEP_ALIVE = true;
    private static final boolean DEFAULT_SOCKET_TCP_NODELAY = true;
    private static final int DEFAULT_SOCKET_BACKLOG_SIZE = 2048;
    private static final int DEFAULT_SOCKET_TIMEOUT = 20000;
    private static final int DEFAULT_SOCKET_RECEIVE_BUFFER_SIZE = 32768;
    private static final int DEFAULT_SOCKET_SEND_BUFFER_SIZE = 32768;
    private String httpUserAgent;
    private String httpAcceptCharset;
    private String httpAcceptLanguage;
    private String httpAcceptEncoding;
    private String httpAcceptRanges;
    private String httpAccept;
    private String httpCacheControl;
    private String httpConnection;
    private int httpConnectionRequestTimeout;
    private int httpConnectTimeout;
    private int httpSocketTimeout;
    private boolean httpRedirectsEnabled;
    private String httpSslProtocolVersion;
    private int httpConnectionBufferSize;
    private int httpConnectionPoolMaxTotal;
    private int httpConnectionPoolMaxPerRoute;
    private int httpConnectionTimeToLive;
    private int httpConnectionPoolValidateAfterInactivity;
    private boolean httpStaleConnectionCheckEnabled;
    private boolean socketAddressReuse;
    private int socketLinger;
    private boolean socketKeepAlive;
    private boolean socketTcpNodelay;
    private int socketBacklogSize;
    private int socketTimeout;
    private int socketReceiveBufferSize;
    private int socketSendBufferSize;

    private HttpClientConfig() {
        initialize();
    }

    public static final HttpClientConfig getInstance() {
        return HttpClientConfig.SingletonHolder.INSTANCE;
    }

    /**
     * 初始化HttpClient相关配置
     */
    public void initialize() {
        this.httpUserAgent = HttpClientConfigLoader.getStringProperty(HTTP_USER_AGENT, DEFAULT_HTTP_USER_AGENT);
        this.httpAcceptCharset = HttpClientConfigLoader.getStringProperty(HTTP_ACCEPT_CHARSET, DEFAULT_HTTP_ACCEPT_CHARSET);
        this.httpAcceptLanguage = HttpClientConfigLoader.getStringProperty(HTTP_ACCEPT_LANGUAGE, DEFAULT_HTTP_ACCEPT_LANGUAGE);
        this.httpAcceptEncoding = HttpClientConfigLoader.getStringProperty(HTTP_ACCEPT_ENCODING, DEFAULT_HTTP_ACCEPT_ENCODING);
        this.httpAcceptRanges = HttpClientConfigLoader.getStringProperty(HTTP_ACCEPT_RANGES);
        this.httpAccept = HttpClientConfigLoader.getStringProperty(HTTP_ACCEPT, DEFAULT_HTTP_ACCEPT);
        this.httpCacheControl = HttpClientConfigLoader.getStringProperty(HTTP_CACHE_CONTROL, DEFAULT_HTTP_CACHE_CONTROL);
        this.httpConnection = HttpClientConfigLoader.getStringProperty(HTTP_CONNECTION, DEFAULT_HTTP_CONNECTION);
        this.httpConnectionRequestTimeout = HttpClientConfigLoader.getIntProperty(HTTP_CONNECTION_REQUEST_TIMEOUT, DEFAULT_HTTP_CONNECTION_REQUEST_TIMEOUT);
        this.httpConnectTimeout = HttpClientConfigLoader.getIntProperty(HTTP_CONNECT_TIMEOUT, DEFAULT_HTTP_CONNECT_TIMEOUT);
        this.httpSocketTimeout = HttpClientConfigLoader.getIntProperty(HTTP_SOCKET_TIMEOUT, DEFAULT_HTTP_SOCKET_TIMEOUT);
        this.httpRedirectsEnabled = HttpClientConfigLoader.getBooleanProperty(HTTP_REDIRECTS_ENABLED, DEFAULT_HTTP_REDIRECTS_ENABLED);
        this.httpSslProtocolVersion = HttpClientConfigLoader.getStringProperty(HTTP_SSL_PROTOCOL_VERSION, DEFAULT_HTTP_SSL_PROTOCOL_VERSION);
        this.httpConnectionBufferSize = HttpClientConfigLoader.getIntProperty(HTTP_CONNECTION_BUFFER_SIZE, DEFAULT_HTTP_CONNECTION_BUFFER_SIZE);
        this.httpConnectionPoolMaxTotal = HttpClientConfigLoader.getIntProperty(HTTP_CONNECTION_POOL_MAX_TOTAL, DEFAULT_HTTP_CONNECTION_POOL_MAX_TOTAL);
        this.httpConnectionPoolMaxPerRoute = HttpClientConfigLoader.getIntProperty(HTTP_CONNECTION_POOL_MAX_PER_ROUTE, DEFAULT_HTTP_CONNECTION_POOL_MAX_PER_ROUTE);
        this.httpConnectionTimeToLive = HttpClientConfigLoader.getIntProperty(HTTP_CONNECTION_TIME_TO_LIVE, DEFAULT_HTTP_CONNECTION_TIME_TO_LIVE);
        this.httpConnectionPoolValidateAfterInactivity = HttpClientConfigLoader.getIntProperty(HTTP_CONNECTION_POOL_VALIDATE_AFTER_INACTIVITY, DEFAULT_HTTP_CONNECTION_POOL_VALIDATE_AFTER_INACTIVITY);
        this.httpStaleConnectionCheckEnabled = HttpClientConfigLoader.getBooleanProperty(HTTP_STATE_CONNECTION_CHECK_ENABLED, DEFAULT_HTTP_STATE_CONNECTION_CHECK_ENABLED);
        this.socketAddressReuse = HttpClientConfigLoader.getBooleanProperty(SOCKET_ADDRESS_REUSE, DEFAULT_SOCKET_ADDRESS_REUSE);
        this.socketLinger = HttpClientConfigLoader.getIntProperty(SOCKET_LINGER, DEFAULT_SOCKET_LINGER);
        this.socketKeepAlive = HttpClientConfigLoader.getBooleanProperty(SOCKET_KEEP_ALIVE, DEFAULT_SOCKET_KEEP_ALIVE);
        this.socketTcpNodelay = HttpClientConfigLoader.getBooleanProperty(SOCKET_TCP_NODELAY, DEFAULT_SOCKET_TCP_NODELAY);
        this.socketBacklogSize = HttpClientConfigLoader.getIntProperty(SOCKET_BACKLOG_SIZE, DEFAULT_SOCKET_BACKLOG_SIZE);
        this.socketTimeout = HttpClientConfigLoader.getIntProperty(SOCKET_TIMEOUT, DEFAULT_SOCKET_TIMEOUT);
        this.socketReceiveBufferSize = HttpClientConfigLoader.getIntProperty(SOCKET_RECEIVE_BUFFER_SIZE, DEFAULT_SOCKET_RECEIVE_BUFFER_SIZE);
        this.socketSendBufferSize = HttpClientConfigLoader.getIntProperty(SOCKET_SEND_BUFFER_SIZE, DEFAULT_SOCKET_SEND_BUFFER_SIZE);
    }

    public String getHttpUserAgent() {
        return httpUserAgent;
    }

    public void setHttpUserAgent(String httpUserAgent) {
        this.httpUserAgent = httpUserAgent;
    }

    public String getHttpAcceptCharset() {
        return httpAcceptCharset;
    }

    public void setHttpAcceptCharset(String httpAcceptCharset) {
        this.httpAcceptCharset = httpAcceptCharset;
    }

    public String getHttpAcceptLanguage() {
        return httpAcceptLanguage;
    }

    public void setHttpAcceptLanguage(String httpAcceptLanguage) {
        this.httpAcceptLanguage = httpAcceptLanguage;
    }

    public String getHttpAcceptEncoding() {
        return httpAcceptEncoding;
    }

    public void setHttpAcceptEncoding(String httpAcceptEncoding) {
        this.httpAcceptEncoding = httpAcceptEncoding;
    }

    public String getHttpAcceptRanges() {
        return httpAcceptRanges;
    }

    public void setHttpAcceptRanges(String httpAcceptRanges) {
        this.httpAcceptRanges = httpAcceptRanges;
    }

    public String getHttpAccept() {
        return httpAccept;
    }

    public void setHttpAccept(String httpAccept) {
        this.httpAccept = httpAccept;
    }

    public String getHttpCacheControl() {
        return httpCacheControl;
    }

    public void setHttpCacheControl(String httpCacheControl) {
        this.httpCacheControl = httpCacheControl;
    }

    public String getHttpConnection() {
        return httpConnection;
    }

    public void setHttpConnection(String httpConnection) {
        this.httpConnection = httpConnection;
    }

    public int getHttpConnectionRequestTimeout() {
        return httpConnectionRequestTimeout;
    }

    public void setHttpConnectionRequestTimeout(int httpConnectionRequestTimeout) {
        this.httpConnectionRequestTimeout = httpConnectionRequestTimeout;
    }

    public int getHttpConnectTimeout() {
        return httpConnectTimeout;
    }

    public void setHttpConnectTimeout(int httpConnectTimeout) {
        this.httpConnectTimeout = httpConnectTimeout;
    }

    public int getHttpSocketTimeout() {
        return httpSocketTimeout;
    }

    public void setHttpSocketTimeout(int httpSocketTimeout) {
        this.httpSocketTimeout = httpSocketTimeout;
    }

    public boolean isHttpRedirectsEnabled() {
        return httpRedirectsEnabled;
    }

    public void setHttpRedirectsEnabled(boolean httpRedirectsEnabled) {
        this.httpRedirectsEnabled = httpRedirectsEnabled;
    }

    public String getHttpSslProtocolVersion() {
        return httpSslProtocolVersion;
    }

    public void setHttpSslProtocolVersion(String httpSslProtocolVersion) {
        this.httpSslProtocolVersion = httpSslProtocolVersion;
    }

    public int getHttpConnectionBufferSize() {
        return httpConnectionBufferSize;
    }

    public void setHttpConnectionBufferSize(int httpConnectionBufferSize) {
        this.httpConnectionBufferSize = httpConnectionBufferSize;
    }

    public int getHttpConnectionPoolMaxTotal() {
        return httpConnectionPoolMaxTotal;
    }

    public void setHttpConnectionPoolMaxTotal(int httpConnectionPoolMaxTotal) {
        this.httpConnectionPoolMaxTotal = httpConnectionPoolMaxTotal;
    }

    public int getHttpConnectionPoolMaxPerRoute() {
        return httpConnectionPoolMaxPerRoute;
    }

    public void setHttpConnectionPoolMaxPerRoute(int httpConnectionPoolMaxPerRoute) {
        this.httpConnectionPoolMaxPerRoute = httpConnectionPoolMaxPerRoute;
    }

    public int getHttpConnectionTimeToLive() {
        return httpConnectionTimeToLive;
    }

    public void setHttpConnectionTimeToLive(int httpConnectionTimeToLive) {
        this.httpConnectionTimeToLive = httpConnectionTimeToLive;
    }

    public int getHttpConnectionPoolValidateAfterInactivity() {
        return httpConnectionPoolValidateAfterInactivity;
    }

    public void setHttpConnectionPoolValidateAfterInactivity(int httpConnectionPoolValidateAfterInactivity) {
        this.httpConnectionPoolValidateAfterInactivity = httpConnectionPoolValidateAfterInactivity;
    }

    public boolean isHttpStaleConnectionCheckEnabled() {
        return httpStaleConnectionCheckEnabled;
    }

    public void setHttpStaleConnectionCheckEnabled(boolean httpStaleConnectionCheckEnabled) {
        this.httpStaleConnectionCheckEnabled = httpStaleConnectionCheckEnabled;
    }

    public boolean isSocketAddressReuse() {
        return socketAddressReuse;
    }

    public void setSocketAddressReuse(boolean socketAddressReuse) {
        this.socketAddressReuse = socketAddressReuse;
    }

    public int getSocketLinger() {
        return socketLinger;
    }

    public void setSocketLinger(int socketLinger) {
        this.socketLinger = socketLinger;
    }

    public boolean isSocketKeepAlive() {
        return socketKeepAlive;
    }

    public void setSocketKeepAlive(boolean socketKeepAlive) {
        this.socketKeepAlive = socketKeepAlive;
    }

    public boolean isSocketTcpNodelay() {
        return socketTcpNodelay;
    }

    public void setSocketTcpNodelay(boolean socketTcpNodelay) {
        this.socketTcpNodelay = socketTcpNodelay;
    }

    public int getSocketBacklogSize() {
        return socketBacklogSize;
    }

    public void setSocketBacklogSize(int socketBacklogSize) {
        this.socketBacklogSize = socketBacklogSize;
    }

    public int getSocketTimeout() {
        return socketTimeout;
    }

    public void setSocketTimeout(int socketTimeout) {
        this.socketTimeout = socketTimeout;
    }

    public int getSocketReceiveBufferSize() {
        return socketReceiveBufferSize;
    }

    public void setSocketReceiveBufferSize(int socketReceiveBufferSize) {
        this.socketReceiveBufferSize = socketReceiveBufferSize;
    }

    public int getSocketSendBufferSize() {
        return socketSendBufferSize;
    }

    public void setSocketSendBufferSize(int socketSendBufferSize) {
        this.socketSendBufferSize = socketSendBufferSize;
    }

    private static class SingletonHolder {
        private static final HttpClientConfig INSTANCE = new HttpClientConfig();
    }
}
