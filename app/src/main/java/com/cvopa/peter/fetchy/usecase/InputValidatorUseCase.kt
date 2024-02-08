package com.cvopa.peter.fetchy.usecase

import com.cvopa.peter.fetchy.ui.base.UseCase
import javax.inject.Inject

class InputValidatorUseCase @Inject constructor() : UseCase<LoginDetails, ValidationResult> {

    override fun invoke(input: LoginDetails): ValidationResult {
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

