package net.rickiekarp.homeassistant.net.communication.vo

import java.util.*

/**
 * Created by rickie on 12/5/17.
 */
class VOData {
    var serverVersion: String? = null
    var features: Properties? = null
        private set // the setter is private and has the default implementation

    fun setFeatureSettings(properties: Properties) {
        this.features = properties
    }
}