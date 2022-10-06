package karma.convertor.utils

import com.google.gson.ExclusionStrategy
import com.google.gson.FieldAttributes

class SerializedExclusionStrategy : ExclusionStrategy {

    override fun shouldSkipClass(clazz: Class<*>?): Boolean {
        return false
    }

    override fun shouldSkipField(f: FieldAttributes?): Boolean {
        return f?.getAnnotation(SkipSerialization::class.java) != null
    }
}