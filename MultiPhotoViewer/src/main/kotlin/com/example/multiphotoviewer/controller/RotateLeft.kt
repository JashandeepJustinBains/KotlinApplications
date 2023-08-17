package com.example.multiphotoviewer.controller

import com.example.multiphotoviewer.model.Model
import javafx.event.EventHandler
import javafx.scene.control.Button
class RotateLeft(model: Model) : Button("Rotate Left") {
    init {
        onAction = EventHandler {
            model.rotateL()
        }
    }
}