package com.chipsea.utils;

import com.chipsea.entity.BodyFatData;

import java.math.BigDecimal;
import java.util.Date;

public class CsAlgoBuilder {

    /** Age*/
    private int Age;


    /** Height*/
    private float Hei;

    /** Gender */
    private byte Sex;

    /** Weight */
    private float Wei;

    /** Resistance */
    private float Res;

    public CsAlgoBuilder() {


        this.Hei = 185;
        this.Wei = 71.3f;

        this.Age = 53;
        this.Res = 1000;
        this.Sex = 1;

    }


    public static float calFM(float f, float f2) {
        return (f2 * f) / 100.0f;
    }

    public static float calPM(float f, byte b, float f2, float f3) {
        return ((100.0f - f2) - f3) - (((b == 1 ? 3.0f : 2.5f) / f) * 100.0f);
    }

    private float calResistance(long j, float f, float f2) {
        if (f == 0.0f || f2 == 0.0f || f == f2 || j > 30) {
            return f;
        }
        float f3 = f - f2;
        return (f3 > 24.0f || f3 <= 0.0f) ? (f3 >= 0.0f || f3 < -24.0f) ? (f3 <= 24.0f || f3 > 32.0f) ? (f3 >= -24.0f || f3 < -32.0f) ? (f3 <= 32.0f || f3 > 64.0f) ? (f3 >= -32.0f || f3 < -64.0f) ? (f3 <= 64.0f || f3 > 89.0f) ? (f3 >= -64.0f || f3 < -89.0f) ? f : (f2 + (f * 3.0f)) / 4.0f : (f2 + (f * 3.0f)) / 4.0f : f2 + (f3 / 2.0f) : f2 + (f3 / 2.0f) : f2 + (f3 / 4.0f) : f2 + (f3 / 4.0f) : f2 + (f3 / 16.0f) : f2 + (f3 / 16.0f);
    }

    public static float calSLM(float f, float f2) {
        return (f2 * f) / 100.0f;
    }

    public static float calScore(float f, float f2, byte b, int i, float f3, float f4, float f5) {
        float f6;
        float f7;
        float f8;
        float f9;
        float f10 = (f2 / (f * f)) * 100.0f * 100.0f;
        float f11 = (((f10 * f10) * -5.686f) + (f10 * 241.7f)) - 2470.0f;
        float f12 = 55.0f;
        if (f11 < 55.0f) {
            f11 = 55.0f;
        }
        if (f11 > 96.0f) {
            f11 = 96.0f;
        }
        float f13 = f3 + (((float) i) * 0.03f);
        if (b == 1) {
            float f14 = f13 * f13;
            float f15 = f14 * f13;
            f7 = ((((f15 * f13) * 1.085E-4f) - (f15 * 0.003181f)) - (f14 * 0.2952f)) + (f13 * 10.85f) + 0.4248f;
            f6 = f2 * 0.77f;
        } else {
            float f16 = f13 * f13;
            float f17 = f16 * f13;
            f7 = (((((f17 * f13) * 2.469E-4f) - (f17 * 0.02788f)) + (f16 * 0.9597f)) - (f13 * 10.02f)) + 80.42f;
            f6 = f2 * 0.735f;
        }
        if (((double) f7) >= 55.0d) {
            f12 = f7;
        }
        float f18 = (f4 + 90.0f) - f6;
        if (f18 > 100.0f) {
            f18 = 100.0f;
        }
        float f19 = -50.0f;
        if (((double) f5) >= 15.0d) {
            f8 = -50.0f;
        } else {
            float f20 = f5 * f5;
            float f21 = f20 * f5;
            float f22 = f21 * f5;
            f8 = 89.35f + (((((f22 * f5) * 0.007212f) - (f22 * 0.2825f)) + (f21 * 3.912f)) - (f20 * 22.27f)) + (30.38f * f5);
        }
        if (f8 >= -50.0f) {
            f19 = f8;
        }
        float f23 = 0.0f;
        if (f5 == 0.0f) {
            f9 = 0.48f;
        } else {
            f9 = 0.4f;
            f23 = 0.08f;
        }
        int i2 = (int) ((f9 * f11) + (0.4f * f12) + (0.1f * f18) + (f23 * f19));
        if (i2 < 45) {
            i2 = 45;
        }
        if (i2 > 100) {
            i2 = 100;
        }
        return (float) i2;
    }

