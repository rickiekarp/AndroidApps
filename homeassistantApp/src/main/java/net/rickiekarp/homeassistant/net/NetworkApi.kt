package net.rickiekarp.homeassistant.net

class NetworkApi {
    val BASE_URL_LOGIN = "/LoginServer/"
    val BASE_URL_APPSERVER = "/HomeServer/"

    companion object {
        const val SHOPPING_DOMAIN = "shopping"
        const val WORLDS_ACTION = "worlds/get"
        const val STORES_ACTION = "stores"
    }
}