package com.kkpa.jbh

import org.junit.Test
import org.junit.runner.RunWith
import org.springframework.beans.BeansException
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.context.ApplicationContext
import org.springframework.context.ApplicationContextAware
import org.springframework.stereotype.Component
import org.springframework.test.context.junit4.SpringRunner

@RunWith(SpringRunner::class)
@SpringBootTest
class JbhApplicationTests {

    companion object {
        var context: ApplicationContext? = null
            private set
    }

    @Component
    class AppContextListener : ApplicationContextAware {
        @Throws(BeansException::class)
        override fun setApplicationContext(applicationContext: ApplicationContext) {
            context = applicationContext
        }
    }

    @Test
    fun contextLoads() {
    }
}
