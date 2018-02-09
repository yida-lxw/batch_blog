package com.yida.framework.blog.utils.httpclient.factory;

import com.yida.framework.blog.utils.httpclient.config.AbstractHttpClientConfigurable;
import org.apache.http.Consts;
import org.apache.http.Header;
import org.apache.http.HttpHost;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.config.ConnectionConfig;
import org.apache.http.config.Registry;
import org.apache.http.config.RegistryBuilder;
import org.apache.http.config.SocketConfig;
import org.apache.http.conn.socket.ConnectionSocketFactory;
import org.apache.http.conn.socket.PlainConnectionSocketFactory;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.impl.client.BasicCookieStore;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.cache.CacheConfig;
import org.apache.http.impl.client.cache.CachingHttpClientBuilder;
import org.apache.http.impl.client.cache.CachingHttpClients;
import org.apache.http.impl.client.cache.ManagedHttpCacheStorage;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.apache.http.message.BasicHeader;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.ArrayList;
import java.util.Collection;
import java.util.concurrent.TimeUnit;

/**
 * @Author Lanxiaowei
 * @Date 2018-02-09 11:43
 * @Description HttpClient实例的工厂类, 用于创建HttpClient类的单例对象,
 * 因为HttpClient类内部已经包含了Http Connection Pool,
 * 故需要设计为单例模式
 */
public class HttpClientFactory extends AbstractHttpClientConfigurable {
    private Logger log = LogManager.getLogger(HttpClientFactory.class.getName());

    private CloseableHttpClient httpClient;

    private HttpClientFactory() {
        initialize();
    }

    public static final HttpClientFactory getInstance() {
        return HttpClientFactory.SingletonHolder.INSTANCE;
    }

    private static class SingletonHolder {
        private static final HttpClientFactory INSTANCE = new HttpClientFactory();
    }

