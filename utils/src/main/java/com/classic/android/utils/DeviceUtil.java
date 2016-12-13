package com.classic.android.utils;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Build;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.telephony.TelephonyManager;
import android.telephony.gsm.GsmCellLocation;
import android.text.TextUtils;
import android.util.Log;

import java.util.UUID;

/**
 * 应用名称: BaseProject
 * 包 名 称: com.classic.android.utils
 *
 * 文件描述: 设备信息工具类
 * 创 建 人: 续写经典
 * 创建时间: 2015/11/4 17:26
 */
@SuppressWarnings({"unused", "WeakerAccess"}) public final class DeviceUtil {
    private static final String TAG = "DeviceUtil";

    private static volatile DeviceUtil sDeviceUtil;

    private String           mAndroidId;
    private TelephonyManager mTelephonyManager;

    @SuppressLint("HardwareIds") private DeviceUtil(Context context) {
        mTelephonyManager = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
        mAndroidId = Settings.Secure.getString(context.getContentResolver(),
                                               Settings.Secure.ANDROID_ID);
    }

    public static DeviceUtil getInstance(@NonNull Context context) {
        if (null == sDeviceUtil) {
            synchronized (DeviceUtil.class) {
                if (null == sDeviceUtil) {
                    sDeviceUtil = new DeviceUtil(context);
                }
            }
        }
        return sDeviceUtil;
    }

    /**
     * 打印设备信息(调试专用)
     */
    public void printInfo() {
        //noinspection StringBufferMayBeStringBuilder,StringBufferReplaceableByString
        StringBuffer sb = new StringBuffer("设备信息：\n");
        sb.append("getID:").append(getID()).append("\n");
        sb.append("getAndroidId:").append(getAndroidId()).append("\n");
        sb.append("getDeviceId:").append(getDeviceId()).append("\n");
        sb.append("getUUID:").append(getUUID()).append("\n");
        sb.append("getNumber:").append(getNumber()).append("\n");
        sb.append("getSubscriberId:").append(getSubscriberId()).append("\n");
        sb.append("getSimSerialNumber:").append(getSimSerialNumber()).append("\n");
        sb.append("getSerialNumber:").append(getSerialNumber()).append("\n");
        sb.append("getPhoneType:").append(getPhoneType()).append("\n");
        sb.append("getNetworkType:").append(getNetworkType()).append("\n");
        Log.d(TAG, sb.toString());
    }

    /**
     * 获取ANDROID_ID
     * <pre>
     * 在设备首次启动时，系统会随机生成一个64位的数字，并把这个数字以16进制字符串的形式保存下来.
     * 在设备恢复出厂设置后，该值可能会改变, ANDROID_ID也可视为作为唯一设备标识号的一个好选择。
     * </pre>
     */
    public String getAndroidId() {
        return TextUtils.isEmpty(mAndroidId) ? "" : mAndroidId;
    }

    /**
     * 获取UUID
     * UUID是指在一台机器上生成的数字，它保证对在同一时空中的所有机器都是唯一的。
     */
    public String getUUID() {
        return UUID.randomUUID().toString();
    }

    /**
     * 获取设备绝对的唯一标识
     * 先获取DeviceId,如果为空,获取ANDROID_ID,如果还是空,生成一个UUID
     */
    public String getID() {
        String result = getDeviceId();
        if (TextUtils.isEmpty(result)) {
            result = getAndroidId();
            if (TextUtils.isEmpty(result)) {
                result = getUUID();
            }
        }
        return result;
    }

    /**
     * 返回当前移动终端的唯一标识
     * <pre>
     * 如果是GSM网络，返回IMEI;
     * 如果是CDMA网络，返回MEID;
     * 权限： 获取DEVICE_ID需要READ_PHONE_STATE权限
     * </pre>
     */
    @SuppressLint("HardwareIds") public String getDeviceId() {
        final String deviceId = mTelephonyManager.getDeviceId();
        return TextUtils.isEmpty(deviceId) ? "" : deviceId;
    }

    /**
     * 返回手机号码 (可能为空)
     */
    @SuppressLint("HardwareIds") public String getNumber() {
        final String number = mTelephonyManager.getLine1Number();
        return TextUtils.isEmpty(number) ? "" : number;
    }

    /**
     * 返回用户唯一标识，比如GSM网络的IMSI编号
     */
    @SuppressLint("HardwareIds") public String getSubscriberId() {
        final String subscriberId = mTelephonyManager.getSubscriberId();
        return TextUtils.isEmpty(subscriberId) ? "" : subscriberId;
    }

