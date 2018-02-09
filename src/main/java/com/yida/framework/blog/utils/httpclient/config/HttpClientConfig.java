package com.yida.framework.blog.utils.httpclient.config;

/**
 * @Author Lanxiaowei
 * @Date 2018-02-08 11:14
 * @Description HttpClient相关配置
 */
public class HttpClientConfig {
    private static final String HTTP_REDIRECT_MAX_TIMES = "http.redirect.max.times";
    private static final String HTTP_SSL_VERIFY_IGNORE = "http.ssl.verify.ignore";
    private static final String CACHE_ASYN_WORKER_MAX_SIZE = "cache.asyn.worker.max.size";

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
    private static final String PROXY_HOSTNAME = "proxy.hostname";
    private static final String PROXY_PORT = "proxy.port";
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
    private static final String CACHE_ENTRY_MAX_SIZE = "cache.entry.max.size";
    private static final String CACHE_OBJECT_MAX_SIZE = "cache.object.max.size";
    private static final String CACHE_UPDATE_RETRY_MAX_TIMES = "cache.update.retry.max.times";
    private static final String CACHE_SHARED_ENABLED = "cache.shared.enable";
    private static final String CACHE_ASYN_WORKER_IDLE_LIFETIME = "cache.asyn.worker.idle.lifetime";
    private static final String CACHE_ASYN_WORKER_CORE_SIZE = "cache.asyn.worker.core.size";
    private static final String PROXY_SCHEMA = "proxy.schema";
    private static final String CACHE_FOR_HTTP_ENABLED = "cache.for.http.enabled";
    private static final Integer DEFAULT_HTTP_REDIRECT_MAX_TIMES = 2;
    private static final Boolean DEFAULT_HTTP_SSL_VERIFY_IGNORE = true;
    private static final Long DEFAULT_HTTP_CONNECTION_TIME_TO_LIVE = 1200000L;



    //Default values
    private static final String DEFAULT_HTTP_USER_AGENT = "Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/60.0.3112.113 Safari/537.36";
    private static final String DEFAULT_HTTP_ACCEPT_CHARSET = "utf8,gbk,gb2312,iso-8859-1";
    private static final String DEFAULT_HTTP_ACCEPT_LANGUAGE = "zh-CN,zh;q=0.6, en-US;q=0.3, *;q=0.1";
    private static final String DEFAULT_HTTP_ACCEPT_ENCODING = "gzip, deflate, br";
    private static final String DEFAULT_HTTP_ACCEPT = "text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,image/apng,*/*;q=0.8";
    private static final String DEFAULT_HTTP_CACHE_CONTROL = "max-age=0";
    private static final String DEFAULT_HTTP_CONNECTION = "keep-alive";
    private static final Integer DEFAULT_HTTP_CONNECTION_REQUEST_TIMEOUT = 3000;
    private static final Integer DEFAULT_HTTP_CONNECT_TIMEOUT = 5000;
    private static final Integer DEFAULT_HTTP_SOCKET_TIMEOUT = 10000;
    private static final Boolean DEFAULT_HTTP_REDIRECTS_ENABLED = true;
    private static final Integer DEFAULT_CACHE_ASYN_WORKER_MAX_SIZE = 100;
    private static final String DEFAULT_PROXY_HOSTNAME = null;
    private static final String DEFAULT_HTTP_SSL_PROTOCOL_VERSION = "SSLv3";
    private static final Integer DEFAULT_HTTP_CONNECTION_BUFFER_SIZE = 16384;
    private static final Integer DEFAULT_HTTP_CONNECTION_POOL_MAX_TOTAL = 100;
    private static final Integer DEFAULT_HTTP_CONNECTION_POOL_MAX_PER_ROUTE = 10;
    private static final Integer DEFAULT_PROXY_PORT = 80;
    private static final Integer DEFAULT_HTTP_CONNECTION_POOL_VALIDATE_AFTER_INACTIVITY = 200;
    private static final Boolean DEFAULT_HTTP_STATE_CONNECTION_CHECK_ENABLED = true;
    private static final Boolean DEFAULT_SOCKET_ADDRESS_REUSE = false;
    private static final Integer DEFAULT_SOCKET_LINGER = -1;
    private static final Boolean DEFAULT_SOCKET_KEEP_ALIVE = true;
    private static final Boolean DEFAULT_SOCKET_TCP_NODELAY = true;
    private static final Integer DEFAULT_SOCKET_BACKLOG_SIZE = 2048;
    private static final Integer DEFAULT_SOCKET_TIMEOUT = 20000;
    private static final Integer DEFAULT_SOCKET_RECEIVE_BUFFER_SIZE = 32768;
    private static final Integer DEFAULT_SOCKET_SEND_BUFFER_SIZE = 32768;
    private static final Integer DEFAULT_CACHE_ENTRY_MAX_SIZE = 1024;
    private static final Integer DEFAULT_CACHE_OBJECT_MAX_SIZE = 8192;
    private static final Integer DEFAULT_CACHE_UPDATE_RETRY_MAX_TIMES = 5;
    private static final Boolean DEFAULT_CACHE_SHARED_ENABLED = false;
    private static final Integer DEFAULT_CACHE_ASYN_WORKER_IDLE_LIFETIME = 30;
    private static final Integer DEFAULT_CACHE_ASYN_WORKER_CORE_SIZE = 20;
    private static final String DEFAULT_PROXY_SCHEMA = "http";
    private static final Boolean DEFAULT_CACHE_FOR_HTTP_ENABLED = true;
    private Integer httpRedirectMaxTimes;
    private Boolean httpSslVerifyIgnore;
    private Long httpConnectionTimeToLive;

