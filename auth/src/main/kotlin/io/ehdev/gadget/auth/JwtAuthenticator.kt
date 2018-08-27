package io.ehdev.gadget.auth

import com.fasterxml.jackson.databind.ObjectMapper
import com.google.common.cache.Cache
import com.google.common.cache.CacheBuilder
import io.dropwizard.auth.Authenticator
import io.ehdev.gadget.model.AccountManagerPrincipal
import okhttp3.HttpUrl
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.Response
import java.io.UncheckedIOException
import java.net.SocketTimeoutException
import java.util.*
import java.util.concurrent.TimeUnit


class JwtAuthenticator(
        private val accountManagerHost: String,
        private val om: ObjectMapper) : Authenticator<JwtCredentials, AccountManagerPrincipal> {

    private val cache: Cache<String, AccountManagerPrincipal> = CacheBuilder.newBuilder()
            .expireAfterWrite(1, TimeUnit.MINUTES)
            .maximumSize(1_000)
            .build()

    private val client = OkHttpClient.Builder()
            .retryOnConnectionFailure(true)
            .connectTimeout(3, TimeUnit.SECONDS)
            .readTimeout(5, TimeUnit.SECONDS)
            .addInterceptor(RetryInterceptor())
            .build()

    override fun authenticate(credentials: JwtCredentials?): Optional<AccountManagerPrincipal> {
        val realCreds = credentials ?: return Optional.empty()

        val cacheValue = cache.getIfPresent(realCreds.jwt)
        if (cacheValue != null) {
            return Optional.of(cacheValue)
        }

        val userUrl = HttpUrl.get(accountManagerHost).newBuilder()
                .encodedPath("/api/v1/user")
                .build()

        val request = Request.Builder()
                .get()
                .url(userUrl)
                .header(UserFilter.HEADER_NAME, realCreds.jwt)
                .build()

        client.newCall(request).execute().use { response ->
            if (!response.isSuccessful) {
                return Optional.empty()
            }

            val body = response.body() ?: return Optional.empty()

            val json = om.readTree(body.bytes())
            val email = json.get("email")?.textValue() ?: return Optional.empty()

            val accountManagerPrincipal = AccountManagerPrincipal(realCreds.jwt, email)
            cache.put(realCreds.jwt, accountManagerPrincipal)

            return Optional.of(accountManagerPrincipal)
        }
    }

    class RetryInterceptor : Interceptor {
        override fun intercept(chain: Interceptor.Chain): Response {
            val call = chain.call()

            var exception: UncheckedIOException? = null
            for (i in 0..3) {
                // try the request
                try {
                    return chain.proceed(call.clone().request())
                } catch (e: SocketTimeoutException) {
                    exception = UncheckedIOException(e)
                }
            }

            throw exception!!
        }

    }
}