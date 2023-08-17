package com.example.multiphotoviewer.controller

import com.example.multiphotoviewer.model.Model
import javafx.event.EventHandler
import javafx.scene.control.Button
class ZoomOut(model: Model) : Button("Zoom Out") {
    init {
        onAction = EventHandler {
            model.zoomOut()
        }
    }
}