package com.example.multiphotoviewer.controller

import com.example.multiphotoviewer.model.Model
import javafx.event.EventHandler
import javafx.scene.control.RadioButton

class CascadeMode(model: Model) : RadioButton("Cascade Mode") {
    init {
        styleClass.remove("radio-button");
        styleClass.add("toggle-button");

        onAction = EventHandler {
            model.cascadify()
        }
    }
}