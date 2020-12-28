package com.openclassrooms.realestatemanager;

import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;

import androidx.room.Room;
import androidx.test.InstrumentationRegistry;
import androidx.test.ext.junit.runners.AndroidJUnit4;

import com.google.gson.Gson;

import com.openclassrooms.realestatemanager.activities.SearchActivity;
import com.openclassrooms.realestatemanager.db.EstateDatabase;
import com.openclassrooms.realestatemanager.model.EstatePhoto;
import com.openclassrooms.realestatemanager.provider.EstateContentProvider;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.util.Collections;
import java.util.EnumSet;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

@RunWith(AndroidJUnit4.class)
public class ContentProviderInstrumentedTest {
    private ContentResolver mContentResolver;
    private Context context;

    ContentValues contentValues = new ContentValues();

    // DATA SET FOR TEST
    private static final long ESTATE_ID = 8;

    @Before
    public void setUp() {
        Room.inMemoryDatabaseBuilder(InstrumentationRegistry.getContext(),
                EstateDatabase.class)
                .allowMainThreadQueries()
                .build();
        mContentResolver = InstrumentationRegistry.getContext().getContentResolver();
        context = InstrumentationRegistry.getInstrumentation().getContext();
    }

    @Test
    public void getItemsWhenNoItemInserted() {
        final Cursor cursor = mContentResolver.query(ContentUris.withAppendedId(EstateContentProvider.utils.getURI_ESTATE(), ESTATE_ID), null, null, null, null);
        assertNotNull(cursor);
        assertEquals(cursor.getCount(), 8);
        cursor.close();
    }

    @Test
    public void insertAndGetEstate() {
        final Uri userUri = mContentResolver.insert(EstateContentProvider.utils.getURI_ESTATE(), generateEstate());

        final Cursor cursor = mContentResolver.query(ContentUris.withAppendedId(EstateContentProvider.utils.getURI_ESTATE(), ESTATE_ID), null, null, null, null);
        assertNotNull(cursor);
        assertEquals(cursor.getCount(), 1);
        assertEquals(cursor.getString(cursor.getColumnIndexOrThrow("type")), contentValues.get("type"));
        assertEquals(cursor.getDouble(cursor.getColumnIndexOrThrow("price")), (double) contentValues.get("price"),0);
        assertEquals(cursor.getFloat(cursor.getColumnIndexOrThrow("size")), (float) contentValues.get("size"),0);
        assertEquals(cursor.getInt(cursor.getColumnIndexOrThrow("rooms")), (int) contentValues.get("rooms"),0);
        assertEquals(cursor.getString(cursor.getColumnIndexOrThrow("description")), contentValues.get("description"));
        assertEquals(cursor.getString(cursor.getColumnIndexOrThrow("poi")), contentValues.get("poi"));
        assertEquals(cursor.getString(cursor.getColumnIndexOrThrow("photosUriWithDescriptions")), contentValues.get("photosUriWithDescriptions"));
        assertEquals(cursor.getString(cursor.getColumnIndexOrThrow("address")), contentValues.get("address"));
        assertEquals(cursor.getString(cursor.getColumnIndexOrThrow("status")), contentValues.get("status"));
        assertEquals(cursor.getString(cursor.getColumnIndexOrThrow("creationDate")), contentValues.get("creationDate"));
        assertEquals(cursor.getString(cursor.getColumnIndexOrThrow("saleDate")), contentValues.get("saleDate"));
        assertEquals(cursor.getString(cursor.getColumnIndexOrThrow("saler")), contentValues.get("saler"));
        assertEquals(cursor.getString(cursor.getColumnIndexOrThrow("latitude")), contentValues.get("latitude"));
        assertEquals(cursor.getString(cursor.getColumnIndexOrThrow("longitude")), contentValues.get("longitude"));
    }

    private ContentValues generateEstate(){
        List<String> address = List.of("143", "Rue de la chartreuse", "46000", "Cahors");
        EnumSet<SearchActivity.NearbyServices> checkboxesStatus = EnumSet.noneOf(SearchActivity.NearbyServices.class);
        List<String> poiList  = Collections.emptyList();
        List <EstatePhoto> photoList = Collections.emptyList();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mmX")
                .withZone(ZoneOffset.UTC);

        checkboxesStatus.add(SearchActivity.NearbyServices.HOSPITAL);
        for (SearchActivity.NearbyServices checkboxes : checkboxesStatus)
        {
            poiList.add(checkboxes.name().toLowerCase());
        }
        EstatePhoto somePhoto = new EstatePhoto(context.getString(R.string.standard_drawable_uri) + R.drawable.maison1,"Juste un appart");
        photoList.add(somePhoto);

        contentValues.put("type",SearchActivity.EstateTypes.HOUSE.name().toLowerCase());
        contentValues.put("price",120000.0);
        contentValues.put("size",120f);
        contentValues.put("rooms",3);
        contentValues.put("description","Nice house in Cahors");
        contentValues.put("poi", new Gson().toJson(poiList));
        contentValues.put("photosUriWithDescriptions", new Gson().toJson(photoList));
        contentValues.put("address", new Gson().toJson(address));
        contentValues.put("status",SearchActivity.SearchStatus.FORSALE.name().toLowerCase());
        contentValues.put("creationDate", OffsetDateTime.now().format(formatter));
        contentValues.put("saleDate", "null");
        contentValues.put("saler","Jean Michel");
        contentValues.put("latitude",44.447996);
        contentValues.put("longitude", 1.437470);

        return contentValues;
    }
}
