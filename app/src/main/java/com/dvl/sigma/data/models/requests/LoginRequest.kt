package com.dvl.sigma.data.models.requests

data class LoginRequest(

    private var email: String? = null,

    private var password: String? = null,

    private var udid: String? = null,

    private var device_token: String? = null,

    private var device_type: String = "0",
)