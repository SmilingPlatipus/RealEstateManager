package com.openclassrooms.realestatemanager;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;


import androidx.test.core.app.ApplicationProvider;
import androidx.test.ext.junit.runners.AndroidJUnit4;

import com.openclassrooms.realestatemanager.utils.Utils;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.koin.core.context.KoinContextHandler;
import org.robolectric.Shadows;
import org.robolectric.annotation.Config;
import org.robolectric.shadows.ShadowNetworkInfo;

import static org.junit.Assert.*;
import static org.robolectric.Shadows.shadowOf;

/**
 * Instrumented test, which will execute on an Android device.
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
@Config(sdk = {Build.VERSION_CODES.Q})
@RunWith(AndroidJUnit4.class)
public class ConnectivityTest {
    private ConnectivityManager connectivityManager;
    private ShadowNetworkInfo shadowOfActiveNetworkInfo;
    private Context context;

    @Before
    public void setUp() throws Exception {
        context = ApplicationProvider.getApplicationContext();
        connectivityManager =
                (ConnectivityManager)
                        context.getSystemService(Context.CONNECTIVITY_SERVICE);
        shadowOfActiveNetworkInfo = Shadows.shadowOf(connectivityManager.getActiveNetworkInfo());
    }

    @After
    public void endTest() {
        KoinContextHandler.INSTANCE.stop();
    }


    @Test
    public void getActiveNetworkInfo_shouldInitializeItself() {
        Assert.assertNotNull(connectivityManager.getActiveNetworkInfo());
    }

    @Test
    public void getActiveNetworkInfo_shouldReturnTrueCorrectly() {
        shadowOfActiveNetworkInfo.setConnectionStatus(NetworkInfo.State.CONNECTED);
        Assert.assertTrue(Utils.isInternetAvailable(context));

        shadowOfActiveNetworkInfo.setConnectionStatus(NetworkInfo.State.CONNECTING);
        Assert.assertFalse(Utils.isInternetAvailable(context));

        shadowOfActiveNetworkInfo.setConnectionStatus(NetworkInfo.State.DISCONNECTED);
        Assert.assertFalse(Utils.isInternetAvailable(context));
    }


}
