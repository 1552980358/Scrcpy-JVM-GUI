package com.skynight.scrcpy.base

import com.skynight.scrcpy.windows.LogOutputWindow


class ConnectedDevices private constructor() {
    companion object {
        @Volatile
        private var connectedDevices: ConnectedDevices? = null

        @Synchronized
        fun getConnectedDevices(): ConnectedDevices {
            if (connectedDevices == null) {
                connectedDevices = ConnectedDevices()
            }
            return connectedDevices as ConnectedDevices
        }

        // 重新加载
        fun reloadConnectedDevices(): ConnectedDevices {
            connectedDevices = ConnectedDevices()
            return connectedDevices as ConnectedDevices
        }
    }

    private val deviceList = mutableListOf<String>()
    private val deviceStateList = mutableMapOf<String, String>()
    private var rawAdbDeviceList: List<String> = runAdbGetList("devices")

    init {
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
        return try {
            runAdbGetList("-s $device shell getprop ro.build.version.sdk").first()
        } catch (e: Exception) {
            LogOutputWindow.takeLog("GetDeviceSDK: $e")
            ""
        }
    }

    fun getDeviceBrand(device: String): String {
        return try {
            runAdbGetList("-s $device shell getprop ro.product.vendor.brand").first()
        } catch (e: Exception) {
            LogOutputWindow.takeLog("GetDeviceBrand: $e")
            ""
        }
    }

    fun getDeviceModel(device: String): String {
        return try {
            runAdbGetList("-s $device shell getprop ro.product.vendor.model").first()
        } catch (e: Exception) {
            LogOutputWindow.takeLog("GetDeviceModel: $e")
            ""
        }
    }

    fun getDeviceImei(device: String): String {
        return try {
            runAdbGetList("-s $device shell \"service call iphonesubinfo 1 | grep -o \'[0-9a-f]\\{8\\} \' | tail -n+3 | while read a; do echo -n \\\\u\${a:4:4}\\\\u\${a:0:4}; done\"").first()
        } catch (e: Exception) {
            LogOutputWindow.takeLog("GetDeviceImei: $e")
            ""
        }
    }

    fun getDeviceAndroidVersion(device: String): String {
        return try {
            runAdbGetList("-s $device shell getprop ro.build.version.release").first()
        } catch (e: Exception) {
            LogOutputWindow.takeLog("GetDeviceAndroidVersion: $e")
            ""
        }
    }
}
