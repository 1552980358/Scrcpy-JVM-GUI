package com.skynight.scrcpy.Base


class GetConnectedDevices private constructor() {
    companion object {
        @Volatile
        private var instance: GetConnectedDevices? = null

        @Synchronized
        fun getInstance(): GetConnectedDevices {
            if (instance == null) {
                instance = GetConnectedDevices()
            }
            return instance as GetConnectedDevices
        }

        // 重新加载
        fun reloadConnectedDevices(): GetConnectedDevices {
            instance = GetConnectedDevices()
            return instance as GetConnectedDevices
        }
    }

    private val deviceList = mutableListOf<String>()
    private val deviceStateList = mutableMapOf<String, String>()
    private var rawAdbDeviceList: List<String> = runAdbGetList("devices")

    init {
        println(rawAdbDeviceList.size)
        if (rawAdbDeviceList.size >= 2) {
            loadList@ for (i in rawAdbDeviceList) {
                if (rawAdbDeviceList.indexOf(i) == 0) {
                    continue@loadList
                }

                if (i.isEmpty()) {
                    break@loadList
                }

                val device = StringBuilder()
                loadDevice@ for (j in i) {
                    if (j.toString() == "\t") {
                        break@loadDevice
                    }
                    device.append(j)
                }
                deviceList.add(device.toString())

                val state = StringBuilder()
                for (j in i.substring(i.indexOf("\t") + 1)) {
                    state.append(j)
                }

                deviceStateList[device.toString()] = state.toString()
            }
        }
    }

    fun getDeviceList(): List<String> {
        return deviceList
    }

    fun getDeviceStateList(): Map<String, String> {
        return deviceStateList
    }

    fun getDeviceNumber(): Int {
        return deviceList.size
    }

    fun getDeviceState(device: String): String {
        if (!deviceList.contains(device)) {
            return "NotFound"
        }

        println(deviceStateList)

        // device
        // offline
        // unauthorize
        return deviceStateList[device]!!
    }

    fun getDeviceState(device: Int): String {
        if (device >= deviceList.size) {
            return "NotFound"
        }
        return deviceStateList[deviceList[device]]!!
    }

    fun getDeviceSDK(device: String): String {
        return runAdbGetList("-s $device shell getprop ro.build.version.sdk").first()
    }

    fun getDeviceBrand(device: String): String {
        return runAdbGetList("-s $device shell getprop ro.product.vendor.brand").first()
    }

    fun getDeviceModel(device: String): String {
        return runAdbGetList("-s $device shell getprop ro.product.vendor.model").first()
    }

    fun getDeviceImei(device: String): String {
        return runAdbGetList("-s $device shell \"service call iphonesubinfo 1 | grep -o \'[0-9a-f]\\{8\\} \' | tail -n+3 | while read a; do echo -n \\\\u\${a:4:4}\\\\u\${a:0:4}; done\"").first()
    }

    fun getDeviceAndroidVersion(device: String): String {
        return runAdbGetList("-s $device shell getprop ro.build.version.release").first()
    }
}
