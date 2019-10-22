package com.makaya.drd.domain;

import java.io.Serializable;

public class StampLite implements Serializable {
    public long Id; // Id (Primary key)
    public String Descr; // Descr (length: 1000)
    public String StampFile; // StampFile (length: 100)
}