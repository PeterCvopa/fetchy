package com.cvopa.peter.fetchy.usecase

import com.cvopa.peter.fetchy.usecase.InputValidatorUseCase
import com.cvopa.peter.fetchy.usecase.LoginDetails
import com.cvopa.peter.fetchy.usecase.ValidationResult
import org.junit.Assert
import org.junit.Before
import org.junit.Test

class InputValidatorUseCaseTest {

    private lateinit var useCase: InputValidatorUseCase

    @Before
    fun setUp() {
        useCase = InputValidatorUseCase()
    }

    @Test
    fun `given that first input is not valid, when the use-case is invoked, then return validation result invalid`(){
        val invalidLoginDetails = LoginDetails("", "rfsd")
        val actual = useCase(invalidLoginDetails)
        Assert.assertTrue(actual is ValidationResult.Invalid)
    }

    @Test
    fun `given that second input is not valid, when the use-case is invoked, then return validation result invalid`(){
        val invalidLoginDetails = LoginDetails("fsdf", "")
        val actual = useCase(invalidLoginDetails)
        Assert.assertTrue(actual is ValidationResult.Invalid)
    }

    @Test
    fun `given that both inputs are not valid, when the use-case is invoked, then return validation result invalid`(){
        val invalidLoginDetails = LoginDetails("", "")
        val actual = useCase(invalidLoginDetails)
        Assert.assertTrue(actual is ValidationResult.Invalid)
    }


    @Test
    fun `given that inputs are valid, when the use-case is invoked, then return inputs are valid`(){
        val invalidLoginDetails = LoginDetails("ff", "ff")
        val actual = useCase(invalidLoginDetails)
        Assert.assertTrue(actual is ValidationResult.Valid)
    }
}