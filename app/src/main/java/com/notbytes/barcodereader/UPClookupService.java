package com.notbytes.barcodereader;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

public class UPClookupService extends Service {
    public UPClookupService() {
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }
}
