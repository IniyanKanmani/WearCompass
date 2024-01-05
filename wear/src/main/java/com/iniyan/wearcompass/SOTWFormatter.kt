package com.iniyan.wearcompass

import android.content.Context

class SOTWFormatter(context: Context) {

    companion object {
        private const val TAG = "SOTWFormatter"
        private val sides = intArrayOf(0, 45, 90, 135, 180, 225, 270, 315, 360)
        private var names: Array<String>? = null
    }

    init {
        initLocalizedNames(context)
    }

    fun format(azimuth: Float): String {
        val iAzimuth = azimuth.toInt()
        val index = findClosestIndex(iAzimuth)
        return "$iAzimuth° ${names!![index]}"
    }

    private fun initLocalizedNames(context: Context) {
        if (names == null) {
            names = arrayOf(
                "sotw_north",
                "sotw_northeast",
                "sotw_east",
                "sotw_southeast",
                "sotw_south",
                "sotw_southwest",
                "sotw_west",
                "sotw_northwest",
                "sotw_north"
            )
        }
    }

    private fun findClosestIndex(target: Int): Int {
        var i = 0
        var j = sides.size
        var mid = 0
        while (i < j) {
            mid = (i + j) / 2
            if (target < sides[mid]) {
                if (mid > 0 && target > sides[mid - 1]) {
                    return getClosest(mid - 1, mid, target)
                }
                j = mid
            } else {
                if (mid < sides.size - 1 && target < sides[mid + 1]) {
                    return getClosest(mid, mid + 1, target)
                }
                i = mid + 1
            }
        }
        return mid
    }

    private fun getClosest(index1: Int, index2: Int, target: Int): Int {
        return if (target - sides[index1] >= sides[index2] - target) index2 else index1
    }
}
