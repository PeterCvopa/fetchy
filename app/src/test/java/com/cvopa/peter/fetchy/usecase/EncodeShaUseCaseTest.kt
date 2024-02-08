package com.cvopa.peter.fetchy.usecase

import com.cvopa.peter.fetchy.usecase.EncodeSha1UseCase
import org.junit.Assert
import org.junit.Before
import org.junit.Test

class EncodeShaUseCaseTest {

    private lateinit var useCase: EncodeSha1UseCase

    @Before
    fun setUp() {
        useCase = EncodeSha1UseCase()

    }

    @Test
    fun `given that input input string ,when use case is invoked, then return hashed string in sha1`(){
        val testInput = "random....string1234"
        val expected = "6c1a45167f32f193bf03a5f0f69747e44d7c4e9a"

        val actual = useCase(testInput)
        Assert.assertTrue(expected == actual)
    }
}