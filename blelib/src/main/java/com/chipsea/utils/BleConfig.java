package com.chipsea.utils;

import android.bluetooth.BluetoothDevice;
import android.os.ParcelUuid;
import android.text.TextUtils;
import android.util.Log;
import android.util.SparseArray;

import com.chipsea.entity.BodyFatData;
import com.chipsea.entity.BroadData;
import com.chipsea.entity.CsFatScale;
import com.chipsea.entity.User;
import com.chipsea.scandecoder.ScanRecord;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

/**
 * @ClassName:BleConfig
 * @PackageName:com.chipsea.utils
 * @Create On 2019/3/24.
 * @Site:te:http://www.handongkeji.com
 * @author:chenzhiguang
 * @Copyrights 2018/8/13  handongkeji All rights reserved.
 */

/**
 * Main Class for read data from device
 */

public class BleConfig {

    private static final String TAG = BleConfig.class.getSimpleName();

    public static final byte BM_CS = 0x0F;     // deviceType = 15

    private static final byte BM_CS_FLAG = (byte) 0xBC;

    /** units */
    public final static byte UNIT_KG = 0x00;    // Kg
    public final static byte UNIT_LB = 0x01;    // Lb
    public final static byte UNIT_JIN = 0x02;   // Jin
    public final static byte UNIT_ST = 0x03;    // Stone

    /** Map corresponding key */
    public final static int WEIGHT_DATA = 0;

    public static float WEIGHT = 0;

    public static float BMI = 0;    // *OK
    public static float BFR = 0;    // *OK
    public static float TFR = 0;    // *OK percent
    public static float SLM = 0;    // *OK
    public static float BMR = 0;    // *OK calorie
    public static  float BOM = 0;   //

    public static String currDate;
    public static String currTime;

    /** instruction */
    public final static byte SYNC_UNIT = (byte) 0x01;

    private static final DecimalFormat df = new DecimalFormat("#.#");      // 0.0
    private static final DecimalFormatSymbols symbols = df.getDecimalFormatSymbols();

    /**
     * Analyze broadcast information to obtain broadcast content
     *
     * @param scanRecord
     * @param device
     * @param rssi
     * @return
     */
    public static BroadData getBroadData(BluetoothDevice device, int rssi, byte[] scanRecord) {
        ParcelUuid[] parcelUuids = device.getUuids();
        if (parcelUuids != null && parcelUuids.length > 0) {
            for (ParcelUuid uuid : parcelUuids
            ) {
                L.i(">> UUID >> " + uuid.getUuid());

            }
        }

        ScanRecord scanResult = ScanRecord.parseFromBytes(scanRecord);
        if (scanResult != null) {
            SparseArray<byte[]> manufacturerData = scanResult.getManufacturerSpecificData();
            List<ParcelUuid> uuidList = scanResult.getServiceUuids();
            if (!isListEmpty(uuidList)) {
                for (int i = 0; i < uuidList.size(); i++) {
                    L.e(TAG, "uuid: " + uuidList.get(i).getUuid().toString());
                }
            }

            /**
             * // todo PROTOCOL 1.0
             * Broadcast package specification
             * For Bluetooth devices connected to OKOK, the broadcast package must meet the specifications before being connected. The broadcast package is required to include manufacture
             * data,The format requirements are as follows ：
             * byte description:
             * 0 CA start byte
             * 1 10 Protocol version number , 1.0
             * 2 0F Data domain length
             * 3 01 locked / no locked 00
             * 4 01 BT body grease scale
             * 5 -  High weight byte
             * 6 -  Low weight byte
             * 7-10 Product ID
             * 11 01 Properties
             */


            /**
             *  TICKR 96 DO         SERVICE HART RATE
             *  Service             0000180d-0000-1000-8000-00805f9b34fb
             *  Characteristic      0000180d-0000-1000-8000-00805f9b34fb
             *  01 02 start bit 03 04 HART RATE
             */


            if (manufacturerData != null
                    && !TextUtils.isEmpty(device.getName())
                    && device.getName().startsWith("Chipsea-BLE")) {    // && device.getName().startsWith("Chipsea-BLE")) device.getName().startsWith("TICKR"))

                L.i("manufacturerData " + manufacturerData.toString());
                BroadData broadData = new BroadData();
                broadData.setAddress(device.getAddress());
                broadData.setName(device.getName());
                broadData.setRssi(rssi);
                broadData.setDeviceType(BM_CS);
                return broadData;
            }
        }
        return null;
    }

