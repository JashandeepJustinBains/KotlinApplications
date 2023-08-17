package com.example.multiphotoviewer.controller

import com.example.multiphotoviewer.model.Model
import javafx.event.EventHandler
import javafx.scene.control.Button
class RotateRight(model: Model) : Button("Rotate Right") {
    init {
        onAction = EventHandler {
            model.rotateR()
        }
    }
}