    public static float getBW(byte b, float f) {
        return b == 1 ? (f - 80.0f) * 0.7f : (f - 70.0f) * 0.6f;
    }

    /* JADX WARNING: Can't fix incorrect switch cases order */
    /* JADX WARNING: Code restructure failed: missing block: B:14:0x002c, code lost:
        if (r8 < 27.0f) goto L_0x002e;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:15:0x002e, code lost:
        r8 = 2;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:27:0x004e, code lost:
        if (r8 < 28.0f) goto L_0x002e;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:37:0x006b, code lost:
        if (r8 < 30.0f) goto L_0x002e;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:48:0x008a, code lost:
        if (r8 < 40.0f) goto L_0x002e;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:59:0x00a9, code lost:
        if (r8 < 41.0f) goto L_0x002e;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:69:0x00c7, code lost:
        if (r8 < 42.0f) goto L_0x002e;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:87:0x00f6, code lost:
        if (r9 == 1) goto L_0x00fb;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:89:0x00fb, code lost:
        r4 = 1;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:90:0x00fd, code lost:
        if (r9 == 1) goto L_0x00fb;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:91:0x0100, code lost:
        r4 = 3;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:94:0x0106, code lost:
        if (r9 == 1) goto L_0x00f1;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:97:0x010d, code lost:
        if (r9 == 1) goto L_0x00f1;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:99:0x0111, code lost:
        return (float) r4;
     */
    /* JADX WARNING: Removed duplicated region for block: B:72:0x00d1  */
    /* JADX WARNING: Removed duplicated region for block: B:73:0x00d3  */
    /* JADX WARNING: Removed duplicated region for block: B:85:0x00f1 A[FALL_THROUGH] */
    /* JADX WARNING: Removed duplicated region for block: B:86:0x00f3  */
    /* JADX WARNING: Removed duplicated region for block: B:88:0x00f9  */
    /* JADX WARNING: Removed duplicated region for block: B:92:0x0102  */
    /* JADX WARNING: Removed duplicated region for block: B:95:0x0109  */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    private static float getShape(float r8, float r9, byte r10, int r11) {
        /*
            r0 = 59
            r1 = 39
            r2 = 1102577664(0x41b80000, float:23.0)
            r3 = 1102053376(0x41b00000, float:22.0)
            r4 = 2
            r5 = 3
            r6 = 0
            r7 = 1
            if (r10 != r7) goto L_0x006e
            if (r11 > r1) goto L_0x0034
            r10 = 1093664768(0x41300000, float:11.0)
            int r11 = (r8 > r10 ? 1 : (r8 == r10 ? 0 : -1))
            if (r11 >= 0) goto L_0x0019
        L_0x0016:
            r8 = 0
            goto L_0x00cb
        L_0x0019:
            int r10 = (r8 > r10 ? 1 : (r8 == r10 ? 0 : -1))
            if (r10 < 0) goto L_0x0024
            int r10 = (r8 > r3 ? 1 : (r8 == r3 ? 0 : -1))
            if (r10 >= 0) goto L_0x0024
        L_0x0021:
            r8 = 1
            goto L_0x00cb
        L_0x0024:
            int r10 = (r8 > r3 ? 1 : (r8 == r3 ? 0 : -1))
            if (r10 < 0) goto L_0x0031
            r10 = 1104674816(0x41d80000, float:27.0)
            int r8 = (r8 > r10 ? 1 : (r8 == r10 ? 0 : -1))
            if (r8 >= 0) goto L_0x0031
        L_0x002e:
            r8 = 2
            goto L_0x00cb
        L_0x0031:
            r8 = 3
            goto L_0x00cb
        L_0x0034:
            if (r11 > r0) goto L_0x0051
            r10 = 1094713344(0x41400000, float:12.0)
            int r11 = (r8 > r10 ? 1 : (r8 == r10 ? 0 : -1))
            if (r11 >= 0) goto L_0x003d
            goto L_0x0016
        L_0x003d:
            int r10 = (r8 > r10 ? 1 : (r8 == r10 ? 0 : -1))
            if (r10 < 0) goto L_0x0046
            int r10 = (r8 > r2 ? 1 : (r8 == r2 ? 0 : -1))
            if (r10 >= 0) goto L_0x0046
            goto L_0x0081
        L_0x0046:
            int r10 = (r8 > r2 ? 1 : (r8 == r2 ? 0 : -1))
            if (r10 < 0) goto L_0x0031
            r10 = 1105199104(0x41e00000, float:28.0)
            int r8 = (r8 > r10 ? 1 : (r8 == r10 ? 0 : -1))
            if (r8 >= 0) goto L_0x0031
            goto L_0x008c
        L_0x0051:
            r10 = 1096810496(0x41600000, float:14.0)
            int r11 = (r8 > r10 ? 1 : (r8 == r10 ? 0 : -1))
            if (r11 >= 0) goto L_0x0058
            goto L_0x0016
        L_0x0058:
            int r10 = (r8 > r10 ? 1 : (r8 == r10 ? 0 : -1))
            r11 = 1103626240(0x41c80000, float:25.0)
            if (r10 < 0) goto L_0x0063
            int r10 = (r8 > r11 ? 1 : (r8 == r11 ? 0 : -1))
            if (r10 >= 0) goto L_0x0063
            goto L_0x0081
        L_0x0063:
            int r10 = (r8 > r11 ? 1 : (r8 == r11 ? 0 : -1))
            if (r10 < 0) goto L_0x0031
            r10 = 1106247680(0x41f00000, float:30.0)
            int r8 = (r8 > r10 ? 1 : (r8 == r10 ? 0 : -1))
            if (r8 >= 0) goto L_0x0031
            goto L_0x008c
        L_0x006e:
            if (r11 > r1) goto L_0x008d
            r10 = 1101529088(0x41a80000, float:21.0)
            int r11 = (r8 > r10 ? 1 : (r8 == r10 ? 0 : -1))
            if (r11 >= 0) goto L_0x0077
            goto L_0x0016
        L_0x0077:
            int r10 = (r8 > r10 ? 1 : (r8 == r10 ? 0 : -1))
            r11 = 1108082688(0x420c0000, float:35.0)
            if (r10 < 0) goto L_0x0082
            int r10 = (r8 > r11 ? 1 : (r8 == r11 ? 0 : -1))
            if (r10 >= 0) goto L_0x0082
        L_0x0081:
            goto L_0x0021
        L_0x0082:
            int r10 = (r8 > r11 ? 1 : (r8 == r11 ? 0 : -1))
            if (r10 < 0) goto L_0x0031
            r10 = 1109393408(0x42200000, float:40.0)
            int r8 = (r8 > r10 ? 1 : (r8 == r10 ? 0 : -1))
            if (r8 >= 0) goto L_0x0031
        L_0x008c:
            goto L_0x002e
        L_0x008d:
            if (r11 > r0) goto L_0x00ac
            int r10 = (r8 > r3 ? 1 : (r8 == r3 ? 0 : -1))
            if (r10 >= 0) goto L_0x0094
            goto L_0x0016
        L_0x0094:
            int r10 = (r8 > r3 ? 1 : (r8 == r3 ? 0 : -1))
            if (r10 < 0) goto L_0x009f
            r10 = 1108344832(0x42100000, float:36.0)
            int r10 = (r8 > r10 ? 1 : (r8 == r10 ? 0 : -1))
            if (r10 >= 0) goto L_0x009f
            goto L_0x0081
        L_0x009f:
            r10 = 1108344832(0x42100000, float:36.0)
            int r10 = (r8 > r10 ? 1 : (r8 == r10 ? 0 : -1))
            if (r10 < 0) goto L_0x0031
            r10 = 1109655552(0x42240000, float:41.0)
            int r8 = (r8 > r10 ? 1 : (r8 == r10 ? 0 : -1))
            if (r8 >= 0) goto L_0x0031
            goto L_0x008c
        L_0x00ac:
            int r10 = (r8 > r2 ? 1 : (r8 == r2 ? 0 : -1))
            if (r10 >= 0) goto L_0x00b2
            goto L_0x0016
        L_0x00b2:
            int r10 = (r8 > r2 ? 1 : (r8 == r2 ? 0 : -1))
            if (r10 < 0) goto L_0x00bd
            r10 = 1108606976(0x42140000, float:37.0)
            int r10 = (r8 > r10 ? 1 : (r8 == r10 ? 0 : -1))
            if (r10 >= 0) goto L_0x00bd
            goto L_0x0081
        L_0x00bd:
            r10 = 1108606976(0x42140000, float:37.0)
            int r10 = (r8 > r10 ? 1 : (r8 == r10 ? 0 : -1))
            if (r10 < 0) goto L_0x0031
            r10 = 1109917696(0x42280000, float:42.0)
            int r8 = (r8 > r10 ? 1 : (r8 == r10 ? 0 : -1))
            if (r8 >= 0) goto L_0x0031
            goto L_0x002e
        L_0x00cb:
            r10 = 1100218368(0x41940000, float:18.5)
            int r11 = (r9 > r10 ? 1 : (r9 == r10 ? 0 : -1))
            if (r11 >= 0) goto L_0x00d3
            r9 = 0
            goto L_0x00ee
        L_0x00d3:
            int r10 = (r9 > r10 ? 1 : (r9 == r10 ? 0 : -1))
            if (r10 < 0) goto L_0x00df
            r10 = 1103101952(0x41c00000, float:24.0)
            int r10 = (r9 > r10 ? 1 : (r9 == r10 ? 0 : -1))
            if (r10 >= 0) goto L_0x00df
            r9 = 1
            goto L_0x00ee
        L_0x00df:
            r10 = 1103101952(0x41c00000, float:24.0)
            int r10 = (r9 > r10 ? 1 : (r9 == r10 ? 0 : -1))
            if (r10 < 0) goto L_0x00ed
            r10 = 1105199104(0x41e00000, float:28.0)
            int r9 = (r9 > r10 ? 1 : (r9 == r10 ? 0 : -1))
            if (r9 >= 0) goto L_0x00ed
            r9 = 2
            goto L_0x00ee
        L_0x00ed:
            r9 = 3
        L_0x00ee:
            switch(r8) {
                case 0: goto L_0x0109;
                case 1: goto L_0x0102;
                case 2: goto L_0x00f9;
                case 3: goto L_0x00f3;
                default: goto L_0x00f1;
            }
        L_0x00f1:
            r4 = 0
            goto L_0x0110
        L_0x00f3:
            if (r9 != 0) goto L_0x00f6
            goto L_0x00fb
        L_0x00f6:
            if (r9 != r7) goto L_0x0100
            goto L_0x00fb
        L_0x00f9:
            if (r9 != 0) goto L_0x00fd
        L_0x00fb:
            r4 = 1
            goto L_0x0110
        L_0x00fd:
            if (r9 != r7) goto L_0x0100
            goto L_0x00fb
        L_0x0100:
            r4 = 3
            goto L_0x0110
        L_0x0102:
            if (r9 != 0) goto L_0x0106
            r4 = -1
            goto L_0x0110
        L_0x0106:
            if (r9 != r7) goto L_0x0110
            goto L_0x00f1
        L_0x0109:
            if (r9 != 0) goto L_0x010d
            r4 = -1
            goto L_0x0110
        L_0x010d:
            if (r9 != r7) goto L_0x0110
            goto L_0x00f1
        L_0x0110:
            float r8 = (float) r4
            return r8
        */
        throw new UnsupportedOperationException("Method not decompiled: com.chipsea.code.algorithm.CsAlgoBuilder.getShape(float, float, byte, int):float");
    }

