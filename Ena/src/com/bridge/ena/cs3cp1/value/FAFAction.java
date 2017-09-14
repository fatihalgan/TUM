/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.bridge.ena.cs3cp1.value;

/**
 *
 * @author db2admin
 */
public enum FAFAction {

    ADD("ADD"),
    SET("SET"),
    DELETE("DEL");

    private final String text;

    private FAFAction(String text) {
        this.text = text;
    }

    public String toString() {
        return text;
    }
}
