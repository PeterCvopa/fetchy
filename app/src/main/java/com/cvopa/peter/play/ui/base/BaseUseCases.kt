package com.cvopa.peter.play.ui.base

abstract class SuspendedUseCase<IN, OUT> {
    suspend operator fun invoke(input: IN): OUT {
        return execute(input)
    }

    abstract suspend fun execute(input: IN): OUT
}


abstract class UseCase<IN, OUT> {
    operator fun invoke(input: IN): OUT {
        return execute(input)
    }

    abstract fun execute(input: IN): OUT
}