    public CsAlgoBuilder(float f, float f2, byte b, int i, float f3) {
        this.Hei = f;
        this.Wei = f2;
        this.Sex = b;
        this.Age = i;
        this.Res = f3;
    }

    public CsAlgoBuilder(float f, byte b, int i, float f2, float f3, Date date, float f4, Date date2) {
        this.Hei = f;
        this.Wei = f2;
        this.Sex = b;
        this.Age = i;
        this.Res = calResistance(((date.getTime() - date2.getTime()) / 1000) / 60, f3, f4);
    }

    public float getZ() {
        return this.Res;
    }

    public static int getHeartValue(String str) {
        if (str == null || str.trim().length() == 0) {
            return 0;
        }
        String[] split = str.trim().split("\\|");
        if (split.length <= 0) {
            return 0;
        }
        for (String trim : split) {
            String[] split2 = trim.trim().split("\\:");
            if (split2.length > 1 && split2[0].equals("2")) {
                return Integer.parseInt(split2[1]);
            }
        }
        return 0;
    }

    public float getH() {
        return this.Hei;
    }

    public byte getSex() {
        return this.Sex;
    }

    public int getAge() {
        return this.Age;
    }

    public float getBMI() {
        return (this.Wei / (this.Hei * this.Hei)) * 100.0f * 100.0f;
    }

