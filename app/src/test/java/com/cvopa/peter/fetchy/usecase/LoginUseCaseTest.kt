package com.cvopa.peter.fetchy.usecase

import android.graphics.Bitmap
import com.cvopa.peter.fetchy.api.LoginResponse
import com.cvopa.peter.fetchy.api.LoginService
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test

class LoginUseCaseTest {

    private lateinit var useCase: LoginUseCase

    private val loginService: LoginService = mockk()
    private val decoder64BaseToBitmapUseCase: Decoder64BaseToBitmapUseCase = mockk()

    @Before
    fun setUp() {
        useCase = LoginUseCase(loginService, decoder64BaseToBitmapUseCase)
    }

    @Test
    fun `when va loginDetail`() = runTest {
        val expectedUserName = "test"

        val loginResponseMock: LoginResponse = mockk(relaxed = true)
        val mockBitmap: Bitmap = mockk(relaxed = true)
        val mock: LoginDetails = mockk(relaxed = true)

        coEvery { loginService.getImage(any(), any()) } returns loginResponseMock
        every { decoder64BaseToBitmapUseCase(any()) } returns mockBitmap
        every { mock.userName } returns expectedUserName

        val result = useCase(mock)

        coVerify(exactly = 1) { loginService.getImage(any(), any()) }
        coVerify(exactly = 1) { decoder64BaseToBitmapUseCase(any()) }

        assertEquals(expectedUserName, result.userName)
    }
}