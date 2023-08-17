package com.example.multiphotoviewer.view

import com.example.multiphotoviewer.model.Model
import javafx.beans.InvalidationListener
import javafx.beans.Observable
import javafx.scene.control.Label

/**
 * DoubleLabel displays the value of the [Model][com.example.multiphotoviewer.model.Model] on a label.
 */
class ActiveLabelView(private val model: Model) : Label(), InvalidationListener {
    init {
        model.addListener(this)
        invalidated(null)
    }
    override fun invalidated(observable: Observable?) {
        val name = if ((model.getIndex() == -1) || (model.getSize() == -1)) {
            ""
        } else {
            model.getImages()[model.getIndex()].getName()
        }
        text = "${model.getSize() + 1} images loaded. Selected image: $name"
    }
}