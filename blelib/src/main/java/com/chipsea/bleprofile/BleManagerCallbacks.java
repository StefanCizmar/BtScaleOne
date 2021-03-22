package com.chipsea.bleprofile;

/**
 * @ClassName:BleManagerCallbacks
 * @PackageName:com.chipsea.bleprofile
 * @Create On 2019/3/24.
 * @Site:te:http://www.handongkeji.com
 * @author:chenzhiguang
 * @Copyrights 2018/8/13  handongkeji All rights reserved.
 */
public interface BleManagerCallbacks {
    /**
     * connected
     */
    void onDeviceConnected();

    /**
     * The line is disconnected
     */
    void onDeviceDisconnected();

    /**
     * Service discovered
     */
    void onServicesDiscovered();

    /**
     * Enable (subscribe to notification) succeeded
     */
    void onIndicationSuccess();

    /**
     * Abnormal connection
     * @param message
     * @param errorCode
     */
    void onError(final String message, final int errorCode);
}
