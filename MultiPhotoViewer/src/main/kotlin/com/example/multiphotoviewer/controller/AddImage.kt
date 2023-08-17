package com.example.multiphotoviewer.controller

import com.example.multiphotoviewer.model.Model
import javafx.event.EventHandler
import javafx.scene.control.Button
import javafx.scene.image.Image
import javafx.stage.FileChooser
import java.io.File

class AddImage(model: Model): Button("Add Image") {
    init {
        onAction = EventHandler {
            val fc = FileChooser()
            fc.title = "Select Image File"
            fc.initialDirectory = File(System.getProperty("user.dir") + "/src/main/resources/com/example/lightbox")
            fc.extensionFilters.addAll(
                FileChooser.ExtensionFilter("All Images", "*.*"),
                FileChooser.ExtensionFilter("JPG", "*.jpg"),
                FileChooser.ExtensionFilter("PNG", "*.png")
            )
            val file = fc.showOpenDialog(null)
            if (file != null) {
                if ((file.extension == "png")
                    || (file.extension == "jpg")
                    || (file.extension == "jpeg")
                    || (file.extension == "bmp")) {
                    val image = Image(file.toURI().toString())
                    model.addToList(image, file.name)
                }
            }

        }
    }
}