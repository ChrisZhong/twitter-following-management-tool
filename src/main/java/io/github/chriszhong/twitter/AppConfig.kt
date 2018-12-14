package io.github.chriszhong.twitter

import org.slf4j.LoggerFactory
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.Scope
import org.springframework.context.annotation.ScopedProxyMode
import org.springframework.social.config.annotation.EnableSocial
import org.springframework.social.connect.ConnectionRepository
import org.springframework.social.connect.UsersConnectionRepository
import org.springframework.social.twitter.api.Twitter
import org.springframework.web.context.WebApplicationContext
import javax.servlet.http.HttpSession

@Configuration
@EnableSocial
open class AppConfig {

    @Bean
    @Scope(scopeName = WebApplicationContext.SCOPE_REQUEST, proxyMode = ScopedProxyMode.INTERFACES)
    open fun connectionRepository(usersConnectionRepository: UsersConnectionRepository, httpSession: HttpSession): ConnectionRepository {
        val connectionRepository = usersConnectionRepository.createConnectionRepository(httpSession.id)
        logger.debug("Using `{}` for '{}'", connectionRepository, httpSession.id)
        return connectionRepository
    }

    @Bean
    @Scope(scopeName = WebApplicationContext.SCOPE_REQUEST, proxyMode = ScopedProxyMode.INTERFACES)
    open fun twitter(repository: ConnectionRepository): Twitter? {
        val connection = repository.findPrimaryConnection(Twitter::class.java)
        return connection?.api
    }

    companion object {
        private val logger = LoggerFactory.getLogger(AppConfig::class.java)
    }
}
