package com.espressoprogrammer.kotlin;

import org.junit.Test;

import java.util.Arrays;

import static org.assertj.core.api.Assertions.assertThat;

public class BookTest {

    @Test
    public void checkBook1Equals1() {
        Book1 actualBook1 = new Book1("Book1", Arrays.asList("Author11", "Author12"));
        Book1 expectedBook1 = new Book1("Book1", Arrays.asList("Author11", "Author12"));

        System.out.println("  actualBook1: " + actualBook1);
        System.out.println("expectedBook1: " + expectedBook1);
        System.out.println(actualBook1.equals(expectedBook1));

        assertThat(actualBook1.toString()).isEqualTo(expectedBook1.toString());
        assertThat(actualBook1).isEqualTo(expectedBook1);
    }

    @Test
    public void checkBook1Equals2() {
        Book1 actualBook1 = Book1Kt.createBook1("Book1", "Author11");
        Book1 expectedBook1 = Book1Kt.createBook1("Book1", "Author11");

        System.out.println("  actualBook1: " + actualBook1);
        System.out.println("expectedBook1: " + expectedBook1);
        System.out.println(actualBook1.equals(expectedBook1));

        assertThat(actualBook1.toString()).isEqualTo(expectedBook1.toString());
        assertThat(actualBook1).isEqualTo(expectedBook1);
    }

    @Test
    public void checkBook2Equals1() {
        Book2 actualBook2 = new Book2("Book2", new String[]{"Author21", "Author22"});
        Book2 expectedBook2 = new Book2("Book2", new String[]{"Author21", "Author22"});

        System.out.println("  actualBook2: " + actualBook2);
        System.out.println("expectedBook2: " + expectedBook2);
        System.out.println(actualBook2.equals(expectedBook2));

        assertThat(actualBook2.toString()).isEqualTo(expectedBook2.toString());
        assertThat(actualBook2).isEqualTo(expectedBook2);
    }

    @Test
    public void checkBook2Equals2() {
        Book2 actualBook2 = Book2Kt.createBook2("Book2", "Author21");
        Book2 expectedBook2 = Book2Kt.createBook2("Book2", "Author21");

        System.out.println("  actualBook2: " + actualBook2);
        System.out.println("expectedBook2: " + expectedBook2);
        System.out.println(actualBook2.equals(expectedBook2));

        assertThat(actualBook2.toString()).isEqualTo(expectedBook2.toString());
        assertThat(actualBook2).isEqualTo(expectedBook2);
    }
}
