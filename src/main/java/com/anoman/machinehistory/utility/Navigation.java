package com.anoman.machinehistory.utility;

import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;

import java.util.Arrays;

public class Navigation {

    public void upDownNav (javafx.scene.Node[] arrayNodes, javafx.scene.Node node, KeyEvent event) {

        int totalNode = arrayNodes.length;
        int currentNode = Arrays.asList(arrayNodes).indexOf(node);

        if (event.getCode() == KeyCode.DOWN) {
            if (currentNode < totalNode - 1) {
                arrayNodes[currentNode + 1].requestFocus();
            } else {
                arrayNodes[0].requestFocus();
            }
        } else if (event.getCode() == KeyCode.UP){
            if (currentNode == 0) {
                arrayNodes[totalNode -1 ].requestFocus();
            } else {
                arrayNodes[currentNode - 1].requestFocus();
            }
        }


    }

}
