package com.route.domain.usecase.auth

import javax.inject.Inject

class RegisterValidationUseCase
    @Inject
    constructor() {
        fun isValidUserName(userName: String?): Boolean {
            return if (userName.isNullOrEmpty()) {
                false
            } else {
                userName.length < 6
            }
        }

        fun isValidEmail(email: String?): Boolean {
            val emailRegex = Regex("[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}")
            return if (email.isNullOrEmpty()) {
                false
            } else {
                emailRegex.matches(email)
            }
        }

        fun isValidPhoneNumber(phoneNumber: String?): Boolean {
            return if (phoneNumber.isNullOrEmpty()) {
                false
            } else if (phoneNumber.length == 11) {
                phoneNumber[0] == '0' && phoneNumber[1] == '1' &&
                    arrayOf(
                        '0',
                        '1',
                        '2',
                        '5',
                    ).contains(phoneNumber[2])
            } else {
                false
            }
        }

        fun isValidPassword(password: String?): Boolean {
            val passwordRegex = Regex("^(?=.*[A-Za-z])(?=.*\\d)[A-Za-z\\d]{8,}$")
            return if (password.isNullOrEmpty()) {
                false
            } else {
                passwordRegex.matches(password)
            }
        }

        fun isValidConfirmPassword(
            password: String?,
            confirmPassword: String?,
        ): Boolean {
            return if (password.isNullOrEmpty() && confirmPassword.isNullOrEmpty()) {
                false
            } else {
                password == confirmPassword
            }
        }
    }
