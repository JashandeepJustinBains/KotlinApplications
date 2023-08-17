package com.example.multiphotoviewer.controller

import com.example.multiphotoviewer.model.Model
import javafx.event.EventHandler
import javafx.scene.control.RadioButton

class TileMode(model: Model) : RadioButton("Tile Mode") {
    init {
        styleClass.remove("radio-button");
        styleClass.add("toggle-button");
        onAction = EventHandler {
            model.tileify()
        }
    }
}