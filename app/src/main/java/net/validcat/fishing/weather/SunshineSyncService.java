package net.validcat.fishing.weather;

//
//public class SunshineSyncService extends Service {
//    private static final Object sSyncAdapterLock = new Object();
//    private static SunshineSyncAdapter sSunshineSyncAdapter = null;
//
//    @Override
//    public void onCreate() {
//        Log.d("SunshineSyncService", "onCreate - SunshineSyncService");
//        synchronized (sSyncAdapterLock) {
//            if (sSunshineSyncAdapter == null) {
//                sSunshineSyncAdapter = new SunshineSyncAdapter(getApplicationContext(), true);
//            }
//        }
//    }
//
//    @Override
//    public IBinder onBind(Intent intent) {
//        return sSunshineSyncAdapter.getSyncAdapterBinder();
//    }
//}