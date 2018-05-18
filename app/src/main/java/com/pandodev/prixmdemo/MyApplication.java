package com.pandodev.prixmdemo;

import android.app.Application;

import com.apollographql.apollo.ApolloClient;
import com.apollographql.apollo.api.Field;
import com.apollographql.apollo.api.Operation;
import com.apollographql.apollo.cache.normalized.CacheKey;
import com.apollographql.apollo.cache.normalized.CacheKeyResolver;
import com.apollographql.apollo.cache.normalized.NormalizedCacheFactory;
import com.apollographql.apollo.cache.normalized.lru.EvictionPolicy;
import com.apollographql.apollo.cache.normalized.lru.LruNormalizedCacheFactory;
import com.apollographql.apollo.cache.normalized.sql.ApolloSqlHelper;
import com.apollographql.apollo.cache.normalized.sql.SqlNormalizedCacheFactory;
import com.facebook.stetho.Stetho;

import java.util.Map;

import javax.annotation.Nonnull;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;

public class MyApplication extends Application {


    private static final String SQL_CACHE_NAME = "prismdemo";
    private static final String BASE_URL = "http://serveraddress:8009/graphql";

    private ApolloClient apolloClient;

    @Override
    public void onCreate() {
        super.onCreate();

        HttpLoggingInterceptor logging = new HttpLoggingInterceptor();
        logging.setLevel(HttpLoggingInterceptor.Level.BODY);

        OkHttpClient okHttpClient = new OkHttpClient.Builder()
                .addInterceptor(logging)
                .build();

        Stetho.initializeWithDefaults(this);


        ApolloSqlHelper apolloSqlHelper = new ApolloSqlHelper(this, SQL_CACHE_NAME);
        NormalizedCacheFactory normalizedCacheFactory = new LruNormalizedCacheFactory(EvictionPolicy.NO_EVICTION,
                new SqlNormalizedCacheFactory(apolloSqlHelper));

        CacheKeyResolver cacheKeyResolver = new CacheKeyResolver() {
            @Nonnull
            @Override
            public CacheKey fromFieldRecordSet(@Nonnull Field field, @Nonnull Map<String, Object> map) {
                String typeName = (String) map.get("__typename");
                if ("User".equals(typeName)) {
                    String userKey = typeName + "." + map.get("login");
                    return CacheKey.from(userKey);
                }
                if (map.containsKey("id")) {
                    String typeNameAndIDKey = map.get("__typename") + "." + map.get("id");
                    return CacheKey.from(typeNameAndIDKey);
                }
                return CacheKey.NO_KEY;
            }

            @Nonnull
            @Override
            public CacheKey fromFieldArguments(@Nonnull Field field, @Nonnull Operation.Variables variables) {
                return CacheKey.NO_KEY;
            }
        };

        apolloClient = ApolloClient.builder()
                .serverUrl(BASE_URL)
                .okHttpClient(okHttpClient)
                .normalizedCache(normalizedCacheFactory, cacheKeyResolver)
                .build();
    }

    public ApolloClient apolloClient() {
        return apolloClient;
    }

}
