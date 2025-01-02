package com.arittek.o2chatsdk.commons;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;

import com.arittek.o2chatsdk.fragments.ReLoadConversationEvent;
import com.microsoft.signalr.HubConnection;
import com.microsoft.signalr.HubConnectionBuilder;

import org.greenrobot.eventbus.EventBus;

import io.reactivex.Single;

public class SignalRHelper {

    public HubConnection createChatHubConnection(String accessToken, Context context){
        HubConnection hubConnection =
                HubConnectionBuilder.create(new Common().getBaseUrlChat(context)+"chatHub").withAccessTokenProvider(Single.defer(() -> {
                    // Your logic here.
                    return Single.just(accessToken);
                })).build();
        return hubConnection;
    }

    @SuppressLint("CheckResult")
    public boolean startSignalRHubClient(HubConnection hubConnection , long customerId,String mobileToken, Context context, Activity activity){
        if (hubConnection!=null){
            final boolean[] isConnected = new boolean[1];
            try {
                Common common = new Common();
                hubConnection.start().doOnComplete(() -> {
                    isConnected[0] = true;
                }).blockingAwait();

                hubConnection.setKeepAliveInterval(2000);
//              hubConnection.invoke("AgentJoined",agentId);
//              hubConnection.invoke("CustomerJoinedFromMobile","6901b42a-0776-41d2-ac76-6cb6f3029d53",customerId,mobileToken);

           hubConnection.invoke("CustomerJoinedFromMobile",common.getChannelID(context),customerId,mobileToken);

//              Log.d("Channel ID------>",new O2ChatConfig(context).getChannelID());
                common.saveConnectionId(context,hubConnection.getConnectionId());
                if (isConnected[0]){
                    return true;
                }else{
                    return false;
                }
            } catch (Exception e) {
                e.printStackTrace();
                if (e.getMessage().equalsIgnoreCase("Unexpected status code returned from negotiate: 401 Unauthorized.")){
                    //logout callhere
                    Common common = new Common();
                    common.savePermission(context,"");
                    common.saveIsSuperAdmin(context,"");
                    common.isLoggedIn(context,false);
                    common.saveSelectedMenu(context,"0");
//                  Intent intent = new Intent(context, LoginActivityChat.class);
//                    intent.putExtra(Constants.CONVERSATION_BY_UID_KEY,"");
//                    context.startActivity(intent);
//                    activity.finish();
                }else{
                    EventBus.getDefault().post(new ReLoadConversationEvent("Reconnecting"));
                }
            }
        }
        return false;
    }

}
