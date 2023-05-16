package com.github.rwsbillyang.composerouter

interface PathMather {
    fun isMatch(routePath: String, path: String): Boolean
}

class EqualPathMather: PathMather{
    override fun isMatch(routePath: String, path: String) = routePath == path
}