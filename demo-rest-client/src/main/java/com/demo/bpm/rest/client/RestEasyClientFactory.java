package com.demo.bpm.rest.client;

import org.apache.http.HttpHost;
import org.apache.http.auth.AuthScope;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.client.AuthCache;
import org.apache.http.client.CredentialsProvider;
import static org.apache.http.client.protocol.ClientContext.AUTH_CACHE;
import org.apache.http.impl.auth.BasicScheme;
import org.apache.http.impl.client.BasicAuthCache;
import org.apache.http.impl.client.BasicCredentialsProvider;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.protocol.BasicHttpContext;
import org.jboss.resteasy.client.ClientRequest;
import org.jboss.resteasy.client.core.executors.ApacheHttpClient4Executor;

public class RestEasyClientFactory
{
    private final String hostname;
    private final int port;
    private final String user;
    private final String password;
    private final HttpHost httpHost;
    private final BasicHttpContext httpContext;
    private final DefaultHttpClient httpClient;
    private final ApacheHttpClient4Executor executor;

    public RestEasyClientFactory(String hostname, int port, String user, String password)
    {
        this.hostname = hostname;
        this.port = port;
        this.user = user;
        this.password = password;

        // the host we will talk to
        httpHost = new HttpHost(hostname, port);

        // credentials used to talk to our host
        CredentialsProvider credentialsProvider = new BasicCredentialsProvider();
        credentialsProvider.setCredentials(
                new AuthScope(hostname, port),
                new UsernamePasswordCredentials(user, password)
        );

        // create a client and bind the credentials to it
        httpClient = new DefaultHttpClient();
        httpClient.setCredentialsProvider(credentialsProvider);

        // pre-load the auth cache so all requests will provide basic credentials automatically
        AuthCache authCache = new BasicAuthCache();
        authCache.put(httpHost, new BasicScheme());
        httpContext = new BasicHttpContext();
        httpContext.setAttribute(AUTH_CACHE, authCache);

        executor = new ApacheHttpClient4Executor(httpClient, httpContext);
    }

    public ClientRequest makeRequest(String uriTemplate)
    {
        return executor.createRequest(uriTemplate);
    }
}