    /**
     * Determine whether the array is empty
     *
     * @param b
     * @return
     */
    private static boolean isArrEmpty(byte[] b) {

        return b == null || b.length == 0;
    }

    /**
     * Determine whether the list is empty
     *
     * @param list
     * @param <T>
     * @return
     */
    private static <T> boolean isListEmpty(List<T> list) {
        return list == null || list.size() == 0;
    }

    /**
     * Initialization instruction
     *
     * @param index
     * @param unitType
     * @return
     */
    public static byte[] initCmd(byte index, byte unitType) {
        byte[] b = new byte[19];
        b[0] = (byte) 0xFF;
        b[1] = (byte) 0xF0;
        b[2] = (byte) 0x02;

        switch (index) {
            case SYNC_UNIT:     // Sync unit
                b[3] = ParseData.bitToByte("000");
                break;
        }
//        b[0] = AICARE_FLAG;
//        b[1] = deviceType;
//        switch (index) {
//            case SYNC_HISTORY:
//                b[2] = SYNC_HISTORY;
//                b[6] = SYNC_HISTORY_OR_LIST;
//                break;
//            case SYNC_USER_ID:
//                b[2] = SYNC_USER_ID;
//                b[3] = (byte) user.getId();
//                b[6] = SETTINGS;
//                break;
//            case SYNC_USER_INFO:
//                b[2] = SYNC_USER_INFO;
//                b[3] = (byte) user.getSex();
//                b[4] = ((Integer) user.getAge()).byteValue();
//                b[5] = ((Integer) user.getHeight()).byteValue();
//                b[6] = SETTINGS;
//                break;
//            case SYNC_DATE:
//                String[] date = ParseData.getDate().split("-");
//                b[2] = SYNC_DATE;
//                b[3] = Integer.valueOf(date[0].substring(2, 4)).byteValue();
//                b[4] = Integer.valueOf(date[1]).byteValue();
//                b[5] = Integer.valueOf(date[2]).byteValue();
//                b[6] = SETTINGS;
//                break;
//            case SYNC_TIME:
//                String[] time = ParseData.getTime().split(":");
//                b[2] = SYNC_TIME;
//                b[3] = Integer.valueOf(time[0]).byteValue();
//                b[4] = Integer.valueOf(time[1]).byteValue();
//                b[5] = Integer.valueOf(time[2]).byteValue();
//                b[6] = SETTINGS;
//                break;
//            case SYNC_UNIT:
//                b[2] = OPERATE_OR_STATE;
//                b[3] = SYNC_UNIT;
//                b[4] = unitType;
//                b[6] = SETTINGS;
//                break;
//            case GET_BLE_VERSION:
//                b[2] = GET_BLE_VERSION;
//                b[6] = SETTINGS;
//                break;
//            case SYNC_LIST_OVER:
//                b[2] = UPDATE_USER_OR_LIST;
//                b[3] = SYNC_LIST_OVER;
//                b[6] = SYNC_HISTORY_OR_LIST;
//                break;
//        }
//        b[7] = getByteSum(b, SUM_START, SUM_END);
        L.i(TAG, "initCmd: " + ParseData.byteArr2Str(b));
        return b;
    }

    /**
     * Get the return value of the device
     *
     * @param b
     * @return
     */
    public static SparseArray<Object> getDatas(byte[] b) {
        L.i(TAG, "getDatas: " + ParseData.byteArr2Str(b));
        SparseArray<Object> sparseArray = new SparseArray<>();
        if (checkData(b)) {
            sparseArray = getCsFatScaleData(b);
        }

        return sparseArray;
    }

