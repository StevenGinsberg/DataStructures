package com.jetbrains;

public class Node {
    //------- Data Field -------//
    Node next;              //
    private char type;      //
//--------------------------//

    // type Setter:
    public void setType(char type) {
        this.type = type;
    }

    // type Getter:
    public char getType() {
        return this.type;
    }
}

