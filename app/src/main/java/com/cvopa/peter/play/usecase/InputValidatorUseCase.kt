package com.cvopa.peter.play.usecase

import com.cvopa.peter.play.ui.base.UseCase
import javax.inject.Inject

class InputValidatorUseCase @Inject constructor() : UseCase<LoginDetails, ValidationResult>() {

    override fun execute(input: LoginDetails): ValidationResult {
        return if (input.token.isBlank().not() && input.userName.isBlank().not()) {
            ValidationResult.Valid
        } else {
            ValidationResult.Invalid
        }
    }
}

sealed class ValidationResult {
    data object Valid : ValidationResult()
    data object Invalid : ValidationResult()
}

