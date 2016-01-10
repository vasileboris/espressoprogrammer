package com.espressoprogrammer.naming.packagename.test;

import com.espressoprogrammer.naming.differentpackagename.DifferentPackageThanPath;
import com.espressoprogrammer.naming.packagename.SamePackageAsPath;

public class PackageNameTest {

    public static void main(String... args) {
        printSomeClassInfo(new SamePackageAsPath());
        printSomeClassInfo(new DifferentPackageThanPath());
    }

    private static void printSomeClassInfo(Object object) {
        System.out.println("----------------------- ");
        System.out.println("  getClass().getName(): " + object.getClass().getName());
        System.out.println("getPackage().getName(): " + object.getClass().getPackage().getName());
    }

}
