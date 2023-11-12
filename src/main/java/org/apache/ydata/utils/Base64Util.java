package org.apache.ydata.utils;

import org.apache.commons.codec.binary.Base64;

public class Base64Util {
    // 加密
    public static String encode(String str) {
        String encodeBase64String = Base64.encodeBase64String(str.getBytes());
        return encodeBase64String;
    }

    // 解密
    public static String decode(String s) {
        byte[] decodeBase64 = Base64.decodeBase64(s);
        s = new String(decodeBase64);
        return s;
    }

    public static void main(String[] args) {
//        String a ="{\"code\":200,\"data\":{\"id\":1,\"appid\":1,\"viewName\":\"ABCcONTROLLER\",\"uri\":\"WWW\",\"sold\":0,\"remark\":\"\",\"created\":\"Sep 25, 2020 11:36:51 AM\"}}";
//        String base64 = getBase64(a);
//        System.out.println(base64);
//        String str = "ODYyNTQwMDU3NzQzNTc5";
        String str = "TndrNmw4MlJFY0s1NVY5ZDgzb0p5dTFNNnVsYklzdzZRTHRNSklKVUdUTEFubmRNSlBPNzJqdThUTjdjOUN5N2J1c1JPc1cxaGJJdnArZm1nOWVWY01BR0h2VWgvU2djaEJCenJKVlo5VkF1NnRGays5ZEJYeUVNTzRLUktGZUlzZTRoUGRYT0VBT2YrSEREMVdaOEJWcXQveThDL3ozRWZEaDEyMmhwQ0xndmUvU3JvSFFFVzcyWHVib0Y1bFQ0K1plVytFQjhReTlpSktMQnFVOU03L3hSYzNWakE2UVpXVXpDK05ZSlpKWHAyREI4SlZ3K1IwYWJpWDBkajg2RjRRcWV6aS8vR21EOGdvcXNIaDVndUt1Ny8veS9vYUFRTFVHcXR1M2k3dHdkSHUreFJYVWdnZGVLdHdWcGVQTFpJWksyWi85cDJkemtWMkFYenFmOVZ5WGFqemRQU0xpWjZLVWdXdkFaMk5pbzdwaGZBQWw2S20waGZweDNXOWlMa2J5dSs0TjJrN2ozeG41cFhvQnN6VmlORno0OXlySjBhOENWbXBZSzlyVEFFaW5wQXFMc2RQaUFmQ0p2RTRCbDF1YjE5UjJuSTFBQStDN3lYcGc4dDVvKzc4MlpDK0o3VSt3THk1YkVncHVmTGFHMkZ1MDFEUVpOa24rY2pCZUx1WnI1ZTZ6V3lFanJTYmIwdDcyYW5TbE01cEo4TTd3aUR5VXlyU0E3cC9LbWxUZmlYdmduWUtyakUvTGM3V3VWa0l4cENYZTQ0bHkyUjQwSVVsSy9TQT09";
        String fromBase64 = decode(str);
        System.out.println(fromBase64);
    }
}
