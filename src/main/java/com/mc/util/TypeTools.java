package com.mc.util;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class TypeTools {
    public TypeTools() {
    }

    /** @deprecated */
    @Deprecated
    public static long toLong(Object value, long defaultValue) {
        if (value == null) {
            return defaultValue;
        } else {
            return value instanceof Long ? (Long)value : Long.parseLong(String.valueOf(value));
        }
    }

    public static long toLong(Object value) {
        return value instanceof Long ? (Long)value : Long.parseLong(String.valueOf(value));
    }

    public static long optLong(Object value, long defaultValue) {
        try {
            return toLong(value);
        } catch (Exception var4) {
            return defaultValue;
        }
    }

    /** @deprecated */
    @Deprecated
    public static String toString(Object value, String defaultValue) {
        return value == null ? defaultValue : value.toString();
    }

    public static String toString(Object value) {
        return value.toString();
    }

    public static String optString(Object value, String defaultValue) {
        return value == null ? defaultValue : value.toString();
    }

    /** @deprecated */
    @Deprecated
    public static int toInt(Object value, int defaultValue) {
        if (value == null) {
            return defaultValue;
        } else {
            return value instanceof Integer ? (Integer)value : Integer.parseInt(String.valueOf(value));
        }
    }

    public static int toInt(Object value) {
        return value instanceof Integer ? (Integer)value : Integer.parseInt(String.valueOf(value));
    }

    public static int optInt(Object value, int defaultValue) {
        try {
            return toInt(value);
        } catch (Exception var3) {
            return defaultValue;
        }
    }

    public static short toShort(Object value) {
        return value instanceof Short ? (Short)value : Short.parseShort(String.valueOf(value));
    }

    public static short optShort(Object value, short defaultValue) {
        try {
            return toShort(value);
        } catch (Exception var3) {
            return defaultValue;
        }
    }

    /** @deprecated */
    @Deprecated
    public static double toDouble(Object value, double defaultValue) {
        if (value == null) {
            return defaultValue;
        } else {
            return value instanceof Double ? (Double)value : Double.parseDouble(String.valueOf(value));
        }
    }

    public static double toDouble(Object value) {
        return value instanceof Double ? (Double)value : Double.parseDouble(String.valueOf(value));
    }

    public static double optDouble(Object value, double defaultValue) {
        try {
            return toDouble(value);
        } catch (Exception var4) {
            return defaultValue;
        }
    }

    public static float toFloat(Object value) {
        return value instanceof Float ? (Float)value : Float.parseFloat(String.valueOf(value));
    }

    public static float optFloat(Object value, float defaultValue) {
        try {
            return toFloat(value);
        } catch (Exception var3) {
            return defaultValue;
        }
    }

    /** @deprecated */
    @Deprecated
    public static boolean toBoolean(Object value, boolean defaultValue) {
        if (value == null) {
            return defaultValue;
        } else {
            return value instanceof Boolean ? (Boolean)value : Boolean.parseBoolean(String.valueOf(value));
        }
    }

    public static boolean toBoolean(Object value) {
        return value instanceof Boolean ? (Boolean)value : Boolean.parseBoolean(String.valueOf(value));
    }

    public static boolean optBoolean(Object value, boolean defaultValue) {
        try {
            return toBoolean(value);
        } catch (Exception var3) {
            return defaultValue;
        }
    }

    public static Byte toByte(Object value) {
        return value instanceof Byte ? (Byte)value : Byte.parseByte(String.valueOf(value));
    }

    public static Byte optByte(Object value, Byte defaultValue) {
        try {
            return toByte(value);
        } catch (Exception var3) {
            return defaultValue;
        }
    }

    /** @deprecated */
    @Deprecated
    public static BigDecimal toBigDecimal(Object value, BigDecimal defaultValue) {
        if (value == null) {
            return defaultValue;
        } else {
            return value instanceof BigDecimal ? (BigDecimal)value : new BigDecimal(String.valueOf(value));
        }
    }

    public static BigDecimal toBigDecimal(Object value) {
        return value instanceof BigDecimal ? (BigDecimal)value : new BigDecimal(String.valueOf(value));
    }

    public static BigDecimal optBigDecimal(Object value, BigDecimal defaultValue) {
        try {
            return toBigDecimal(value);
        } catch (Exception var3) {
            return defaultValue;
        }
    }

    public static boolean validDateTime(Object value, String pattern) throws ParseException {
        DateFormat dateFormat = new SimpleDateFormat(pattern);
        Date parsed = dateFormat.parse(value.toString());
        return dateFormat.format(parsed).equals(value);
    }

    public static long toTimestamp(Object value, String pattern) throws ParseException {
        if (value instanceof Timestamp) {
            return ((Timestamp)value).getTime();
        } else if (value instanceof java.sql.Date) {
            return ((java.sql.Date)value).getTime();
        } else if (value instanceof Date) {
            return ((Date)value).getTime();
        } else {
            DateFormat dateFormat = new SimpleDateFormat(pattern);
            Date parsed = dateFormat.parse(value.toString());
            boolean valid = dateFormat.format(parsed).equals(value);
            if (!valid) {
                throw new IllegalArgumentException("the value is not valid DateString, Value: " + value);
            } else {
                return parsed.getTime();
            }
        }
    }

    public static long toTimestamp(Object value) throws ParseException {
        return toTimestamp(value, "yyyy-MM-dd HH:mm:ss");
    }

    public static long optTimestamp(Object value, String pattern, long defaultValue) {
        try {
            return toTimestamp(value, pattern);
        } catch (Exception var5) {
            return defaultValue;
        }
    }

    public static long optTimestamp(Object value) {
        return optTimestamp(value, 0L);
    }

    public static long optTimestamp(Object value, long defaultValue) {
        return optTimestamp(value, "yyyy-MM-dd HH:mm:ss", defaultValue);
    }
}