    public void initialize() {
        //Http Request Config
        RequestConfig requestConfig = RequestConfig.custom()
                .setContentCompressionEnabled(false)
                .setAuthenticationEnabled(false)
                .setCircularRedirectsAllowed(false)
                .setRelativeRedirectsAllowed(true)
                .setRedirectsEnabled(this.clientConfig.getHttpRedirectsEnabled())
                .setMaxRedirects(this.clientConfig.getHttpRedirectMaxTimes())
                .setConnectionRequestTimeout(this.clientConfig.getHttpConnectionRequestTimeout())
                .setConnectTimeout(this.clientConfig.getHttpConnectTimeout())
                .setSocketTimeout(this.clientConfig.getHttpSocketTimeout())
                .setStaleConnectionCheckEnabled(this.clientConfig.getHttpStaleConnectionCheckEnabled())
                .build();

        CachingHttpClientBuilder httpClientBuilder = (CachingHttpClientBuilder) CachingHttpClients.custom()
                .setDefaultRequestConfig(requestConfig);
        //If Http Cache was enabled
        if (this.clientConfig.getCacheForHttpEnabled()) {
            //Http Cache Config
            CacheConfig cacheConfig = CacheConfig.custom()
                    .setMaxCacheEntries(this.clientConfig.getCacheEntryMaxSize())
                    .setMaxObjectSize(this.clientConfig.getCacheObjectMaxSize())
                    .setMaxUpdateRetries(this.clientConfig.getCacheUpdateRetryMaxTimes())
                    .setSharedCache(this.getClientConfig().getCacheSharedEnabled())
                    .setAsynchronousWorkerIdleLifetimeSecs(this.clientConfig.getCacheAsynWorkerIdleLifetime())
                    .setAsynchronousWorkersCore(this.clientConfig.getCacheAsynWorkerCoreSize())
                    .setAsynchronousWorkersMax(this.clientConfig.getCacheAsynWorkerMaxSize())
                    .build();
            httpClientBuilder = httpClientBuilder
                    .setCacheConfig(cacheConfig)
                    .setHttpCacheStorage(new ManagedHttpCacheStorage(cacheConfig));
        }

        RegistryBuilder registryBuilder = RegistryBuilder.<ConnectionSocketFactory>create()
                .register("http", PlainConnectionSocketFactory.getSocketFactory());
        Registry<ConnectionSocketFactory> socketFactoryRegistry = null;
        if (this.clientConfig.getHttpSslVerifyIgnore()) {
            SSLContext sslcontext = null;
            try {
                sslcontext = createIgnoreVerifySSL();
            } catch (NoSuchAlgorithmException e) {
                log.error("Create the instance of SSLContext occured unexpected [NoSuchAlgorithmException]:\n{}", e.getMessage());
            } catch (KeyManagementException e) {
                log.error("Create the instance of SSLContext occured unexpected [KeyManagementException]:\n{}", e.getMessage());
            }
            registryBuilder = registryBuilder.register("https", new SSLConnectionSocketFactory(sslcontext));
        }

        Collection<Header> defaultHeaders = new ArrayList<>();
        defaultHeaders.add(new BasicHeader("Accept", this.clientConfig.getHttpAccept()));
        defaultHeaders.add(new BasicHeader("Accept-Charset", this.clientConfig.getHttpAcceptCharset()));
        defaultHeaders.add(new BasicHeader("Accept-Language", this.clientConfig.getHttpAcceptLanguage()));
        defaultHeaders.add(new BasicHeader("Cache-Control", this.clientConfig.getHttpCacheControl()));
        defaultHeaders.add(new BasicHeader("Connection", this.clientConfig.getHttpConnection()));
        defaultHeaders.add(new BasicHeader("User-Agent", this.clientConfig.getHttpUserAgent()));
        if (null != this.getClientConfig().getHttpAcceptEncoding() && !"".equals(this.getClientConfig().getHttpAcceptEncoding())) {
            defaultHeaders.add(new BasicHeader("Accept-Encoding", this.clientConfig.getHttpAcceptEncoding()));
        }
        if (null != this.getClientConfig().getHttpAcceptRanges() && !"".equals(this.getClientConfig().getHttpAcceptRanges())) {
            defaultHeaders.add(new BasicHeader("Accept-Ranges", this.clientConfig.getHttpAcceptRanges()));
        }
        socketFactoryRegistry = registryBuilder.build();
        PoolingHttpClientConnectionManager poolingHttpClientConnectionManager =
                new PoolingHttpClientConnectionManager(socketFactoryRegistry, null, null, null,
                        this.clientConfig.getHttpConnectionTimeToLive(), TimeUnit.MILLISECONDS);
        poolingHttpClientConnectionManager.setDefaultMaxPerRoute(this.clientConfig.getHttpConnectionPoolMaxPerRoute());
        poolingHttpClientConnectionManager.setMaxTotal(this.clientConfig.getHttpConnectionPoolMaxTotal());
        poolingHttpClientConnectionManager.setValidateAfterInactivity(this.clientConfig.getHttpConnectionPoolValidateAfterInactivity());

        //Build ConnectionConfig Instance
        ConnectionConfig connectionConfig = ConnectionConfig.custom()
                .setBufferSize(this.clientConfig.getHttpConnectionBufferSize())
                .setCharset(Consts.ASCII).build();
        poolingHttpClientConnectionManager.setDefaultConnectionConfig(connectionConfig);

        //Build SocketConfig Instance
        SocketConfig socketConfig = SocketConfig.custom()
                .setBacklogSize(this.clientConfig.getSocketBacklogSize())
                .setRcvBufSize(this.clientConfig.getSocketReceiveBufferSize())
                .setSndBufSize(this.clientConfig.getSocketSendBufferSize())
                .setSoKeepAlive(this.clientConfig.getSocketKeepAlive())
                .setSoLinger(this.clientConfig.getSocketLinger())
                .setSoReuseAddress(this.clientConfig.getSocketAddressReuse())
                .setSoTimeout(this.clientConfig.getSocketTimeout())
                .setTcpNoDelay(this.clientConfig.getSocketTcpNodelay())
                .build();
        poolingHttpClientConnectionManager.setDefaultSocketConfig(socketConfig);


        httpClientBuilder = (CachingHttpClientBuilder) httpClientBuilder
                .setConnectionTimeToLive(this.clientConfig.getHttpConnectionTimeToLive(), TimeUnit.MILLISECONDS)
                .setDefaultConnectionConfig(ConnectionConfig.custom().setBufferSize(this.clientConfig.getHttpConnectionBufferSize()).build())
                .setDefaultCookieStore(new BasicCookieStore())
                .setDefaultHeaders(defaultHeaders)
                .setConnectionManager(poolingHttpClientConnectionManager);

        //Http Proxy Config
        String proxyHostName = this.clientConfig.getProxyHostName();
        if (null != proxyHostName && !"".equals(proxyHostName)) {
            httpClientBuilder = (CachingHttpClientBuilder) httpClientBuilder.setProxy(
                    new HttpHost(proxyHostName,
                            this.clientConfig.getProxyPort(),
                            this.getClientConfig().getProxySchema()));
        }
        this.httpClient = httpClientBuilder.build();
    }

    public CloseableHttpClient getHttpClient() {
        return httpClient;
    }

    public SSLContext createIgnoreVerifySSL() throws NoSuchAlgorithmException, KeyManagementException {
        SSLContext sslContext = SSLContext.getInstance(this.clientConfig.getHttpSslProtocolVersion());
        // 实现一个X509TrustManager接口，用于绕过验证
        X509TrustManager trustManager = new X509TrustManager() {
            @Override
            public void checkClientTrusted(
                    X509Certificate[] paramArrayOfX509Certificate,
                    String paramString) throws CertificateException {
            }

            @Override
            public void checkServerTrusted(
                    X509Certificate[] paramArrayOfX509Certificate,
                    String paramString) throws CertificateException {
            }

            @Override
            public X509Certificate[] getAcceptedIssuers() {
                return null;
            }
        };
        sslContext.init(null, new TrustManager[]{trustManager}, null);
        return sslContext;
    }
}