    public float getFM() {
        return (getBFR() * this.Hei) / 100.0f;
    }

    public float getBFR_Raw() {
        if (this.Sex == 1) {
            return ((((((this.Hei * -0.3315f) + (this.Wei * 0.6216f)) + ((((float) this.Age) * 1.0f) * 0.0183f)) + (this.Res * 0.0085f)) + 22.554f) / this.Wei) * 100.0f;
        }
        return ((((((this.Hei * -0.3332f) + (this.Wei * 0.7509f)) + ((((float) this.Age) * 1.0f) * 0.0196f)) + (this.Res * 0.0072f)) + 22.7193f) / this.Wei) * 100.0f;
    }

    public float getBFR() {
        float bFR_Raw = getBFR_Raw();
        if (bFR_Raw < 5.0f) {
            bFR_Raw = 5.0f;
        }
        if (bFR_Raw > 45.0f) {
            bFR_Raw = 45.0f;
        }
        return new BigDecimal((double) bFR_Raw).setScale(2, 4).floatValue();
    }

    public float getVFR() {
        float f;
        if (this.Age <= 17) {
            return 0.0f;
        }
        if (this.Sex == 1) {
            f = (this.Hei * -0.2675f) + (this.Wei * 0.42f) + (((float) this.Age) * 1.0f * 0.1462f) + (this.Res * 0.0123f) + 13.9871f;
        } else {
            f = (this.Hei * -0.1651f) + (this.Wei * 0.2628f) + (((float) this.Age) * 1.0f * 0.0649f) + (this.Res * 0.0024f) + 12.3445f;
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
        return f2;
    }

    public float getTF() {
        if (this.Age > 17) {
            return (getTFR() * this.Wei) / 100.0f;
        }
        return 0.0f;
    }

    public float getTFR() {
        float f;
        if (this.Age <= 17) {
            return 0.0f;
        }
        if (this.Sex == 1) {
            f = ((((((this.Hei * 0.0939f) + (this.Wei * 0.3758f)) - ((((float) this.Age) * 1.0f) * 0.0032f)) - (this.Res * 0.006925f)) + 0.097f) / this.Wei) * 100.0f;
        } else {
            f = ((((((this.Hei * 0.0877f) + (this.Wei * 0.2973f)) + ((((float) this.Age) * 1.0f) * 0.0128f)) - (this.Res * 0.00603f)) + 0.5175f) / this.Wei) * 100.0f;
        }
        float f2 = 20.0f;
        if (f >= 20.0f) {
            f2 = f;
        }
        if (f2 > 85.0f) {
            f2 = 85.0f;
        }


        return new BigDecimal((double) f2).setScale(2, 4).floatValue();
    }

    private float getSLM_Raw() {
        float f;
        float bFR_Raw = getBFR_Raw();
        if (bFR_Raw > 45.0f) {
            return (this.Wei - (this.Wei * 0.45f)) - 4.0f;
        }
        if (bFR_Raw < 5.0f) {
            return (this.Wei - (this.Wei * 0.05f)) - 1.0f;
        }
        if (this.Sex == 1) {
            f = ((((this.Hei * 0.2867f) + (this.Wei * 0.3894f)) - ((((float) this.Age) * 1.0f) * 0.0408f)) - (this.Res * 0.01235f)) - 15.7665f;
        } else {
            f = ((((this.Hei * 0.3186f) + (this.Wei * 0.1934f)) - ((((float) this.Age) * 1.0f) * 0.0206f)) - (this.Res * 0.0132f)) - 16.4556f;
        }
        float f2 = 20.0f;
        if (f >= 20.0f) {
            f2 = f;
        }
        if (f2 > 70.0f) {
            return 70.0f;
        }
        return f2;
    }

    public float getSLM() {
        return new BigDecimal((double) getSLM_Raw()).setScale(2, 4).floatValue();
    }

    public float getSLMPercent(float f) {
        float f2;
        if (f == 0.0f) {
            f2 = (getSLM_Raw() / this.Wei) * 100.0f;
        } else {
            f2 = (f / this.Wei) * 100.0f;
        }
        return new BigDecimal((double) f2).setScale(2, 4).floatValue();
    }

    public float getMSW() {
        float fm = (this.Wei - getFM()) - getSLM();
        if (fm < 1.0f) {
            return 1.0f;
        }
        if (fm > 4.0f) {
            return 4.0f;
        }
        return fm;
    }

    public float getBMR() {
        float f;
        if (this.Sex == 1) {
            f = ((((this.Hei * 7.5037f) + (this.Wei * 13.1523f)) - ((((float) this.Age) * 1.0f) * 4.3376f)) - (this.Res * 0.3486f)) - 311.7751f;
        } else {
            f = ((((this.Hei * 7.5432f) + (this.Wei * 9.9474f)) - ((((float) this.Age) * 1.0f) * 3.4382f)) - (this.Res * 0.309f)) - 288.2821f;
        }
        return new BigDecimal((double) f).setScale(1, 4).floatValue();
    }

    public int getBodyAge() {
        float f;
        if (this.Age <= 17) {
            return 0;
        }
        if (this.Sex == 1) {
            f = (this.Hei * -0.7471f) + (this.Wei * 0.9161f) + (((float) this.Age) * 1.0f * 0.4184f) + (this.Res * 0.0517f) + 54.2267f;
        } else {
            f = (this.Hei * -1.1165f) + (this.Wei * 1.5784f) + (((float) this.Age) * 1.0f * 0.4615f) + (this.Res * 0.0415f) + 83.2548f;
        }
        int i = (int) f;
        if (i - this.Age > 10) {
            i = this.Age + 10;
        } else if (this.Age - i > 10) {
            i = this.Age - 10;
        }
        if (i < 18) {
            i = 18;
        }
        if (i > 80) {
            return 80;
        }
        return i;
    }

    public float getBW() {
        return getBW(this.Sex, this.Hei);
    }

    public float getWC() {
        return getBW() - this.Wei;
    }

    public float getBM() {
        if (this.Sex == 1) {
            return this.Wei * 0.77f;
        }
        return this.Wei * 0.735f;
    }

    public float getMC() {
        return getBM() - getSLM();
    }

    public float getFC() {
        float bw = getBW();
        float mc = getMC();
        float wc = getWC();
        if (this.Wei < bw) {
            return wc - mc;
        }
        if (this.Sex == 1) {
            return (((this.Wei + mc) * 0.15f) - getFM()) / 0.85f;
        }
        return (((this.Wei + mc) * 0.2f) - getFM()) / 0.8f;
    }

    public float getOD() {
        float bw = getBW();
        return ((this.Wei - bw) / bw) * 100.0f;
    }

    public float getPM() {
        if (this.Age > 17) {
            return getSLM_Raw() - getTF();
        }
        return 0.0f;
    }

    public float getLBM() {
        return this.Wei - getFM();
    }

    public float getShape() {
        return getShape(getBFR(), getBMI(), this.Sex, this.Age);
    }

    public static float calOD(float f, float f2, byte b, int i) {
        float bw = getBW(b, f);
        return ((f2 - bw) / bw) * 100.0f;
    }

    public static float calLBM(float f, float f2) {
        return f - calFM(f, f2);
    }

    public static int calBodyAge(float f, float f2, byte b, int i, float f3) {
        double d = (double) ((f * -0.2387f) + (f2 * 0.2258f));
        double d2 = (double) i;
        Double.isNaN(d2);
        Double.isNaN(d);
        double d3 = d + (d2 * 0.3452d);
        double d4 = (double) f3;
        Double.isNaN(d4);
        int round = (int) Math.round(d3 + (d4 * 1.2675d) + 9.5081d);
        if (round - i > 10) {
            round = i + 10;
        } else if (i - round > 10) {
            round = i - 10;
        }
        if (round < 18) {
            return 18;
        }
        if (round > 80) {
            return 80;
        }
        return round;
    }

    public static float calShape(float f, float f2, byte b, int i, float f3) {
        return getShape(f3, (f2 / (f * f)) * 100.0f * 100.0f, b, i);
    }

    public float getScore() {
        return getScoreWithAge(this.Hei, this.Wei, this.Sex, this.Age, getBFR(), getSLM(), getVFR());
    }

    private float getScoreWithAge(float f, float f2, byte b, int i, float f3, float f4, float f5) {
        if (i > 17) {
            return calScore(f, f2, b, i, f3, f4, f5);
        }
        return 0.0f;
    }
}
