package com.timmy.codelab.customview

import android.content.Context

class UriProvider(context: Context) {

    private val appContext = context.applicationContext

    val clientUriScheme = appContext.getString(R.string.uri_client_scheme)
    val clientUriHost = appContext.getString(R.string.uri_client_host)
    val clientUriDomain = "$clientUriScheme://$clientUriHost"
}