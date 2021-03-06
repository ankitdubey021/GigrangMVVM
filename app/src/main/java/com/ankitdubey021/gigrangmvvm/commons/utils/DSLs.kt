package com.ankitdubey021.gigrangmvvm.commons.utils

import org.json.JSONObject

@DslMarker
annotation class MJsonMarker

@MJsonMarker
class Json() : JSONObject() {

    @MJsonMarker
    constructor(json: Json.() -> Unit) : this() {
        this.json()
    }
    @MJsonMarker
    infix fun <T> String.to(value: T) {
        put(this, value)
    }
}

