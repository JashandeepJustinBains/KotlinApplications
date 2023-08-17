package com.example.multiphotoviewer.controller

import com.example.multiphotoviewer.model.Model
import javafx.event.EventHandler
import javafx.scene.control.Button
class Reset(model: Model) : Button("Reset") {
    init {
        onAction = EventHandler {
            model.reset()
        }
    }
}