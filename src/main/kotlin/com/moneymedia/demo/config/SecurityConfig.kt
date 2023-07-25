package com.moneymedia.demo.config

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.http.HttpHeaders
import org.springframework.http.HttpMethod
import org.springframework.security.authentication.ReactiveAuthenticationManager
import org.springframework.security.authorization.AuthorizationDecision
import org.springframework.security.authorization.ReactiveAuthorizationManager
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity
import org.springframework.security.config.web.server.SecurityWebFiltersOrder
import org.springframework.security.config.web.server.ServerHttpSecurity
import org.springframework.security.config.web.server.invoke
import org.springframework.security.core.Authentication
import org.springframework.security.web.authentication.preauth.PreAuthenticatedAuthenticationToken
import org.springframework.security.web.server.SecurityWebFilterChain
import org.springframework.security.web.server.authentication.AuthenticationWebFilter
import org.springframework.security.web.server.authorization.AuthorizationContext
import org.springframework.security.web.server.util.matcher.PathPatternParserServerWebExchangeMatcher
import org.springframework.security.web.server.util.matcher.ServerWebExchangeMatcher
import org.springframework.web.server.ServerWebExchange
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono
import reactor.kotlin.core.publisher.toMono

@Configuration
@EnableWebFluxSecurity
class SecurityConfig {

    private val AUTHORIZATION_REGEX = Regex("^Bearer (?<token>[a-zA-Z0-9-._~+/]+=*)$", RegexOption.IGNORE_CASE)

    @Bean
    fun apiFilterChain(http: ServerHttpSecurity): SecurityWebFilterChain =
        http {
            csrf { disable() }
            logout { disable() }
            requestCache { disable() }
            authorizeExchange { authorize(optionsExchangeMatcher(), permitAll) }
            authorizeExchange { authorize("/actuator/**", permitAll) }
            authorizeExchange { authorize("/api/**", authorizationManager()) }
            addFilterAfter( authenticationWebFilter(), SecurityWebFiltersOrder.AUTHENTICATION )
        }

    private fun optionsExchangeMatcher(): ServerWebExchangeMatcher =
        PathPatternParserServerWebExchangeMatcher("/**", HttpMethod.OPTIONS)

    @Bean
    fun authenticationManager(): ReactiveAuthenticationManager =
        ReactiveAuthenticationManager { authentication ->
            authentication.isAuthenticated = authCheck(authentication)
            Mono.just(authentication)
        }

    fun authenticationWebFilter(): AuthenticationWebFilter =
        AuthenticationWebFilter(authenticationManager())
            .apply { setServerAuthenticationConverter(::bearerAuthenticationConverter) }

    private fun bearerAuthenticationConverter(serverWebExchange: ServerWebExchange): Mono<Authentication> =
        Flux.fromIterable(serverWebExchange.request.headers.getOrEmpty(HttpHeaders.AUTHORIZATION))
            .mapNotNull { AUTHORIZATION_REGEX.matchEntire(it)?.groups?.get("token") }
            .toMono()
            .map { token -> PreAuthenticatedAuthenticationToken(token!!.value, token.value) }

    private fun authorizationManager(): ReactiveAuthorizationManager<AuthorizationContext> =
        ReactiveAuthorizationManager { mono, _ ->
            mono.map{ authentication -> AuthorizationDecision(authCheck(authentication)) }
        }

    private fun authCheck(authentication: Authentication): Boolean =
        "api-key".equals(authentication.principal)

}