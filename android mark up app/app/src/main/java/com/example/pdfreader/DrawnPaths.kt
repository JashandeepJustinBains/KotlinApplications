package com.example.pdfreader

import android.graphics.Path
import android.os.Parcel
import android.os.Parcelable

class DrawnPaths() : Parcelable {

    private val pathOrder = mutableListOf<Path?>()
    private val redoOrder = mutableListOf<Path?>()

    private val pathOrderNumber = mutableListOf<Int?>()
    private val redoOrderNumber = mutableListOf<Int?>()

    constructor(parcel: Parcel) : this() {
        parcel.readList(pathOrder as List<*>, Path::class.java.classLoader)
        parcel.readList(redoOrder as List<*>, Path::class.java.classLoader)
        parcel.readList(pathOrderNumber as List<*>, Int::class.java.classLoader)
        parcel.readList(redoOrderNumber as List<*>, Int::class.java.classLoader)
    }

    fun getAllPaths(): MutableList<Path?> { return pathOrder }

    fun addPenPath(path : Path?) {
        pathOrder.add( path )
        pathOrderNumber.add( 1 )
        redoOrder.clear()
        redoOrderNumber.clear()
    }
    fun addMarkerPath(path : Path?) {
        pathOrder.add( path )
        pathOrderNumber.add( 2 )
        redoOrder.clear()
        redoOrderNumber.clear()
    }
    fun addErasePath(path : Path?) {
        pathOrder.add( path )
        pathOrderNumber.add( 3 )
        redoOrder.clear()
        redoOrderNumber.clear()
    }

    fun getPathOrder(): MutableList<Int?> { return pathOrderNumber }

    fun undo() {
        val newest = pathOrder.lastOrNull()
        val newestNumber = pathOrderNumber.lastOrNull()

        if ( redoOrder.lastOrNull() != newest ) {
            pathOrder.removeLast()
            pathOrderNumber.removeLast()
            redoOrder.add(newest)
            redoOrderNumber.add(newestNumber)
        }
    }

    fun redo() {
        val newest = redoOrder.lastOrNull()
        val newestNumber = redoOrderNumber.lastOrNull()
        redoOrder.removeLast()
        redoOrderNumber.removeLast()

        pathOrder.add(newest)
        pathOrderNumber.add(newestNumber)
    }

    fun getRedoable(): Int { return redoOrder.size }
    fun getUndoable(): Int { return pathOrder.size }
    override fun writeToParcel(parcel: Parcel, flags: Int) {

    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<DrawnPaths> {
        override fun createFromParcel(parcel: Parcel): DrawnPaths {
            return DrawnPaths(parcel)
        }

        override fun newArray(size: Int): Array<DrawnPaths?> {
            return arrayOfNulls(size)
        }
    }


}