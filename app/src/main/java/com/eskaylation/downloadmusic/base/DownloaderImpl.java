package com.eskaylation.downloadmusic.base;

import android.os.Build.VERSION;
import com.eskaylation.downloadmusic.utils.CookieUtils;
import com.eskaylation.downloadmusic.utils.TLSSocketFactoryCompat;
import java.io.IOException;
import java.security.KeyManagementException;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.concurrent.TimeUnit;
import javax.net.ssl.TrustManager;
import javax.net.ssl.TrustManagerFactory;
import javax.net.ssl.X509TrustManager;
import okhttp3.CipherSuite;
import okhttp3.ConnectionSpec;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.OkHttpClient.Builder;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import org.schabi.newpipe.extractor.downloader.Downloader;
import org.schabi.newpipe.extractor.downloader.Request;
import org.schabi.newpipe.extractor.downloader.Response;
import org.schabi.newpipe.extractor.exceptions.ReCaptchaException;

public class DownloaderImpl extends Downloader {
    public static DownloaderImpl instance;
    public OkHttpClient client;
    public Map<String, String> mCookies;

    public DownloaderImpl(Builder builder) {
        if (VERSION.SDK_INT == 19) {
            enableModernTLS(builder);
        }
        builder.readTimeout(30, TimeUnit.SECONDS);
        this.client = builder.build();
        this.mCookies = new HashMap();
    }

    public static DownloaderImpl init(Builder builder) {
        if (builder == null) {
            builder = new Builder();
        }
        instance = new DownloaderImpl(builder);
        return instance;
    }

    public static void enableModernTLS(Builder builder) {
        try {
            TrustManagerFactory instance2 = TrustManagerFactory.getInstance(TrustManagerFactory.getDefaultAlgorithm());
            instance2.init((KeyStore) null);
            TrustManager[] trustManagers = instance2.getTrustManagers();
            if (trustManagers.length != 1 || !(trustManagers[0] instanceof X509TrustManager)) {
                StringBuilder sb = new StringBuilder();
                sb.append("Unexpected default trust managers:");
                sb.append(Arrays.toString(trustManagers));
                throw new IllegalStateException(sb.toString());
            }
            builder.sslSocketFactory(TLSSocketFactoryCompat.getInstance(), (X509TrustManager) trustManagers[0]);
            ArrayList arrayList = new ArrayList();
            arrayList.addAll(ConnectionSpec.MODERN_TLS.cipherSuites());
            arrayList.add(CipherSuite.TLS_ECDHE_ECDSA_WITH_AES_128_CBC_SHA);
            arrayList.add(CipherSuite.TLS_ECDHE_ECDSA_WITH_AES_256_CBC_SHA);
            ConnectionSpec.Builder builder2 = new ConnectionSpec.Builder(ConnectionSpec.MODERN_TLS);
            builder2.cipherSuites((CipherSuite[]) arrayList.toArray(new CipherSuite[0]));
            builder.connectionSpecs(Arrays.asList(new ConnectionSpec[]{builder2.build(), ConnectionSpec.CLEARTEXT}));
        } catch (KeyManagementException | KeyStoreException | NoSuchAlgorithmException unused) {
        }
    }

    public String getCookies(String str) {
        ArrayList arrayList = new ArrayList();
        if (str.contains("youtube.com")) {
            String cookie = getCookie("youtube_restricted_mode_key");
            if (cookie != null) {
                arrayList.add(cookie);
            }
        }
        return CookieUtils.concatCookies(arrayList);
    }

    public String getCookie(String str) {
        return (String) this.mCookies.get(str);
    }

    public Response execute(Request request) throws IOException, ReCaptchaException {
        String httpMethod = request.httpMethod();
        String url = request.url();
        Map<String, List<String>> headers = request.headers();
        byte[] dataToSend = request.dataToSend();
        String str = null;
        RequestBody create = dataToSend != null ? RequestBody.create((MediaType) null, dataToSend) : null;
        okhttp3.Request.Builder builder = new okhttp3.Request.Builder();
        builder.method(httpMethod, create);
        builder.url(url);
        builder.addHeader("User-Agent", "Mozilla/5.0 (Windows NT 10.0; WOW64; rv:68.0) Gecko/20100101 Firefox/68.0");
        String cookies = getCookies(url);
        if (!cookies.isEmpty()) {
            builder.addHeader("Cookie", cookies);
        }
        for (Entry entry : headers.entrySet()) {
            String str2 = (String) entry.getKey();
            List<String> list = (List) entry.getValue();
            if (list.size() > 1) {
                builder.removeHeader(str2);
                for (String addHeader : list) {
                    builder.addHeader(str2, addHeader);
                }
            } else if (list.size() == 1) {
                builder.header(str2, (String) list.get(0));
            }
        }
        okhttp3.Response execute = this.client.newCall(builder.build()).execute();
        if (execute.code() != 429) {
            ResponseBody body = execute.body();
            if (body != null) {
                str = body.string();
            }
            Response response = new Response(execute.code(), execute.message(), execute.headers().toMultimap(), str, execute.request().url().toString());
            return response;
        }
        execute.close();
        throw new ReCaptchaException("reCaptcha Challenge requested", url);
    }
}
