package com.kkpa.jbh.services

import com.kkpa.jbh.services.accounts.AccountServiceTests
import com.kkpa.jbh.services.categories.CategoryServiceTests
import com.kkpa.jbh.services.categories.SubCategoriesServiceTests
import com.kkpa.jbh.services.transactions.TransactionsServiceTests
import com.kkpa.jbh.services.users.UserServicesTests
import org.junit.runner.RunWith
import org.junit.runners.Suite
import org.springframework.test.context.ActiveProfiles

@ActiveProfiles("test")
@RunWith(Suite::class)
@Suite.SuiteClasses(
    CategoryServiceTests::class,
    SubCategoriesServiceTests::class,
    UserServicesTests::class,
    AccountServiceTests::class,
    TransactionsServiceTests::class
)
class ServicesSuiteTests