package com.github.rwsbillyang.composerouter

import androidx.compose.foundation.layout.PaddingValues

/**
 * parameters pass to screen
 * @param route
 * @param props 任意参数，可以被中途修改
 * @param scaffoldPadding
 * @param parameters parameters in path(TODO:Not implement), for example firstName and lastName in "/path/{firstName}/{lastName}"
 * @param query search query in uri(TODO:Not implement), for example key1 and key2 in "/path?key1=value1&key2=value2"
 * */
class ScreenCall(
    val route: Route,
    var props: Any?,
    val scaffoldPadding: PaddingValues = PaddingValues(),
    val parameters: Map<String, String> = mutableMapOf(),
    val query: Map<String, String> = mutableMapOf()
)