    /**
     * 获取IMSI
     */
    public String getIMSI() {
        return getSubscriberId();
    }

    /**
     * 返回设备的当前位置
     * <p>Requires Permission:
     * {@link android.Manifest.permission#ACCESS_COARSE_LOCATION ACCESS_COARSE_LOCATION} or
     * {@link android.Manifest.permission#ACCESS_COARSE_LOCATION ACCESS_FINE_LOCATION}.
     *
     * @return GsmCellLocation
     */
    public GsmCellLocation getGsmCellLocation() {
        return (GsmCellLocation) mTelephonyManager.getCellLocation();
    }

    /**
     * 返回手机服务商名字
     * <pre>
     * 46000 中国移动 (GSM)
     * 46001 中国联通 (GSM)
     * 46002 中国移动 (TD-S)
     * 46003 中国电信 (CDMA)
     * 46005 中国电信 (CDMA)
     * 46006 中国联通 (WCDMA)
     * 46007 中国移动 (TD-S)
     * 46011 中国电信 (FDD-LTE)
     * </pre>
     */
    public String getProvidersName() {
        String providersName;
        //IMSI号前面3位460是国家，紧接着后面2位00 02是中国移动，01是中国联通，03是中国电信。
        String imsi = getSubscriberId();
        if (imsi.startsWith("46000") || imsi.startsWith("46002") || imsi.startsWith("46007")) {
            providersName = "中国移动";
        } else if (imsi.startsWith("46001") || imsi.startsWith("46006")) {
            providersName = "中国联通";
        } else if (imsi.startsWith("46003") ||
                   imsi.startsWith("46005") ||
                   imsi.startsWith("46011")) {
            providersName = "中国电信";
        } else {
            providersName = "其它服务商";
        }
        return providersName;
    }

    /**
     * 返回SIM卡的序列号(IMEI)
     * 注意：对于CDMA设备，返回的是一个空值！
     */
    @SuppressLint("HardwareIds") public String getSimSerialNumber() {
        final String simSerialNumber = mTelephonyManager.getSimSerialNumber();
        return TextUtils.isEmpty(simSerialNumber) ? "" : simSerialNumber;
    }

    /**
     * 返回序列号 (Android 2.3以上可以使用此方法)
     */
    public String getSerialNumber() {
        final String serialNumber = Build.SERIAL;
        return TextUtils.isEmpty(serialNumber) ? "" : serialNumber;
    }

    /**
     * 返回移动终端的类型
     * <pre>
     * 0：PHONE_TYPE_NONE 手机制式未知,可能是平板
     * 1：PHONE_TYPE_GSM  手机制式为GSM，移动和联通
     * 2：PHONE_TYPE_CDMA 手机制式为CDMA，电信
     * </pre>
     */
    public int getPhoneType() {
        return mTelephonyManager.getPhoneType();
    }

    /**
     * 获取网络类型
     * <pre>
     * NETWORK_TYPE_CDMA   网络类型为CDMA
     * NETWORK_TYPE_EDGE   网络类型为EDGE
     * NETWORK_TYPE_EVDO_0 网络类型为EVDO0
     * NETWORK_TYPE_EVDO_A 网络类型为EVDOA
     * NETWORK_TYPE_GPRS   网络类型为GPRS
     * NETWORK_TYPE_HSDPA  网络类型为HSDPA
     * NETWORK_TYPE_HSPA   网络类型为HSPA
     * NETWORK_TYPE_HSUPA  网络类型为HSUPA
     * NETWORK_TYPE_UMTS   网络类型为UMTS
     *
     * 在国内，
     * 联通的3G为UMTS或HSDPA，
     * 移动和联通的2G为GPRS或EGDE，
     * 电信的2G为CDMA，
     * 电信的3G为EVDO
     * </pre>
     */
    public int getNetworkType() {
        return mTelephonyManager.getNetworkType();
    }

    /** 手机品牌 */
    public String getBrand() { return Build.BRAND; }

    /** 手机型号 */
    public String getModel() { return Build.MODEL; }

    /** sdk版本 */
    public int getSdkVersion() { return Build.VERSION.SDK_INT; }

    /** 系统版本 */
    public String getUserVersion() { return Build.VERSION.RELEASE; }

    /** 硬件名称 */
    public String getHardware() { return Build.HARDWARE; }
}