    /**
     * Verify that the data is correct
     *
     * @param b
     * @return
     */
    private static boolean checkData(byte[] b) {
       if (b == null || b.length == 0) return false;
//        if (b.length == 8 && b[0] == AICARE_FLAG && (b[1] == TYPE_WEI || b[1] == TYPE_WEI_TEMP || b[1] == TYPE_WEI_TEMP_BROAD || b[1] == TYPE_WEI_BROAD)) {
//            byte result = getByteSum(b, SUM_START, SUM_END);
//            L.e(TAG, "result = " + result);
//            L.e(TAG, "b[SUM_END] = " + b[SUM_END]);
//            return result == b[SUM_END];
//        }
//        return false;
        return true;
    }

    /**
     * Get weight data
     *
     * @param b
     * @return
     */
    private static SparseArray<Object> getCsFatScaleData(byte[] b) {
        L.i(TAG, "getCsFatScaleData: " + ParseData.byteArr2Str(b));
        SparseArray<Object> sparseArray = new SparseArray<>();


        /** NEW PROTOCOL **/
        int protocol = ParseData.binaryToDecimal(b[1]);
        int properties = ParseData.binaryToDecimal(b[11]);

        /** weight bytes **/
        int weight = ParseData.getDataInt(b[5], b[6]);

        int year = ParseData.getDataInt(b[3], b[2]);
        int month = ParseData.binaryToDecimal(b[4]);
        int day = ParseData.binaryToDecimal(b[5]);

        int hour = ParseData.binaryToDecimal(b[6]);
        int minute = ParseData.binaryToDecimal(b[7]);
        int second = ParseData.binaryToDecimal(b[8]);


        int resistanceOne = ParseData.getDataInt(b[10], b[9]);

        //int weight = ParseData.getDataInt(b[12], b[11]);

        int resistanceTwo = ParseData.getDataInt(b[14], b[13]);

        int msg = ParseData.binaryToDecimal(b[15]);

        //b[16] - b[19]  Reserved field

        //Date date = new Date(year, month, day, hour, minute, second);


        //Date date = new Date(year, month, day, hour, minute, second);
        Date date = new Date(2021, 2, 17, 8, 10, 00);

        /** Get current date and time **/
        SimpleDateFormat sdf = new SimpleDateFormat("dd.MM.yyyy");

        //SimpleDateFormat sdf = new SimpleDateFormat("yyyy.MM.dd");
        SimpleDateFormat sdf2 = new SimpleDateFormat("HH:mm:ss");

        currDate = sdf.format(new Date());
        currTime = sdf2.format(new Date());

        CsFatScale csFatScale = new CsFatScale(protocol, weight, date, resistanceOne, resistanceTwo, properties);

        sparseArray.put(WEIGHT_DATA, csFatScale);
        return sparseArray;
    }

