package com.kkpa.jbh.services

import com.kkpa.jbh.exceptions.ConstraintViolationsException
import org.junit.Test
import org.junit.runner.RunWith
import org.springframework.beans.BeansException
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.context.ApplicationContext
import org.springframework.context.ApplicationContextAware
import org.springframework.test.context.ActiveProfiles
import org.springframework.test.context.junit4.SpringRunner
import org.springframework.transaction.annotation.Transactional

@RunWith(SpringRunner::class)
@SpringBootTest
@ActiveProfiles("test")
@Transactional
abstract class ServicesTests : ApplicationContextAware {

    companion object {
        var context: ApplicationContext? = null
            private set
    }

    @Throws(BeansException::class)
    override fun setApplicationContext(applicationContext: ApplicationContext) {
        context = applicationContext
    }

    @Test(expected = ConstraintViolationsException::class)
    abstract fun givenEntityAgainstConstraintsThenSaveItShouldThrowException()

    @Test
    abstract fun givenEntityThenShouldBeCreated()

    @Test
    abstract fun givenEntityUpdateItShouldBeSuccessful()

    @Test(expected = ConstraintViolationsException::class)
    abstract fun givenEntityAgainstConstraintsThenUpdateItShouldThrowException()

    @Test
    abstract fun giveEntityCreatedThenDeleteIt()

    @Test
    abstract fun givenNotExistingEntityFindByIdShouldReturnNull()

    @Test(expected = IllegalArgumentException::class)
    abstract fun givenEntityNotExistingThenDeleteItShouldThrowAnException()

    @Test(expected = IllegalArgumentException::class)
    abstract fun givenEntityWithIdNullThenDeleteItShouldThrowAnException()

    @Test(expected = IllegalArgumentException::class)
    abstract fun givenNotUniqueEntityThenSaveItShouldThrowAnException()

    @Test(expected = IllegalArgumentException::class)
    abstract fun givenNotUniqueEntityThenUpdateItShouldThrowAnException()
}
