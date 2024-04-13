package com.example.messenger.Service;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Handler;
import android.os.IBinder;
import android.widget.Toast;

import com.example.messenger.MyAccount.InfoMyAccount;
import com.example.messenger.operationServer.SocketClient;
import com.example.messenger.operationServer.OperationServer;
import com.example.messenger.ui.App;
import com.example.messenger.ui.Fragments.Fragment_1;
import com.example.messenger.ui.MyActivityLifecycleCallbacks;
import com.example.messenger.ui.User;
import com.example.messenger.ui.UserChat;
import com.example.messenger.ui.LogIn;

import java.net.URISyntaxException;


public class ServiceSendRetrieveMessage extends Service {
    public static ServiceSendRetrieveMessage service;
    public SocketClient webSocketClient;
    private NetworkChangeReceiver receiver;
    private final Handler handler = new Handler();
    String serverUri = "ws://192.168.75.1:8080/projectMessenger/websocket"; // Đặt đúng địa chỉ của server WebSocket
    public ServiceSendRetrieveMessage() {
    }


    @Override
    public IBinder onBind(Intent intent) {
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        ServiceSendRetrieveMessage.service = this;
        new Thread(new Runnable() {
            @Override
            public void run() {
                newConnectToServer();
                ServiceSendRetrieveMessage.this.receiver = new NetworkChangeReceiver();
                IntentFilter filter = new IntentFilter();
                filter.addAction("android.net.conn.CONNECTIVITY_CHANGE");
                registerReceiver(ServiceSendRetrieveMessage.this.receiver , filter);
            }
        }).start(); // chay mot luong ngam ngay ben trong service để có thể nhận thông điệp từ server kể cả khi thoát app
        return super.onStartCommand(intent, flags, startId);
    }


    public class NetworkChangeReceiver extends BroadcastReceiver {
        boolean neverConnected = true;
         int statesConnect = 0;// 0 là chưa có kết nối. 1 là kết nối 3g(type_mobile) . 2 là wifi

        @Override
        synchronized public void onReceive(Context context, Intent intent) {
            ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo activeNetwork = connectivityManager.getActiveNetworkInfo();
            if (!isOnline(ServiceSendRetrieveMessage.this)) {
                statesConnect = 0;
                return;
            }
            if(neverConnected){
                if (activeNetwork != null) {
                    if (activeNetwork.getType() == ConnectivityManager.TYPE_WIFI) {
                        statesConnect = 2;
                    } else if (activeNetwork.getType() == ConnectivityManager.TYPE_MOBILE) {
                        statesConnect = 1;
                    }
                }
                neverConnected = false;
                return;
            }
            if (activeNetwork != null) {
                if (activeNetwork.getType() == ConnectivityManager.TYPE_WIFI) {
                    if(statesConnect == 2) return;
                    else statesConnect = 2;
                } else if (activeNetwork.getType() == ConnectivityManager.TYPE_MOBILE) {
                    if(statesConnect == 1) return;
                    else statesConnect = 1;
                }
            }

                // Thiết bị đã kết nối internet
                // yeu cau may chu ket noi lai. vi co thể địa chỉ ip đã thay đổi từ mạng này sang mạng khác. hoac user khac login vao.  phải thông báo cho server
                String clientCode = ServiceSendRetrieveMessage.this.webSocketClient.getCodeClient();
                ServiceSendRetrieveMessage.this.webSocketClient = null;
                // gui 1 http request len server de xac lap lai ket noi. neu thanh cong thi 200
                boolean result = new OperationServer().checkAllowEstablishNewSession(InfoMyAccount.getAccountId(),clientCode);
                if(!result){
                    handler.post(new Runnable() {
                        @Override
                        public void run() {
                            if( MyActivityLifecycleCallbacks.currentActivity != null ){
                                SharedPreferences sharedPreferences= MyActivityLifecycleCallbacks.currentActivity.getSharedPreferences("account", MODE_PRIVATE);
                                SharedPreferences.Editor editor = sharedPreferences.edit();
                                editor.clear();
                                editor.apply();
                                LogIn.requestFinish = false;
                            }
                            MyActivityLifecycleCallbacks.finish = true;
                            ServiceSendRetrieveMessage.this.stopSelf();
                            ServiceSendRetrieveMessage.service = null;
                            if(MyActivityLifecycleCallbacks.currentActivity != null) MyActivityLifecycleCallbacks.currentActivity.finish();
                        }
                    });
                }else{
                    newConnectToServer();
                    if(MyActivityLifecycleCallbacks.currentActivity != null) UpdateNewInfoFromServer();
                }
        }

        private boolean isOnline(Context context) {
            ConnectivityManager connectivityManager =
                    (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);

            if (connectivityManager != null) {
                return connectivityManager.getActiveNetworkInfo() != null &&
                        connectivityManager.getActiveNetworkInfo().isConnectedOrConnecting();
            }
            return false;
        } // tạm thời k dùng. kiểm tra xem có ok trong check internet k
    }
    public void newConnectToServer(){
        try {
            this.webSocketClient = new SocketClient(serverUri,handler);
            this.webSocketClient.connect();
        } catch (URISyntaxException e) {
            // co ngoai le xảy ra . thoát tất cả các activity current và chính service này
            handler.post(new Runnable() {
                @Override
                public void run() {
                    if ( MyActivityLifecycleCallbacks.currentActivity != null )
                        MyActivityLifecycleCallbacks.currentActivity.finish();
                    ServiceSendRetrieveMessage.this.stopSelf();
                }
            });
        }
    }
    public void UpdateNewInfoFromServer(){
        if(MyActivityLifecycleCallbacks.currentActivity instanceof UserChat){
            handler.post(new Runnable() {
                @Override
                public void run() {
                    UserChat currentActivity = (UserChat)MyActivityLifecycleCallbacks.currentActivity;
                    currentActivity.requestInternetToResult();
                }
            });
        }
        if(MyActivityLifecycleCallbacks.currentActivity instanceof App && ((App)MyActivityLifecycleCallbacks.currentActivity).getCurrentFragmentInOnResume() instanceof Fragment_1){
            handler.post(new Runnable() {
                @Override
                public void run() {
                    Fragment_1 fragment1 = (Fragment_1)((App)MyActivityLifecycleCallbacks.currentActivity).getCurrentFragmentInOnResume();
                    fragment1.requestInternetToResult();
                }
            });
        }
        if(MyActivityLifecycleCallbacks.currentActivity instanceof User){
            handler.post(new Runnable() {
                @Override
                public void run() {
                    User currentActivity = (User)MyActivityLifecycleCallbacks.currentActivity;
                    int userId = currentActivity.getUserId();
                    if(new OperationServer().checkRelationshipUser(InfoMyAccount.getAccountId(),userId) == -1){
                       currentActivity.finish();
                       Toast.makeText(ServiceSendRetrieveMessage.this,"Bạn không có quyền truy cập vào tài khoản này",Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }
    }

    public void onDestroy() {
        super.onDestroy();
        // Unregister the receiver to prevent memory leaks
        if (receiver != null) {
            unregisterReceiver(receiver);
            receiver = null;
        }
    }
}