    /**
     * Get body fat data
     *
     * @param user
     * @param csFatScale
     * @return
     */
    public static BodyFatData getBodyFatData(User user, CsFatScale csFatScale) {
        BodyFatData bodyFatData = null;

        if (user != null) {
            //CsAlgoBuilder csAlgoBuilder = new CsAlgoBuilder();

            //CsAlgoBuilderEx csAlgoBuilderEx = CsAlgoBuilderExUtil.getAlgoBuilderEx();

            /**
             * Set user information
             * @param height Height in cm
             * @param sex male-1 female-0
             * @param age age
             * @param weight Current measured weight in kg
             * @param r Current measurement resistance
             *
             */

            //csAlgoBuilder.setUserInfo(user.getHeight(), user.getSex(), user.getAge(), (csFatScale.getWeight() / 10f), (csFatScale.getAdc() / 10f));

            /**    If you need to perform resistance filtering, please call the following method
             * Filtering principle: compare the current and last measured resistance value,
             * Filter the resistance according to the measurement time and certain filtering rules,
             * Avoid continuous measurement due to the large difference in measurement results caused by resistance interference
             * public float setUserInfo(float height, byte sex, int age, float curWeight, float curR, Date curTime, float lastR, Date lastTime)
             * Constructor after adding resistance filtering (used to call and calculate the result according to the data uploaded by the Bluetooth scale)
             *
             * @param height height
             * @param sex Male-1 Female-0
             * @param age age
             * @param curWeight Currently measured weight
             * @param curR Current measurement resistance
             * @param curTime Current measurement resistance
             * @param lastR Last measured resistance
             * @param lastTime Last measurement time
             * @return Return the filtered resistance
             */

            /**
            StringBuilder sb = new StringBuilder();
            sb.append("细胞外液EXF:" + csAlgoBuilderEx.getEXF() + "\r\n");
            sb.append("细胞内液Inf:" + csAlgoBuilderEx.getInF() + "\r\n");
            sb.append("总水重TF:" + csAlgoBuilderEx.getTF() + "\r\n");
            sb.append("含水百分比TFR:" + csAlgoBuilderEx.getTFR() + "\r\n");
            sb.append("去脂体重LBM:" + csAlgoBuilderEx.getLBM() + "\r\n");
            sb.append("Muscle weight:" + csAlgoBuilderEx.getSLM() + "\r\n");
            sb.append("蛋白质PM:" + csAlgoBuilderEx.getPM() + "\r\n");
            sb.append("Fat weight:" + csAlgoBuilderEx.getFM() + "\r\n");
            sb.append("脂肪百份比BFR:" + csAlgoBuilderEx.getBFR() + "\r\n");
            sb.append("水肿测试EE:" + csAlgoBuilderEx.getEE() + "\r\n");
            sb.append("肥胖度OD:" + csAlgoBuilderEx.getOD() + "\r\n");
            sb.append("肌肉控制MC:" + csAlgoBuilderEx.getMC() + "\r\n");
            sb.append("体重控制WC:" + csAlgoBuilderEx.getWC() + "\r\n");
            sb.append("Basal metabolic BMR:" + csAlgoBuilderEx.getBMR() + "\r\n");
            sb.append("骨(无机盐)MSW:" + csAlgoBuilderEx.getMSW() + "\r\n");
            sb.append("内脏脂肪等级VFR:" + csAlgoBuilderEx.getVFR() + "\r\n");
            sb.append("身体年龄BodyAge:" + csAlgoBuilderEx.getBodyAge() + "\r\n");
            sb.append("评分:" + csAlgoBuilderEx.getScore() + "\r\n");


            L.i(TAG, ">>>>>>>>>>FatData>>>>>>>>>>" + sb.toString());
*/
            /** OK */
            /** body weight */
            float weight = floatFormat((csFatScale.getWeight() / 10.0f));
            WEIGHT = weight;

            /** OK */
            /** bmi */
            float bmi = floatFormat(((csFatScale.getWeight() / 10.0f) / ((user.getHeight() / 100.0f) * (user.getHeight()) / 100.0f)));
            BMI = bmi;

            /** OK */
            /** Body fat rate */
            float bfr = floatFormat(getBFR());
            L.i(TAG, "BFR  " + bfr);
            BFR = bfr;

            /** Muscle weight */
            float rom = floatFormat(getSLM());
            SLM = rom;

            /** OK */
            /** Water content (moisture) */
            float vwc = floatFormat(getTFR());
            TFR = vwc;

            /** Bone mass */
            float bm = getMSW();
            BOM = bm;

            /** subcutaneous fat */
            float sfr = 444;
            //float sfr = floatFormat(csAlgoBuilderEx.getFM());

            /** OK */
            /** Basal metabolic rate */
            float bmr = floatFormat(getBMR());
            BMR = bmr;

            /** Protein rate */
            float pp = 4444;
            //float pp = floatFormat((csAlgoBuilderEx.getPM() / csFatScale.getWeight() * 100f));

            /** Visceral fat */
            float vui = getVFR();
            //float vui = floatFormat(csAlgoBuilderEx.getVFR());

            /** Body age */
            float bodyAge = getBAge();
            //float bodyAge = floatFormat(csAlgoBuilderEx.getBodyAge());

            /** score */
            float score = 4444;
            //float score = floatFormat(csAlgoBuilderEx.getScore());

            bodyFatData = new BodyFatData(currDate, currTime,
                    weight, bmi, bfr, sfr, vui, rom, bmr, bm, vwc, bodyAge, pp, score, user.getId(), user.getSex(), user.getAge(), user.getHeight(), user.getAdc());
        }

        return bodyFatData;
    }




    /*************************************** FOMULAS *************************************************************/

