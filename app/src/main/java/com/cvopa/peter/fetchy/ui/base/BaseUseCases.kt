package com.cvopa.peter.fetchy.ui.base


sealed interface BaseUseCase<in I, out O>

/**
 * Base use case for use cases with synchronous invoke method.
 *
 * @see BaseUseCase
 * @see SuspendUseCase
 */
interface UseCase<in I, out O> : BaseUseCase<I, O> {
    operator fun invoke(input: I): O
}

/**
 * Base use case for use cases which need `suspend` invoke method.
 *
 * @see BaseUseCase
 * @see UseCase
 */
interface SuspendUseCase<in I, out O> : BaseUseCase<I, O> {
    suspend operator fun invoke(input: I): O
}

/**
 * Syntax sugar extension for calling synchronous use case without an input.
 *
 * @see UseCase
 * @see BaseUseCase
 */
operator fun <O> UseCase<Unit, O>.invoke(): O = invoke(Unit)

/**
 * Syntax sugar extension for calling suspend use case without an input.
 *
 * @see SuspendUseCase
 * @see BaseUseCase
 */
suspend operator fun <O> SuspendUseCase<Unit, O>.invoke(): O = invoke(Unit)