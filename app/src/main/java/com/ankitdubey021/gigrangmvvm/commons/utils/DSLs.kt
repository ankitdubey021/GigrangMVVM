package com.ankitdubey021.gigrangmvvm.commons.utils

import com.google.gson.JsonElement
import com.google.gson.JsonObject

@DslMarker
annotation class MJsonMarker

operator fun JsonObject.invoke(func: JsonObject.()->Unit){func()}
@MJsonMarker fun JsonObject.with(key:String, value:String?){addProperty(key,value)}
@MJsonMarker fun JsonObject.with(key:String, value:Int?){addProperty(key,value)}
@MJsonMarker fun JsonObject.with(key:String, value:Double?){addProperty(key,value)}
@MJsonMarker fun JsonObject.with(key:String, value:Float?){addProperty(key,value)}
@MJsonMarker fun JsonObject.with(key:String, value:Boolean?){addProperty(key,value)}
@MJsonMarker fun JsonObject.with(key:String, value: JsonElement?){add(key,value)}

@MJsonMarker fun giveMeJson(func: JsonObject.()->Unit): JsonObject {
    val json= JsonObject()
    json.func()
    return json
}