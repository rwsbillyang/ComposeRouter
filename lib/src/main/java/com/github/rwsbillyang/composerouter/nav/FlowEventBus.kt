package com.github.rwsbillyang.composerouter.nav

import androidx.lifecycle.Lifecycle
import androidx.lifecycle.repeatOnLifecycle
import kotlinx.coroutines.channels.BufferOverflow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow

//https://juejin.cn/post/7054462654902960135
object FlowEventBus {
    private val busMap: HashMap<String, MutableSharedFlow<out Any>> = hashMapOf()

    private fun <T : Any> with(bus: String): MutableSharedFlow<T> {
        if (!busMap.containsKey(bus)) {
            val flow = MutableSharedFlow<T>(0,16, BufferOverflow.DROP_OLDEST)
            busMap[bus] = flow
        }
        return busMap[bus] as MutableSharedFlow<T>
    }

    /**
     * 对外只暴露SharedFlow
     * @param bus String
     * @return SharedFlow<T>
     */
    fun <T> getFlow(bus: String): SharedFlow<T> {
        return with(bus)
    }


    /**
     * 挂起函数
     * @param bus String
     * @param data T
     */
    suspend fun <T : Any> post(bus: String, data: T) {
        with<T>(bus).emit(data)
    }

    /**
     * 详见tryEmit和emit的区别
     * @param bus String
     * @param data T
     * @return Boolean
     */
    fun <T : Any> tryPost(bus: String, data: T): Boolean {
        return with<T>(bus).tryEmit(data)
    }

    /**
     * sharedFlow会长久持有，所以要加声明周期限定，不然会出现内存溢出
     * @param lifecycle Lifecycle
     * @param bus String
     * @param block Function1<T, Unit>
     */
    suspend fun <T : Any> subscribe(lifecycle: Lifecycle, bus: String, block: (T) -> Unit) {
        lifecycle.repeatOnLifecycle(Lifecycle.State.CREATED) {
            with<T>(bus).collect {
                block(it)
            }
        }
    }

    /**
     * 注意，使用这个方法需要将协程在合适的时候取消，否则会导致内存溢出
     * @param bus String
     * @param block Function1<T, Unit>
     */
    suspend fun <T : Any> subscribe(bus: String, block: (T) -> Unit) {
        with<T>(bus).collect {
            block(it)
        }
    }

}
