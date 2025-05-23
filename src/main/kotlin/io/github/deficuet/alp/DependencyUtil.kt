package io.github.deficuet.alp

import io.github.deficuet.unitykt.UnityAssetManager
import io.github.deficuet.unitykt.classes.AssetBundle
import io.github.deficuet.unitykt.classes.MonoBehaviour
import io.github.deficuet.unitykt.firstObjectOf
import io.github.deficuet.unitykt.pptr.getAs
import kotlin.io.path.Path

class DependencyUtil {
    companion object{
        fun loadDependencies(pathString: String): Map<String, List<String>> {
            return UnityAssetManager.new().use { manager ->
                val depContext = manager.loadFile(Path(pathString).resolve("dependencies"))
                val bundle = depContext.objectList.firstObjectOf<AssetBundle>()
                val mono = bundle.mContainer.values.first()[0].asset.getAs<MonoBehaviour>()
                mono.toTypeTreeJson()!!.let { json ->
                    val keys = json.getJSONArray("m_Keys")
                    val values = json.getJSONArray("m_Values")
                    val table = mutableMapOf<String, List<String>>()
                    for (i in 0 until keys.length()) {
                        val key = keys.getString(i)
                        val value = values.getJSONObject(i).getJSONArray("m_Dependencies").map { it.toString() }
                        table[key] = value
                    }
                    table
                }
            }
        }
    }
}