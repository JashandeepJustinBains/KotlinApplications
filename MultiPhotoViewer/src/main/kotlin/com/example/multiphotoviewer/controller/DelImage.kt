package com.example.multiphotoviewer.controller

import com.example.multiphotoviewer.model.Model
import javafx.event.EventHandler
import javafx.scene.control.Button

class DelImage(model: Model) : Button("Delete Image") {
    init {
        onAction = EventHandler {
            model.remFromList()
        }
    }
}