    /** get BONE */
    private static float getMSW() {

        float b = (getBFR() * User.getHeight()) / 100.0f;

        float fm = (WEIGHT - b) - getSLM();
        if (fm < 1.0f) {
            return 1.0f;
        }
        /**
        if (fm > 4.0f) {
            return 4.0f;
        }**/
        return fm;
    }

    /** get Visceral fat*/
    private static float getVFR() {
        float f;
        if (User.getAge() <= 17) {
            return 0.0f;
        }
        if (User.getSex() == 1) {
            f = (User.getHeight() * -0.2675f) + (WEIGHT * 0.42f) + (((float) User.getAge()) * 1.0f * 0.1462f) + (User.getAdc() * 0.0123f) + 13.9871f;
        } else {
            f = (User.getHeight() * -0.1651f) + (WEIGHT * 0.2628f) + (((float) User.getAge()) * 1.0f * 0.0649f) + (User.getAdc() * 0.0024f) + 12.3445f;
        }
        float f2 = (float) ((int) f);
        if (f - f2 >= 0.5f) {
            f2 += 0.5f;
        }
        if (f2 < 1.0f) {
            return 1.0f;
        }
        if (f2 > 59.0f) {
            return 59.0f;
        }

        L.i(TAG, "Visceral  " + f2);

        return f2;
    }

    /** get Body age */
    private static float getBAge() {

        float f;
        if (User.getAge() <= 17) {
            return 0;
        }
        if (User.getSex() == 1) {
            f = (User.getHeight() * -0.7471f) + (WEIGHT * 0.9161f) + (((float) User.getAge()) * 1.0f * 0.4184f) + (User.getAdc() * 0.0517f) + 54.2267f;
        } else {
            f = (User.getHeight() * -1.1165f) + (WEIGHT * 1.5784f) + (((float) User.getAge()) * 1.0f * 0.4615f) + (User.getAdc() * 0.0415f) + 83.2548f;
        }
        int i = (int) f;
        if (i - User.getAge() > 10) {
            i = User.getAge() + 10;
        } else if (User.getAge() - i > 10) {
            i = User.getAge() - 10;
        }
        if (i < 18) {
            i = 18;
        }
        if (i > 80) {
            return 80;
        }
        L.i(TAG, "AGE  " + i);


        return i;


    }

    /** get BMR CALORIE */
    private static float getBMR() {

        /**
         *      For men: BMR = (10 x weight) + (6.25 x height) – (5 x age) + 5
         *
         *     For women: BMR = (10 x weight) + (6.25 x height) – (5 x age) – 161
         *
         *     modification original formula Adc change from 0.3486f to 0.06486
         */

        float f;
        if (User.getSex() == 1) {

            /** MAN  OK **/
            f = ((((User.getHeight() * 7.5037f) + (WEIGHT * 13.1523f)) - ((((float) User.getAge()) * 1.0f) * 4.3376f)) - (User.getAdc() * 0.06486f)) + 5.7751f;
        } else {

            f = ((((User.getHeight() * 7.5432f) + (WEIGHT * 9.9474f)) - ((((float) User.getAge()) * 1.0f) * 3.4382f)) - (User.getAdc() * 0.309f)) - 288.2821f;
        }
        L.i(TAG, "BASAL  " + f);

        return new BigDecimal((double) f).setScale(1, 4).floatValue();


    }


    /** get SLM MUSCLE */
    private static float getSLM() {

        return new BigDecimal((double) getSLM_Raw()).setScale(2, 4).floatValue();
    }

