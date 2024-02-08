package com.cvopa.peter.fetchy.viewmodel

import app.cash.turbine.test
import com.cvopa.peter.fetchy.ui.components.ErrorState
import com.cvopa.peter.fetchy.ui.profile.Event
import com.cvopa.peter.fetchy.ui.profile.MainScreenState
import com.cvopa.peter.fetchy.ui.profile.ProfileScreenViewModel
import com.cvopa.peter.fetchy.usecase.EncodeSha1UseCase
import com.cvopa.peter.fetchy.usecase.HasInternetConnectionUseCase
import com.cvopa.peter.fetchy.usecase.InputValidatorUseCase
import com.cvopa.peter.fetchy.usecase.LoginUseCase
import com.cvopa.peter.fetchy.usecase.NetworkState
import com.cvopa.peter.fetchy.usecase.SuccessLoginData
import com.cvopa.peter.fetchy.usecase.ValidationResult
import com.cvopa.peter.fetchy.coroutine.CoroutineTestRule
import io.mockk.coEvery
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.Assert
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import retrofit2.HttpException

class ProfileViewModelTest {

    @get:Rule
    val coroutineTestRule = CoroutineTestRule()

    private val encodeSha1UseCase: EncodeSha1UseCase = mockk(relaxed = true)
    private val loginUseCase: LoginUseCase = mockk(relaxed = true)
    private val inputValidatorUseCase: InputValidatorUseCase = mockk(relaxed = true)
    private val hasInternetConnectionUseCase: HasInternetConnectionUseCase = mockk(relaxed = true)

    private lateinit var viewModel: ProfileScreenViewModel

    @Before
    fun setUp() {

        viewModel = ViewModelBuilder()
            .inputValidationCondition(true)
            .build()
    }

    @Test
    fun `when view model initialized value, then first observed value in state, then correct initial state value `() = runTest {

        viewModel.state.test {
            awaitItem()
            assertThat(viewModel)
                .assertInitialState()
                .assertNoError()
                .assertIsNotLoading()
        }
    }

    @Test
    fun `when event username, then observe change of state with corresponding username`() = runTest {

        val expected = "test_srt"
        viewModel.onEvent(Event.OnUserNameChanged(expected))

        viewModel.state.test {
            awaitItem()
            assertThat(viewModel)
                .assertUserNameIs(expected)
                .assertNoError()
                .assertIsNotLoading()
        }
    }

    @Test
    fun `when event password, then observe change of state with corresponding password`() = runTest {

        val expected = "test_password"
        viewModel.onEvent(Event.OnPasswordChanged(expected))

        viewModel.state.test {
            awaitItem()
            assertThat(viewModel)
                .assertPasswordIs(expected)
                .assertNoError()
                .assertIsNotLoading()
        }
    }

    @Test
    fun `when event username reset, then observe username is cleared`() = runTest {

        val userName = "test_srt"
        val expectedUserName = ""

        viewModel = ViewModelBuilder()
            .inputValidationCondition(true)
            .build()

        viewModel.state.test {
            viewModel.onEvent(Event.OnUserNameChanged(userName))
            skipItems(2)
            viewModel.onEvent(Event.OnUserNameReset)
            awaitItem()

            assertThat(viewModel)
                .assertUserNameIs(expectedUserName)
                .assertNoError()
        }

    }

    @Test
    fun `when invalid input during login, then observe error invalid input error state`() = runTest {

        val mockEvent: Event.OnLogin = mockk(relaxed = true)

        viewModel = ViewModelBuilder()
            .inputValidationCondition(false)
            .internetConnectionCondition(true)
            .build()

        viewModel.onEvent(mockEvent)

        viewModel.state.test {
            awaitItem()
            assertThat(viewModel)
                .assertError(ErrorState.InputErrorState)
        }
    }

    @Test
    fun `when no internet during login, then observe error no internet error state`() = runTest {

        val mockEvent: Event.OnLogin = mockk(relaxed = true)

        viewModel = ViewModelBuilder()
            .inputValidationCondition(true)
            .internetConnectionCondition(false)
            .build()

        viewModel.onEvent(mockEvent)

        viewModel.state.test {
            awaitItem()
            assertThat(viewModel)
                .assertError(ErrorState.NoInternet)

        }
    }

    @Test
    fun `when correct login conditions, then observe login state`() = runTest {

        val mockEvent: Event.OnLogin = mockk(relaxed = true)

        viewModel = ViewModelBuilder()
            .inputValidationCondition(true)
            .internetConnectionCondition(true)
            .build()

        val successMock: SuccessLoginData = mockk(relaxed = true)
        coEvery { loginUseCase(any()) } returns successMock


        viewModel.state.test {
            viewModel.onEvent(mockEvent)
            skipItems(1)

            val expectedLoadingState = awaitItem()
            Assert.assertTrue(expectedLoadingState is MainScreenState.LoggedOut && expectedLoadingState.isLoading)

            val expectedSuccess = awaitItem()
            Assert.assertTrue(expectedSuccess is MainScreenState.LoggedIn)
        }
    }

    @Test
    fun `when login service encounter http 401 code, then observe authorization error`() = runTest {

        val mockEvent: Event.OnLogin = mockk(relaxed = true)

        viewModel = ViewModelBuilder()
            .inputValidationCondition(true)
            .internetConnectionCondition(true)
            .build()

        val mockException: HttpException = mockk()

        every { mockException.code() }.returns(401)
        coEvery { loginUseCase(any()) } throws mockException

        viewModel.onEvent(mockEvent)
        viewModel.state.test {
            awaitItem()
            assertThat(viewModel)
                .assertError(ErrorState.AuthorizationErrorState)
        }
    }

    @Test
    fun `when login service encounter http 500 code, then observe general error`() = runTest {

        val mockEvent: Event.OnLogin = mockk(relaxed = true)

        viewModel = ViewModelBuilder()
            .inputValidationCondition(true)
            .internetConnectionCondition(true)
            .build()

        val mockException: HttpException = mockk()

        every { mockException.code() }.returns(500)
        coEvery { loginUseCase(any()) } throws mockException

        viewModel.onEvent(mockEvent)
        viewModel.state.test {
            awaitItem()
            assertThat(viewModel)
                .assertGeneralError()
        }
    }

    @Test
    fun `when login is successful perform logout, then observe logged-out state`() = runTest {

        val mockEvent: Event.OnLogin = mockk(relaxed = true)

        viewModel = ViewModelBuilder()
            .inputValidationCondition(true)
            .internetConnectionCondition(true)
            .build()

        val successMock: SuccessLoginData = mockk(relaxed = true)
        coEvery { loginUseCase(any()) } returns successMock

        viewModel.onEvent(mockEvent)
        viewModel.onEvent(Event.OnLogout)

        viewModel.state.test {
            awaitItem()
            assertThat(viewModel).assertIsLogout()
        }
    }

    inner class ViewModelBuilder {

        fun inputValidationCondition(condition: Boolean): ViewModelBuilder {
            coEvery { inputValidatorUseCase(any()) } returns if (condition) ValidationResult.Valid else ValidationResult.Invalid
            return this
        }

        fun internetConnectionCondition(condition: Boolean): ViewModelBuilder {
            coEvery { hasInternetConnectionUseCase(Unit) } returns if (condition) NetworkState.Available else NetworkState.NotAvailable
            return this
        }

        fun build(): ProfileScreenViewModel {
            return ProfileScreenViewModel(
                encodeSha1UseCase,
                loginUseCase,
                inputValidatorUseCase,
                hasInternetConnectionUseCase
            )
        }
    }
}