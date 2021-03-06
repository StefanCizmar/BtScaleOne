package com.chipsea.entity;

import java.util.Arrays;

/**
 * Broadcast data
 *
 * @ClassName:BroadData
 * @PackageName:com.chipsea.entity
 * @Create On 2019/3/24.
 * @Site:te:http://www.handongkeji.com
 * @author:chenzhiguang
 * @Copyrights 2018/8/13  handongkeji All rights reserved.
 */

public class BroadData {
    private String name;        // Bluetooth name
    private String address;     // Bluetooth address
    private boolean isBright;   // Whether the screen is bright
    private int rssi;           // signal
    private byte[] specificData;
    private int deviceType;     // Device Type

    public String getName() {
        return name == null ? "" : name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAddress() {
        return address == null ? "" : address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public boolean isBright() {
        return isBright;
    }

    public void setBright(boolean bright) {
        isBright = bright;
    }

    public int getRssi() {
        return rssi;
    }

    public void setRssi(int rssi) {
        this.rssi = rssi;
    }

    public byte[] getSpecificData() {
        return specificData;
    }

    public void setSpecificData(byte[] specificData) {
        this.specificData = specificData;
    }

    public int getDeviceType() {
        return deviceType;
    }

    public void setDeviceType(int deviceType) {
        this.deviceType = deviceType;
    }

    @Override
    public boolean equals(Object o) {
        if (o instanceof BroadData) {
            final BroadData that = (BroadData) o;
            return address.equals(that.getAddress());
        }
        return super.equals(o);
    }

    public static class AddressComparator {
        public String address;

        @Override
        public boolean equals(Object o) {
            if (o instanceof BroadData) {
                final BroadData that = (BroadData) o;
                return address.equals(that.getAddress());
            }
            return super.equals(o);
        }
    }

    @Override
    public String toString() {
        return "BroadData{" +
                "name='" + name + '\'' +
                ", address='" + address + '\'' +
                ", isBright=" + isBright +
                ", rssi=" + rssi +
                ", specificData=" + Arrays.toString(specificData) +
                ", deviceType=" + deviceType +
                '}';
    }
}
