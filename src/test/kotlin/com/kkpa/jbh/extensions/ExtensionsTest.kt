package com.kkpa.jbh.extensions

import com.kkpa.jbh.payload.ApiResponse
import io.mockk.every
import io.mockk.mockk
import io.mockk.mockkStatic
import io.mockk.verify
import org.assertj.core.api.Assertions.assertThat
import org.junit.Test
import org.springframework.boot.test.context.SpringBootTest

@SpringBootTest
class ExtensionsTest {

    @Test
    fun testDateExtension() {

        with(mockk<ExtClass>()) {
            every {
                5.justTest()
            } returns 20

            assertThat(20).isEqualTo(5.justTest())

            verify {
                5.justTest()
            }
        }
    }

    @Test
    fun testExtensionWithoutClass() {

        val myApiResponse = ApiResponse(true, "Success")
        mockkStatic("com.kkpa.jbh.extensions.LocalDateExtKt")
        every { myApiResponse.fakeResponse() } returns ApiResponse(false, "mocked")

        assertThat("mocked").isEqualTo(myApiResponse.fakeResponse().message)
    }
}