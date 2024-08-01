package com.example.o2chatsdk.retrofit;

import com.example.o2chatsdk.fragments.UploadFilesDataModel;
import com.example.o2chatsdk.model.chat.AgentsByGroupIdModel;
import com.example.o2chatsdk.model.chat.Conversation;
import com.example.o2chatsdk.model.chat.ConversationByUID;
import com.example.o2chatsdk.model.chat.ConversationsCount;
import com.example.o2chatsdk.model.chat.GroupsDataModel;
import com.example.o2chatsdk.model.chat.OrganizationModelData;
import com.example.o2chatsdk.model.chat.TopicModelResponse;
import com.example.o2chatsdk.model.chat.UploadFilesData;
import com.example.o2chatsdk.model.chat.UserProfile;
import com.example.o2chatsdk.model.login.LoginRequest;
import com.example.o2chatsdk.model.login.LoginResponseData;
import com.example.o2chatsdk.retrofit.GroupsByUserDataModel;
import com.example.o2chatsdk.retrofit.WebResponse;
import com.example.o2chatsdk.retrofit.WebResponse2;
import com.example.o2chatsdk.retrofit.WebResponseBusinessHour;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Query;

public interface WebService {

    @POST("Account/Login")
    Call<WebResponse<LoginResponseData>> loginUser(@Body LoginRequest loginRequest);

    @GET("Chat/GetConversations")
    Call<WebResponse2<ArrayList<Conversation>>> getConversationList(@Query("PageNumber") int PageNumber,
                                                                    @Query("PageSize")int PageSize,
                                                                    @Query("agentId") String agentId,
                                                                    @Query("isAdmin") String isAdmin,
                                                                    @Query("status") int status);

    @GET("Chat/GetCustomerConversationsByEmail")
    Call<WebResponse2<ArrayList<ConversationByUID>>> getConversationByUID(@Query("pageNumber") int pageNumber,
                                                                          @Query("pageSize") int pageSize,
                                                                          @Query("conversationUId") String conversationUId,
                                                                          @Query("customerId") long customerId,
                                                                          @Query("Email") String Email);


   /* Three endpoints added:
    GetTopicsByEmail
            GetCustomerConversationsByEmail
    GetCustomerAllConversationsByEmail*/

    @POST("Chat/UploadFilesNew")
    Call<WebResponse<ArrayList<UploadFilesData>>> uploadFiles(@Body ArrayList<UploadFilesDataModel> files);

    @GET("UserManagement/GetUserById")
    Call<WebResponse<UserProfile>> getUserById(@Query("userId") String userId);


    @GET("UserManagement/GetAllGroups")
    Call<WebResponse<ArrayList<GroupsDataModel>>> getAllGroups(@Query("isActive") boolean isActive);

    @GET("UserManagement/GetGroupByUser")
    Call<WebResponse<GroupsByUserDataModel>> getGroupsByUserId(@Query("userId") int userId);

    @GET("UserManagement/GetAgentsByGroupId")
    Call<WebResponse<ArrayList<AgentsByGroupIdModel>>> getAgentsByGroupId(@Query("groupId") int groupId);

    @GET("Chat/GetConversationsCount")
    Call<WebResponse<ArrayList<ConversationsCount>>> getConversationCount(@Query("userId") int userId,@Query("isAdmin") boolean isAdmin);

    @GET("ChatHub/GetAccessToken")
    Call<WebResponse>  getAccessTokenByChannelId(@Query("ChannelId") String channelId);

    @GET("/api/BusinessHours/IsValidBusinessHour")
    Call<WebResponseBusinessHour> getValidBusinessHours(@Query("utcTimeDate") String utcTime,
                                                        @Query("dayIndex") String dayIndex);

    @PUT("ClientProfile/LogoutClient")
    Call<WebResponse> logoutApiForChat(@Query("customerId") int customerId);

    //    @GET("Topic/GetTopicsByConversationUId")
//    Call<WebResponse<TopicModelResponse>> getTopicsByConversationUId(@Query("conversationUId") String conversationUUid);

    @GET("Topic/GetTopicsByEmail")
    Call<WebResponse<TopicModelResponse>> getTopicsByEmail(@Query("Email") String Email);

    @GET("Organization/GetOrganization")
    Call<WebResponse<OrganizationModelData>> getOrganization();


}