    private static float getSLM_Raw() {

        float f;
        float bFR_Raw = getBFR_Raw();
        if (bFR_Raw > 45.0f) {
            return (WEIGHT - (WEIGHT * 0.45f)) - 4.0f;
        }
        if (bFR_Raw < 5.0f) {
            return (WEIGHT - (WEIGHT * 0.05f)) - 1.0f;
        }

        if (User.getSex() == 1) {
            /** MAN  OK **/
            f = ((((User.getHeight() * 0.167f) + (WEIGHT * 0.26f)) - ((((float) User.getAge()) * 1.0f) * 0.0408f)) - (User.getAdc() * 0.01235f)) - 15.7665f;
        } else {

            f = ((((User.getHeight() * 0.187f) + (WEIGHT * 0.1289f)) - ((((float) User.getAge()) * 1.0f) * 0.0206f)) - (User.getAdc() * 0.0132f)) - 16.4556f;
        }

        float f2 = 20.0f;
        if (f >= 20.0f) {
            f2 = f;
        }
        if (f2 > 70.0f) {
            return 70.0f;
        }

        L.i(TAG, "MUSCLE  " + f2);

        // percent
        //f2 = (f2 / WEIGHT) * 100.0f;

        return f2;
    }

    /** get TFR WATER  percent */

    private static float getTFR() {

        /**
         *  Watson formula for male: OK!!!!
         *
         *      2.447 – (0.09145 x age) + (0.1074 x height ) + (0.3362 x weight ) = total body weight (TBW) in liters
         *
         *  Watson formula for female:
         *
         *      –2.097 + (0.1069 x height) + (0.2466 x weight ) = total body weight (TBW) in liters
         *
         *      modification original formula in percents
         */

        float f;
        if (User.getAge() <= 17) {
            return 0.0f;
        }
        if (User.getSex() == 1) {

            /** MAN  OK **/
            f = (((( 2.447f + ((User.getHeight() * 0.110f) + (WEIGHT * 0.376f)) - ((((float) User.getAge()) * 1.0f) * 0.095f)) - (User.getAdc() * 0.006925f)) + 0.097f) / WEIGHT) * 100.0f;
        } else {

            f = (((( -2.097f + ((User.getHeight() * 0.1075f) + (WEIGHT * 0.2973f)) + ((((float) User.getAge()) * 1.0f) * 0.0128f)) - (User.getAdc() * 0.00603f)) + 0.5175f) / WEIGHT) * 100.0f;
        }
        float f2 = 20.0f;
        if (f >= 20.0f) {
            f2 = f;
        }
        if (f2 > 85.0f) {
            f2 = 85.0f;
        }

        L.i(TAG, "WATER " + f2);

        return new BigDecimal((double) f2).setScale(2, 4).floatValue();
    }


    /** OK get BFR  FAT */
    /**
     * Women: (1,20 * BMI) + (0,23 * Age) - 5,4 = Body Fat Percentage
     *
     * Men: (1,20 * BMI) + (0,23 * Age) - 16,2 = Body Fat Percentage
     */

    private static float getBFR() {
        float bFR_Raw = getBFR_Raw();
        if (bFR_Raw < 5.0f) {
            bFR_Raw = 5.0f;
        }
        if (bFR_Raw > 45.0f) {
            bFR_Raw = 45.0f;
        }
        return new BigDecimal((double) bFR_Raw).setScale(2, 4).floatValue();
    }

    public static float getBFR_Raw() {
        float f ;

        if (User.getSex() == 1) {

            f = (1.2f * BMI) + (0.23f * User.getAge()) - 16.2f;
            //f = ((((((User.getHeight() * -0.3315f) + (WEIGHT * 0.6216f)) + ((((float) User.getAge()) * 1.0f) * 0.0183f)) + (User.getAdc() * 0.0085f)) + 22.554f) / WEIGHT) * 100.0f;
        }

        else {
            f = (1.2f * BMI) + (0.23f * User.getAge()) - 5.4f;

            //f =  ((((((User.getHeight() * -0.3332f) + (WEIGHT * 0.7509f)) + ((((float) User.getAge()) * 1.0f) * 0.0196f)) + (User.getAdc() * 0.0072f)) + 22.7193f) / WEIGHT) * 100.0f;
        }
        return f;
    }

    /**
     * Get one-digit float
     *
     * @param value
     * @return
     */
    private static float floatFormat(float value) {
        float data;
        double dataD;
        String dataStr = String.valueOf(value);

        //L.e(TAG,dataStr);

        dataD = Double.valueOf(dataStr);
        symbols.setDecimalSeparator('.');
        df.setDecimalFormatSymbols(symbols);
        dataStr = df.format(dataD);
        data = Float.parseFloat(dataStr);
        return data;
    }

}
