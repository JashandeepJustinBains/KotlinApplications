package com.example.multiphotoviewer.view

import com.example.multiphotoviewer.controller.*
import com.example.multiphotoviewer.model.Model
import javafx.beans.InvalidationListener
import javafx.beans.Observable
import javafx.geometry.Insets
import javafx.geometry.Orientation
import javafx.scene.control.Separator
import javafx.scene.control.ToggleGroup
import javafx.scene.layout.HBox

class ToolBarView(private val model: Model): HBox(), InvalidationListener {
    private val addImageButton = AddImage(model)
    private val delImageButton = DelImage(model)
    private val vSeparator1 = Separator()
    private val resetButton = Reset(model)
    private val rotateLeftButton = RotateLeft(model)
    private val rotateRightButton = RotateRight(model)
    private val zoomINButton = ZoomIn(model)
    private val zoomOUTButton = ZoomOut(model)
    private val toggles = ToggleGroup()
    private val vSeparator2 = Separator()
    private val cascadeModeButton = CascadeMode(model)
    private val tileModeButton = TileMode(model)

    init {
        vSeparator1.orientation = Orientation.VERTICAL
        vSeparator2.orientation = Orientation.VERTICAL
        toggles.toggles.addAll(cascadeModeButton, tileModeButton)
        cascadeModeButton.isSelected = true
        children.addAll(addImageButton , delImageButton , vSeparator1,
            rotateLeftButton, rotateRightButton, zoomINButton, zoomOUTButton, resetButton, vSeparator2,
            cascadeModeButton, tileModeButton)

        delImageButton.isDisable = true
        rotateLeftButton.isDisable = true
        zoomINButton.isDisable = true
        zoomOUTButton.isDisable = true
        resetButton.isDisable = true

        spacing = 10.0
        padding = Insets(10.0)

        model.addListener( this )
        invalidated((null))
    }

    override fun invalidated(observable: Observable?) {
        if (model.getType() == 1) {
            rotateLeftButton.isDisable = true
            rotateRightButton.isDisable = true
            zoomINButton.isDisable = true
            zoomOUTButton.isDisable = true
            resetButton.isDisable = true
            delImageButton.isDisable = model.getIndex() == -1

        } else {
            if (model.getIndex() == -1) {
                rotateLeftButton.isDisable = true
                rotateRightButton.isDisable = true
                zoomINButton.isDisable = true
                zoomOUTButton.isDisable = true
                resetButton.isDisable = true
                delImageButton.isDisable = true
            } else {
                rotateLeftButton.isDisable = false
                rotateRightButton.isDisable = false
                zoomINButton.isDisable = false
                zoomOUTButton.isDisable = false
                resetButton.isDisable = false
                delImageButton.isDisable = false
            }

        }
     }


}