    private String httpUserAgent;
    private String httpAcceptCharset;
    private String httpAcceptLanguage;
    private String httpAcceptEncoding;
    private String httpAcceptRanges;
    private String httpAccept;
    private String httpCacheControl;
    private String httpConnection;
    private Integer httpConnectionRequestTimeout;
    private Integer httpConnectTimeout;
    private Integer httpSocketTimeout;
    private Boolean httpRedirectsEnabled;
    private Integer cacheAsynWorkerMaxSize;
    private String proxyHostName;
    private String httpSslProtocolVersion;
    private Integer httpConnectionBufferSize;
    private Integer httpConnectionPoolMaxTotal;
    private Integer httpConnectionPoolMaxPerRoute;
    private Integer proxyPort;
    private Integer httpConnectionPoolValidateAfterInactivity;
    private Boolean httpStaleConnectionCheckEnabled;
    private Boolean socketAddressReuse;
    private Integer socketLinger;
    private Boolean socketKeepAlive;
    private Boolean socketTcpNodelay;
    private Integer socketBacklogSize;
    private Integer socketTimeout;
    private Integer socketReceiveBufferSize;
    private Integer socketSendBufferSize;
    private Integer cacheUpdateRetryMaxTimes;
    private Integer cacheEntryMaxSize;
    private Integer cacheObjectMaxSize;
    private Boolean cacheSharedEnabled;
    private Integer cacheAsynWorkerIdleLifetime;
    private Integer cacheAsynWorkerCoreSize;
    private String proxySchema;
    private Boolean cacheForHttpEnabled;
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
        this.httpAcceptRanges = HttpClientConfigLoader.getStringProperty(HTTP_ACCEPT_RANGES, null);
        this.httpAccept = HttpClientConfigLoader.getStringProperty(HTTP_ACCEPT, DEFAULT_HTTP_ACCEPT);
        this.httpCacheControl = HttpClientConfigLoader.getStringProperty(HTTP_CACHE_CONTROL, DEFAULT_HTTP_CACHE_CONTROL);
        this.httpConnection = HttpClientConfigLoader.getStringProperty(HTTP_CONNECTION, DEFAULT_HTTP_CONNECTION);
        this.httpConnectionRequestTimeout = HttpClientConfigLoader.getIntProperty(HTTP_CONNECTION_REQUEST_TIMEOUT, DEFAULT_HTTP_CONNECTION_REQUEST_TIMEOUT);
        this.httpConnectTimeout = HttpClientConfigLoader.getIntProperty(HTTP_CONNECT_TIMEOUT, DEFAULT_HTTP_CONNECT_TIMEOUT);
        this.httpSocketTimeout = HttpClientConfigLoader.getIntProperty(HTTP_SOCKET_TIMEOUT, DEFAULT_HTTP_SOCKET_TIMEOUT);
        this.httpRedirectsEnabled = HttpClientConfigLoader.getBooleanProperty(HTTP_REDIRECTS_ENABLED, DEFAULT_HTTP_REDIRECTS_ENABLED);
        this.httpRedirectMaxTimes = HttpClientConfigLoader.getIntProperty(HTTP_REDIRECT_MAX_TIMES, DEFAULT_HTTP_REDIRECT_MAX_TIMES);
        this.httpSslVerifyIgnore = HttpClientConfigLoader.getBooleanProperty(HTTP_SSL_VERIFY_IGNORE, DEFAULT_HTTP_SSL_VERIFY_IGNORE);
        this.httpSslProtocolVersion = HttpClientConfigLoader.getStringProperty(HTTP_SSL_PROTOCOL_VERSION, DEFAULT_HTTP_SSL_PROTOCOL_VERSION);
        this.httpConnectionBufferSize = HttpClientConfigLoader.getIntProperty(HTTP_CONNECTION_BUFFER_SIZE, DEFAULT_HTTP_CONNECTION_BUFFER_SIZE);
        this.httpConnectionPoolMaxTotal = HttpClientConfigLoader.getIntProperty(HTTP_CONNECTION_POOL_MAX_TOTAL, DEFAULT_HTTP_CONNECTION_POOL_MAX_TOTAL);
        this.httpConnectionPoolMaxPerRoute = HttpClientConfigLoader.getIntProperty(HTTP_CONNECTION_POOL_MAX_PER_ROUTE, DEFAULT_HTTP_CONNECTION_POOL_MAX_PER_ROUTE);
        this.httpConnectionTimeToLive = HttpClientConfigLoader.getLongProperty(HTTP_CONNECTION_TIME_TO_LIVE, DEFAULT_HTTP_CONNECTION_TIME_TO_LIVE);
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

