package com.monster.disciple.util

import java.lang.reflect.ParameterizedType
import java.lang.reflect.Type
import java.util.ArrayList

class CacheBuilder private constructor() {

    var cacheKey: String? = null
        private set

    private val args = ArrayList<Type>()

    var type: Type? = null
        private set

    fun withCacheKey(cacheKey: String): CacheBuilder {
        this.cacheKey = cacheKey
        return this
    }

    fun addType(vararg classes: Class<*>): CacheBuilder {
        args.addAll(classes)
        return this
    }

    fun build(): CacheBuilder {

        if (1 == args.size) {
            type = args[0]
        } else {
            type = newInstanceType(args, 0)
        }
        return this
    }

    private fun newInstanceType(args: List<Type>, index: Int): Type {
        return if (index + 1 == args.size - 1) {
            ParameterizedTypeImpl(args[index] as Class<*>, arrayOf(args[index + 1]))
        } else ParameterizedTypeImpl(args[index] as Class<*>, arrayOf(newInstanceType(args, index + 1)))
    }

    private class ParameterizedTypeImpl internal constructor(private val raw: Class<*>, args: Array<Type>?) : ParameterizedType {
        private val args: Array<Type> = args ?: emptyArray()

        override fun getActualTypeArguments(): Array<Type> {
            return args
        }

        override fun getRawType(): Type {
            return raw
        }

        override fun getOwnerType(): Type? {
            return null
        }
    }

    companion object {

        fun newBuilder(): CacheBuilder {
            return CacheBuilder()
        }
    }
}
