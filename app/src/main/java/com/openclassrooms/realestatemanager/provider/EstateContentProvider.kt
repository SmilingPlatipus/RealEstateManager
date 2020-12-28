package com.openclassrooms.realestatemanager.provider

import android.content.ContentProvider
import android.content.ContentUris
import android.content.ContentValues
import android.database.Cursor
import android.net.Uri
import com.openclassrooms.realestatemanager.dao.EstateDao
import com.openclassrooms.realestatemanager.db.EstateDatabase
import com.openclassrooms.realestatemanager.model.Estate
import org.koin.android.ext.android.inject
import java.lang.IllegalArgumentException

class EstateContentProvider : ContentProvider() {
    private val estateDatabase : EstateDatabase by inject()
    override fun onCreate(): Boolean {
        return true
    }

    override fun query(uri: Uri, projection: Array<out String>?, selection: String?, selectionArgs: Array<out String>?, sortOrder: String?): Cursor? {
        if (context != null){
            var estateId = ContentUris.parseId(uri)

            val cursor : Cursor = estateDatabase.estateDao.getEstatesWithCursor(estateId)
            cursor.setNotificationUri(context!!.contentResolver,uri)
            return cursor
        }

        throw IllegalArgumentException("Failed to query row for uri $uri")
    }

    override fun getType(uri: Uri): String? {
        return "vnd.android.cursor.estate/$AUTHORITY.$TABLE_NAME"
    }

    override fun insert(uri: Uri, values: ContentValues?): Uri? {
        if (context != null){
            val id = estateDatabase.estateDao.insert(EstateDao.estateFromContentValues(values))

            if (id.compareTo(0) != 0){
                context!!.contentResolver.notifyChange(uri, null)
                return ContentUris.withAppendedId(uri, id)
            }
        }

        throw IllegalArgumentException("Failed to insert row into $uri")
    }

    override fun delete(uri: Uri, selection: String?, selectionArgs: Array<out String>?): Int {
        return 0
    }

    override fun update(uri: Uri, values: ContentValues?, selection: String?, selectionArgs: Array<out String>?): Int {
        return 0
    }

    companion object utils{
        const val AUTHORITY = "com.openclassrooms.realestatemanager.provider"
        val TABLE_NAME = Estate::class.java.simpleName
        val URI_ESTATE = Uri.parse("content://$AUTHORITY/$TABLE_NAME")
    }
}