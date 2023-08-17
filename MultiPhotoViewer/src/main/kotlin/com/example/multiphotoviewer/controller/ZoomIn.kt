package com.example.multiphotoviewer.controller

import com.example.multiphotoviewer.model.Model
import javafx.event.EventHandler
import javafx.scene.control.Button
class ZoomIn(model: Model) : Button("Zoom In") {
    init {
        onAction = EventHandler {
            model.zoomIN()
        }
    }

}