        //Http Cache Configuration
        this.cacheForHttpEnabled = HttpClientConfigLoader.getBooleanProperty(CACHE_FOR_HTTP_ENABLED, DEFAULT_CACHE_FOR_HTTP_ENABLED);
        this.cacheEntryMaxSize = HttpClientConfigLoader.getIntProperty(CACHE_ENTRY_MAX_SIZE, DEFAULT_CACHE_ENTRY_MAX_SIZE);
        this.cacheObjectMaxSize = HttpClientConfigLoader.getIntProperty(CACHE_OBJECT_MAX_SIZE, DEFAULT_CACHE_OBJECT_MAX_SIZE);
        this.cacheSharedEnabled = HttpClientConfigLoader.getBooleanProperty(CACHE_SHARED_ENABLED, DEFAULT_CACHE_SHARED_ENABLED);
        this.cacheUpdateRetryMaxTimes = HttpClientConfigLoader.getIntProperty(CACHE_UPDATE_RETRY_MAX_TIMES, DEFAULT_CACHE_UPDATE_RETRY_MAX_TIMES);
        this.cacheAsynWorkerIdleLifetime = HttpClientConfigLoader.getIntProperty(CACHE_ASYN_WORKER_IDLE_LIFETIME, DEFAULT_CACHE_ASYN_WORKER_IDLE_LIFETIME);
        this.cacheAsynWorkerCoreSize = HttpClientConfigLoader.getIntProperty(CACHE_ASYN_WORKER_CORE_SIZE, DEFAULT_CACHE_ASYN_WORKER_CORE_SIZE);
        this.cacheAsynWorkerMaxSize = HttpClientConfigLoader.getIntProperty(CACHE_ASYN_WORKER_MAX_SIZE, DEFAULT_CACHE_ASYN_WORKER_MAX_SIZE);
        this.proxyHostName = HttpClientConfigLoader.getStringProperty(PROXY_HOSTNAME, DEFAULT_PROXY_HOSTNAME);
        this.proxyPort = HttpClientConfigLoader.getIntProperty(PROXY_PORT, DEFAULT_PROXY_PORT);
        this.proxySchema = HttpClientConfigLoader.getStringProperty(PROXY_SCHEMA, DEFAULT_PROXY_SCHEMA);
    }

    public Integer getHttpConnectionRequestTimeout() {
        return httpConnectionRequestTimeout;
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

    public Integer getHttpRedirectMaxTimes() {
        return httpRedirectMaxTimes;
    }

    public void setHttpConnectionRequestTimeout(Integer httpConnectionRequestTimeout) {
        this.httpConnectionRequestTimeout = httpConnectionRequestTimeout;
    }

    public Integer getHttpConnectTimeout() {
        return httpConnectTimeout;
    }

    public void setHttpConnectTimeout(Integer httpConnectTimeout) {
        this.httpConnectTimeout = httpConnectTimeout;
    }

    public Integer getHttpSocketTimeout() {
        return httpSocketTimeout;
    }

    public void setHttpSocketTimeout(Integer httpSocketTimeout) {
        this.httpSocketTimeout = httpSocketTimeout;
    }

    public Boolean getHttpRedirectsEnabled() {
        return httpRedirectsEnabled;
    }

    public void setHttpRedirectsEnabled(Boolean httpRedirectsEnabled) {
        this.httpRedirectsEnabled = httpRedirectsEnabled;
    }

    public void setHttpRedirectMaxTimes(Integer httpRedirectMaxTimes) {
        this.httpRedirectMaxTimes = httpRedirectMaxTimes;
    }

    public Boolean getHttpSslVerifyIgnore() {
        return httpSslVerifyIgnore;
    }

    public void setHttpSslVerifyIgnore(Boolean httpSslVerifyIgnore) {
        this.httpSslVerifyIgnore = httpSslVerifyIgnore;
    }

    public Integer getHttpConnectionBufferSize() {
        return httpConnectionBufferSize;
    }

    public String getHttpSslProtocolVersion() {
        return httpSslProtocolVersion;
    }

    public void setHttpSslProtocolVersion(String httpSslProtocolVersion) {
        this.httpSslProtocolVersion = httpSslProtocolVersion;
    }

    public Long getHttpConnectionTimeToLive() {
        return httpConnectionTimeToLive;
    }

    public void setHttpConnectionBufferSize(Integer httpConnectionBufferSize) {
        this.httpConnectionBufferSize = httpConnectionBufferSize;
    }

    public Integer getHttpConnectionPoolMaxTotal() {
        return httpConnectionPoolMaxTotal;
    }

    public void setHttpConnectionPoolMaxTotal(Integer httpConnectionPoolMaxTotal) {
        this.httpConnectionPoolMaxTotal = httpConnectionPoolMaxTotal;
    }

    public Integer getHttpConnectionPoolMaxPerRoute() {
        return httpConnectionPoolMaxPerRoute;
    }

    public void setHttpConnectionPoolMaxPerRoute(Integer httpConnectionPoolMaxPerRoute) {
        this.httpConnectionPoolMaxPerRoute = httpConnectionPoolMaxPerRoute;
    }

    public void setHttpConnectionTimeToLive(Long httpConnectionTimeToLive) {
        this.httpConnectionTimeToLive = httpConnectionTimeToLive;
    }

    public Integer getCacheUpdateRetryMaxTimes() {
        return cacheUpdateRetryMaxTimes;
    }

    public Integer getHttpConnectionPoolValidateAfterInactivity() {
        return httpConnectionPoolValidateAfterInactivity;
    }

    public void setHttpConnectionPoolValidateAfterInactivity(Integer httpConnectionPoolValidateAfterInactivity) {
        this.httpConnectionPoolValidateAfterInactivity = httpConnectionPoolValidateAfterInactivity;
    }

    public Boolean getHttpStaleConnectionCheckEnabled() {
        return httpStaleConnectionCheckEnabled;
    }

    public void setHttpStaleConnectionCheckEnabled(Boolean httpStaleConnectionCheckEnabled) {
        this.httpStaleConnectionCheckEnabled = httpStaleConnectionCheckEnabled;
    }

    public Boolean getSocketAddressReuse() {
        return socketAddressReuse;
    }

    public void setSocketAddressReuse(Boolean socketAddressReuse) {
        this.socketAddressReuse = socketAddressReuse;
    }

    public Integer getSocketLinger() {
        return socketLinger;
    }

    public void setSocketLinger(Integer socketLinger) {
        this.socketLinger = socketLinger;
    }

    public Boolean getSocketKeepAlive() {
        return socketKeepAlive;
    }

    public void setSocketKeepAlive(Boolean socketKeepAlive) {
        this.socketKeepAlive = socketKeepAlive;
    }

    public Boolean getSocketTcpNodelay() {
        return socketTcpNodelay;
    }

    public void setSocketTcpNodelay(Boolean socketTcpNodelay) {
        this.socketTcpNodelay = socketTcpNodelay;
    }

    public Integer getSocketBacklogSize() {
        return socketBacklogSize;
    }

    public void setSocketBacklogSize(Integer socketBacklogSize) {
        this.socketBacklogSize = socketBacklogSize;
    }

    public Integer getSocketTimeout() {
        return socketTimeout;
    }

    public void setSocketTimeout(Integer socketTimeout) {
        this.socketTimeout = socketTimeout;
    }

    public Integer getSocketReceiveBufferSize() {
        return socketReceiveBufferSize;
    }

    public void setSocketReceiveBufferSize(Integer socketReceiveBufferSize) {
        this.socketReceiveBufferSize = socketReceiveBufferSize;
    }

    public Integer getSocketSendBufferSize() {
        return socketSendBufferSize;
    }

    public void setSocketSendBufferSize(Integer socketSendBufferSize) {
        this.socketSendBufferSize = socketSendBufferSize;
    }

    public void setCacheUpdateRetryMaxTimes(Integer cacheUpdateRetryMaxTimes) {
        this.cacheUpdateRetryMaxTimes = cacheUpdateRetryMaxTimes;
    }

    public Integer getCacheAsynWorkerMaxSize() {
        return cacheAsynWorkerMaxSize;
    }

    public Integer getCacheEntryMaxSize() {
        return cacheEntryMaxSize;
    }

    public void setCacheEntryMaxSize(Integer cacheEntryMaxSize) {
        this.cacheEntryMaxSize = cacheEntryMaxSize;
    }

    public Integer getCacheObjectMaxSize() {
        return cacheObjectMaxSize;
    }

    public void setCacheObjectMaxSize(Integer cacheObjectMaxSize) {
        this.cacheObjectMaxSize = cacheObjectMaxSize;
    }

    public Boolean getCacheSharedEnabled() {
        return cacheSharedEnabled;
    }

    public void setCacheSharedEnabled(Boolean cacheSharedEnabled) {
        this.cacheSharedEnabled = cacheSharedEnabled;
    }

    public Integer getCacheAsynWorkerIdleLifetime() {
        return cacheAsynWorkerIdleLifetime;
    }

    public void setCacheAsynWorkerIdleLifetime(Integer cacheAsynWorkerIdleLifetime) {
        this.cacheAsynWorkerIdleLifetime = cacheAsynWorkerIdleLifetime;
    }

    public Integer getCacheAsynWorkerCoreSize() {
        return cacheAsynWorkerCoreSize;
    }

    public void setCacheAsynWorkerCoreSize(Integer cacheAsynWorkerCoreSize) {
        this.cacheAsynWorkerCoreSize = cacheAsynWorkerCoreSize;
    }

    public void setCacheAsynWorkerMaxSize(Integer cacheAsynWorkerMaxSize) {
        this.cacheAsynWorkerMaxSize = cacheAsynWorkerMaxSize;
    }

    public String getProxyHostName() {
        return proxyHostName;
    }

    public void setProxyHostName(String proxyHostName) {
        this.proxyHostName = proxyHostName;
    }

    public Integer getProxyPort() {
        return proxyPort;
    }

    public void setProxyPort(Integer proxyPort) {
        this.proxyPort = proxyPort;
    }

    public String getProxySchema() {
        return proxySchema;
    }

    public void setProxySchema(String proxySchema) {
        this.proxySchema = proxySchema;
    }

    public Boolean getCacheForHttpEnabled() {
        return cacheForHttpEnabled;
    }

    public void setCacheForHttpEnabled(Boolean cacheForHttpEnabled) {
        this.cacheForHttpEnabled = cacheForHttpEnabled;
    }

    private static class SingletonHolder {
        private static final HttpClientConfig INSTANCE = new HttpClientConfig();
    }
}
