package com.zougy.tools.android

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationChannelGroup
import android.app.NotificationManager
import android.content.Context
import android.os.Build
import androidx.annotation.RequiresApi
import org.xutils.x

class NotificationMgr private constructor() {

    companion object {

        private val instance = NotificationMgr()

        fun getInstance(): NotificationMgr = instance
    }

    /**
     * Android8.0通知渠道相关
     */
    object Channel {
        private val channelGroupKeys = mutableMapOf<String, String>()

        private val channelKeys = mutableMapOf<String, String>()

        private val notificationManager = x.app().getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        /**
         * 向系统注册channelGroup
         */
        @RequiresApi(Build.VERSION_CODES.O)
        fun registerChannelGroup(groupID: String, groupName: String): Channel {
            channelGroupKeys[groupID] = groupName
            val group = NotificationChannelGroup(groupID, groupName)
            notificationManager.createNotificationChannelGroup(group)
            return this
        }

        @RequiresApi(Build.VERSION_CODES.P)
        fun getChannelGroup(groupID: String): NotificationChannelGroup {
            return notificationManager.getNotificationChannelGroup(groupID)
        }

        /**
         * 向系统注册channel
         */
        @RequiresApi(Build.VERSION_CODES.O)
        fun registerChannel(
            channelID: String,
            channelName: String,
            importance: Int = NotificationManager.IMPORTANCE_DEFAULT,
            block: (channel: NotificationChannel) -> Unit
        ): Channel{
            channelGroupKeys[channelID] = channelName
            val channel = NotificationChannel(channelID, channelName, importance)
            notificationManager.createNotificationChannel(channel)
            channelKeys[channelID] = channelName
            block(channel)
            return this
        }

        @RequiresApi(Build.VERSION_CODES.O)
        fun getChannel(channelID: String): NotificationChannel {
            return notificationManager.getNotificationChannel(channelID)
        }

        /**
         * 设置channelID对应的分组
         */
        @RequiresApi(Build.VERSION_CODES.O)
        fun setChannelGroup(channelID: String, groupID: String) {
            if (channelKeys.containsKey(channelID) && channelGroupKeys.containsKey(groupID)) {
                notificationManager.getNotificationChannel(channelID).group = groupID
            }
        }
    }

}